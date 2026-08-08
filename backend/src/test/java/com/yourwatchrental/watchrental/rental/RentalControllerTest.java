package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.rental.dto.request.PaymentStatusChangeRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.request.RentalFilterRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.request.RentalRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalResponseDTO;
import com.yourwatchrental.watchrental.security.CustomUserDetailsService;
import com.yourwatchrental.watchrental.security.JwUtil;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RentalController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwUtil jwUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    RentalService rentalService;


    @Test
    void shouldCreateRental() throws Exception
    {
        RentalResponseDTO response = mock(RentalResponseDTO.class);

        RentalRequestDTO request = new RentalRequestDTO(
                LocalDate.of(2026, 8, 10),
                LocalDate.of(2026, 8, 15),
                PaymentMethod.CARD,
                UUID.randomUUID()
        );

        when(rentalService.createRental(any(RentalRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(rentalService)
                .createRental(any(RentalRequestDTO.class));
    }


    @Test
    void shouldCancelRental() throws Exception
    {
        RentalResponseDTO response = mock(RentalResponseDTO.class);

        UUID rentalId = UUID.randomUUID();

        when(rentalService.cancelRental(rentalId))
                .thenReturn(response);

        mockMvc.perform(
                patch("/rentals/{id}/cancel", rentalId)
        ).andExpect(status().isOk());

        verify(rentalService)
                .cancelRental(any());
    }


    @Test
    void shouldCompleteRental() throws Exception
    {
        RentalResponseDTO response = mock(RentalResponseDTO.class);

        UUID rentalId = UUID.randomUUID();

        when(rentalService.completeRental(rentalId))
                .thenReturn(response);

        mockMvc.perform(
                patch("/rentals/{id}/complete", rentalId)
        ).andExpect(status().isOk());

        verify(rentalService)
                .completeRental(any());
    }


    @Test
    void shouldGetRentalById() throws Exception
    {
        RentalResponseDTO response = mock(RentalResponseDTO.class);

        UUID rentalId = UUID.randomUUID();

        when(rentalService.getRentalById(rentalId))
                .thenReturn(response);

        mockMvc.perform(
                get("/rentals/{id}", rentalId)
        ).andExpect(status().isOk());

        verify(rentalService)
                .getRentalById(any());
    }


    @Test
    void shouldGetMyRentals() throws Exception
    {
        RentalResponseDTO response = mock(RentalResponseDTO.class);

        Page<RentalResponseDTO> page = new PageImpl<>(
                List.of(response)
        );

        when(rentalService.getMyRentals(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                get("/rentals/my")
                        .param("page", "0")
                        .param("size", "10")
        ).andExpect(status().isOk());

        verify(rentalService)
                .getMyRentals(any(Pageable.class));
    }


    @Test
    void shouldGetAllRentals() throws Exception
    {
        RentalResponseDTO response = mock(RentalResponseDTO.class);

        Page<RentalResponseDTO> page = new PageImpl<>(
                List.of(response)
        );

        when(rentalService.getAllRentals(
                any(RentalFilterRequestDTO.class),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(
                get("/rentals")
                        .param("page", "0")
                        .param("size", "10")
        ).andExpect(status().isOk());

        verify(rentalService)
                .getAllRentals(
                        any(RentalFilterRequestDTO.class),
                        any(Pageable.class)
                );
    }


    @Test
    void shouldChangePaymentStatus() throws Exception
    {
        RentalResponseDTO response = mock(RentalResponseDTO.class);

        UUID rentalId = UUID.randomUUID();

        PaymentStatusChangeRequestDTO request = new PaymentStatusChangeRequestDTO(
                PaymentStatus.FAILED
        );

        when(rentalService.changePaymentStatus(
                any(UUID.class),
                any(PaymentStatusChangeRequestDTO.class)
        ))
                .thenReturn(response);

        mockMvc.perform(
                patch("/rentals/{id}/payment", rentalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(rentalService)
                .changePaymentStatus(any(), any());
    }
}