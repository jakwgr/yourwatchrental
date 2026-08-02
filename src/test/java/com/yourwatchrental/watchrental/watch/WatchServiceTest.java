package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.branch.Branch;
import com.yourwatchrental.watchrental.branch.BranchRepository;
import com.yourwatchrental.watchrental.branch.dto.BranchShortResponseDTO;
import com.yourwatchrental.watchrental.branch.exceptions.BranchNotFoundException;
import com.yourwatchrental.watchrental.watch.dto.request.*;
import com.yourwatchrental.watchrental.watch.dto.response.WatchFullInfoResponseDTO;
import com.yourwatchrental.watchrental.watch.enums.*;
import com.yourwatchrental.watchrental.watch.exceptions.WatchNotFoundException;
import com.yourwatchrental.watchrental.watch.exceptions.WatchSameSerialNumberAsBeforeException;
import com.yourwatchrental.watchrental.watch.exceptions.WatchSameSerialNumberException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class WatchServiceTest {

    @Mock
    private WatchRepository watchRepository;
    @Mock
    private WatchMapper watchMapper;
    @Mock
    private BranchRepository branchRepository;
    @InjectMocks
    private WatchService watchService;

    WatchFullInfoResponseDTO watchFullInfoResponseDTO = new WatchFullInfoResponseDTO(
            UUID.randomUUID(),
            "Seiko",
            "Prospex Diver",
            "SRPE93K1",
            "SEIKO123456",
            "Japanese automatic diver watch with 200m water resistance",
            2022,
            new BigDecimal("80.00"),
            Condition.EXCELLENT,
            Gender.MALE,
            MovementType.AUTOMATIC,
            Status.AVAILABLE,
            WatchType.DIVER,
            new BranchShortResponseDTO(
                    UUID.randomUUID(),
                    "Krakow",
                    "Main Watch Branch",
                    "Main Street 15",
                    "123456789"
            ),
            List.of()
    );

    WatchRequestDTO requestDTO = new WatchRequestDTO(
            "Seiko",
            "Prospex Diver",
            "SRPE93K1",
            "SEIKO123456",
            "Automatic",
            "Japanese automatic diver watch",
            2022,
            new BigDecimal("80.00"),
            Condition.EXCELLENT,
            Gender.MALE,
            MovementType.AUTOMATIC,
            Status.AVAILABLE,
            WatchType.DIVER,
            UUID.randomUUID()
    );

    WatchUpdateRequestDTO updateRequestDTO = new WatchUpdateRequestDTO(
            "Seiko",
            "Prospex Diver",
            "SRPE93K1",
            "Automatic",
            "Updated Japanese automatic diver watch with 200m water resistance",
            2022,
            new BigDecimal("90.00"),
            Condition.EXCELLENT,
            Gender.MALE,
            MovementType.AUTOMATIC,
            WatchType.DIVER
    );

    WatchSerialNumberUpdateRequestDTO updateSerialRequest =
            new WatchSerialNumberUpdateRequestDTO(
                    UUID.randomUUID(),
                    "SEIKO987654"
            );


    @Test
    void shouldCreateWatch() {
        Watch watch = new Watch();
        Branch branch = new Branch();

        when(watchRepository.existsBySerialNumber(requestDTO.serialNumber()))
                .thenReturn(false);

        when(branchRepository.findById(requestDTO.branchId()))
                .thenReturn(Optional.of(branch));
        when(watchMapper.toEntity(requestDTO))
                .thenReturn(watch);
        when(watchRepository.save(watch))
                .thenReturn(watch);
        when(watchMapper.toFullInfoDTO(watch))
                .thenReturn(watchFullInfoResponseDTO);

        WatchFullInfoResponseDTO result = watchService.createWatch(requestDTO);

        assertEquals(result, watchFullInfoResponseDTO);

        assertEquals(branch, watch.getBranch()
        );

        verify(watchRepository)
                .existsBySerialNumber(requestDTO.serialNumber());

        verify(branchRepository)
                .findById(requestDTO.branchId());

        verify(watchRepository)
                .save(watch);

        verify(watchMapper)
                .toFullInfoDTO(watch);
    }

    @Test
    void ShouldThrowSameSerialNumber1() {
        when(watchRepository.existsBySerialNumber(requestDTO.serialNumber()))
                .thenReturn(true);

        assertThrows(WatchSameSerialNumberException.class,
                () -> watchService.createWatch(requestDTO));

        verify(watchRepository, never())
                .save(any());

    }

    @Test
    void ShouldThrowBranchNotFound1() {
        when(watchRepository.existsBySerialNumber(requestDTO.serialNumber()))
                .thenReturn(false);

        when(branchRepository.findById(requestDTO.branchId()))
                .thenReturn(Optional.empty());

        assertThrows(BranchNotFoundException.class,
                () -> watchService.createWatch(requestDTO));

        verify(watchRepository, never())
                .save(any());

    }

    @Test
    void shouldGetWatch() {
        Watch watch = new Watch();

        UUID id = UUID.randomUUID();
        when(watchRepository.findById(id))
                .thenReturn(Optional.of(watch));
        when(watchMapper.toFullInfoDTO(watch))
                .thenReturn(watchFullInfoResponseDTO);

        WatchFullInfoResponseDTO result = watchService.getWatch(id);

        assertEquals(result, watchFullInfoResponseDTO);

        verify(watchRepository).findById(id);
        verify(watchMapper).toFullInfoDTO(watch);
    }

    @Test
    void shouldThrowWatchNotFound1() {
        Watch watch = new Watch();

        UUID id = UUID.randomUUID();
        when(watchRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThrows(WatchNotFoundException.class,
                () -> watchService.getWatch(id));
    }

    @Test
    void shouldUpdateWatch() {
        Watch watch = new Watch();

        UUID id = UUID.randomUUID();
        when(watchRepository.findById(id))
                .thenReturn(Optional.of(watch));
        when(watchRepository.save(watch))
                .thenReturn(watch);
        when(watchMapper.toFullInfoDTO(watch))
                .thenReturn(watchFullInfoResponseDTO);

        WatchFullInfoResponseDTO result = watchService.updateWatch(id, updateRequestDTO);

        assertEquals(result, watchFullInfoResponseDTO);

        verify(watchRepository).findById(id);
        verify(watchRepository).save(watch);
        verify(watchMapper).updateEntityFromDTO(updateRequestDTO, watch);
        verify(watchMapper).toFullInfoDTO(watch);
    }

    @Test
    void shouldThrowWatchNotFound2() {
        Watch watch = new Watch();

        UUID id = UUID.randomUUID();
        when(watchRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThrows(WatchNotFoundException.class,
                () -> watchService.updateWatch(id, updateRequestDTO));

        verify(watchRepository, never()).save(any());
    }

    @Test
    void shouldUpdateSerialNumber() {
        Watch watch = new Watch();
        watch.setSerialNumber("test");

        UUID id = UUID.randomUUID();
        when(watchRepository.findById(id))
                .thenReturn(Optional.of(watch));
        when(watchRepository.existsBySerialNumber(updateSerialRequest.serialNumber()))
                .thenReturn(false);
        when(watchRepository.save(watch))
                .thenReturn(watch);
        when(watchMapper.toFullInfoDTO(watch))
                .thenReturn(watchFullInfoResponseDTO);

        WatchFullInfoResponseDTO result = watchService.updateWatchSerialNumber(id, updateSerialRequest);

        assertEquals(result, watchFullInfoResponseDTO);
        assertEquals(updateSerialRequest.serialNumber(), watch.getSerialNumber());

        verify(watchRepository).findById(id);
        verify(watchRepository).existsBySerialNumber(updateSerialRequest.serialNumber());
        verify(watchRepository).save(watch);
        verify(watchMapper).toFullInfoDTO(watch);
    }

    @Test
    void shouldThrowWatchNotFound3() {
        UUID id = UUID.randomUUID();
        when(watchRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThrows(WatchNotFoundException.class,
                () -> watchService.updateWatchSerialNumber(id, updateSerialRequest));

        verify(watchRepository, never()).save(any());
    }

    @Test
    void shouldThrowSameSerialNumber2() {

        Watch watch = new Watch();
        watch.setSerialNumber("test");

        UUID id = UUID.randomUUID();
        when(watchRepository.findById(id))
                .thenReturn(Optional.of(watch));
        when(watchRepository.existsBySerialNumber(updateSerialRequest.serialNumber()))
                .thenReturn(true);

        assertThrows(WatchSameSerialNumberException.class,
                () -> watchService.updateWatchSerialNumber(id, updateSerialRequest));

        verify(watchRepository).findById(id);
        verify(watchRepository, never()).save(any());
    }

    @Test
    void shouldThrowSameSerialAsBefore() {
        Watch watch = new Watch();
        watch.setSerialNumber(updateSerialRequest.serialNumber());

        UUID id = UUID.randomUUID();
        when(watchRepository.findById(id))
                .thenReturn(Optional.of(watch));

        assertThrows(WatchSameSerialNumberAsBeforeException.class,
                () -> watchService.updateWatchSerialNumber(id, updateSerialRequest));

        verify(watchRepository).findById(id);
        verify(watchRepository, never()).save(any());
    }

    @Test
    void shouldUpdateWatchStatus() {
        Watch watch = new Watch();

        UUID id = UUID.randomUUID();

        WatchStatusUpdateRequestDTO request =
                new WatchStatusUpdateRequestDTO(
                        Status.RENTED
                );

        when(watchRepository.findById(id))
                .thenReturn(Optional.of(watch));

        when(watchRepository.save(watch))
                .thenReturn(watch);

        when(watchMapper.toFullInfoDTO(watch))
                .thenReturn(watchFullInfoResponseDTO);


        WatchFullInfoResponseDTO result =
                watchService.updateWatchStatus(id, request);


        assertEquals(result, watchFullInfoResponseDTO);
        assertEquals(request.status(), watch.getStatus());


        verify(watchRepository).findById(id);
        verify(watchRepository).save(watch);
        verify(watchMapper).toFullInfoDTO(watch);
    }

    @Test
    void shouldThrowWatchNotFound4() {
        UUID id = UUID.randomUUID();

        WatchStatusUpdateRequestDTO request =
                new WatchStatusUpdateRequestDTO(
                        Status.RENTED
                );


        when(watchRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThrows(
                WatchNotFoundException.class,
                () -> watchService.updateWatchStatus(id, request)
        );


        verify(watchRepository, never())
                .save(any());
    }

    @Test
    void shouldUpdateWatchBranch() {
        Watch watch = new Watch();

        Branch branch = new Branch();

        UUID id = UUID.randomUUID();

        WatchBranchUpdateRequestDTO request =
                new WatchBranchUpdateRequestDTO(
                        UUID.randomUUID()
                );


        when(watchRepository.findById(id))
                .thenReturn(Optional.of(watch));

        when(branchRepository.findById(request.branchId()))
                .thenReturn(Optional.of(branch));

        when(watchMapper.toFullInfoDTO(watch))
                .thenReturn(watchFullInfoResponseDTO);


        WatchFullInfoResponseDTO result =
                watchService.updateWatchBranch(id, request);


        assertEquals(result, watchFullInfoResponseDTO);
        assertEquals(branch, watch.getBranch());


        verify(watchRepository).findById(id);
        verify(branchRepository).findById(request.branchId());
        verify(watchMapper).toFullInfoDTO(watch);
    }

    @Test
    void shouldThrowWatchNotFound5() {
        UUID id = UUID.randomUUID();

        WatchBranchUpdateRequestDTO request =
                new WatchBranchUpdateRequestDTO(
                        UUID.randomUUID()
                );


        when(watchRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThrows(WatchNotFoundException.class,
                () -> watchService.updateWatchBranch(id, request)
        );


        verify(branchRepository, never())
                .findById(any());
    }

    @Test
    void shouldThrowBranchNotFound() {
        Watch watch = new Watch();

        UUID id = UUID.randomUUID();

        WatchBranchUpdateRequestDTO request =
                new WatchBranchUpdateRequestDTO(
                        UUID.randomUUID()
                );


        when(watchRepository.findById(id))
                .thenReturn(Optional.of(watch));

        when(branchRepository.findById(request.branchId()))
                .thenReturn(Optional.empty());


        assertThrows(BranchNotFoundException.class,
                () -> watchService.updateWatchBranch(id, request)
        );


        verify(watchMapper, never())
                .toFullInfoDTO(any());
    }
}
