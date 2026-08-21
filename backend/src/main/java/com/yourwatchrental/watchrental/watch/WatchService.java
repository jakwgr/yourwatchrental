package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.branch.Branch;
import com.yourwatchrental.watchrental.branch.BranchRepository;
import com.yourwatchrental.watchrental.branch.exceptions.BranchNotFoundException;
import com.yourwatchrental.watchrental.watch.dto.request.*;
import com.yourwatchrental.watchrental.watch.dto.response.WatchCardResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchFullInfoResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchFullInfoResponseDTO;
import com.yourwatchrental.watchrental.watch.exceptions.WatchNotFoundException;
import com.yourwatchrental.watchrental.watch.exceptions.WatchSameSerialNumberAsBeforeException;
import com.yourwatchrental.watchrental.watch.exceptions.WatchSameSerialNumberException;
import com.yourwatchrental.watchrental.watch.specification.WatchSpecification;
import jakarta.transaction.Transactional;
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
    private final BranchRepository branchRepository;

    public WatchFullInfoResponseDTO createWatch(WatchRequestDTO request)
    {
        if(watchRepository.existsBySerialNumber(request.serialNumber())) throw new WatchSameSerialNumberException();

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new BranchNotFoundException(null));

        Watch watch = watchMapper.toEntity(request);
        watch.setBranch(branch);
        Watch createdWatch = watchRepository.save(watch);

        return watchMapper.toFullInfoDTO(createdWatch);
    }

    public Page<WatchCardResponseDTO> getWatchPage(
            WatchFilterRequestDTO request,
            Pageable pageable)
    {
        Specification<Watch> specification = WatchSpecification.buildSpecification(request);

        return watchRepository.findAll(specification, pageable)
                .map(watchMapper::toCardDTO);
    }

    public WatchFullInfoResponseDTO getWatch(UUID id)
    {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new WatchNotFoundException(null));

        return watchMapper.toFullInfoDTO(watch);
    }

    public WatchFullInfoResponseDTO updateWatch(UUID id, WatchUpdateRequestDTO request)
    {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new WatchNotFoundException(id));

        watchMapper.updateEntityFromDTO(request, watch);

        return watchMapper.toFullInfoDTO(watchRepository.save(watch));
    }

    @Transactional
    public WatchFullInfoResponseDTO updateWatchSerialNumber(UUID id, WatchSerialNumberUpdateRequestDTO request)
    {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new WatchNotFoundException(id));

        if(watch.getSerialNumber().equals(request.serialNumber())) throw new WatchSameSerialNumberAsBeforeException();
        if(watchRepository.existsBySerialNumber(request.serialNumber())) throw new WatchSameSerialNumberException();

        watch.setSerialNumber(request.serialNumber());

        return watchMapper.toFullInfoDTO(watchRepository.save(watch));
    }

    public WatchFullInfoResponseDTO updateWatchBranch(UUID id, WatchBranchUpdateRequestDTO request)
    {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new WatchNotFoundException(id));

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new BranchNotFoundException(request.branchId()));


        watch.setBranch(branch);

        return watchMapper.toFullInfoDTO(watchRepository.save(watch));
    }


    public WatchFullInfoResponseDTO updateWatchStatus(UUID id, WatchStatusUpdateRequestDTO request)
    {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new WatchNotFoundException(id));

        watch.setStatus(request.status());
        return watchMapper.toFullInfoDTO(watchRepository.save((watch)));
    }

    public void hardDeleteWatch(UUID id)
    {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new WatchNotFoundException(id));

        watchRepository.delete(watch);
    }
}
