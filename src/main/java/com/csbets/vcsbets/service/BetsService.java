package com.csbets.vcsbets.service;

import com.csbets.vcsbets.dto.bet.*;
import com.csbets.vcsbets.entity.bet.*;
import com.csbets.vcsbets.entity.match.Match;
import com.csbets.vcsbets.entity.match.MatchResult;
import com.csbets.vcsbets.entity.match.MatchStatus;
import com.csbets.vcsbets.entity.user.User;
import com.csbets.vcsbets.repository.BetsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class BetsService {

    private final BetsRepository betsRepository;
    private final MatchService matchService;
    private final UserService userService;

    private static final short BET_AMOUNT = 150;
    private static final short MATCH_OUTCOME_COEFFICIENT = 2;
    private static final double TOTAL_ROUNDS_COEFFICIENT = 1.5;

    public void placeMatchOutcomeBet(MatchOutcomeBetPlaceDto matchOutcomeBetPlaceDto, String username) {
        Match match = matchService.getMatch(matchOutcomeBetPlaceDto.matchId());

        if (userService.getUserByUsername(username).getCreditBalance() < BET_AMOUNT)
            throw new RuntimeException("Средств на балансе не хватает чуток(");

        if (match.getTeam1Players().contains(username) || match.getTeam2Players().contains(username))
            throw new RuntimeException("322 на свои матчи запрещены!");

        MatchOutcomeBet existingBet = (MatchOutcomeBet) betsRepository.findMatchOutcomeBetByUserAndMatch_Id(
                        userService.getUserByUsername(username), match.getId())
                .orElse(null);

        if (existingBet != null) {
            if (match.getStatus() == MatchStatus.LIVE || match.getStatus() == MatchStatus.ENDED)
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
        newBet.setMatch(match);
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
        Match match = matchService.getMatch(totalRoundsBetPlaceDto.matchId());

        if (match.getTeam1Players().contains(username) || match.getTeam2Players().contains(username))
            throw new RuntimeException("322 на свои матчи запрещены!");

        TotalRoundsBet existingBet = (TotalRoundsBet) betsRepository.findTotalRoundsBetByUserAndMatch_Id(
                        userService.getUserByUsername(username), match.getId())
                .orElse(null);

        if (existingBet != null) {
            if (match.getStatus() == MatchStatus.LIVE || match.getStatus() == MatchStatus.ENDED)
                throw new RuntimeException("Матч уже начался. Изменение ставки запрещено!");
            existingBet.setRoundsCount(totalRoundsBetPlaceDto.roundsCount());
            existingBet.setType(totalRoundsBetPlaceDto.type());
            existingBet.setCoefficient(TOTAL_ROUNDS_COEFFICIENT);
            existingBet.setWinningsAmount((short) (BET_AMOUNT * TOTAL_ROUNDS_COEFFICIENT));
            log.info("Updated existing total rounds bet: {}", existingBet);
            betsRepository.save(existingBet);
            return;
        }

        TotalRoundsBet newBet = new TotalRoundsBet();
        newBet.setMatch(match);
        newBet.setUser(userService.getUserByUsername(username));
        newBet.setRoundsCount(totalRoundsBetPlaceDto.roundsCount());
        newBet.setType(totalRoundsBetPlaceDto.type());
        newBet.setBetAmount(BET_AMOUNT);
        newBet.setCoefficient(TOTAL_ROUNDS_COEFFICIENT);
        newBet.setWinningsAmount((short) (BET_AMOUNT * TOTAL_ROUNDS_COEFFICIENT));
        log.info("Placed new total rounds bet: {}", newBet);
        betsRepository.save(newBet);
    }

    @Transactional
    public void withdrawalPlacedBets(Long matchId) {
        Match match = matchService.getMatch(matchId);
        List<Bet> bets = betsRepository.findAllByMatch_Id(matchId);
        for (Bet bet : bets) {
            processBet(match, bet);
        }
        log.info("Withdrawal for match {} completed ({} bets processed)", matchId, bets.size());
    }

    private void processBet(Match match, Bet bet) {
        User user = bet.getUser();
        boolean won = isBetWinning(match, bet);

        updateBetAndUserBalances(bet, user, won);
        updateUserStatistics(user);

        betsRepository.save(bet);
        userService.save(user);
    }

    private boolean isBetWinning(Match match, Bet bet) {
        if (bet instanceof MatchOutcomeBet matchOutcomeBet) {
            return isMatchOutcomeBetWinning(match, matchOutcomeBet);
        } else if (bet instanceof TotalRoundsBet totalRoundsBet) {
            return isTotalRoundsBetWinning(match, totalRoundsBet);
        }
        return false;
    }

    private boolean isMatchOutcomeBetWinning(Match match, MatchOutcomeBet bet) {
        String winnerTeam = match.getMatchResult() == MatchResult.TEAM1_WON
                ? match.getTeam1Name()
                : match.getTeam2Name();

        return bet.getTeamName().equals(winnerTeam);
    }

    private boolean isTotalRoundsBetWinning(Match match, TotalRoundsBet bet) {
        short totalRounds = (short) (match.getTeam1Score() + match.getTeam2Score());
        return switch (bet.getType()) {
            case OVER -> totalRounds > bet.getRoundsCount();
            case UNDER -> totalRounds < bet.getRoundsCount();
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
        return betsRepository.findAllByMatch_Id(matchId).stream()
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
                bet.getUser().getSteamLink(),
                bet.getMatch().getId(),
                bet instanceof MatchOutcomeBet ? BetType.MATCH_WINNER : BetType.TOTAL_ROUNDS,
                bet.getBetResult(),
                bet.getBetAmount(),
                bet.getCoefficient(),
                bet.getWinningsAmount(),
                bet.getTimestamp(),
                bet.getUser().getUsername(),
                bet instanceof MatchOutcomeBet ? ((MatchOutcomeBet) bet).getTeamName() : null,
                bet instanceof MatchOutcomeBet ? MatchOutcomeResult.valueOf(String.valueOf(((MatchOutcomeBet) bet).getMatchOutcomeResult())) : null,
                bet instanceof TotalRoundsBet ? TotalRoundsBetType.valueOf(String.valueOf(((TotalRoundsBet) bet).getType())) : null,
                bet instanceof TotalRoundsBet ? ((TotalRoundsBet) bet).getRoundsCount() : (short) 0
        );
    }

}
