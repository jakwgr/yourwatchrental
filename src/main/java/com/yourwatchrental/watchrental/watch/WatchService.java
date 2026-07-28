package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.watch.dto.request.WatchFilterRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchStatusUpdateRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchPageResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchUpdateRequestDTO;
import com.yourwatchrental.watchrental.watch.exceptions.WatchNotFoundException;
import com.yourwatchrental.watchrental.watch.exceptions.WatchSameSerialNumberException;
import com.yourwatchrental.watchrental.watch.specification.WatchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchService {
    private final WatchRepository watchRepository;
    private final WatchMapper watchMapper;

    public WatchResponseDTO createWatch(WatchRequestDTO request)
    {
        if(watchRepository.existsBySerialNumber(request.serialNumber())) throw new WatchSameSerialNumberException();

        Watch watch = watchMapper.toEntity(request);
        Watch createdWatch = watchRepository.save(watch);

        return watchMapper.toResponseDTO(createdWatch);
    }

    public Page<WatchPageResponseDTO> getWatchPage(
            WatchFilterRequestDTO request,
            Pageable pageable)
    {
        Specification<Watch> specification = WatchSpecification.buildSpecification(request);

        return watchRepository.findAll(specification, pageable)
                .map(watchMapper::toWatchPageResponseDTO);
    }

    public WatchResponseDTO getWatch(UUID id)
    {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new WatchNotFoundException(null));

        return watchMapper.toResponseDTO(watch);
    }

    public WatchResponseDTO updateWatch(UUID id, WatchUpdateRequestDTO request)
    {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new WatchNotFoundException(id));

        watchMapper.updateEntityFromDTO(request, watch);

        return watchMapper.toResponseDTO(watchRepository.save(watch));
    }

    public WatchResponseDTO updateWatchStatus(UUID id, WatchStatusUpdateRequestDTO request)
    {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new WatchNotFoundException(id));

        watch.setStatus(request.status());
        return watchMapper.toResponseDTO(watchRepository.save((watch)));
    }

    public void hardDeleteWatch(UUID id)
    {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new WatchNotFoundException(id));

        watchRepository.delete(watch);
    }
}
