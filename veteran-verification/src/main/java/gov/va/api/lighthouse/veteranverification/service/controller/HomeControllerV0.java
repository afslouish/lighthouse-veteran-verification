package gov.va.api.lighthouse.veteranverification.service.controller;

import java.io.InputStream;
import java.nio.charset.Charset;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v0/", produces = "application/json")
public class HomeControllerV0 {
  private final Resource veteranConfirmationOpenapi;

  @Autowired
  HomeControllerV0(
      @Value("classpath:/veteran-confirmation-openapi.json") Resource veteranConfirmationOpenapi) {
    this.veteranConfirmationOpenapi = veteranConfirmationOpenapi;
  }

  /** REST endpoint for OpenAPI JSON + redirect. */
  @SneakyThrows
  @GetMapping(
      value = {"veteran_confirmation/", "veteran_confirmation/openapi.json"},
      produces = "application/json")
  @ResponseBody
  public Object veteranConfirmationOpenApiJson() {
    try (InputStream is = veteranConfirmationOpenapi.getInputStream()) {
      return StreamUtils.copyToString(is, Charset.defaultCharset());
    }
  }
}
