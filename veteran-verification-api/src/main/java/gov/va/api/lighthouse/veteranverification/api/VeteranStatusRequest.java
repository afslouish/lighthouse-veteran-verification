package gov.va.api.lighthouse.veteranverification.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.va.api.lighthouse.mpi.Mpi1305RequestAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

/** Veteran Confirmation Status request body model. */
@Value
@Builder
@Accessors(fluent = false)
public class VeteranStatusRequest {
  @Pattern(regexp = "^[0-9]{9}|^\\d{3}-\\d{2}-\\d{4}$")
  @NonNull
  String ssn;

  @Pattern(regexp = "^(?:M|F)$")
  String gender;

  @JsonProperty(value = "first_name")
  @NonNull
  String firstName;

  @JsonProperty(value = "middle_name")
  String middleName;

  @JsonProperty(value = "last_name")
  @NonNull
  String lastName;

  @JsonProperty(value = "birth_date")
  @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")
  @NonNull
  String birthDate;

  /** Translate Veteran Status Attributes object to 1305 Request Attributes object. */
  public Mpi1305RequestAttributes toMpi1305RequestAttributes() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate birthTime = LocalDate.parse(birthDate, formatter);
    return Mpi1305RequestAttributes.builder()
        .ssn(ssn.replaceAll("-", ""))
        .gender(gender)
        .firstName(firstName)
        .middleName(middleName)
        .lastName(lastName)
        .birthTime(birthTime)
        .build();
  }
}
