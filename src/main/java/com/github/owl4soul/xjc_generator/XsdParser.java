package com.github.owl4soul.xjc_generator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XsdParser {

	private String sparkDirName = "20200203";
	private String sparkSchemasVersion = "v2_66";

	private Set<Element> allElementSet = new HashSet<>();
	private List<Element> allElementList = new ArrayList<>();

	private Set<Object> allChildrenSet = new HashSet<>();
	private List<Object> allChildrenList = new ArrayList<>();


	//Размапленные элементы, имя элемента = элемент
	private Map<String, Node> mappedElements = new HashMap<>();

	void parseDataInFolder() throws ParserConfigurationException, IOException, SAXException, TransformerException {
		String sparkFilesDir = "D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\" + sparkDirName;
		File sourceDir = new File(sparkFilesDir);

		// Получим первую .xsd
//		File xsd = sourceDir.listFiles()[0].isDirectory() ? sourceDir.listFiles()[1] : sourceDir.listFiles()[0];

		File resultXsd = new File("D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\20200203\\result\\result.xsd");


		// Создаем document builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document resultDoc = builder.parse(resultXsd);

		for (File file : sourceDir.listFiles()) {
			if (!file.isDirectory()) {
				Document document = builder.parse(file);

				initNodesSetByXsd(document, resultDoc);
			}
		}


		// Импорт нодов
		for (String nodeName : mappedElements.keySet()) {
			Node imported = resultDoc.importNode(mappedElements.get(nodeName), true);
			resultDoc.getFirstChild().appendChild(imported);
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(resultDoc);
		StreamResult result = new StreamResult(new File("D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\20200203\\result\\result.xsd"));
		transformer.transform(source, result);

		System.out.println("Done");

	}




	private void initNodesSetByXsd(Document document, Document resultDoc) throws ParserConfigurationException, IOException, SAXException {


		NodeList elements = document.getElementsByTagName("xs:element");
		for (int i = 0; i < elements.getLength(); i++) {
			Element element = (Element) elements.item(i);
			allElementSet.add(element);
			allElementList.add(element);
			mappedElements.put(element.getAttribute("name"), element);
		}

		NodeList children = document.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {

			Node childNode = children.item(i);
			allChildrenSet.add(childNode);
			allChildrenList.add(childNode);

			NodeList deeper = children.item(i).getChildNodes();
			for (int j = 0; j < deeper.getLength(); j++) {
				Node deeperNode = deeper.item(j);
				mappedElements.put(deeperNode.getNodeName(), deeperNode);

			}




			//			Node child = children.item(i);
//
//			NodeList childs = document.getElementsByTagName("xs:element");
//			for (int j = 0; j < childs.getLength(); j++) {
//				Node imported = resultDoc.importNode(childs.item(j), true);
//				resultDoc.getFirstChild().appendChild(imported);
//			}
//
//<?xml version="1.0" encoding="UTF-8"?>
//<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" elementFormDefault="qualified" attributeFormDefault="unqualified" id="ArchivedResult">
//
//</xs:schema>


		}

	}
}
