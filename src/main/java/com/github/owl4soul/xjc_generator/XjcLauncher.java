package com.github.owl4soul.xjc_generator;

import com.github.owl4soul.xjc_gen.XjcComandsInitializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class XjcLauncher {

	public static void main(String[] args)
			throws IllegalAccessException, InstantiationException, IOException, ParserConfigurationException,
				   SAXException, TransformerException {

		XsdParser.class.newInstance().parseDataInFolder();

//		SparkClassesGenerator.class.newInstance().readNode_Test();
//		SparkClassesGenerator.class.newInstance().sparkGenerateCreateDirs();
//		SparkClassesGenerator.class.newInstance().sparkReplacePackajeInGeneratedFiles();
	}
}
