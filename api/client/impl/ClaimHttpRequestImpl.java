package org.openmrs.module.insuranceclaims.api.client.impl;

import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.dstu3.model.ClaimResponse;
import org.hl7.fhir.exceptions.FHIRException;
import org.openmrs.module.insuranceclaims.api.client.ClaimHttpRequest;
import org.openmrs.module.insuranceclaims.api.client.FHIRClient;
import org.openmrs.module.insuranceclaims.api.model.InsuranceClaim;
import org.openmrs.module.insuranceclaims.api.service.fhir.FHIRInsuranceClaimService;
import org.springframework.web.client.HttpServerErrorException;

import java.net.URISyntaxException;

public class ClaimHttpRequestImpl implements ClaimHttpRequest {

    private FHIRClient fhirRequestClient;

    private FHIRInsuranceClaimService fhirInsuranceClaimService;

    @Override
    public Claim getClaimRequest(String resourceUrl, String claimCode)
            throws URISyntaxException {
        String url = resourceUrl + "/" + claimCode;
        return fhirRequestClient.getObject(url, Claim.class);
    }

    @Override
    public ClaimResponse sendClaimRequest(String resourceUrl, InsuranceClaim insuranceClaim)
            throws URISyntaxException, HttpServerErrorException, FHIRException {
        String url = resourceUrl + "/";
        Claim claimToSend = fhirInsuranceClaimService.generateClaim(insuranceClaim);

        return fhirRequestClient.postObject(url, claimToSend, ClaimResponse.class);
    }

    @Override
    public ClaimResponse getClaimResponse(String baseUrl, String claimCode) throws URISyntaxException {
        String url = baseUrl + "/" + claimCode;
        return fhirRequestClient.getObject(url, ClaimResponse.class);
    }

    public void setFhirInsuranceClaimService(FHIRInsuranceClaimService fhirInsuranceClaimService) {
        this.fhirInsuranceClaimService = fhirInsuranceClaimService;
    }

    public void setFhirRequestClient(FHIRClient fhirRequestClient) {
        this.fhirRequestClient = fhirRequestClient;
    }
}