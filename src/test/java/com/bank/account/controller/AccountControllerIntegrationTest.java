package com.bank.account.controller;

import com.bank.account.dto.CreateAccountRequest;
import com.bank.account.model.AccountType;
import com.bank.account.repository.CustomerAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CustomerAccountRepository repository;

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
    }

    private CreateAccountRequest validRequest() {
        CreateAccountRequest req = new CreateAccountRequest();
        req.setCustomerName("Juan dela Cruz");
        req.setCustomerMobile("09171234567");
        req.setCustomerEmail("juan@example.com");
        req.setAddress1("123 Rizal St, Manila");
        req.setAccountType(AccountType.SAVINGS);
        return req;
    }

    // ── POST tests ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/account – 201 Created")
    void createAccount_returns201() throws Exception {
        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerNumber").isNumber())
                .andExpect(jsonPath("$.transactionStatusCode").value(201))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer account created"));
    }

    @Test
    @DisplayName("POST /api/v1/account – 400 when customerName is missing")
    void missingCustomerName_returns400() throws Exception {
        CreateAccountRequest req = validRequest();
        req.setCustomerName(null);
        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.transactionStatusCode").value(400));
    }

    @Test
    @DisplayName("POST /api/v1/account – 400 for invalid email")
    void invalidEmail_returns400() throws Exception {
        CreateAccountRequest req = validRequest();
        req.setCustomerEmail("not-an-email");
        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.transactionStatusCode").value(400));
    }

    @Test
    @DisplayName("POST /api/v1/account – 409 Conflict for duplicate email")
    void duplicateEmail_returns409() throws Exception {
        String body = objectMapper.writeValueAsString(validRequest());
        mockMvc.perform(post("/api/v1/account").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/account").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.transactionStatusCode").value(409));
    }

    // ── GET tests ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/account/{customerNumber} – 302 Found with savings array")
    void getAccount_savings_returns302() throws Exception {
        // Create first, then retrieve using returned customerNumber
        MvcResult result = mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated())
                .andReturn();

        Long customerNumber = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("customerNumber").asLong();

        mockMvc.perform(get("/api/v1/account/{customerNumber}", customerNumber))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.customerNumber").value(customerNumber))
                .andExpect(jsonPath("$.customerName").value("Juan dela Cruz"))
                .andExpect(jsonPath("$.savings").isArray())
                .andExpect(jsonPath("$.savings[0].accountType").value("Savings"))
                .andExpect(jsonPath("$.savings[0].availableBalance").value(0))
                .andExpect(jsonPath("$.transactionStatusCode").value(302))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer Account found"))
                .andExpect(jsonPath("$.checking").doesNotExist());
    }

    @Test
    @DisplayName("GET /api/v1/account/{customerNumber} – 302 Found with checking array")
    void getAccount_checking_returns302() throws Exception {
        CreateAccountRequest req = validRequest();
        req.setAccountType(AccountType.CHECKING);

        MvcResult result = mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        Long customerNumber = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("customerNumber").asLong();

        mockMvc.perform(get("/api/v1/account/{customerNumber}", customerNumber))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.checking").isArray())
                .andExpect(jsonPath("$.checking[0].accountType").value("Checking"))
                .andExpect(jsonPath("$.savings").doesNotExist());
    }

    @Test
    @DisplayName("GET /api/v1/account/{customerNumber} – 401 when customer not found")
    void getAccount_notFound_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/account/99999999"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.transactionStatusCode").value(401))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer not found"));
    }
}
