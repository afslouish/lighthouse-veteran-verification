package gov.va.api.lighthouse.veteranverification.service;

import lombok.experimental.UtilityClass;

/** Custom exceptions utility class. */
@UtilityClass
public class Exceptions {
  /** Custom class for EMIS inaccessible wsdl to be used by veteran verification status endpoint. */
  public static final class EmisInaccesibleWsdlException extends RuntimeException {
    public EmisInaccesibleWsdlException() {
      super("EMIS WSDL not accessible.");
    }
  }
}
