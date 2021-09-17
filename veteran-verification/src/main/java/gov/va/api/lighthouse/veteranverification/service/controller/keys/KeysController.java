package gov.va.api.lighthouse.veteranverification.service.controller.keys;

import gov.va.api.lighthouse.veteranverification.api.v1.JwkKeyset;
import gov.va.api.lighthouse.veteranverification.service.utils.JwksProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = {"application/json"})
public class KeysController {
  JwksProperties jwksProperties;

  public KeysController(@Autowired JwksProperties jwksProperties) {
    this.jwksProperties = jwksProperties;
  }

  @GetMapping({"/v1/keys"})
  public JwkKeyset keyset() {
    KeysTransformer transformer = KeysTransformer.builder().jwksProperties(jwksProperties).build();
    return transformer.keysTransformer();
  }
}
