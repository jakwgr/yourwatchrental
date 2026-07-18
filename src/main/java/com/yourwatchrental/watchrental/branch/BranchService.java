package com.yourwatchrental.watchrental.branch;

import com.yourwatchrental.watchrental.branch.dto.BranchFilterCriteriaRequest;
import com.yourwatchrental.watchrental.branch.exceptions.BranchEmailUsedException;
import com.yourwatchrental.watchrental.branch.exceptions.BranchNotFoundException;
import com.yourwatchrental.watchrental.branch.dto.BranchRequestDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchResponseDTO;
import com.yourwatchrental.watchrental.branch.exceptions.BranchPhoneNumberUsedException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {


    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

        public BranchResponseDTO createBranch(BranchRequestDTO request){
            if(branchRepository.existsByEmail(request.email()))
            {
                throw new BranchEmailUsedException();
            }
            else if(branchRepository.existsByPhoneNumber(request.phoneNumber()))
            {
                throw new BranchPhoneNumberUsedException();
            }
            Branch branch = branchMapper.toEntity(request);
            Branch createdBranch = branchRepository.save(branch);
            return branchMapper.toResponseDTO(createdBranch);
        }

    public BranchResponseDTO getBranchById(UUID id)
    {
        return branchRepository.findById(id)
                .map(branchMapper::toResponseDTO)
                .orElseThrow(() -> new BranchNotFoundException(id));
    }

    public List<BranchResponseDTO> getBranches (BranchFilterCriteriaRequest criteria)
    {
        Branch probe = new Branch();
        probe.setName(criteria.name());
        probe.setEmail(criteria.email());
        probe.setCity(criteria.city());
        probe.setPhoneNumber(criteria.phoneNumber());
        probe.setAddress(criteria.address());

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();

        Example<Branch> example = Example.of(probe, matcher);

        return branchRepository.findAll(example)
                .stream()
                .map(branchMapper::toResponseDTO)
                .toList();
    }

    public BranchResponseDTO updateBranch(UUID id, BranchRequestDTO request)
    {
        Branch entity = branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));

        if(branchRepository.existsByPhoneNumber(request.phoneNumber()) && !entity.getPhoneNumber().equals(request.phoneNumber()))
        {
            throw new BranchPhoneNumberUsedException();
        }
        else if(branchRepository.existsByEmail(request.email()) && !entity.getEmail().equals(request.email()))
        {
            throw new BranchEmailUsedException();
        }
       branchMapper.updateEntityFromDTO(request, entity);

       Branch updatedBranch = branchRepository.save(entity);

        return branchMapper.toResponseDTO(updatedBranch);
    }

    void deleteBranch(UUID id)
    {
        Branch entity = branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));

        branchRepository.delete(entity);
    }
}
