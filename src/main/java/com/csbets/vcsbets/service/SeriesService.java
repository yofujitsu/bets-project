package com.csbets.vcsbets.service;

import com.csbets.vcsbets.dto.match.SeriesDto;
import com.csbets.vcsbets.dto.match.SeriesInitDto;
import com.csbets.vcsbets.entity.match.Series;
import com.csbets.vcsbets.entity.match.SeriesFormat;
import com.csbets.vcsbets.entity.match.SeriesResult;
import com.csbets.vcsbets.entity.match.SeriesStatus;
import com.csbets.vcsbets.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final BetsService betsService;

    public Series getSeries(Long matchId) {
        return seriesRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Матч не найден"));
    }

    public SeriesDto getSeriesDto(Long matchId) {
        return convertToSeriesDTO(seriesRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Матч не найден")));
    }

    public List<SeriesDto> getAllSeries() {
        return seriesRepository.findAll().stream()
                .map(this::convertToSeriesDTO)
                .toList();
    }

    public void fillMatchData(Long id, SeriesInitDto seriesInitDto) {
        Series series = seriesRepository.findById(id).orElse(null);
        assert series != null;
        series.setTeam1Players(seriesInitDto.team1Players());
        series.setTeam2Players(seriesInitDto.team2Players());
        log.info("Set match data: id {}, {}, {}",
                id, series.getTeam1Name(), series.getTeam2Name());
        seriesRepository.save(series);
    }

    public void changeSeriesStatus(Long id, SeriesStatus seriesStatus) {
        Series series = seriesRepository.findById(id).orElse(null);
        assert series != null;
        series.setStatus(seriesStatus);
        log.info("Match {} status set to: {}", id, series.getStatus());
        seriesRepository.save(series);
    }

    @Transactional
    public void changeSeriesResult(Long id, boolean team1won) {
        Series series = seriesRepository.findById(id).orElse(null);
        assert series != null;
        if (team1won) {
            series.setTeam1Score((short) (series.getTeam1Score() + 1));
        } else {
            series.setTeam2Score((short) (series.getTeam2Score() + 1));
        }
        if (series.getSeriesFormat().equals(SeriesFormat.BO3)) {
            if (series.getTeam1Score() == 2 || series.getTeam2Score() == 2) {
                finalizeSeries(series);
            }
        } else if (series.getSeriesFormat().equals(SeriesFormat.BO1)) {
            finalizeSeries(series);
        }
        seriesRepository.save(series);
        switch(id.intValue()) {
            case 1,2,3,4: processFirstUBRound(); break;
            case 5,6: processFirstLBRound(); break;
            case 7,8: processSecondUBRound(); break;
            case 9,10: processSecondLBRound(); break;
            case 11: processThirdLbRound(); break;
            case 12: processUBFinals(); break;
            case 13: processLBFinals(); break;
        }
    }

    private void finalizeSeries(Series series) {
        series.setSeriesResult(series.getTeam1Score() > series.getTeam2Score() ? SeriesResult.TEAM1_WON : SeriesResult.TEAM2_WON);
        log.info("Series {} result set to: {}", series.getId(), series.getSeriesResult());
        changeSeriesStatus(series.getId(), SeriesStatus.ENDED);
        betsService.withdrawalPlacedBets(series.getId());
    }

    private SeriesDto convertToSeriesDTO(Series series) {
        return new SeriesDto(
                series.getId(),
                series.getTeam1Name(),
                series.getTeam2Name(),
                series.getTeam1Players(),
                series.getTeam2Players(),
                series.getTeam1Score(),
                series.getTeam2Score(),
                series.getStatus(),
                series.getSeriesResult(),
                series.getSeriesFormat()
        );
    }

    private void processFirstUBRound() {
        List<Series> ubR1Series = seriesRepository.findAllById(List.of(1L, 2L, 3L, 4L));
        for (int i = 0; i <= 3; ++i) {
            if (ubR1Series.get(i).getStatus().equals(SeriesStatus.ENDED)) {
                List<SeriesResult> winners = ubR1Series.stream().map(Series::getSeriesResult).toList();
                Series seriesForWinners, seriesForLosers;
                Series current = ubR1Series.get(i);
                List<String> winnerTeam = winners.get(i).equals(SeriesResult.TEAM1_WON)
                        ? current.getTeam1Players() : current.getTeam2Players();
                List<String> loserTeam = !winners.get(i).equals(SeriesResult.TEAM1_WON)
                        ? current.getTeam1Players() : current.getTeam2Players();
                if (i <= 1) {
                    seriesForWinners = seriesRepository.findById(7L).orElse(null);
                    seriesForLosers = seriesRepository.findById(5L).orElse(null);
                    if (i % 2 == 0) {
                        seriesForLosers.setTeam1Players(new ArrayList<>(loserTeam));
                        seriesForWinners.setTeam1Players(new ArrayList<>(winnerTeam));
                    } else {
                        seriesForLosers.setTeam2Players(new ArrayList<>(loserTeam));
                        seriesForWinners.setTeam2Players(new ArrayList<>(winnerTeam));
                    }
                } else {
                    seriesForWinners = seriesRepository.findById(8L).orElse(null);
                    seriesForLosers = seriesRepository.findById(6L).orElse(null);
                    if (i % 2 == 0) {
                        seriesForLosers.setTeam1Players(new ArrayList<>(loserTeam));
                        seriesForWinners.setTeam1Players(new ArrayList<>(winnerTeam));
                    } else {
                        seriesForLosers.setTeam2Players(new ArrayList<>(loserTeam));
                        seriesForWinners.setTeam2Players(new ArrayList<>(winnerTeam));
                    }
                }
                seriesRepository.saveAll(List.of(seriesForWinners, seriesForLosers));
            }
        }
    }

    private void processSecondUBRound() {
        List<Series> ubR2Series = seriesRepository.findAllById(List.of(7L, 8L));
        for (int i = 0; i <= 1; ++i) {
            if (ubR2Series.get(i).getStatus().equals(SeriesStatus.ENDED)) {
                List<SeriesResult> winners = ubR2Series.stream().map(Series::getSeriesResult).toList();
                Series seriesForWinners = seriesRepository.findById(12L).orElse(null),
                        seriesForLosers;
                Series current = ubR2Series.get(i);
                List<String> winnerTeam = winners.get(i).equals(SeriesResult.TEAM1_WON)
                        ? current.getTeam1Players() : current.getTeam2Players();
                List<String> loserTeam = !winners.get(i).equals(SeriesResult.TEAM1_WON)
                        ? current.getTeam1Players() : current.getTeam2Players();
                if (i == 0) {
                    seriesForLosers = seriesRepository.findById(9L).orElse(null);
                    seriesForLosers.setTeam1Players(new ArrayList<>(loserTeam));
                    seriesForWinners.setTeam1Players(new ArrayList<>(winnerTeam));
                } else {
                    seriesForLosers = seriesRepository.findById(10L).orElse(null);
                    seriesForLosers.setTeam1Players(new ArrayList<>(loserTeam));
                    seriesForWinners.setTeam2Players(new ArrayList<>(winnerTeam));
                }
                seriesRepository.saveAll(List.of(seriesForWinners, seriesForLosers));
            }
        }
    }

    private void processLBFinals() {
        Series lbR4Series = seriesRepository.findById(13L).orElse(null);
        if (lbR4Series.getStatus().equals(SeriesStatus.ENDED)) {
            Series seriesForWinners = seriesRepository.findById(14L).orElse(null);
            List<String> winnerTeam = lbR4Series.getSeriesResult().equals(SeriesResult.TEAM1_WON)
                    ? lbR4Series.getTeam1Players() : lbR4Series.getTeam2Players();
            seriesForWinners.setTeam2Players(new ArrayList<>(winnerTeam));
            seriesRepository.save(seriesForWinners);
        }
    }

    private void processUBFinals() {
        Series ubR4Series = seriesRepository.findById(12L).orElse(null);
        if (ubR4Series.getStatus().equals(SeriesStatus.ENDED)) {
            Series seriesForWinners = seriesRepository.findById(14L).orElse(null);
            List<String> winnerTeam = ubR4Series.getSeriesResult().equals(SeriesResult.TEAM1_WON)
                    ? ubR4Series.getTeam1Players() : ubR4Series.getTeam2Players();
            seriesForWinners.setTeam1Players(new ArrayList<>(winnerTeam));
            seriesRepository.save(seriesForWinners);
        }
    }

    private void processThirdLbRound() {
        Series lbR3Series = seriesRepository.findById(11L).orElse(null);
        if (lbR3Series.getStatus().equals(SeriesStatus.ENDED)) {
            Series seriesForWinners = seriesRepository.findById(13L).orElse(null);
            List<String> winnerTeam = lbR3Series.getSeriesResult().equals(SeriesResult.TEAM1_WON)
                    ? lbR3Series.getTeam1Players() : lbR3Series.getTeam2Players();
            seriesForWinners.setTeam2Players(new ArrayList<>(winnerTeam));
            seriesRepository.save(seriesForWinners);
        }
    }

    private void processSecondLBRound() {
        List<Series> lbR2Series = seriesRepository.findAllById(List.of(9L, 10L));
        for (int i = 0; i <= 1; ++i) {
            if (lbR2Series.get(i).getStatus().equals(SeriesStatus.ENDED)) {
                List<SeriesResult> winners = lbR2Series.stream().map(Series::getSeriesResult).toList();
                Series seriesForWinners = seriesRepository.findById(11L).orElse(null);
                Series current = lbR2Series.get(i);
                List<String> winnerTeam = winners.get(i).equals(SeriesResult.TEAM1_WON)
                        ? current.getTeam1Players() : current.getTeam2Players();
                if (i == 0) {
                    seriesForWinners.setTeam1Players(new ArrayList<>(winnerTeam));
                } else {
                    seriesForWinners.setTeam2Players(new ArrayList<>(winnerTeam));
                }
                seriesRepository.save(seriesForWinners);
            }
        }
    }

    private void processFirstLBRound() {
        List<Series> lbR1Series = seriesRepository.findAllById(List.of(5L, 6L));
        for (int i = 0; i <= 1; ++i) {
            if (lbR1Series.get(i).getStatus().equals(SeriesStatus.ENDED)) {
                List<SeriesResult> winners = lbR1Series.stream().map(Series::getSeriesResult).toList();
                Series seriesForWinners;
                Series current = lbR1Series.get(i);
                List<String> winnerTeam = winners.get(i).equals(SeriesResult.TEAM1_WON)
                        ? current.getTeam1Players() : current.getTeam2Players();
                if (i == 0) {
                    seriesForWinners = seriesRepository.findById(10L).orElse(null);
                    seriesForWinners.setTeam2Players(new ArrayList<>(winnerTeam));
                } else {
                    seriesForWinners = seriesRepository.findById(9L).orElse(null);
                    seriesForWinners.setTeam2Players(new ArrayList<>(winnerTeam));
                }
                seriesRepository.save(seriesForWinners);
            }
        }
    }
}
