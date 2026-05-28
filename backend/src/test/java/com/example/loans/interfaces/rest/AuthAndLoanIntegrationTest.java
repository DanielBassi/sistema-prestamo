package com.example.loans.interfaces.rest;

import com.example.loans.domain.enumtype.LoanStatus;
import com.example.loans.infrastructure.persistence.entity.LoanEntity;
import com.example.loans.infrastructure.persistence.repository.SpringDataLoanRepository;
import com.example.loans.infrastructure.persistence.repository.SpringDataUserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthAndLoanIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataUserRepository userRepository;

    @Autowired
    private SpringDataLoanRepository loanRepository;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"usuario@test.com\",\"password\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void shouldFailLoginWithInvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"usuario@test.com\",\"password\":\"bad\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldCreateLoanWithJwt() throws Exception {
        String token = loginAndGetToken("usuario@test.com", "123");
        mockMvc.perform(post("/api/loans")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000,\"termInMonths\":12}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldBlockLoanApprovalForUserRole() throws Exception {
        Long loanId = createPendingLoanForUser("usuario@test.com");
        String userToken = loginAndGetToken("usuario@test.com", "123");

        mockMvc.perform(patch("/api/admin/loans/{id}/approve", loanId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowLoanApprovalForAdminRole() throws Exception {
        Long loanId = createPendingLoanForUser("usuario@test.com");
        String adminToken = loginAndGetToken("admin@test.com", "123");

        mockMvc.perform(patch("/api/admin/loans/{id}/approve", loanId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldOnlyAllowOwnerOrAdminToGetLoanById() throws Exception {
        Long loanId = createPendingLoanForUser("admin@test.com");
        String userToken = loginAndGetToken("usuario@test.com", "123");
        String adminToken = loginAndGetToken("admin@test.com", "123");

        mockMvc.perform(get("/api/loans/{id}", loanId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/loans/{id}", loanId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loanId));
    }

    @Test
    void shouldRestrictUsersListToAdmin() throws Exception {
        String userToken = loginAndGetToken("usuario@test.com", "123");
        String adminToken = loginAndGetToken("admin@test.com", "123");

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode json = objectMapper.readTree(response);
        return json.get("token").asText();
    }

    private Long createPendingLoanForUser(String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        LoanEntity loan = new LoanEntity();
        loan.setUser(user);
        loan.setAmount(BigDecimal.valueOf(700));
        loan.setTermInMonths(8);
        loan.setStatus(LoanStatus.PENDING);
        loan.setCreatedAt(LocalDateTime.now());
        loan.setUpdatedAt(LocalDateTime.now());
        return loanRepository.save(loan).getId();
    }
}
