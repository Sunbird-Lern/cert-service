package org.incredible.certProcessor.store;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.incredible.certProcessor.JsonKey;

import javax.ws.rs.HttpMethod;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class LocalStore implements ICertStore {

    private Logger logger = Logger.getLogger(LocalStore.class);

    @Override
    public String save(File file, Map<String, String> properties) throws IOException {
        FileUtils.copyFileToDirectory(file, new File("public/"));
        return properties.get(JsonKey.DOMAIN_URL) + "/" + JsonKey.ASSETS + "/" + file.getName();
    }


    @Override
    public void get(String url, String fileName) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(HttpMethod.GET);
        InputStream inputStream = connection.getInputStream();
        FileOutputStream out = new FileOutputStream("conf/" + fileName);
        copy(inputStream, out, 1024);
        out.close();
        inputStream.close();
        logger.info(fileName + "downloaded");
    }

    @Override
    public void init() {

    }

    private void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int read = input.read(buf);
        while (read >= 0) {
            output.write(buf, 0, read);
            read = input.read(buf);
        }
        output.flush();
    }

}
