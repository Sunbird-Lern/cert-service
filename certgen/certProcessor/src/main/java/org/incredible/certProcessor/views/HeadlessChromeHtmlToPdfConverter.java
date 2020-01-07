package org.incredible.certProcessor.views;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeadlessChromeHtmlToPdfConverter {
    private static Logger logger = LoggerFactory.getLogger(HeadlessChromeHtmlToPdfConverter.class);

    public static void convert(File htmlFile, File pdfFile) {
        Process process = null;
        try {
            String operatingSystem = System.getProperty("os.name");
            boolean isWindows = operatingSystem.toLowerCase().startsWith("windows");
            boolean isMac = operatingSystem.contains("Mac OS X");
            String appInvokeCommand = "";
            String appArgs = "--no-sandbox --headless --print-to-pdf=" + pdfFile.getAbsolutePath() + " " + htmlFile.getAbsolutePath();
            Runtime rt = Runtime.getRuntime();
            logger.info("HeadlessChromeHtmlToPdfConverter: convert: operating system used is {}", operatingSystem);
            if (isWindows) {
                appInvokeCommand = "chromium-browser";
                process = rt.exec(new String[]{"cmd.exe", "/c", appInvokeCommand + " " + appArgs});
            } else if (isMac) {
                appInvokeCommand = "/Applications/Chromium.app/Contents/MacOS/Chromium";
                process = rt.exec(appInvokeCommand + " " + appArgs);
            } else {
                long start = System.currentTimeMillis();
                appInvokeCommand = "chromium-browser";
                process = rt.exec(new String[]{"sh", "-c", appInvokeCommand + " " + appArgs});
                logger.info("Time took to run command "+ (System.currentTimeMillis()-start)+"");
            }
        } catch (Exception e) {
            logger.error("HeadlessChromeHtmlToPdfConverter: convert: error occurred "+e);
            e.printStackTrace();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        if (inputStream == null)
            return "Empty";
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        inputStream.close();
        return result.toString(StandardCharsets.UTF_8.name());
    }
}
