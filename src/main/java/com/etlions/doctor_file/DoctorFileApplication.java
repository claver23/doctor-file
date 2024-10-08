package com.etlions.doctor_file;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class DoctorFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorFileApplication.class, args);
	}

	@Bean
	ApplicationRunner runner() {
		return args -> {
			try {
				// Simulate reading the PDF from resources as a byte array
				byte[] pdfBytes = readPdfAsByteArray();

				// Protect the PDF byte array
				byte[] protectedPdfBytes = protectPdf(pdfBytes, "userpass", "ownerpass");

				String outputDir = "target/classes/output/";
				Files.createDirectories(Paths.get(outputDir)); // Create the directory if it doesn't exist
				Files.write(Paths.get(outputDir + "protected_William_Resistencia_Rebelion.pdf"), protectedPdfBytes);

				System.out.println("File written to classpath output directory: " + outputDir);

			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	// Read the PDF from the resources directory as byte[]
	public byte[] readPdfAsByteArray() throws IOException {
		return getPdfFromResourceAsByteArray("William_Resistencia.pdf");
	}

	// Method to protect a byte array PDF
	public byte[] protectPdf(byte[] inputPdfBytes, String userPassword, String ownerPassword) throws IOException {
		// Load the PDF from the byte array
		try (PDDocument document = PDDocument.load(new ByteArrayInputStream(inputPdfBytes))) {
			// Apply protection
			StandardProtectionPolicy protectionPolicy = getStandardProtectionPolicy(userPassword, ownerPassword);
			document.protect(protectionPolicy);

			// Save the protected document to a byte array output stream
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			document.save(outputStream);

			// Return the protected PDF as byte[]
			return outputStream.toByteArray();
		}
	}

	private StandardProtectionPolicy getStandardProtectionPolicy(String userPassword, String ownerPassword) {
		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setCanPrint(true);

		StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(ownerPassword, userPassword, accessPermission);
		protectionPolicy.setEncryptionKeyLength(128); // Use 128-bit AES encryption
		return protectionPolicy;
	}

	// Method to get InputStream from resources and convert it to byte[]
	public byte[] getPdfFromResourceAsByteArray(String resourceName) throws IOException {
		try (InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream(resourceName)) {
			if (resourceStream == null) {
				throw new IOException("File not found in resources: " + resourceName);
			}

			// Convert InputStream to byte[]
			return resourceStream.readAllBytes();
		}
	}
}
