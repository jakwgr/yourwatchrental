package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.watch.dto.WatchFilterRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.WatchPageResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.WatchRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.WatchResponseDTO;
import com.yourwatchrental.watchrental.watch.exceptions.WatchSameReferenceNumberException;
import com.yourwatchrental.watchrental.watch.specification.WatchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WatchService {
    private final WatchRepository watchRepository;
    private final WatchMapper watchMapper;

    public WatchResponseDTO createWatch(WatchRequestDTO request)
    {
        if(watchRepository.existsByReferenceNumber(request.referenceNumber())) throw new WatchSameReferenceNumberException();

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
}
