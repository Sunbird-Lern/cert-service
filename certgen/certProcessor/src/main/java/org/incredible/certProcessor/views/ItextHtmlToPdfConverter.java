package org.incredible.certProcessor.views;


import com.itextpdf.html2pdf.HtmlConverter;
/*import com.itextpdf.licensekey.LicenseKey;
import org.apache.commons.lang3.StringUtils;
import org.incredible.certProcessor.JsonKey;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class ItextHtmlToPdfConverter {

    private static Logger logger = LoggerFactory.getLogger(ItextHtmlToPdfConverter.class);

    public static void convert(File htmlSource, File pdfFile) {
        try {
            /*if(Boolean.parseBoolean(System.getenv(JsonKey.ITEXT_LICENSE_ENABLED)) &&
                    StringUtils.isNotEmpty(System.getenv(JsonKey.ITEXT_LICENSE_PATH))) {
                try {
                    InputStream ip = PdfConverter.class.getResourceAsStream("cd ");
                    LicenseKey.loadLicenseFile(ip);
                    logger.info("license is loaded");
                } catch (Exception e) {
                    logger.error("Exception in loading license");
                }
            }*/
            HtmlConverter.convertToPdf(htmlSource, pdfFile);
            logger.info("Pdf file is created ");
        } catch (FileNotFoundException e) {
            logger.error("exception while generating pdf file {}", e.getMessage());
        } catch (IOException e) {
            logger.error("exception while generating pdf file {}", e.getMessage());
        }
    }

}
