package com.yourwatchrental.watchrental.branch;

import com.yourwatchrental.watchrental.branch.dto.BranchRequestDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchResponseDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchStatusUpdateRequestDTO;
import com.yourwatchrental.watchrental.branch.exceptions.BranchEmailUsedException;
import com.yourwatchrental.watchrental.branch.exceptions.BranchNotFoundException;
import com.yourwatchrental.watchrental.branch.exceptions.BranchPhoneNumberUsedException;
import com.yourwatchrental.watchrental.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private BranchMapper branchMapper;
    @InjectMocks
    private BranchService branchService;

    @Test
    void shouldCreateBranch()
    {
        BranchRequestDTO request = new BranchRequestDTO(
                "City branch1 ",
                "Branch 1",
                "Branch street 1",
                "123456789",
                "branch@test.com"
        );

        Branch branch = new Branch();

        BranchResponseDTO response = mock(BranchResponseDTO.class);

        when(branchRepository.existsByEmail(request.email()))
                .thenReturn(false);
        when(branchRepository.existsByPhoneNumber(request.phoneNumber()))
                .thenReturn(false);

        when(branchMapper.toEntity(request))
                .thenReturn(branch);

        when(branchRepository.save(branch))
                .thenReturn(branch);

        when(branchMapper.toResponseDTO(branch))
                .thenReturn(response);


        BranchResponseDTO result =
                branchService.createBranch(request);

        assertNotNull(result);

        assertEquals(
                BranchStatus.ACTIVE,
                branch.getStatus()
        );

        verify(branchRepository)
                .save(branch);

        verify(branchMapper)
                .toResponseDTO(branch);
    }

    @Test
    void ShouldThrowWhenEmailAlreadyExists()
    {
        BranchRequestDTO request = new BranchRequestDTO(
                "City branch1 ",
                "Branch 1",
                "Branch street 1",
                "123456789",
                "branch@test.com"
        );

        when(branchRepository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                BranchEmailUsedException.class,
                () -> branchService.createBranch(request)
        );

        verify(branchRepository, never())
                .save(any());

    }

    @Test
    void shouldThrowWhenPhoneAlreadyExists()
    {
        BranchRequestDTO request = new BranchRequestDTO(
                "City branch1 ",
                "Branch 1",
                "Branch street 1",
                "123456789",
                "branch@test.com"
        );

        when(branchRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(branchRepository.existsByPhoneNumber(request.phoneNumber()))
                .thenReturn(true);

        assertThrows(
                BranchPhoneNumberUsedException.class,
                () -> branchService.createBranch(request)
        );

        verify(branchRepository, never())
                .save(any()
        );
    }

    @Test
    void ShouldReturnBranchById()
    {
        UUID id = UUID.randomUUID();

        Branch branch = new Branch();

        BranchResponseDTO response = new BranchResponseDTO(
                id,
                "City branch1 ",
                "Branch 1",
                "Branch street 1",
                "123456789",
                "branch@test.com",
                BranchStatus.ACTIVE
        );

        when(branchRepository.findById(id))
                .thenReturn(Optional.of(branch));

        when(branchMapper.toResponseDTO(branch))
                .thenReturn(response);

        BranchResponseDTO result =
                branchService.getBranchById(id);

        assertEquals(response,result);

        verify(branchRepository)
                .findById(id);
        verify(branchMapper)
                .toResponseDTO(branch);
    }

    @Test
    void ShouldThrowBranchNotExists1()
    {
        UUID id = UUID.randomUUID();

        when(branchRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(BranchNotFoundException.class,
        () -> branchService.getBranchById(id));

        verify(branchRepository)
                .findById(id);
    }

    @Test
    void shouldUpdateBranch()
    {
        UUID id = UUID.randomUUID();

        BranchRequestDTO request = new BranchRequestDTO(
                "City branch1 ",
                "Branch 1",
                "Branch street 1",
                "123456789",
                "branch@test.com"
        );

        BranchResponseDTO response = new BranchResponseDTO(
                id,
                "City branch1 ",
                "Branch 1",
                "Branch street 1",
                "123456789",
                "branch@test.com",
                BranchStatus.ACTIVE
        );

        Branch branch = new Branch();

        when(branchRepository.findById(id))
                .thenReturn(Optional.of(branch));

        when(branchRepository.existsByPhoneNumber(request.phoneNumber()))
                .thenReturn(false);

        when(branchRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(branchRepository.save(branch))
                .thenReturn(branch);

        when(branchMapper.toResponseDTO(branch))
                .thenReturn(response);

        BranchResponseDTO result =
                branchService.updateBranch(id, request);

        verify(branchRepository).findById(id);
        verify(branchMapper).updateEntityFromDTO(request, branch);
        verify(branchRepository).save(branch);
        verify(branchMapper).toResponseDTO(branch);

        assertEquals(response, result);
    }

    @Test
    void ShouldThrowEmailUsed()
    {


        UUID id = UUID.randomUUID();

        BranchRequestDTO request = new BranchRequestDTO(
                "City branch1 ",
                "Branch 1",
                "Branch street 1",
                "123456789",
                "branch@test.com"
        );

        Branch branch = new Branch();

        branch.setEmail("test@test.pl");
        branch.setPhoneNumber("000000000");

        when(branchRepository.findById(id))
                .thenReturn(Optional.of(branch));

        when(branchRepository.existsByPhoneNumber(request.phoneNumber()))
                .thenReturn(false);

        when(branchRepository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                BranchEmailUsedException.class,
                () -> branchService.updateBranch(id, request)
        );

        verify(branchRepository, never()).save(any());
    }

    @Test
    void ShouldThrowPhoneUsed()
    {


        UUID id = UUID.randomUUID();

        BranchRequestDTO request = new BranchRequestDTO(
                "City branch1 ",
                "Branch 1",
                "Branch street 1",
                "123456789",
                "branch@test.com"
        );

        Branch branch = new Branch();

        branch.setEmail("test@test.pl");
        branch.setPhoneNumber("000000000");

        when(branchRepository.findById(id))
                .thenReturn(Optional.of(branch));

        when(branchRepository.existsByPhoneNumber(request.phoneNumber()))
                .thenReturn(true);


        assertThrows(
                BranchPhoneNumberUsedException.class,
                () -> branchService.updateBranch(id, request)
        );

        verify(branchRepository, never()).save(any());
    }

    @Test
    void ShouldUpdateBranchStatus()
    {
        UUID id = UUID.randomUUID();

        BranchResponseDTO response = new BranchResponseDTO(
                id,
                "City branch1 ",
                "Branch 1",
                "Branch street 1",
                "123456789",
                "branch@test.com",
                BranchStatus.ACTIVE
        );

        BranchStatusUpdateRequestDTO request = new BranchStatusUpdateRequestDTO(
                BranchStatus.ACTIVE
        );

        Branch branch = new Branch();

        when(branchRepository.findById(id))
                .thenReturn(Optional.of(branch));

        when(branchRepository.save(branch))
                .thenReturn(branch);
        when(branchMapper.toResponseDTO(branch))
                .thenReturn(response);

        BranchResponseDTO result =
            branchService.updateBranchStatus(id, request);

        assertEquals(
                BranchStatus.ACTIVE,
                branch.getStatus()
        );

        assertEquals(response,result);

        verify(branchRepository).findById(id);
        verify(branchRepository).save(branch);
        verify(branchMapper).toResponseDTO(branch);
    }
}
