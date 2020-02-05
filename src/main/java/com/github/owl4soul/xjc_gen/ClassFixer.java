package com.github.owl4soul.xjc_gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ClassFixer {

	private String fixedName;

	private boolean shouldReplaceFileWithFixed;

	void deleteTargetFilesFromCommonDir() {
		String commonDirPath = Constants.FULLPATH_TO_COMMON_FOLDER;

		File commonDir = new File(commonDirPath);
		List<File> commonFiles = Arrays.asList(commonDir.listFiles());

		Iterator<File> fileIterator = commonFiles.iterator();
		while (fileIterator.hasNext()) {
			File javaFile = fileIterator.next();
			String fileName = javaFile.getName();
			if (fileName.equals("ObjectFactory.java") || fileName.equals("Response.java")) {
				boolean succsesfullyDeleted = javaFile.delete();
				if (!succsesfullyDeleted) {
					System.out.println("COULD NOT DELETE FILE:  " + fileName);
				}
			}
		}
	}

	void moveFiles() {
		String srcPath = Constants.ROOT_PATH + Constants.CURRENT_SPARK_DIR_NAME + "\\src\\";
		File srcDir = new File(srcPath);

		List<File> targetDirs = new ArrayList<>();
		List<File> fileList = Arrays.asList(srcDir.listFiles());

		// добавление всех вложенных директорий в одну папку
		for (File file : fileList) {
			if (file.isDirectory() && !file.getName().equals(Constants.COMMON_FOLDER)) {
				targetDirs.addAll(Arrays.asList(file.listFiles()));
			}
		}

		for (File targetDir : targetDirs) {
			List<File> javaFiles = Arrays.asList(targetDir.listFiles());
			for (File javaFile : javaFiles) {
				String fileName = javaFile.getName();
				if (fileName.equals("ObjectFactory.java") || fileName.equals("Response.java")) {
					// Достаем контент, который будем заменять вместе с названием файла
					String fixedContent = readFixedContentFromFile(javaFile);
					writeContentToFile(Constants.FULLPATH_TO_COMMON_FOLDER + fixedName, fixedContent);
				}
			}
		}

	}

	void fixClass(String line, String command) {
		String className = Constants.KNOWN_UNIC_CONTENT_CLASSES.get(line);
		String classFullPath = Constants.FULLPATH_TO_COMMON_FOLDER + className + ".java";

		File classFile = new File(classFullPath);

		String fileContent = readFixedContentFromFile(classFile, command);

		if (shouldReplaceFileWithFixed) {
//			classFile.delete();
			String replacementFileName = className + "_" + getPostfixByCommand(command);
			String replacementFileFullPath = Constants.FULLPATH_TO_COMMON_FOLDER + replacementFileName + ".java";
			writeContentToFile(replacementFileFullPath, fileContent);
		}

	}

	public void writeContentToFile(String filePath, String content) {
		try (FileWriter fileWriter = new FileWriter(filePath, false)) {
			fileWriter.write(content);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readFixedContentFromFile(File file) {
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
					String postfix = getPostfixByFile(file);
					// Инициализируем исправленное имя для файла, под которым его будем сохранять
					fixedName = file.getName().replace(".java", "") + "_" + postfix + ".java";
					// Получаем исправленную строку и записываем ее в буфер контента, с которым будем сохранять файл
					String fixedLine = targetTrigger.getFixedLineRepresentation(line, postfix);
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
					String fixedLine = targetTrigger.getFixedLineRepresentation(line, postfix);
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

	private String getPostfixByFile(File file) {
		String dirNameToPostfix = file.getParentFile().getParentFile().getName();
		return dirNameToPostfix;
	}

	private String getPostfixByCommand(String command) {
		int lastIndexOfSlash = command.lastIndexOf("\\");
		int lastIndexOfPoint = command.lastIndexOf(".");

		String postfix = command.substring(lastIndexOfSlash + 1, lastIndexOfPoint);

		return postfix;
	}

	private enum TargetTriggers {
//		QUOTED_INCLUDEINLIST("IncludeInList","\"IncludeInList\""),
		QUOTED_RESPONSE("Response", "\"Response\""),
		TRIANGLE_BRACKETS_RESPONSE("Response", "<Response"),
		CYCLE_BRACKETS_RESPONSE("Response", "(Response"),
		PUBLIC_CLASS_RESPONSE(" Response", "Response"),
		PUBLIC_CLASS_OBJECTFACTORY(" ObjectFactory", "ObjectFactory"),
//		PUBLIC_CLASS_INCLUDEINLIST(" IncludeInList", "IncludeInList"),
		GENERATED_POINT("generated.", "generated."){
			@Override
			String getFixedLineRepresentation(String line, String postfix) {
				return line.replace(targetCharsToReplace, Constants.PATH_IN_PROJECT);
			}
		};

		String targetCharsToReplace;
		String lineRepresentation;

		TargetTriggers(String targetCharsToReplace, String lineRepresentation) {
			this.targetCharsToReplace = targetCharsToReplace;
			this.lineRepresentation = lineRepresentation;
		}

		 String getFixedLineRepresentation(String line, String postfix){
			 return line.replace(this.targetCharsToReplace, targetCharsToReplace + "_"  + postfix);
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
