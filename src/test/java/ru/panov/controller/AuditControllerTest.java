package ru.panov.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.panov.config.WebConfiguration;
import ru.panov.model.Audit;
import ru.panov.model.AuditType;
import ru.panov.service.AuditService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.panov.util.PathConstants.AUDIT_PATH;

@SpringJUnitWebConfig(classes = WebConfiguration.class)
class AuditControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private AuditController auditController;
    @Mock
    private AuditService auditService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(auditController)
                .build();
    }

    @Test
    @DisplayName("????????? ???? ??????? ??????")
    void getAllAudits() throws Exception {
        List<Audit> mockAudits = Arrays.asList(
                Audit.builder()
                        .id(1L)
                        .className("TestClass")
                        .methodName("testMethod")
                        .type(AuditType.SUCCESS)
                        .username("user1")
                        .build(),
                Audit.builder()
                        .id(2L)
                        .className("TestClass")
                        .methodName("testMethod")
                        .type(AuditType.FAIL)
                        .username("user2")
                        .build()
        );
        Mockito.when(auditService.showAllAudits()).thenReturn(mockAudits);

        mockMvc.perform(get(AUDIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(mockAudits.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].className").value("TestClass"))
                .andExpect(jsonPath("$[0].methodName").value("testMethod"))
                .andExpect(jsonPath("$[0].type").value("SUCCESS"))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].className").value("TestClass"))
                .andExpect(jsonPath("$[1].methodName").value("testMethod"))
                .andExpect(jsonPath("$[1].type").value("FAIL"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }
}