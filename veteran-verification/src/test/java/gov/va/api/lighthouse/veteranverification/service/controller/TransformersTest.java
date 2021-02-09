package gov.va.api.lighthouse.veteranverification.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class TransformersTest {
  @Test
  void isBlankCharSqequence() {
    assertThat(Transformers.isBlank(new StringBuffer())).isTrue();
  }

  @Test
  void isBlankCollection() {
    assertThat(Transformers.isBlank(new LinkedList())).isTrue();
  }

  @Test
  void isBlankMap() {
    assertThat(Transformers.isBlank(new HashMap())).isTrue();
  }

  @Test
  void isBlankNull() {
    assertThat(Transformers.isBlank(null)).isTrue();
  }

  @Test
  void isBlankOptional() {
    assertThat(Transformers.isBlank(Optional.empty())).isTrue();
  }
}