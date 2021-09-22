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
  public static final String CONFIRMED_STATUS_ICN_DEFAULT = "1012667145V762142";

  public static final String V5_STATUS_ICN_DEFAULT = "1012666182V203559";

  public static final String NO_EMIS_USER_STATUS_ICN_DEFAULT = "1012830453V141481";

  public static final String DISABILITY_RATING_ICN = "1012829620V654328";

  public static final String SERVICE_HISTORY_ICN = "1012832025V743496";

  public static final String SERVICE_HISTORY_ICN_NULL_END_DATE = "1012845631V882122";

  public static final String NO_MPI_USER_ICN = "1012855585V634865";

  public static final String NO_EMIS_EPISODES_USER = "1012667122V019349";

  public static final String N0_BGS_USER_DISABILITY_RATING_ICN = "1012661611V839382";

  /** Class of ICNs to be used for Veteran Verification ITs. */
  public static Icns icns() {
    return Icns.builder()
        .confirmedStatusIcn(
            systemPropertyOrDefault("confirmed-status-icn", CONFIRMED_STATUS_ICN_DEFAULT))
        .v5StatusIcn(systemPropertyOrDefault("v5-status-icn", V5_STATUS_ICN_DEFAULT))
        .noEmisUserStatusIcn(
            systemPropertyOrDefault("no-emis-user-status-icn", NO_EMIS_USER_STATUS_ICN_DEFAULT))
        .disabilityRatingIcn(
            systemPropertyOrDefault("disability-rating-icn", DISABILITY_RATING_ICN))
        .serviceHistoryIcn(systemPropertyOrDefault("service-history-icn", SERVICE_HISTORY_ICN))
        .serviceHistoryIcnNullEndDate(
            systemPropertyOrDefault(
                "service-history-icn-null-end-date", SERVICE_HISTORY_ICN_NULL_END_DATE))
        .noMpiUserIcn(systemPropertyOrDefault("no-mpi-user-icn", NO_MPI_USER_ICN))
        .noEmisEpisodesUser(systemPropertyOrDefault("no-emis-episodes-user", NO_EMIS_EPISODES_USER))
        .noBgsUserDisabilityRatingIcn(
            systemPropertyOrDefault(
                "no-bgs-user-disability-rating-icn", N0_BGS_USER_DISABILITY_RATING_ICN))
        .build();
  }

  private static SystemDefinition lab() {
    return SystemDefinition.builder()
        .icns(icns())
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
        .icns(icns())
        .veteranVerification(
            serviceDefinition("veteran-verification", "http://localhost", 8080, "/"))
        .build();
  }

  private static SystemDefinition production() {
    return SystemDefinition.builder()
        .icns(icns())
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
        .icns(icns())
        .veteranVerification(
            serviceDefinition(
                "veteran-verification",
                "https://blue.qa.lighthouse.va.gov",
                443,
                "/veteran_verification/"))
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
        .icns(icns())
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
        .icns(icns())
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

    @NonNull Icns icns;
  }

  @Data
  @Builder
  static final class Icns {
    @NonNull String v5StatusIcn;

    @NonNull String noEmisUserStatusIcn;

    @NonNull String confirmedStatusIcn;

    @NonNull String disabilityRatingIcn;

    @NonNull String serviceHistoryIcn;

    @NonNull String serviceHistoryIcnNullEndDate;

    @NonNull String noMpiUserIcn;

    @NonNull String noEmisEpisodesUser;

    @NonNull String noBgsUserDisabilityRatingIcn;
  }
}
