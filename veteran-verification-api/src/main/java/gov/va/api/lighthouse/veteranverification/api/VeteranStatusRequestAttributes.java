package gov.va.api.lighthouse.veteranverification.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.va.api.lighthouse.mpi.Mpi1305RequestAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = false)
public class VeteranStatusRequestAttributes {
  @NonNull String ssn;
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
  @NonNull
  String birthDate;

  /** Translate Veteran Status Attributes object to 1305 Request Attributes object. */
  public Mpi1305RequestAttributes toMpi1305RequestAttributes() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate birthTime = LocalDate.parse(birthDate, formatter);
    return Mpi1305RequestAttributes.builder()
        .ssn(ssn)
        .gender(gender)
        .firstName(firstName)
        .middleName(middleName)
        .lastName(lastName)
        .birthTime(birthTime)
        .build();
  }
}
