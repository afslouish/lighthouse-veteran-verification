package gov.va.api.lighthouse.veteranverification.service.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {HomeControllerV0.class})
public class HomeControllerV0Test {
  @Mock Resource veteranConfirmationOpenapi;

  HomeControllerV0 connerCaseController;

  @Autowired private MockMvc mvc;

  @BeforeEach
  void _init() {
    connerCaseController = new HomeControllerV0(veteranConfirmationOpenapi);
  }

  @Test
  @SneakyThrows
  public void inputStreamError() {
    given(veteranConfirmationOpenapi.getInputStream())
        .willAnswer(
            invocation -> {
              throw new Exception("Test Exception");
            });
    Assertions.assertThrows(
        Exception.class,
        () -> {
          connerCaseController.veteranConfirmationOpenApiJson();
        });
  }

  @Test
  @SneakyThrows
  public void testVeteranConfirmationOpenapiPath() {
    mvc.perform(get("/v0/veteran_confirmation/openapi.json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.openapi", equalTo("3.0.1")));
  }
}
