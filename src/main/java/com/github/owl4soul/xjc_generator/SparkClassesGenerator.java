package com.github.owl4soul.xjc_generator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SparkClassesGenerator {

	private String sparkDirName = "20191225";
	private String sparkSchemasVersion = "v2_52";

	private void sparkGenerateCreateDirs() throws IOException {

		String sparkFilesDir = "D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\" + sparkDirName;
		File sourceDir = new File(sparkFilesDir);

		File srcDir = new File(sparkFilesDir + "\\src");
		srcDir.mkdir();

		for (File file : sourceDir.listFiles()) {
			if (!file.isDirectory()) {

				String fileName = file.getName();
				String newDirName = fileName.substring(0, fileName.indexOf("."));

				if (fileName.contains("MonGetEventsList") || fileName.contains("GetPurchases") ||
					fileName.contains("IdentifyCompany") || fileName.contains("MonGetCompanyEvents") ||
					fileName.contains("ValidatePassport") || fileName.contains("MonPersonGetEventsList") ||
					fileName.contains("MonGetEntrepreneursEventsList") || fileName.contains("MonGetEntrepreneurEvents") ||
					fileName.contains("CheckCompany115FederalLaw")) {
					newDirName = newDirName + "1";
				}

				System.out.println(
						"CALL xjc -encoding UTF-8 -d " + sparkDirName + "\\src\\" + newDirName + " " + sparkDirName +
						"\\" + fileName);
				//CALL xjc -encoding UTF-8 -d 20150819\src\BankAccountingReport101 20150819\BankAccountingReport101.xsd

				File newDir = new File(sparkFilesDir + "\\src\\" + newDirName);
				newDir.mkdir();
			}
		}
	}



	private void sparkReplacePackajeInGeneratedFiles() throws IOException {

		String rootDirstr = "D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\" + sparkDirName + "\\src";
		File rootDir = new File(rootDirstr);
		for (File folderInRoot : rootDir.listFiles()) {
			String folderName = folderInRoot.getName();
			//		logger.debug("will replace in folder" + folderName);
			for (File folderGenerated : folderInRoot.listFiles()) {
				for (File file : folderGenerated.listFiles()) {
					String filePath = file.getAbsolutePath();
					Path path = Paths.get(filePath);
					Charset charset = StandardCharsets.UTF_8;
					String content = new String(Files.readAllBytes(path), charset);
					content = content.replace("package generated;",
											  "package ru.spi2.irule.javaee.web.services.clients.spark." +
											  sparkSchemasVersion + "." + folderName + ".generated;");
					content = content.replace("generated.",
											  "ru.spi2.irule.javaee.web.services.clients.spark." + sparkSchemasVersion +
											  "." + folderName + ".generated.");
					Files.write(path, content.getBytes(charset));
				}
			}
		}

		System.out.println("end ReplacePackajeInGeneratedFiles");

	}
}
