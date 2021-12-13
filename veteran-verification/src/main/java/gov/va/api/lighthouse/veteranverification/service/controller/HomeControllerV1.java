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

/** V1 Home Controller class for open api. */
@RestController
@RequestMapping(produces = "application/json")
public class HomeControllerV1 {
  private final Resource veteranVerificationOpenapi;

  @Autowired
  HomeControllerV1(
      @Value("classpath:/veteran-verification-openapi.json") Resource veteranVerificationOpenapi) {
    this.veteranVerificationOpenapi = veteranVerificationOpenapi;
  }

  /** REST endpoint for OpenAPI JSON + redirect. */
  @SneakyThrows
  @GetMapping(
      value = {"docs/v1/veteran_verification"},
      produces = "application/json")
  @ResponseBody
  public Object veteranVerificationOpenApiJson() {
    try (InputStream is = veteranVerificationOpenapi.getInputStream()) {
      return StreamUtils.copyToString(is, Charset.defaultCharset());
    }
  }
}
