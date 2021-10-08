package gov.va.api.lighthouse.veteranverification.service.controller.health;

import gov.va.api.lighthouse.bgs.BenefitsGatewayServicesClient;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.Callable;

@Configuration
public class BackEndHealthCheckRegistry {

    private final BenefitsGatewayServicesClient bgsClient;
    private final EmisVeteranStatusServiceClient emisClient;

    /** constructor. */
    public BackEndHealthCheckRegistry(
            @Autowired BenefitsGatewayServicesClient bgsClient,
            @Autowired EmisVeteranStatusServiceClient emisClient) {
        this.bgsClient = bgsClient;
        this.emisClient = emisClient;
    }

    public Map<String, Callable<ResponseEntity<String>>> getRegistry() {
        return Map.ofEntries(
                new AbstractMap.SimpleEntry<String, Callable<ResponseEntity<String>>>("BGS", () -> bgsClient.wsdl()),
                new AbstractMap.SimpleEntry<String, Callable<ResponseEntity<String>>>("EMIS", () -> emisClient.wsdl())
        );
    }
}
