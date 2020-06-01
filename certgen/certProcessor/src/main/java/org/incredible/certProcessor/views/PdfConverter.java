package org.incredible.certProcessor.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.itextpdf.html2pdf.HtmlConverter;



public class PdfConverter {

    private static Logger logger = LoggerFactory.getLogger(PdfConverter.class);

    public static void convertor(File htmlSource, String certUuid, String directory) {
        File file = new File(directory, certUuid + ".pdf");
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
            HtmlConverter.convertToPdf(htmlSource, file);
            logger.info("Pdf file is created ");
        } catch (FileNotFoundException e) {
            logger.error("exception while generating pdf file {}", e.getMessage());
        } catch (IOException e) {
            logger.error("exception while generating pdf file {}", e.getMessage());
        }
    }

}
