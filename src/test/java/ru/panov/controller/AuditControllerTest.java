package ru.panov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Audit;
import model.AuditType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.panov.config.TestConfig;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.panov.util.PathConstants.AUDIT_PATH;

@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
class AuditControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithUserDetails(value = "admin")
    @DisplayName("????????? ???? ??????? ??????")
    void getAllAudits_ReturnsAuditList() throws Exception {
        Audit audit = Audit.builder()
                .id(1L)
                .className("Service")
                .methodName("save")
                .type(AuditType.SUCCESS)
                .username("user")
                .build();
        List<Audit> audits = List.of(audit);

        mockMvc.perform(get(AUDIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(audits)));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("????????? ???? ??????? ??????")
    void getAllAudits_BadGrantedAuthorities_Returns() throws Exception {
        mockMvc.perform(get(AUDIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isUnauthorized(),
                        content().json("""
                                {
                                "detail":"Access Denied"
                                }
                                """));
    }
}