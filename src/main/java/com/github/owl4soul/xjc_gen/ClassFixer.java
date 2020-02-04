package com.github.owl4soul.xjc_gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassFixer {

	private static List<String> TARGET_LINES = Arrays.asList("@XmlRootElement", "public class Response", "public class ObjectFactory", "public class IncludeInList");

	private String fixedName;

	private boolean shouldReplaceFileWithFixed;

	void fixClass(String line, String command) {
		String className = Constants.KNOWN_UNIC_CONTENT_CLASSES.get(line);
		String classFullPath = Constants.FULLPATH_TO_COMMON_FOLDER + className + ".java";

		File classFile = new File(classFullPath);

		String fileContent = readFixedContentFromFile(classFile, command);

		if (shouldReplaceFileWithFixed) {
			classFile.delete();
			String replacementFileName = className + "_" + getPostfixByCommand(command);
			String replacementFileFullPath = Constants.FULLPATH_TO_COMMON_FOLDER + replacementFileName + ".java";
			writeContentToFile(replacementFileFullPath, fileContent);
		}

	}

	public void writeContentToFile(String fileName, String content) {
		try (FileWriter fileWriter = new FileWriter(fileName, false)) {
			fileWriter.write(content);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readFixedContentFromFile(File file, String command) {
		StringBuilder stringBuilder = new StringBuilder();

		try {
			FileReader fileReader = new FileReader(file);
			//создаем BufferedReader с существующего FileReader для построчного считывания
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			// считываем все строки
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				// Если попали в целевую строку, которую требуется заменить
				if (TargetTriggers.isTriggerFired(line)) {
					shouldReplaceFileWithFixed = true;
					// Получаем сработавший триггер
					TargetTriggers targetTrigger = TargetTriggers.getTargetTriggerByLine(line);
					// Получаем постфикс по имени схемы (которая соответствовала ранее имени пакета)
					String postfix = getPostfixByCommand(command);
					String fixedLine = targetTrigger.getFixedLineRepresentation(postfix);
					stringBuilder.append(fixedLine + "\r\n");
				} else {
					stringBuilder.append(line + "\r\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

	private String getPostfixByCommand(String command) {
		int lastIndexOfSlash = command.lastIndexOf("\\");
		int lastIndexOfPoint = command.lastIndexOf(".");

		String postfix = command.substring(lastIndexOfSlash + 1, lastIndexOfPoint);

		return postfix;
	}

	private enum TargetTriggers {
		ROOT_ELEMENT_RESPONSE("Response", "@XmlRootElement(name = \"Response\")"),
		PUBLIC_CLASS_RESPONSE("Response", "public class Response"),
		PUBLIC_CLASS_OBJECTFACTORY("ObjectFactory", "public class ObjectFactory"),
		PUBLIC_CLASS_INCLUDEINLIST("IncludeInList", "public class IncludeInList");

		String targetCharsToReplace;
		String lineRepresentation;

		TargetTriggers(String targetCharsToReplace, String lineRepresentation) {
			this.targetCharsToReplace = targetCharsToReplace;
			this.lineRepresentation = lineRepresentation;
		}

		 String getFixedLineRepresentation(String postfix){
			 String result = this.lineRepresentation;
			 return result.replace(this.targetCharsToReplace, targetCharsToReplace + "_"  + postfix);
		 }

		static List<String> getAllTargetRepresentationLines() {
			List<String> triggersLinesRepresentations = new ArrayList<>();
			for (TargetTriggers targetTrigger : TargetTriggers.values()) {
				triggersLinesRepresentations.add(targetTrigger.lineRepresentation);
			}
			return triggersLinesRepresentations;
		}

		static TargetTriggers getTargetTriggerByLine(String line) {
			return Arrays.stream(TargetTriggers.values())
											  .filter(targetTrigger -> line.contains(targetTrigger.lineRepresentation))
											  .findFirst().get();
		}

		static boolean isTriggerFired(String line) {
			boolean isTriggerFired = TargetTriggers.getAllTargetRepresentationLines().stream().anyMatch(line::contains);
			return isTriggerFired;
		}
	}

}
