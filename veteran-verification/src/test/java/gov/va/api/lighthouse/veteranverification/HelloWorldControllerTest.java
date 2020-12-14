package gov.va.api.lighthouse.veteranverification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HelloWorldControllerTest {

  private HelloWorldController controller() {
    return new HelloWorldController();
  }

  @Test
  void helloWorldTest() {
    String helloWorld = controller().helloWorld();
    assertThat(helloWorld).isEqualTo("Hello World");
  }
}
