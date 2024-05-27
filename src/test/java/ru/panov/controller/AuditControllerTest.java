package ru.panov.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.panov.util.PathConstants.AUDIT_PATH;

@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
class AuditControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Успешное получение всех записей аудитов")
    @WithUserDetails(value = "admin")
    void getAllAudits_ReturnsAuditList() throws Exception {
        mockMvc.perform(get(AUDIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk(),
                        content().json("""
                                [
                                {
                                    "id": 1,
                                    "className": "Service",
                                    "methodName": "save",
                                    "type": "SUCCESS",
                                    "username": "user"
                                }
                                ]
                                """));
    }

    @Test
    @DisplayName("Получение всех аудитов с неподходящей ролью пользователя")
    @WithUserDetails(value = "user1")
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