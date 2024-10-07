package com.etlions.doctor_file;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.etlions.doctor_file.pdf.secure.PasswordProtector.protectPdf;

@SpringBootApplication
public class DoctorFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorFileApplication.class, args);
	}

	@Bean
	ApplicationRunner runner (){
		return  args  -> {
			try {
				// Path of the PDF in the resources folder
				String resourceFileName = "William_Resistencia_Rebelión.pdf";
				String inputPdfPath = getResourcePath(resourceFileName);


				String userPassword = "userpass";
				String ownerPassword = "ownerpass";


				String outputPdfPath = "protected_William_Resistencia_Rebelión.pdf";


				protectPdf(inputPdfPath, outputPdfPath, userPassword, ownerPassword);

			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	public String getResourcePath(String resourceName) throws IOException {
		InputStream resourceStream = this.getClass().getResourceAsStream(resourceName);
		if (resourceStream == null) {
			throw new IOException("File not found in resources: " + resourceName);
		}
		Files.copy(resourceStream, Paths.get(resourceName));
		return resourceName;
	}



}
