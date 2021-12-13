package gov.va.api.lighthouse.veteranverification.tests;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.SentinelProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SystemDefinitions {
  public static final String CONFIRMED_STATUS_USER_DEFAULT = "va.api.user+idme.001@gmail.com";

  public static final String V5_STATUS_USER_DEFAULT = "va.api.user+idme.008@gmail.com";

  public static final String NO_EMIS_USER_STATUS_USER_DEFAULT = "va.api.user+idme.012@gmail.com";

  public static final String DISABILITY_RATING_USER_DEFAULT = "va.api.user+idme.037@gmail.com";

  public static final String SERVICE_HISTORY_USER_DEFAULT = "va.api.user+idme.025@gmail.com";

  public static final String NO_EMIS_EPISODES_USER_DEFAULT = "va.api.user+idme.011@gmail.com";

  public static final String N0_BGS_USER_DEFAULT = "va.api.user+idme.031@gmail.com";

  private static SystemDefinition lab() {
    return SystemDefinition.builder()
        .users(users())
        .veteranVerification(
            serviceDefinition(
                "veteran-verification",
                "https://blue.lab.lighthouse.va.gov",
                443,
                "/veteran_verification/"))
        .build();
  }

  private static SystemDefinition local() {
    return SystemDefinition.builder()
        .users(users())
        .veteranVerification(
            serviceDefinition("veteran-verification", "http://localhost", 8080, "/"))
        .build();
  }

  private static SystemDefinition production() {
    return SystemDefinition.builder()
        .users(users())
        .veteranVerification(
            serviceDefinition(
                "veteran-verification",
                "https://blue.production.lighthouse.va.gov",
                443,
                "/veteran_verification/"))
        .build();
  }

  private static SystemDefinition qa() {
    return SystemDefinition.builder()
        .users(users())
        .veteranVerification(
            serviceDefinition(
                "veteran-verification",
                "https://blue.qa.lighthouse.va.gov",
                443,
                "/veteran_verification"))
        .build();
  }

  private static Service serviceDefinition(String name, String url, int port, String apiPath) {
    return Service.builder()
        .url(SentinelProperties.optionUrl(name, url))
        .port(port)
        .apiPath(SentinelProperties.optionApiPath(name, apiPath))
        .build();
  }

  private static SystemDefinition staging() {
    return SystemDefinition.builder()
        .users(users())
        .veteranVerification(
            serviceDefinition(
                "veteran-verification",
                "https://blue.staging.lighthouse.va.gov",
                443,
                "/veteran_verification/"))
        .build();
  }

  private static SystemDefinition stagingLab() {
    return SystemDefinition.builder()
        .users(users())
        .veteranVerification(
            serviceDefinition(
                "veteran-verification",
                "https://blue.staging-lab.lighthouse.va.gov",
                443,
                "/veteran_verification/"))
        .build();
  }

  static SystemDefinition systemDefinition() {
    switch (Environment.get()) {
      case LOCAL:
        return local();
      case QA:
        return qa();
      case LAB:
        return lab();
      case PROD:
        return production();
      case STAGING:
        return staging();
      case STAGING_LAB:
        return stagingLab();
      default:
        throw new IllegalArgumentException(
            "Unsupported sentinel environment: " + Environment.get());
    }
  }

  /** Class of user emails to be used for Veteran Verification ITs. */
  public static Users users() {
    return Users.builder()
        .confirmedStatusUser(
            systemPropertyOrDefault("confirmed-status-user", CONFIRMED_STATUS_USER_DEFAULT))
        .v5StatusUser(systemPropertyOrDefault("v5-status-user", V5_STATUS_USER_DEFAULT))
        .noEmisUserStatusUser(
            systemPropertyOrDefault("no-emis-user-status-user", NO_EMIS_USER_STATUS_USER_DEFAULT))
        .disabilityRatingUser(
            systemPropertyOrDefault("disability-rating-user", DISABILITY_RATING_USER_DEFAULT))
        .serviceHistoryUser(
            systemPropertyOrDefault("service-history-user", SERVICE_HISTORY_USER_DEFAULT))
        .noEmisEpisodesUser(
            systemPropertyOrDefault("no-emis-episodes-user", NO_EMIS_EPISODES_USER_DEFAULT))
        .noBgsUser(systemPropertyOrDefault("no-bgs-user", N0_BGS_USER_DEFAULT))
        .build();
  }

  private String systemPropertyOrDefault(String property, String defaultValue) {
    String value = System.getProperty(property);
    return value == null ? defaultValue : value;
  }

  @Value
  @Builder
  static final class Service {
    @NonNull String url;

    @NonNull Integer port;

    @NonNull String apiPath;

    String urlWithApiPath() {
      StringBuilder builder = new StringBuilder(url());
      if (!apiPath().startsWith("/")) {
        builder.append('/');
      }
      builder.append(apiPath());
      if (!apiPath.endsWith("/")) {
        builder.append('/');
      }
      return builder.toString();
    }
  }

  @Value
  @Builder
  static final class SystemDefinition {
    @NonNull Service veteranVerification;

    Users users;
  }

  @Data
  @Builder
  static final class Users {
    @NonNull String v5StatusUser;

    @NonNull String noEmisUserStatusUser;

    @NonNull String confirmedStatusUser;

    @NonNull String disabilityRatingUser;

    @NonNull String serviceHistoryUser;

    @NonNull String noEmisEpisodesUser;

    @NonNull String noBgsUser;
  }
}
