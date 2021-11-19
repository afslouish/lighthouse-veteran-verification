package gov.va.api.lighthouse.veteranverification.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class TransformersTest {
  @Test
  void formatDateString() {
    assertThat(Transformers.formatDateString("03272018")).isEqualTo(LocalDate.of(2018, 03, 27));
  }

  @Test
  void isBlankCharSequence() {
    assertThat(Transformers.isBlank(new StringBuffer())).isTrue();
  }

  @Test
  void isBlankCollection() {
    assertThat(Transformers.isBlank(new LinkedList<>())).isTrue();
  }

  @Test
  void isBlankMap() {
    assertThat(Transformers.isBlank(new HashMap<>())).isTrue();
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
