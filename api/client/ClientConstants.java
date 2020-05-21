package org.openmrs.module.insuranceclaims.api.client;

public final class ClientConstants {

    public static final String API_LOGIN_PROPERTY = "insuranceclaims.externalApiLogin";
    public static final String API_PASSWORD_PROPERTY = "insuranceclaims.externalApiPassword";
    public static final String USER_AGENT = "user-agent";
    //When user agent header is not present, rest template throws 403 exception
    public static final String CLIENT_HELPER_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";

    public static final String BASE_URL_PROPERTY = "insuranceclaims.baseInsuranceClaimUrl";
    public static final String CLAIM_RESPONSE_SOURCE_URI = "insuranceclaims.claimResponseResource";
    public static final String CLAIM_SOURCE_URI = "insuranceclaims.claimResource";
    public static final String ELIGIBILITY_SOURCE_URI = "insuranceclaims.eligibilityRequestResource";
    public static final String PATIENT_SOURCE_URI = "insuranceclaims.patientRequestResource";
    public static final String PATIENT_ID_PARAM_PROPERTY = "insuranceclaims.patientRequestId";

    private ClientConstants() {};
}
