package org.openmrs.module.insuranceclaims.api.service.fhir.util;

import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.dstu3.model.ClaimResponse;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.exceptions.FHIRException;
import org.openmrs.module.insuranceclaims.api.model.InsuranceClaim;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openmrs.module.insuranceclaims.api.service.fhir.util.IdentifierUtil.createIdentifier;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.IdentifierUtil.getIdentifierValueByCode;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.InsuranceClaimConstants.ACCESSION_ID;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.InsuranceClaimConstants.EXPLANATION_CATEGORY;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.InsuranceClaimConstants.GUARANTEE_ID_CATEGORY;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.InsuranceClaimConstants.HL7_VALUESET_SYSTEM;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.InsuranceClaimConstants.MEDICAL_RECORD_NUMBER;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.InsuranceClaimConstants.MISSING_DATE_CREATED;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.InsuranceClaimConstants.MISSING_DATE_FROM;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.InsuranceClaimConstants.MISSING_DATE_TO;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.InsuranceClaimConstants.PERIOD_FROM;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.InsuranceClaimConstants.PERIOD_TO;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.SpecialComponentUtil.createSpecialComponent;
import static org.openmrs.module.insuranceclaims.api.service.fhir.util.SpecialComponentUtil.getSpecialConditionComponentFromCategory;

public final class InsuranceClaimUtil {

    public static CodeableConcept createClaimVisitType(InsuranceClaim omrsClaim) {
        String omrsVisitType = omrsClaim.getVisitType().getName();
        CodeableConcept visitType = new CodeableConcept();
        visitType.setText(omrsVisitType);
        return visitType;
    }

    public static List<Identifier> createClaimIdentifier(InsuranceClaim omrsClaim) {
        List<Identifier> fhirIdentifier = new ArrayList<>();
        fhirIdentifier.add(createUuidIdentifier(omrsClaim));
        fhirIdentifier.add(createClaimCodeIdentifier(omrsClaim));

        return fhirIdentifier;
    }

    public static Map<String, Date> getClaimBillablePeriod(Claim claim, List<String> errors) {
        Map<String, Date> claimPeriod = new HashMap<>();
        Period billablePeriod = claim.getBillablePeriod();
        Date from = billablePeriod.getStart();
        if (from == null) {
            errors.add(MISSING_DATE_FROM);
        }
        Date to = billablePeriod.getEnd();
        if (to == null) {
            errors.add(MISSING_DATE_TO);
        }
        claimPeriod.put(PERIOD_FROM, from);
        claimPeriod.put(PERIOD_TO, to);

        return claimPeriod;
    }

    public static String getClaimExplanation(Claim claim, List<String> errors) {
        try {
            return getSpecialConditionComponentFromCategory(claim, EXPLANATION_CATEGORY);
        } catch (FHIRException e) {
            errors.add(e.getMessage());
            return null;
        }
    }

    public static String getClaimGuaranteeId(Claim claim, List<String> errors) {
        try {
            return getSpecialConditionComponentFromCategory(claim, GUARANTEE_ID_CATEGORY);
        } catch (FHIRException e) {
            errors.add(e.getMessage());
            return null;
        }
    }

    public static String getClaimCode(Claim claim, List<String> errors) {
        return getIdentifierValueByCode(claim, MEDICAL_RECORD_NUMBER, errors);
    }

    public static String getClaimUuid(Claim claim, List<String> errors) {
        return getIdentifierValueByCode(claim, ACCESSION_ID, errors);
    }

    public static String getClaimResponseId(ClaimResponse claimResponse) {
        //Claim ID should be in format "ClaimResponse/claimId";
        return claimResponse.getId().split("/")[1];
    }

    public static Date getClaimDateCreated(Claim claim, List<String> errors) {
        Date created = claim.getCreated();
        if (created == null) {
            errors.add(MISSING_DATE_CREATED);
            return null;
        }
        return created;
    }

    public static Claim.SpecialConditionComponent createClaimExplanationInformation(InsuranceClaim omrsClaim) {
        return createSpecialComponent(omrsClaim.getExplanation(), EXPLANATION_CATEGORY);
    }

    public static Claim.SpecialConditionComponent createClaimGuaranteeIdInformation(InsuranceClaim omrsClaim) {
        return createSpecialComponent(omrsClaim.getGuaranteeId(), GUARANTEE_ID_CATEGORY);
    }

    private static Identifier createClaimCodeIdentifier(InsuranceClaim omrsClaim) {
        return createIdentifier(omrsClaim.getClaimCode(), MEDICAL_RECORD_NUMBER, HL7_VALUESET_SYSTEM);
    }

    private static Identifier createUuidIdentifier(InsuranceClaim omrsClaim) {
        return createIdentifier(omrsClaim.getUuid(), ACCESSION_ID, HL7_VALUESET_SYSTEM);
    }

    private InsuranceClaimUtil() {}
}
