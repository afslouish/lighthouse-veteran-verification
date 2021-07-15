package gov.va.api.lighthouse.veteranverification.service;

import static gov.va.api.lighthouse.veteranverification.service.controller.Transformers.emptyToNull;
import static gov.va.api.lighthouse.veteranverification.service.controller.Transformers.isBlank;

import gov.va.viers.cdi.emis.commonservice.v1.InputEdipiIcn;
import gov.va.viers.cdi.emis.requestresponse.v1.InputEdiPiOrIcn;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.JAXBElement;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;

/** Utility class for common MPI lookup methods. */
public class MpiLookupUtils {
  private static final String ICN_LOOKUP_VALUE = "^NI^200M^USVHA^P";

  private static final String EDIPI_LOOKUP_VALUE = "^NI^200DOD^USDOD^A";

  private static final String SSN_LOOKUP_ROOT = "2.16.840.1.113883.4.1";

  /**
   * Retrieves first name from MPI response.
   *
   * @param mpiResponse Mpi Response.
   * @return Returns first name of veteran.
   */
  @SneakyThrows
  public static String getFirstName(@NonNull PRPAIN201306UV02 mpiResponse) {
    String firstName;
    try {
      firstName =
          mpiResponse
              .getControlActProcess()
              .getSubject()
              .get(0)
              .getRegistrationEvent()
              .getSubject1()
              .getPatient()
              .getPatientPerson()
              .getValue()
              .getName()
              .get(0)
              .getContent()
              .stream()
              .filter(item -> item.getClass() == JAXBElement.class)
              .map(i -> (JAXBElement) i)
              .filter(name -> name.getValue().getClass() == EnGiven.class)
              .map(i -> (EnGiven) i.getValue())
              .toList()
              .get(0)
              .getContent()
              .get(0)
              .toString();
    } catch (Exception e) {
      throw new Exception("No last name found in mpi response.");
    }
    return firstName;
  }

  /** Loop through list of patient's IDs in search of one that matches lookup pattern. */
  public static String getId(PRPAMT201310UV02Patient patient, String lookup) {
    if (patient == null || patient.getId().isEmpty() || patient.getId() == null) {
      return null;
    }
    for (II id : patient.getId()) {
      if (id == null || id.getExtension() == null || isBlank(id.getExtension())) {
        continue;
      }
      String extension = id.getExtension();
      if (extension.contains(lookup)) {
        return extension.substring(0, extension.indexOf("^"));
      }
    }
    return null;
  }

  /** Build InputEdipiOrIcn object for eMIS lookup. */
  public static InputEdiPiOrIcn getInputEdipiOrIcn(PRPAIN201306UV02 response) {
    PRPAMT201310UV02Patient patient =
        Optional.ofNullable(response)
            .map(value -> value.getControlActProcess())
            .map(controlActProcess -> emptyToNull(controlActProcess.getSubject()))
            .map(subject -> subject.get(0))
            .map(getSubject -> getSubject.getRegistrationEvent())
            .map(registrationEvent -> registrationEvent.getSubject1())
            .map(subject1 -> subject1.getPatient())
            .orElse(null);
    if (getId(patient, EDIPI_LOOKUP_VALUE) != null) {
      return InputEdiPiOrIcn.builder()
          .edipiORicn(
              InputEdipiIcn.builder()
                  .inputType("EDIPI")
                  .edipiORicnValue(getId(patient, EDIPI_LOOKUP_VALUE))
                  .build())
          .build();
    } else if (getId(patient, ICN_LOOKUP_VALUE) != null) {
      return InputEdiPiOrIcn.builder()
          .edipiORicn(
              InputEdipiIcn.builder()
                  .inputType("ICN")
                  .edipiORicnValue(getId(patient, ICN_LOOKUP_VALUE))
                  .build())
          .build();
    }
    return null;
  }

  /**
   * Retrieves last name from MPI response.
   *
   * @param mpiResponse Mpi Response.
   * @return Returns last name of veteran.
   */
  @SneakyThrows
  public static String getLastName(@NonNull PRPAIN201306UV02 mpiResponse) {
    String firstName;

    try {
      firstName =
          mpiResponse
              .getControlActProcess()
              .getSubject()
              .get(0)
              .getRegistrationEvent()
              .getSubject1()
              .getPatient()
              .getPatientPerson()
              .getValue()
              .getName()
              .get(0)
              .getContent()
              .stream()
              .filter(item -> item.getClass() == JAXBElement.class)
              .map(i -> (JAXBElement) i)
              .filter(name -> name.getValue().getClass() == EnFamily.class)
              .map(i -> (EnFamily) i.getValue())
              .toList()
              .get(0)
              .getContent()
              .get(0)
              .toString();
    } catch (Exception e) {
      throw new Exception("No last name found in mpi response.");
    }

    return firstName;
  }

  /** Extract SSN String from PRPAIN201306UV02 MPI response object. */
  public static String getSsn(PRPAIN201306UV02 response) {
    List<PRPAMT201310UV02OtherIDs> asOtherIds =
        Optional.ofNullable(response)
            .map(value -> value.getControlActProcess())
            .map(controlActProcess -> emptyToNull(controlActProcess.getSubject()))
            .map(subject -> subject.get(0))
            .map(getSubject -> getSubject.getRegistrationEvent())
            .map(registrationEvent -> registrationEvent.getSubject1())
            .map(subject1 -> subject1.getPatient())
            .map(person -> person.getPatientPerson().getValue())
            .map(ids -> ids.getAsOtherIDs())
            .orElse(null);

    if (asOtherIds == null || asOtherIds.isEmpty()) {
      return null;
    }
    for (PRPAMT201310UV02OtherIDs asOtherId : asOtherIds) {
      if (asOtherId == null || asOtherId.getId() == null || isBlank(asOtherId.getId())) {
        continue;
      } else {
        for (II id : asOtherId.getId()) {
          if (id.getRoot().contains(SSN_LOOKUP_ROOT)) {
            return Optional.ofNullable(id.getExtension()).orElse(null);
          }
        }
      }
    }
    return null;
  }
}
