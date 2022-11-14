package org.sunbird.incredible.processor.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sunbird.incredible.processor.JsonKey;

import java.util.Map;

public class StoreConfig {

    private ObjectMapper mapper = new ObjectMapper();

    private String type;

    private String cloudRetryCount = "3";

    private String containerName;

    private String path;

    private String account;

    private String key;


    private StoreConfig() {
    }

    public StoreConfig(Map<String, Object> storeParams) {
        setType((String) storeParams.get(JsonKey.TYPE));
        setContainerName((String) storeParams.get(JsonKey.containerName));
        setAccount((String) storeParams.get(JsonKey.ACCOUNT));
        setKey((String) storeParams.get(JsonKey.KEY));
    }

    public boolean isCloudStore() {
        return getType() != null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCloudRetryCount() {
        return cloudRetryCount;
    }

    public void setCloudRetryCount(String cloudRetryCount) {
        this.cloudRetryCount = cloudRetryCount;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public String toString() {
        String stringRep = null;
        try {
            stringRep = mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
        }
        return stringRep;
    }
}
