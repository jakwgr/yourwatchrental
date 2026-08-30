package com.yourwatchrental.watchrental.branch;

import com.yourwatchrental.watchrental.branch.dto.BranchFilterCriteriaRequest;
import com.yourwatchrental.watchrental.branch.dto.BranchRequestDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchResponseDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchStatusUpdateRequestDTO;
import com.yourwatchrental.watchrental.security.CustomUserDetailsService;
import com.yourwatchrental.watchrental.security.JwUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BranchController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BranchService branchService;

    @MockitoBean
    private JwUtil jwUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetBranches() throws Exception {
        BranchResponseDTO responseDTO = mock(BranchResponseDTO.class);

        when(branchService.getBranches(any(BranchFilterCriteriaRequest.class)))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(
                get("/branches")
        ).andExpect(
                status().isOk()
        );

        verify(branchService).getBranches(any(BranchFilterCriteriaRequest.class));
    }


    @Test
    void shouldGetBranchesAdmin() throws Exception {
        BranchResponseDTO responseDTO = mock(BranchResponseDTO.class);

        when(branchService.getBranchesAdmin(any(BranchFilterCriteriaRequest.class)))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(
                get("/branches/admin")
        ).andExpect(
                status().isOk()
        );

        verify(branchService).getBranchesAdmin(any(BranchFilterCriteriaRequest.class));
    }

    @Test
    void shouldCreateBranch() throws Exception {

        BranchRequestDTO request = new BranchRequestDTO(
                "Kraków",
                "Oddział Centrum",
                "ul. Długa 10",
                "123456789",
                "branch@test.com"
        );

        BranchResponseDTO responseDTO = mock(BranchResponseDTO.class);

        when(branchService.createBranch(any(BranchRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(
                post("/branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated());

        verify(branchService)
                .createBranch(any(BranchRequestDTO.class));
    }

    @Test
    void shouldUpdateBranch() throws Exception
    {
        UUID id = UUID.randomUUID();
        BranchRequestDTO request = new BranchRequestDTO(
                "Kraków",
                "Oddział Centrum",
                "ul. Długa 10",
                "123456789",
                "branch@test.com"
        );

        BranchResponseDTO responseDTO = mock(BranchResponseDTO.class);

        when(branchService.updateBranchStatus(
                eq(id),
                any(BranchStatusUpdateRequestDTO.class)
        ))
                .thenReturn(responseDTO);

        mockMvc.perform(
                put("/branches/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk());

        verify(branchService).updateBranch(eq(id), any(BranchRequestDTO.class));
    }

    @Test
    void shouldHardDeleteBranch() throws Exception
    {
        UUID id = UUID.randomUUID();

        mockMvc.perform(
                delete("/branches/{id}", id)
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateBranchStatus() throws Exception
    {
        UUID id = UUID.randomUUID();

        BranchStatusUpdateRequestDTO request = new BranchStatusUpdateRequestDTO(
                BranchStatus.DISABLED
        );
        BranchResponseDTO responseDTO = mock(BranchResponseDTO.class);

        when(branchService.updateBranchStatus(eq(id), any(BranchStatusUpdateRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(
                patch("/branches/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk());

        verify(branchService).updateBranchStatus(eq(id), any(BranchStatusUpdateRequestDTO.class));
    }
}
