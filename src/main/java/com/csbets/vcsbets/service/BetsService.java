package com.csbets.vcsbets.service;

import com.csbets.vcsbets.dto.bet.BetDto;
import com.csbets.vcsbets.dto.bet.MatchOutcomeBetPlaceDto;
import com.csbets.vcsbets.dto.bet.TotalRoundsBetPlaceDto;
import com.csbets.vcsbets.entity.bet.*;
import com.csbets.vcsbets.entity.match.*;
import com.csbets.vcsbets.entity.user.User;
import com.csbets.vcsbets.repository.BetsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class BetsService {

    private final BetsRepository betsRepository;
    private final SeriesService seriesService;
    private final UserService userService;

    private static final short BET_AMOUNT = 150;
    private static final short MATCH_OUTCOME_COEFFICIENT = 2;
    private static final double TOTAL_ROUNDS_COEFFICIENT = 1.5;

    public void placeMatchOutcomeBet(MatchOutcomeBetPlaceDto matchOutcomeBetPlaceDto, String username) {
        Series series = seriesService.getSeries(matchOutcomeBetPlaceDto.seriesId());

        if (userService.getUserByUsername(username).getCreditBalance() < BET_AMOUNT)
            throw new RuntimeException("Средств на балансе не хватает чуток(");

        if (series.getTeam1Players().contains(username) || series.getTeam2Players().contains(username))
            throw new RuntimeException("322 на свои матчи запрещены!");

        if(series.getTeam1Players().isEmpty() || series.getTeam2Players().isEmpty()) {
            throw new RuntimeException("Нельзя сделать ставку на неизвестность :/");
        }

        MatchOutcomeBet existingBet = betsRepository.findMatchOutcomeBetByUserAndSeries_Id(
                        userService.getUserByUsername(username), series.getId())
                .orElse(null);

        if (existingBet != null) {
            if (series.getStatus() == SeriesStatus.LIVE || series.getStatus() == SeriesStatus.ENDED)
                throw new RuntimeException("Матч уже начался. Изменение ставки запрещено!");

            existingBet.setTeamName(matchOutcomeBetPlaceDto.teamName());
            existingBet.setMatchOutcomeResult(matchOutcomeBetPlaceDto.matchOutcomeResult());
            existingBet.setCoefficient(MATCH_OUTCOME_COEFFICIENT);
            existingBet.setWinningsAmount((short) (BET_AMOUNT * MATCH_OUTCOME_COEFFICIENT));
            log.info("Updated existing match outcome bet: {}", existingBet);
            betsRepository.save(existingBet);
            return;
        }

        MatchOutcomeBet newBet = new MatchOutcomeBet();
        newBet.setSeries(series);
        newBet.setTeamName(matchOutcomeBetPlaceDto.teamName());
        newBet.setMatchOutcomeResult(matchOutcomeBetPlaceDto.matchOutcomeResult());
        newBet.setBetAmount(BET_AMOUNT);
        newBet.setCoefficient(MATCH_OUTCOME_COEFFICIENT);
        newBet.setUser(userService.getUserByUsername(username));
        newBet.setWinningsAmount((short) (BET_AMOUNT * MATCH_OUTCOME_COEFFICIENT));
        log.info("Placed new match outcome bet: {}", newBet);
        betsRepository.save(newBet);
    }

    public void placeTotalRoundsBet(TotalRoundsBetPlaceDto totalRoundsBetPlaceDto, String username) {
        Series series = seriesService.getSeries(totalRoundsBetPlaceDto.seriesId());

        if (series.getTeam1Players().contains(username) || series.getTeam2Players().contains(username))
            throw new RuntimeException("322 на свои матчи запрещены!");

        if(series.getTeam1Players().isEmpty() || series.getTeam2Players().isEmpty()) {
            throw new RuntimeException("Нельзя сделать ставку на неизвестность :/");
        }

        if (series.getSeriesFormat().equals(SeriesFormat.BO1) &&
                (totalRoundsBetPlaceDto.roundsCount() < 18 ||
                        totalRoundsBetPlaceDto.roundsCount() > 23)) {
            throw new RuntimeException("Для БО1 можно ставить на тотал раундов в 17.5Б(М) - 22.5Б(М)");
        }

        if (series.getSeriesFormat().equals(SeriesFormat.BO3) &&
                totalRoundsBetPlaceDto.roundsCount() != 2) {
            throw new RuntimeException("Для БО3 можно ставить на тотал карт: 2.5Б 2.5М");
        }

        TotalRoundsBet existingBet = betsRepository.findTotalRoundsBetByUserAndSeries_Id(
                        userService.getUserByUsername(username), series.getId())
                .orElse(null);

        if (existingBet != null) {
            if (series.getStatus() == SeriesStatus.LIVE || series.getStatus() == SeriesStatus.ENDED)
                throw new RuntimeException("Матч уже начался. Изменение ставки запрещено!");
            existingBet.setRoundsCount(totalRoundsBetPlaceDto.roundsCount());
            existingBet.setType(totalRoundsBetPlaceDto.type());
            existingBet.setCoefficient(TOTAL_ROUNDS_COEFFICIENT);
            existingBet.setWinningsAmount((short) ((BET_AMOUNT - 100) * TOTAL_ROUNDS_COEFFICIENT));
            log.info("Updated existing total rounds bet: {}", existingBet);
            betsRepository.save(existingBet);
            return;
        }

        TotalRoundsBet newBet = new TotalRoundsBet();
        newBet.setSeries(series);
        newBet.setUser(userService.getUserByUsername(username));
        newBet.setRoundsCount(totalRoundsBetPlaceDto.roundsCount());
        newBet.setType(totalRoundsBetPlaceDto.type());
        newBet.setBetAmount((short) (BET_AMOUNT - 100));
        newBet.setCoefficient(TOTAL_ROUNDS_COEFFICIENT);
        newBet.setWinningsAmount((short) (BET_AMOUNT * TOTAL_ROUNDS_COEFFICIENT));
        log.info("Placed new total rounds bet: {}", newBet);
        betsRepository.save(newBet);
    }

    @Transactional
    public void withdrawalPlacedBets(Long matchId) {
        Series series = seriesService.getSeries(matchId);
        List<Bet> bets = betsRepository.findAllBySeries_Id(matchId);
        for (Bet bet : bets) {
            processBet(series, bet);
        }
        log.info("Withdrawal for match {} completed ({} bets processed)", matchId, bets.size());
    }

    private void processBet(Series series, Bet bet) {
        User user = bet.getUser();
        boolean won = isBetWinning(series, bet);

        updateBetAndUserBalances(bet, user, won);
        updateUserStatistics(user);

        betsRepository.save(bet);
        userService.save(user);
    }

    private boolean isBetWinning(Series series, Bet bet) {
        if (bet instanceof MatchOutcomeBet matchOutcomeBet) {
            return isMatchOutcomeBetWinning(series, matchOutcomeBet);
        } else if (bet instanceof TotalRoundsBet totalRoundsBet) {
            return isTotalRoundsBetWinning(series, totalRoundsBet);
        }
        return false;
    }

    private boolean isMatchOutcomeBetWinning(Series series, MatchOutcomeBet bet) {
        String winnerTeam = series.getSeriesResult() == SeriesResult.TEAM1_WON
                ? series.getTeam1Name()
                : series.getTeam2Name();

        return bet.getTeamName().equals(winnerTeam);
    }

    private boolean isTotalRoundsBetWinning(Series series, TotalRoundsBet bet) {
        List<Match> matches = series.getMatches();
        short total;
        if (series.getSeriesFormat().equals(SeriesFormat.BO1)) {
            total = (short) (matches.get(0).getTeam1Rounds() + matches.get(0).getTeam2Rounds());
        } else {
            total = (short) (series.getTeam1Score() + series.getTeam2Score());
        }
        return switch (bet.getType()) {
            case OVER -> total > bet.getRoundsCount();
            case UNDER -> total <= bet.getRoundsCount();
        };
    }

    private void updateBetAndUserBalances(Bet bet, User user, boolean won) {
        user.setCreditBalance((short) (user.getCreditBalance() - bet.getBetAmount()));
        if (won) {
            bet.setBetResult(BetResult.WON);
            user.setWinningsBalance((short) (user.getWinningsBalance() + bet.getWinningsAmount()));
            log.info("Bet {} WON! +{} to user {}", bet.getId(), bet.getWinningsAmount(), user.getUsername());
        } else {
            bet.setBetResult(BetResult.LOSS);
            log.info("Bet {} LOST. -{} from user {}", bet.getId(), bet.getBetAmount(), user.getUsername());
        }
    }

    private void updateUserStatistics(User user) {
        user.setPlacedBetsCount(user.getPlacedBetsCount() + 1);

        long winCount = user.getPlacedBets().stream()
                .filter(b -> b.getBetResult() == BetResult.WON)
                .count();

        user.setBetsWinRate(winCount / (double) user.getPlacedBetsCount());
    }

    public List<BetDto> getAllBets() {
        return betsRepository.findAll().stream()
                .map(this::convertToBetDTO)
                .toList();
    }

    public List<BetDto> getAllBetsByMatchId(Long matchId) {
        return betsRepository.findAllBySeries_Id(matchId).stream()
                .map(this::convertToBetDTO)
                .toList();
    }

    public List<BetDto> getAllBetsByUser(String username) {
        return betsRepository.findAllByUser(userService.getUserByUsername(username)).stream()
                .map(this::convertToBetDTO)
                .toList();
    }

    public BetDto convertToBetDTO(Bet bet) {
        return new BetDto(
                bet.getId(),
                bet.getSeries().getId(),
                bet instanceof MatchOutcomeBet ? BetType.MATCH_WINNER : BetType.TOTAL_ROUNDS,
                bet.getBetResult(),
                bet.getBetAmount(),
                bet.getCoefficient(),
                bet.getWinningsAmount(),
                bet.getTimestamp(),
                bet.getUser().getUsername(),
                bet.getSeries().getTeam1Players().get(0) + " " + bet.getSeries().getTeam1Players().get(1),
                bet.getSeries().getTeam2Players().get(0) + " " + bet.getSeries().getTeam2Players().get(1),
                bet instanceof MatchOutcomeBet ? ((MatchOutcomeBet) bet).getTeamName() : null,
                bet instanceof MatchOutcomeBet ? MatchOutcomeResult.valueOf(String.valueOf(((MatchOutcomeBet) bet).getMatchOutcomeResult())) : null,
                bet instanceof TotalRoundsBet ? TotalRoundsBetType.valueOf(String.valueOf(((TotalRoundsBet) bet).getType())) : null,
                bet instanceof TotalRoundsBet ? ((TotalRoundsBet) bet).getRoundsCount() : (short) 0
        );
    }

    public Integer forStream(Long matchId) {
        return betsRepository.findAllBySeries_Id(matchId).stream().mapToInt(Bet::getBetAmount).sum();
    }
}
