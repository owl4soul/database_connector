package com.github.owl4soul.xjc_generator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Deprecated
public class SparkClassesGenerator {

	private String sparkDirName = "20200203";
	private String sparkSchemasVersion = "v2_66";

	void readNode_Test() throws ParserConfigurationException, IOException, SAXException {
		String sparkFilesDir = "D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\" + sparkDirName;
		File sourceDir = new File(sparkFilesDir);

		// Получим первую .xsd
		File xsd = sourceDir.listFiles()[0].isDirectory() ? sourceDir.listFiles()[1] : sourceDir.listFiles()[0];

		// Создаем document builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(xsd);

		///////////////////////
		NodeList nodeList = document.getElementsByTagName("xs:element");

		boolean hasChildren = nodeList.getLength() > 0;
		NodeList childrenNodeList = null;


			for (int i = 0; i < nodeList.getLength(); i++) {
				childrenNodeList = getChildren((Element) nodeList.item(i));
				while (hasChildren) {
					for (int j = 0; j < childrenNodeList.getLength(); j++) {
						NodeList inner = ((Element)childrenNodeList.item(j)).getElementsByTagName("xs:element");
						for (int k = 0; k < inner.getLength(); k++) {
							hasChildren = getChildren((Element) inner.item(k)).getLength() > 0 ? true : false;
						}
					}
				}
			}


//		for (int i = 0; i < nodeList.getLength(); i++) {
//			Element element = (Element) nodeList.item(i);
//			if (element.hasAttributes()) {
//				String name = element.getAttribute("name");
//				String type = element.getAttribute("type");
//				System.out.println(name + " : " + type);
//			}
//		}
	}



	private NodeList getChildren(Element element) {
		System.out.println("CHILDREN FOR " + element.getAttribute("name") + " : " + element.getAttribute("type") + " -> ");
		NodeList childNodes = null;
		if (element.hasChildNodes()) {
			childNodes = element.getElementsByTagName("xs:element");
			for (int i = 0; i < childNodes.getLength(); i++) {
				Element child = (Element) childNodes.item(i);
				if (child.hasAttributes()) {
					String name = child.getAttribute("name");
					String type = child.getAttribute("type");
					System.out.println(name + " : " + type);
				}
			}
		}
		return childNodes;
	}







	void sparkGenerateCreateDirs() throws IOException {

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



	void sparkReplacePackajeInGeneratedFiles() throws IOException {

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
