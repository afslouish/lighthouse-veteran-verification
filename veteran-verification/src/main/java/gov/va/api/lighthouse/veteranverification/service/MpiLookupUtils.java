package gov.va.api.lighthouse.veteranverification.service;

import static gov.va.api.lighthouse.veteranverification.service.controller.Transformers.emptyToNull;
import static gov.va.api.lighthouse.veteranverification.service.controller.Transformers.isBlank;

import gov.va.viers.cdi.emis.commonservice.v1.InputEdipiIcn;
import gov.va.viers.cdi.emis.requestresponse.v1.InputEdiPiOrIcn;
import java.util.Optional;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201310UV02Patient;

public class MpiLookupUtils {
  private static final String ICN_LOOKUP_VALUE = "^NI^200M^USVHA^P";

  private static final String EDIPI_LOOKUP_VALUE = "^NI^200DOD^USDOD^A";

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
}
