package org.sunbird;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * this constant file is used to get the Constants which is used by entire actors
 */
public class CertsConstant {
    private static Logger logger = Logger.getLogger(CertsConstant.class);

    private static final String BADGE_URL = "Badge.json";
    private static final String ISSUER_URL = "Issuer.json";
    private static final String CONTEXT = "v1/context.json";
    private static final String PUBLIC_KEY_URL = "_publicKey.json";
    private static final String VERIFICATION_TYPE = "SignedBadge";
    private static final String CLOUD_UPLOAD_RETRY_COUNT = "3";
    private static final String ACCESS_CODE_LENGTH = "6";
    public static final String DOWNLOAD_LINK_EXPIRY_TIMEOUT = "download_link_expiry_timeout";
    private static final String LINK_TIMEOUT = "600";
    private static final String SIGNATORY_EXTENSION = "v1/extensions/SignatoryExtension";
    private static String DOMAIN_URL = getDomainUrlFromEnv();
    private String CONTAINER_NAME;
    private static final String ENC_SERVICE_URL = getEncServiceUrl();
    private String CLOUD_STORAGE_TYPE;
    private static final String SLUG = getSlugFormEnv();
    private static final String DOMAIN_SLUG = DOMAIN_URL + "/" + SLUG;

    public String getBADGE_URL(String rootOrgId, String batchId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DOMAIN_URL + "/" + SLUG);
        if (StringUtils.isNotEmpty(rootOrgId))
            stringBuilder.append("/" + rootOrgId);
        if (StringUtils.isNotEmpty(batchId))
            stringBuilder.append("/" + batchId);
        return stringBuilder.append("/" + BADGE_URL).toString();
    }


    public String getISSUER_URL(String rootOrgId) {
        return appendOrgIdIfExists(DOMAIN_SLUG, rootOrgId) + "/" + ISSUER_URL;
    }

    public String getCONTEXT() {
        return String.format("%s/%s/%s", DOMAIN_URL, SLUG, CONTEXT);
    }

    public String getPUBLIC_KEY_URL(String rootOrgId, String keyId) {
        return appendOrgIdIfExists(DOMAIN_SLUG, rootOrgId) + "/" + keyId + PUBLIC_KEY_URL;
    }

    public String getVERIFICATION_TYPE() {
        return VERIFICATION_TYPE;
    }

    public String getCLOUD_UPLOAD_RETRY_COUNT() {
        String retryCount = getPropertyFromEnv(JsonKey.CLOUD_UPLOAD_RETRY_COUNT);
        return StringUtils.isNotBlank(retryCount) ? retryCount : CLOUD_UPLOAD_RETRY_COUNT;
    }

    public String getACCESS_CODE_LENGTH() {
        return ACCESS_CODE_LENGTH;
    }

    public String getDOMAIN_URL() {
        return DOMAIN_URL;
    }

    public String getCONTAINER_NAME() {
        CONTAINER_NAME = getContainerNameFromEnv();
        return CONTAINER_NAME;
    }

    private static String getDomainUrlFromEnv() {
        String domainUrl = getPropertyFromEnv(JsonKey.DOMAIN_URL);
        return StringUtils.isNotBlank(domainUrl) ? domainUrl : "https://dev.sunbirded.org";
//        validateEnvProperty(domainUrl);
//        return domainUrl;
    }

    private static String getContainerNameFromEnv() {
        return getPropertyFromEnv(JsonKey.CONTAINER_NAME);
    }

    private static String getPropertyFromEnv(String property) {
        return System.getenv(property);
    }

    private static void validateEnvProperty(String property) {
        if (StringUtils.isBlank(property)) {
            printErrorForMissingEnv(property);
            System.exit(-1);
        }
    }

    private static void printErrorForMissingEnv(String env) {
        logger.error("Constant:printErrorForMissingEnv:No env variable found ".concat(env));
    }

    private static String getEncServiceUrl() {
        String encServiceUrl = getPropertyFromEnv(JsonKey.ENC_SERVICE_URL);
//        validateEnvProperty(encServiceUrl);
//        return encServiceUrl;
        return StringUtils.isNotBlank(encServiceUrl) ? encServiceUrl : "http://enc-service:8013";

    }

    public String getEncSignUrl() {
        return String.format("%s/%s", ENC_SERVICE_URL, JsonKey.SIGN);
    }

    public String getEncSignVerifyUrl() {
        return String.format("%s/%s", ENC_SERVICE_URL, JsonKey.VERIFY);

    }


    public String getSignCreator(String orgId, String keyId) {
        return appendOrgIdIfExists(DOMAIN_SLUG, orgId) + "/" + keyId + PUBLIC_KEY_URL;
    }

    public String getEncryptionServiceUrl() {
        return getEncServiceUrl();
    }

    public static String getExpiryLink(String key) {
        return getPropertyFromEnv(key) != null ? getPropertyFromEnv(key) : LINK_TIMEOUT;
    }


    private static String getCloudStorageTypeFromEnv() {
        return getPropertyFromEnv(JsonKey.CLOUD_STORAGE_TYPE);
    }


    public String getCloudStorageType() {
        CLOUD_STORAGE_TYPE = getCloudStorageTypeFromEnv();
        return CLOUD_STORAGE_TYPE;
    }

    public String getAzureStorageSecret() {
        return getPropertyFromEnv(JsonKey.AZURE_STORAGE_SECRET);
    }

    public String getAzureStorageKey() {
        return getPropertyFromEnv(JsonKey.AZURE_STORAGE_KEY);
    }

    public String getAwsStorageSecret() {
        return getPropertyFromEnv(JsonKey.AWS_STORAGE_SECRET);
    }

    public String getAwsStorageKey() {
        return getPropertyFromEnv(JsonKey.AWS_STORAGE_KEY);
    }

    public String getSignatoryExtensionUrl() {
        return String.format("%s/%s/%s/%s", DOMAIN_URL, SLUG, SIGNATORY_EXTENSION, "context.json");
    }


    private static String getSlugFormEnv() {
        String slug = getPropertyFromEnv(JsonKey.SLUG);
        return StringUtils.isNotBlank(slug) ? slug : "certs";
//        validateEnvProperty(slug);
//        return slug;
    }

    public String getSlug() {
        return SLUG;
    }

    /**
     * appends orgId to the domain_slug if exists
     *
     * @param uri
     * @param orgId
     * @return
     */
    private String appendOrgIdIfExists(String uri, String orgId) {
        if (StringUtils.isNotEmpty(orgId))
            return uri + "/" + orgId;
        else return uri;
    }


    public String getPreview(String preview) {
        if (StringUtils.isNotBlank(preview))
            return preview;
        return Boolean.toString(false);
    }

    public Map<String, Object> getStorageParamsFromEvn() {
        logger.info("getting storage params from env");
        String type = getCloudStorageType();
        Map<String, Object> storeParams = new HashMap<>();
        storeParams.put(JsonKey.TYPE, type);
        if (StringUtils.isNotBlank(type)) {
            if (type.equals(JsonKey.AZURE)) {
                storeParams.put(JsonKey.AZURE, getAzureParams());
            }
            if (type.equals(JsonKey.AWS)) {
                storeParams.put(JsonKey.AWS, getAwsParams());
            }
        }
        return storeParams;
    }

    private Map<String, String> getAzureParams() {
        Map<String, String> azureParams = new HashMap<>();
        azureParams.put(JsonKey.containerName, getCONTAINER_NAME());
        azureParams.put(JsonKey.ACCOUNT, getAzureStorageKey());
        azureParams.put(JsonKey.KEY, getAzureStorageSecret());
        return azureParams;
    }

    private Map<String, String> getAwsParams() {
        Map<String, String> awsParams = new HashMap<>();
        awsParams.put(JsonKey.containerName, getCONTAINER_NAME());
        awsParams.put(JsonKey.ACCOUNT, getAwsStorageKey());
        awsParams.put(JsonKey.KEY, getAwsStorageSecret());
        return awsParams;
    }

}
