package com.yourwatchrental.watchrental.watch.watchhistory;

import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.WatchRepository;
import com.yourwatchrental.watchrental.watch.exceptions.WatchNotFoundException;
import com.yourwatchrental.watchrental.watch.watchhistory.dto.WatchHistoryRequestDTO;
import com.yourwatchrental.watchrental.watch.watchhistory.dto.WatchHistoryResponseDTO;
import com.yourwatchrental.watchrental.watch.watchhistory.exceptions.WatchHistoryNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchHistoryService {
    private final WatchHistoryRepository watchHistoryRepository;
    private final WatchRepository watchRepository;
    private final WatchHistoryMapper watchHistoryMapper;

    @Transactional
    public WatchHistoryResponseDTO createHistory(UUID watchId, WatchHistoryRequestDTO request) {

        Watch watch = watchRepository.findById(watchId)
                .orElseThrow(() -> new WatchNotFoundException(watchId));

        WatchHistory watchHistory = watchHistoryRepository.save(watchHistoryMapper.toEntity(request, watch));
        return watchHistoryMapper.toResponseDTO(watchHistory);
    }

    @Transactional
    public List<WatchHistoryResponseDTO> getHistoryByWatch(UUID watchId) {

        Watch watch = watchRepository.findById(watchId)
                .orElseThrow(() -> new WatchNotFoundException(watchId));

        return watchHistoryRepository.findAllByWatch(watch)
                .stream()
                .map(watchHistoryMapper::toResponseDTO)
                .toList();

    }

    @Transactional
    public WatchHistoryResponseDTO updateHistory(UUID id, WatchHistoryRequestDTO request) {

        WatchHistory watchHistory = watchHistoryRepository.findById(id)
                .orElseThrow(() -> new WatchHistoryNotFoundException(id));

        watchHistoryMapper.updateEntityFromDTO(request, watchHistory);

        return watchHistoryMapper.toResponseDTO(
                watchHistoryRepository.save(watchHistory)
        );
    }

    @Transactional
    public void deleteHistory(UUID id) {

        WatchHistory watchHistory = watchHistoryRepository.findById(id)
                .orElseThrow(() -> new WatchHistoryNotFoundException(id));

        watchHistoryRepository.delete(watchHistory);
    }
}
