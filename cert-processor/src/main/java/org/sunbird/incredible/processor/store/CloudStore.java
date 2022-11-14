package org.sunbird.incredible.processor.store;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sunbird.cloud.storage.BaseStorageService;
import org.sunbird.cloud.storage.exception.StorageServiceException;
import org.sunbird.cloud.storage.factory.StorageConfig;
import org.sunbird.cloud.storage.factory.StorageServiceFactory;

import java.io.File;

public class CloudStore implements ICertStore {

    private Logger logger = LoggerFactory.getLogger(CloudStore.class);

    private StoreConfig storeConfig;

    private BaseStorageService storageService = null;

    private CloudStorage cloudStorage = null;

    private int retryCount;


    public CloudStore(StoreConfig storeConfig) {
        this.storeConfig = storeConfig;
        retryCount = Integer.parseInt(storeConfig.getCloudRetryCount());
        init();
    }

    @Override
    public String save(File file, String uploadPath) {
        return upload(file, uploadPath);
    }

    @Override
    public String getPublicLink(File file, String uploadPath) {
        String path = getPath(uploadPath);
        return cloudStorage.upload(storeConfig.getContainerName(), path, file, false, retryCount);
    }

    @Override
    public void get(String url, String fileName, String localPath) throws StorageServiceException {
        download(fileName, localPath);
    }

    @Override
    public void get(String fileName) throws StorageServiceException {
        String path = "conf/";
        download(fileName, path);
    }

    @Override
    public void init() {
        if (StringUtils.isNotBlank(storeConfig.getType())) {
            String storageKey = storeConfig.getAccount();
            String storageSecret = storeConfig.getKey();
            StorageConfig storageConfig = new StorageConfig(storeConfig.getType(), storageKey, storageSecret);
            logger.info("StorageParams:init:all storage params initialized for azure block");
            storageService = StorageServiceFactory.getStorageService(storageConfig);
            cloudStorage = new CloudStorage(storageService);
        } else {
            logger.error("StorageParams:init:provided cloud store type doesn't match supported storage devices:".concat(storeConfig.getType()));
        }
    }

    private String getPath(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(path);
        if (StringUtils.isNotBlank(storeConfig.getPath())) {
            stringBuilder.append(storeConfig.getPath() + "/");
        }
        return stringBuilder.toString();
    }

    private String upload(File file, String path){
        String uploadPath = getPath(path);
        return cloudStorage.uploadFile(storeConfig.getContainerName(), uploadPath, file, false, retryCount);
    }

    private void download(String fileName, String localPath) {
        cloudStorage.downloadFile(storeConfig.getContainerName(), fileName, localPath, false);
    }

    @Override
    public void close() {
        cloudStorage.closeConnection();
    }

}
