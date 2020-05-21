package org.openmrs.module.insuranceclaims.api.client;

import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.dstu3.model.ClaimResponse;
import org.hl7.fhir.exceptions.FHIRException;
import org.openmrs.module.insuranceclaims.api.model.InsuranceClaim;

import java.net.URISyntaxException;

public interface ClaimHttpRequest {

    ClaimResponse sendClaimRequest(String resourceUrl, InsuranceClaim insuranceClaim) throws URISyntaxException, FHIRException;

    Claim getClaimRequest(String resourceUrl, String claimCode) throws URISyntaxException;

    ClaimResponse getClaimResponse(String baseUrl, String claimCode) throws URISyntaxException;
}
