package nextstep.subway.station.application;

import static nextstep.subway.config.cache.CacheKey.LINE;
import static nextstep.subway.config.cache.CacheKey.LINES;
import static nextstep.subway.config.cache.CacheKey.PATH;
import static nextstep.subway.config.cache.CacheKey.STATION;
import static nextstep.subway.config.cache.CacheKey.STATIONS;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }


    @Caching(evict = {
            @CacheEvict(value = STATIONS),
            @CacheEvict(value = STATION, key = "#id"),
            @CacheEvict(value = PATH),
            @CacheEvict(value = LINES),
            @CacheEvict(value = LINE, key = "#id")
    })
    public StationResponse saveStation(StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    @Cacheable(value = STATIONS)
    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = STATIONS),
            @CacheEvict(value = STATION, key = "#id"),
            @CacheEvict(value = PATH),
            @CacheEvict(value = LINES),
            @CacheEvict(value = LINE, key = "#id")
    })
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    @Cacheable(value = STATION, key = "#id")
    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Cacheable(value = STATION, key = "#id")
    public Station findById(Long id) {
        return stationRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
