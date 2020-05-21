package org.openmrs.module.insuranceclaims.api.service.fhir;

import org.hamcrest.Matchers;
import org.hl7.fhir.dstu3.model.ClaimResponse;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.exceptions.FHIRException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Provider;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.module.insuranceclaims.api.model.InsuranceClaim;
import org.openmrs.module.insuranceclaims.api.model.InsuranceClaimStatus;
import org.openmrs.module.insuranceclaims.api.mother.InsuranceClaimMother;
import org.openmrs.module.insuranceclaims.api.service.InsuranceClaimService;
import org.openmrs.module.insuranceclaims.api.util.TestConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FHIRClaimResponseServiceTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private InsuranceClaimService insuranceClaimService;

    @Autowired
    private FHIRClaimResponseService insuranceClaimResponseService;

    private InsuranceClaim insuranceClaim;

    @Before
    public void setUp() throws Exception {
        insuranceClaim = createTestInstance();
        insuranceClaimService.saveOrUpdate(insuranceClaim);

        Context.flushSession();
        Context.clearSession();
    }

    @Test
    public void generateFhirClaimResponse_shouldMapInsuranceClaimToFhirClaimResponse() throws FHIRException {
        InsuranceClaim savedInsuranceClaim = insuranceClaimService.getByUuid(insuranceClaim.getUuid());
        ClaimResponse generated = insuranceClaimResponseService.generateClaimResponse(savedInsuranceClaim);

        Assert.assertThat(generated, Matchers.notNullValue());
        Assert.assertThat(generated.getId(), Matchers.equalTo(insuranceClaim.getClaimCode()));
        Assert.assertThat(generated.getTotalBenefit().getValue(), Matchers.equalTo(insuranceClaim.getApprovedTotal()));
        Assert.assertThat(generated.getPayment().getAdjustmentReason().getText(), Matchers.equalTo(insuranceClaim.getAdjustment()));
        Assert.assertThat(generated.getPayment().getDate(), Matchers.equalTo(insuranceClaim.getDateProcessed()));
        Assert.assertThat(generated.getCreated(), Matchers.equalTo(insuranceClaim.getDateProcessed()));

        Assert.assertThat(generated.getRequest().getReference(), Matchers.equalTo(getExpectedClaimReference()));
        Assert.assertThat(generated.getCommunicationRequest(), Matchers.hasSize(1));
        Assert.assertThat(generated.getCommunicationRequest().get(0).getReference(), Matchers.equalTo(getExpectedCommunicationRequest()));

        Assert.assertThat(generated.getIdentifier(), Matchers.hasSize(2));
        Assert.assertThat(generated.getIdentifier()
                .stream()
                .map(Identifier::getValue)
                .collect(Collectors.toList()), Matchers.contains(getExpectedIdentifierCodes().toArray()));

        Assert.assertThat(
                Integer.parseInt(generated.getOutcome().getCoding().get(0).getCode()),
                Matchers.equalTo(InsuranceClaimStatus.ENTERED.getNumericStatus()));

        //TODO: Add item and processNote after changes in database
    }

    @Test
    public void generateOmrsClaim_shouldMapFhirClaimResponseToOmrsClaim() throws FHIRException {
        InsuranceClaim savedInsuranceClaim = insuranceClaimService.getByUuid(insuranceClaim.getUuid());
        ClaimResponse response = insuranceClaimResponseService.generateClaimResponse(savedInsuranceClaim);
        List<String> errors = new ArrayList<>();
        InsuranceClaim generated = insuranceClaimResponseService.generateOmrsClaim(response, errors);

        Assert.assertThat(errors, Matchers.hasSize(0));
        Assert.assertThat(generated.getUuid(), Matchers.equalTo(savedInsuranceClaim.getUuid()));
        Assert.assertThat(generated.getClaimCode(), Matchers.equalTo(savedInsuranceClaim.getClaimCode()));
        Assert.assertThat(generated.getDateProcessed(), Matchers.equalTo(savedInsuranceClaim.getDateProcessed()));
        Assert.assertThat(generated.getAdjustment(), Matchers.equalTo(savedInsuranceClaim.getAdjustment()));
        Assert.assertThat(generated.getApprovedTotal(), Matchers.equalTo(savedInsuranceClaim.getApprovedTotal()));
        Assert.assertThat(generated.getRejectionReason(), Matchers.equalTo(savedInsuranceClaim.getRejectionReason()));
        Assert.assertThat(generated.getStatus(), Matchers.equalTo(savedInsuranceClaim.getStatus()));
    }

    private InsuranceClaim createTestInstance() {
        Location location = Context.getLocationService().getLocation(TestConstants.TEST_LOCATION_ID);
        Provider provider = Context.getProviderService().getProvider(TestConstants.TEST_PROVIDER_ID);
        VisitType visitType = Context.getVisitService().getVisitType(TestConstants.TEST_VISIT_TYPE_ID);
        PatientIdentifierType identifierType = Context.getPatientService()
                .getPatientIdentifierType(TestConstants.TEST_IDENTIFIER_TYPE_ID);
        return InsuranceClaimMother.createTestInstance(location, provider, visitType, identifierType);
    }

    private String getExpectedClaimReference() {
        return "Claim/" + insuranceClaim.getClaimCode();
    }

    private String getExpectedCommunicationRequest() {
        return "CommunicationRequest/" + insuranceClaim.getUuid();
    }

    private List<String> getExpectedIdentifierCodes() {
        List<String> expectedCodes = new ArrayList<>();
        expectedCodes.add(insuranceClaim.getUuid());
        expectedCodes.add(insuranceClaim.getClaimCode());
        return expectedCodes;
    }
}
