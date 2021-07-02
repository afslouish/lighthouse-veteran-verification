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
  public static final String EDIPI_CONFIRMED_FIRST_NAME_DEFAULT = "ALFREDO";

  public static final String EDIPI_CONFIRMED_LAST_NAME_DEFAULT = "ARMSTRONG";

  public static final String EDIPI_CONFIRMED_BIRTH_DATE_DEFAULT = "1993-06-08";

  public static final String EDIPI_CONFIRMED_SSN_DEFAULT = "796012476";

  public static final String ICN_CONFIRMED_FIRST_NAME_DEFAULT_DEFAULT = "ARTHUR";

  public static final String ICN_CONFIRMED_LAST_NAME_DEFAULT = "ROSE";

  public static final String ICN_CONFIRMED_BIRTH_DATE_DEFAULT = "1954-05-26";

  public static final String ICN_CONFIRMED_SSN_DEFAULT = "796220828";

  public static final String V5_STATUS_FIRST_NAME_DEFAULT = "GREG";

  public static final String V5_STATUS_LAST_NAME_DEFAULT = "ANDERSON";

  public static final String V5_STATUS_BIRTH_DATE_DEFAULT = "1933-04-05";

  public static final String V5_STATUS_SSN_DEFAULT = "796121200";

  public static final String NO_EMIS_USER_FIRST_NAME_DEFAULT = "WILLARD";

  public static final String NO_EMIS_USER_LAST_NAME_DEFAULT = "RILEY";

  public static final String NO_EMIS_USER_BIRTH_DATE_DEFAULT = "1959-02-25";

  public static final String NO_EMIS_USER_SSN_DEFAULT = "796013145";

  public static final String CONFIRMED_STATUS_ICN_DEFAULT = "1012667145V762142";

  public static final String V5_STATUS_ICN_DEFAULT = "1012666182V203559";

  public static final String NO_EMIS_USER_STATUS_ICN_DEFAULT = "1012830453V141481";

  /** Class of search attributes to be used for veteran confirmation status ITs. */
  public static Attributes attributes() {
    return Attributes.builder()
        .edipiConfirmedFirstName(
            systemPropertyOrDefault(
                "edipi-confirmed-first-name", EDIPI_CONFIRMED_FIRST_NAME_DEFAULT))
        .edipiConfirmedLastName(
            systemPropertyOrDefault("edipi-confirmed-last-name", EDIPI_CONFIRMED_LAST_NAME_DEFAULT))
        .edipiConfirmedBirthDate(
            systemPropertyOrDefault(
                "edipi-confirmed-birth-date", EDIPI_CONFIRMED_BIRTH_DATE_DEFAULT))
        .edipiConfirmedSsn(
            systemPropertyOrDefault("edipi-confirmed-ssn", EDIPI_CONFIRMED_SSN_DEFAULT))
        .icnConfirmedFirstName(
            systemPropertyOrDefault(
                "icn-confirmed-first-name", ICN_CONFIRMED_FIRST_NAME_DEFAULT_DEFAULT))
        .icnConfirmedLastName(
            systemPropertyOrDefault("icn-confirmed-last-name", ICN_CONFIRMED_LAST_NAME_DEFAULT))
        .icnConfirmedBirthDate(
            systemPropertyOrDefault("icn-confirmed-birth-date", ICN_CONFIRMED_BIRTH_DATE_DEFAULT))
        .icnConfirmedSsn(systemPropertyOrDefault("icn-confirmed-ssn", ICN_CONFIRMED_SSN_DEFAULT))
        .v5StatusFirstName(
            systemPropertyOrDefault("v5-status-first-name", V5_STATUS_FIRST_NAME_DEFAULT))
        .v5StatusLastName(
            systemPropertyOrDefault("v5-status-last-name", V5_STATUS_LAST_NAME_DEFAULT))
        .v5StatusBirthDate(
            systemPropertyOrDefault("v5-status-birth-date", V5_STATUS_BIRTH_DATE_DEFAULT))
        .v5StatusSsn(systemPropertyOrDefault("v5-status-ssn", V5_STATUS_SSN_DEFAULT))
        .noEmisUserFirstName(
            systemPropertyOrDefault("no-emis-user-first-name", NO_EMIS_USER_FIRST_NAME_DEFAULT))
        .noEmisUserLastName(
            systemPropertyOrDefault("no-emis-user-last-name", NO_EMIS_USER_LAST_NAME_DEFAULT))
        .noEmisUserBirthDate(
            systemPropertyOrDefault("no-emis-user-birth-date", NO_EMIS_USER_BIRTH_DATE_DEFAULT))
        .noEmisUserSsn(systemPropertyOrDefault("no-emis-user-ssn", NO_EMIS_USER_SSN_DEFAULT))
        .build();
  }

  /** Class of ICNs to be used for veteran verifciation ITs. */
  public static Icns icns() {
    return Icns.builder()
        .confirmedStatusIcn(
            systemPropertyOrDefault("confirmed-status-icn", CONFIRMED_STATUS_ICN_DEFAULT))
        .v5StatusIcn(systemPropertyOrDefault("v5-status-icn", V5_STATUS_ICN_DEFAULT))
        .noEmisUserStatusIcn(
            systemPropertyOrDefault("no-emis-user-status-icn", NO_EMIS_USER_STATUS_ICN_DEFAULT))
        .build();
  }

  private static SystemDefinition lab() {
    return SystemDefinition.builder()
        .attributes(attributes())
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
        .attributes(attributes())
        .icns(icns())
        .veteranVerification(
            serviceDefinition("veteran-verification", "http://localhost", 8080, "/"))
        .build();
  }

  private static SystemDefinition production() {
    return SystemDefinition.builder()
        .attributes(attributes())
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
        .attributes(attributes())
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
        .attributes(attributes())
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
        .attributes(attributes())
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

    @NonNull Attributes attributes;

    @NonNull Icns icns;
  }

  @Data
  @Builder
  static final class Icns {
    @NonNull String v5StatusIcn;

    @NonNull String noEmisUserStatusIcn;

    @NonNull String confirmedStatusIcn;
  }

  @Data
  @Builder
  static final class Attributes {
    @NonNull String edipiConfirmedFirstName;

    @NonNull String edipiConfirmedLastName;

    @NonNull String edipiConfirmedBirthDate;

    @NonNull String edipiConfirmedSsn;

    @NonNull String icnConfirmedFirstName;

    @NonNull String icnConfirmedLastName;

    @NonNull String icnConfirmedBirthDate;

    @NonNull String icnConfirmedSsn;

    @NonNull String v5StatusFirstName;

    @NonNull String v5StatusLastName;

    @NonNull String v5StatusBirthDate;

    @NonNull String v5StatusSsn;

    @NonNull String noEmisUserFirstName;

    @NonNull String noEmisUserLastName;

    @NonNull String noEmisUserBirthDate;

    @NonNull String noEmisUserSsn;
  }
}
