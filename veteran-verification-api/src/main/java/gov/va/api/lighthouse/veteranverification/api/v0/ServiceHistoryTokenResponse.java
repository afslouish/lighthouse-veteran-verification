package gov.va.api.lighthouse.veteranverification.api.v0;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.NonNull;

public class ServiceHistoryTokenResponse {
  @NonNull
  @ArraySchema(
      schema =
          @Schema(
              description = "A token containing signed claims about a Veteran's service history",
              example =
                  "eyJhbGciOiJSUzI1NiIsImtpZCI6IjA4OGQyNDIzMmZmNmZhYTRjZDRjZmVjMTI2YWQwNDMxZGZmMWV"
                      + "hMDI4YWZkYjFjODZiMzcxOGQ3MDE3MWFlZDYifQ.eyJkYXRhIjp7ImlkIjoiMTIzMTJBQVNEZ"
                      + "iIsInR5cGUiOiJzZXJ2aWNlLWhpc3RvcnktZXBpc29kZXMiLCJhdHRyaWJ1dGVzIjp7InN0YX"
                      + "J0X2RhdGUiOiIxOTQ4LTA0LTA4IiwiZW5kX2RhdGUiOiIxOTUwLTA1LTEwIiwiYnJhbmNoIjo"
                      + "iQWlyIEZvcmNlIiwiZGlzY2hhcmdlX3N0YXR1cyI6Imhvbm9yYWJsZSIsImRlcGxveW1lbnRz"
                      + "IjpbeyJzdGFydF9kYXRlIjoiMTk0OC0xMC0xMCIsImVuZF9kYXRlIjoiMTk0OS0xMC0wOSIsI"
                      + "mxvY2F0aW9uIjoiS09SIn1dfX19.S39hmL-5nxvdJnJ_PSHdfiP744dTcGzrUKsqcVQa1FomU"
                      + "U15Dr2A2gQMF7XWO2DCLrdM0bCtIS8mCSwI00nqQFqXGjZVGQTIyzQJDPHxV6jnYwwaXD1vzb"
                      + "xlsycWF2ZQ_5Wx6TIhnXiEkbn_rzW_VleVnHjLCRQwzmpwGGDsL_I"))
  public List<String> data;
}
