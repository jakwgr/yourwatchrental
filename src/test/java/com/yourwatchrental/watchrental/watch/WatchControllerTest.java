package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.branch.BranchController;
import com.yourwatchrental.watchrental.branch.dto.BranchFilterCriteriaRequest;
import com.yourwatchrental.watchrental.branch.dto.BranchResponseDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchShortResponseDTO;
import com.yourwatchrental.watchrental.rental.RentalService;
import com.yourwatchrental.watchrental.security.CustomUserDetailsService;
import com.yourwatchrental.watchrental.security.JwUtil;
import com.yourwatchrental.watchrental.watch.dto.request.*;
import com.yourwatchrental.watchrental.watch.dto.response.WatchAvailabilityResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchCardResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchFullInfoResponseDTO;
import com.yourwatchrental.watchrental.watch.enums.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WatchController.class)
@AutoConfigureMockMvc(addFilters = false)
public class WatchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwUtil jwUtil;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    WatchService watchService;
    @MockitoBean
    RentalService rentalService;

    @Test
    void shouldCreateWatch() throws Exception
    {
        WatchRequestDTO request = new WatchRequestDTO(
                "Omega",
                "Seamaster Diver 300M",
                "210.30.42.20.03.001",
                "abcddfdfd",
                "Omega Movement",
                "Zegarek automatyczny w bardzo dobrym stanie",
                2025,
                new BigDecimal("150.00"),
                Condition.EXCELLENT,
                Gender.MALE,
                MovementType.AUTOMATIC,
                Status.AVAILABLE,
                WatchType.DIVER,
                UUID.randomUUID()
        );

        WatchFullInfoResponseDTO responseDTO = mock(WatchFullInfoResponseDTO.class);

        when(watchService.createWatch(any(WatchRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(
                post("/watches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(watchService).createWatch(any(WatchRequestDTO.class));
    }

    @Test
    void shouldGetWatchesPage() throws Exception
    {
        WatchCardResponseDTO responseDTO = mock(WatchCardResponseDTO.class);

        Page<WatchCardResponseDTO> page = new PageImpl<>(
                List.of(responseDTO)
        );

        WatchFilterRequestDTO request = mock(WatchFilterRequestDTO.class);

        when(watchService.getWatchPage(any(WatchFilterRequestDTO.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                        get("/watches")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk());

        verify(watchService)
                .getWatchPage(any(WatchFilterRequestDTO.class), any(Pageable.class));

    }

    @Test
    void shouldGetWatchById() throws Exception
    {
        WatchFullInfoResponseDTO response = mock(WatchFullInfoResponseDTO.class);
        UUID watchId = UUID.randomUUID();

        when(watchService.getWatch(watchId))
                .thenReturn(response);

        mockMvc.perform(
                get("/watches/{id}", watchId)
        ).andExpect(status().isOk());

        verify(watchService).getWatch(any());
    }

    @Test
    void shouldUpdateWatch() throws Exception
    {
        WatchFullInfoResponseDTO response = mock(WatchFullInfoResponseDTO.class);
        UUID watchId = UUID.randomUUID();

        WatchUpdateRequestDTO request = new WatchUpdateRequestDTO(
                "Omega",
                "Seamaster Diver 300M",
                "210.30.42.20.03.001",
                "abcddfdfd",
                "Omega Movement",
                2025,
                new BigDecimal("150.00"),
                Condition.EXCELLENT,
                Gender.MALE,
                MovementType.AUTOMATIC,
                WatchType.DIVER
        );

        when(watchService.updateWatch(watchId, request))
                .thenReturn(response);

        mockMvc.perform(
                put("/watches/{id}", watchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(watchService).updateWatch(any(), any());
    }

    @Test
    void shouldUpdateWatchStatus() throws Exception
    {
        WatchFullInfoResponseDTO response = mock(WatchFullInfoResponseDTO.class);
        UUID watchId = UUID.randomUUID();

        WatchStatusUpdateRequestDTO request = new WatchStatusUpdateRequestDTO(
                Status.AVAILABLE
        );

        when(watchService.updateWatchStatus(watchId, request))
                .thenReturn(response);

        mockMvc.perform(
                patch("/watches/{id}/status", watchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(watchService).updateWatchStatus(any(), any());
    }

    @Test
    void shouldHardDeleteWatch() throws Exception
    {
        UUID watchId = UUID.randomUUID();

        doNothing().when(watchService)
                .hardDeleteWatch(watchId);

        mockMvc.perform(
                delete("/watches/{id}", watchId)
        ).andExpect(status().isNoContent());

        verify(watchService).hardDeleteWatch(any());
    }

    @Test
    void shouldUpdateWatchBranch() throws Exception
    {
        WatchFullInfoResponseDTO response = mock(WatchFullInfoResponseDTO.class);
        UUID watchId = UUID.randomUUID();

        WatchBranchUpdateRequestDTO request = new WatchBranchUpdateRequestDTO(
                UUID.randomUUID()
        );

        when(watchService.updateWatchBranch(watchId, request))
                .thenReturn(response);

        mockMvc.perform(
                patch("/watches/{id}/branch", watchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(watchService).updateWatchBranch(any(), any());
    }

    @Test
    void shouldUpdateWatchSerialNumber() throws Exception
    {
        WatchFullInfoResponseDTO response = mock(WatchFullInfoResponseDTO.class);
        UUID watchId = UUID.randomUUID();

        WatchSerialNumberUpdateRequestDTO request = new WatchSerialNumberUpdateRequestDTO(
                UUID.randomUUID(),
                "SN123456789"
        );

        when(watchService.updateWatchSerialNumber(watchId, request))
                .thenReturn(response);

        mockMvc.perform(
                patch("/watches/{id}/serial_number", watchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(watchService).updateWatchSerialNumber(any(), any());
    }

    @Test
    void shouldGetWatchAvailability() throws Exception
    {
        UUID watchId = UUID.randomUUID();

        WatchAvailabilityResponseDTO response = mock(WatchAvailabilityResponseDTO.class);

        when(rentalService.watchAvailabilityStatus(
                eq(watchId),
                any(),
                any()
        )).thenReturn(response);

        mockMvc.perform(
                get("/watches/{id}/availability", watchId)
                        .param("startDate", "2026-08-01")
                        .param("endDate", "2026-08-10")
        ).andExpect(status().isOk());

        verify(rentalService).watchAvailabilityStatus(
                any(),
                any(),
                any()
        );
    }
}
