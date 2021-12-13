package gov.va.api.lighthouse.veteranverification.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.va.api.lighthouse.testclients.PropertiesLoader;
import gov.va.api.lighthouse.testclients.RetryingOauthClient;
import gov.va.api.lighthouse.testclients.patientcredentials.PatientCredentialsOauthClient;
import gov.va.api.lighthouse.testclients.patientcredentials.PatientCredentialsRequestConfiguration;
import gov.va.api.lighthouse.testclients.patientcredentials.PatientCredentialsRequestConfiguration.VaOauthRobotConfiguration;
import gov.va.api.lighthouse.testclients.selenium.WebDriverConfiguration;
import gov.va.api.lighthouse.veteranverification.api.v1.ServiceHistoryResponse;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class TestUtils {
  public static final Set<String> SCOPES =
      new CopyOnWriteArraySet<>(
          Set.of(
              "phone",
              "email",
              "offline_access",
              "launch/patient",
              "disability_rating.read",
              "service_history.read",
              "veteran_status.read",
              "profile",
              "address",
              "openid"));

  public static final Set<String> BAD_SCOPES =
      new CopyOnWriteArraySet<>(
          Set.of(
              "phone",
              "email",
              "offline_access",
              "launch/patient",
              "profile",
              "address",
              "openid"));

  PropertiesLoader propertiesLoader = PropertiesLoader.usingSystemProperties();

  public static RetryingOauthClient buildOauthClient(
      VaOauthRobotConfiguration vaOauthRobotConfig, Set<String> scopes) {
    try {
      PatientCredentialsRequestConfiguration.AuthorizationConfiguration authConfig =
          createAuthorizationConfiguration(scopes);
      var webDriverConfig = WebDriverConfiguration.fromProperties(propertiesLoader);
      var oauthConfig =
          PatientCredentialsRequestConfiguration.builder()
              .webDriverConfiguration(webDriverConfig)
              .oauthRobotConfiguration(vaOauthRobotConfig)
              .authorizationConfiguration(authConfig)
              .build();
      var client = RetryingOauthClient.of(PatientCredentialsOauthClient.of(oauthConfig));
      return client;
    } catch (IllegalArgumentException e) {
      log.error("Failed to create oauth client: " + e.getMessage());
      return null;
    }
  }

  public static PatientCredentialsRequestConfiguration.AuthorizationConfiguration
      createAuthorizationConfiguration(Set<String> scopes) {
    var authConfig =
        PatientCredentialsRequestConfiguration.AuthorizationConfiguration.builder()
            .authorizeUrl(propertiesLoader.valueOf("oauth.veteran-credentials.authorize-url"))
            .redirectUrl(propertiesLoader.valueOf("oauth.veteran-credentials.redirect-url"))
            .clientId(propertiesLoader.valueOf("oauth.veteran-credentials.client-id"))
            .clientSecret(propertiesLoader.valueOf("oauth.veteran-credentials.client-secret"))
            .state(propertiesLoader.valueOf("oauth.veteran-credentials.state"))
            .aud(propertiesLoader.valueOf("oauth.veteran-credentials.audience"))
            .scopes(scopes)
            .build();
    return authConfig;
  }

  public static VaOauthRobotConfiguration createVaOauthRobotConfiguration(String user) {
    var oauthRobotConfig =
        PatientCredentialsRequestConfiguration.VaOauthRobotConfiguration.builder()
            .credentialsType(
                PatientCredentialsRequestConfiguration.VaOauthRobotConfiguration
                    .OauthCredentialsType.valueOf("ID_ME"))
            .tokenUrl(propertiesLoader.valueOf("oauth.veteran-credentials.token-url"))
            .user(
                PatientCredentialsRequestConfiguration.VaOauthRobotConfiguration.UserCredentials
                    .builder()
                    .id(user)
                    .password(propertiesLoader.valueOf("oauth.veteran-credentials.user-password"))
                    .build())
            .build();
    return oauthRobotConfig;
  }

  @SneakyThrows
  public static ServiceHistoryResponse toServiceEpisodesResponse(String response) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    return objectMapper.readValue(response, ServiceHistoryResponse.class);
  }
}
