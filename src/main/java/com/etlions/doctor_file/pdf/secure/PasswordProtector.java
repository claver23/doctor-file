package com.etlions.doctor_file.pdf.secure;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PasswordProtector {


    public static void protectPdf(String inputPdfPath, String outputPdfPath, String userPassword, String ownerPassword) throws IOException {
        try (PDDocument document = PDDocument.load(new File(inputPdfPath))) {

            StandardProtectionPolicy protectionPolicy = getStandardProtectionPolicy(userPassword, ownerPassword);
            document.protect(protectionPolicy);
            document.save(outputPdfPath);

            System.out.println("PDF password protection applied successfully.");
        }
    }

    private static StandardProtectionPolicy getStandardProtectionPolicy(String userPassword, String ownerPassword) {
        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setCanPrint(true);


        StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(ownerPassword, userPassword, accessPermission);
        protectionPolicy.setEncryptionKeyLength(128);  // Use 128-bit AES encryption
        return protectionPolicy;
    }



}
