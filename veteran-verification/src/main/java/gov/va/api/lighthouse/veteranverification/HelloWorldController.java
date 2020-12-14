package gov.va.api.lighthouse.veteranverification;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = {"application/json"})
public class HelloWorldController {

  @GetMapping({"/hello-world"})
  public String helloWorld() {
    return "Hello World";
  }
}
