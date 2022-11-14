package org.sunbird;

import org.apache.commons.lang3.StringUtils;
import org.sunbird.incredible.processor.JsonKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class QRStorageParams {

    private Logger logger = LoggerFactory.getLogger(QRStorageParams.class);
    public Map<String, Object> storeParams;

    public QRStorageParams(String storageType) {
        storeParams = getStorageParamsFromEnv(storageType);
    }

    private Map<String, Object> getStorageParamsFromEnv(String type) {
        logger.info("QRStorageParams getting storage params from env ");
        Map<String, Object> storeParams = new HashMap<>();
        storeParams.put(JsonKey.TYPE, type);
        if (StringUtils.isNotBlank(type)) {
            storeParams.put(type, getCloudStoreParams());
        }
        return storeParams;
    }

    private Map<String, String> getCloudStoreParams() {
        Map<String, String> storeParams = new HashMap<>();
        storeParams.put(JsonKey.containerName, System.getenv(JsonKey.PUBLIC_CONTAINER_NAME));
        storeParams.put(JsonKey.ACCOUNT, System.getenv(JsonKey.PUBLIC_CLOUD_STORAGE_KEY));
        storeParams.put(JsonKey.KEY, System.getenv(JsonKey.PUBLIC_CLOUD_STORAGE_SECRET));
        return storeParams;
    }
}
