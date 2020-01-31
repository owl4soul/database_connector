package com.github.owl4soul.test_files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lera.feoktistova on 29.01.2020.
 */
public class FoldersExaminator {


	private static Map<File, File[]> mappedDirectories;

	private static Set<File> equalByNameFiles;

	private static Set<File> notEqualByName;

	private static List<File> allFolders = new ArrayList<>();

	private static List<File> differentByContentFromEqualByNameFiles = new ArrayList<>();

	public static void main(String... paths) throws IOException {
		mappedDirectories = new HashMap<>();

		for (String path : paths) {

			// Получение всех файлов в папке
			File folder = new File(path);
			allFolders.add(folder);
			File[] filesInFolder = folder.listFiles();
			mappedDirectories.put(folder, filesInFolder);
		}

		equalByNameFiles = new HashSet<>();
		notEqualByName = new HashSet<>();

		List<File> foldersList = new ArrayList<>(mappedDirectories.keySet());

		for (int i = 0; i < foldersList.size() - 1; i++) {
			// сравниваем этот каталог со следующим, находим файлы с одинаквыми названиями и суем в сет
			addSameNamedFilesToSet(foldersList.get(i), foldersList.get(i + 1));
		}

		for (File commonFile : equalByNameFiles) {
			List<File> filesToCheck = new ArrayList<>();

			for (Map.Entry<File, File[]> mappedFolderEntry : mappedDirectories.entrySet()) {
				File[] filesInFolder = mappedFolderEntry.getValue();
				for (File fileInFolder : filesInFolder) {
					if (fileInFolder.getName().equals(commonFile.getName())) {
						filesToCheck.add(fileInFolder);
					}
				}
			}

			for (int i = 0; i < filesToCheck.size() - 1; i++) {
				boolean contentsAreIdentical = checkIfContentsAreIdentical(filesToCheck.get(i), filesToCheck.get(i + 1));

				if (!contentsAreIdentical) {
					// TODO: пока так
					differentByContentFromEqualByNameFiles.add(filesToCheck.get(i));
				}
			}
		}

		System.out.println("Not equal named files: ");
		if (notEqualByName.isEmpty()) {
			System.out.println("NOT FOUND");
		} else {
			for (File uncommonFile : notEqualByName) {
				System.out.println(uncommonFile.getName());
			}
		}

		System.out.println("--------------------------------------------------------------");

		System.out.println("Equal named files: ");

		if (equalByNameFiles.isEmpty()) {
			System.out.println("NOT FOUND");
		} else {
			for (File commonFile : equalByNameFiles) {
				System.out.println(commonFile.getName());
			}
		}

		System.out.println("--------------------------------------------------------------");

		System.out.println("Files with different content from equal by name files: ");
		if (differentByContentFromEqualByNameFiles.isEmpty()) {
			System.out.println("NOT FOUND");
		} else {
			for (File differentContentedFile : differentByContentFromEqualByNameFiles) {
				System.out.println(differentContentedFile.getName());
			}
		}

	}

	public static boolean checkIfContentsAreIdentical(File file1, File file2) throws IOException {
		String content1 = getFileMainContent(file1);
		String content2 = getFileMainContent(file2);

		return content1.equals(content2);
	}

	public static String getFileMainContent(File file) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();

		FileReader fileReader = new FileReader(file);
		//создаем BufferedReader с существующего FileReader для построчного считывания
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		// считаем сначала первую строку
		String line = bufferedReader.readLine();
		boolean headerLinesFinished = false;

		while (line != null) {
			if (line.contains("public class")) {
				headerLinesFinished = true;
			}
			if (headerLinesFinished) {
				stringBuilder.append(line);
				// считываем остальные строки в цикле
			}
			line = bufferedReader.readLine();
		}

		return stringBuilder.toString();
	}

	public static void addSameNamedFilesToSet(File folder1, File folder2) {
		// достанем из мапы все файлы данного каталога
//		List<File> folder1Files = Arrays.asList(mappedDirectories.get(folder1));
//		List<File> folder2Files = Arrays.asList(mappedDirectories.get(folder2));

		Set<File> folder1Files = new HashSet<>();
		Set<File> folder2Files = new HashSet<>();

		Collections.addAll(folder1Files, mappedDirectories.get(folder1));
		Collections.addAll(folder2Files, mappedDirectories.get(folder2));

		folder1Files.retainAll(folder2Files);

		notEqualByName.addAll(folder1Files);

		Set<File> folder1FromMap = new HashSet<>();
		Collections.addAll(folder1FromMap, mappedDirectories.get(folder1));

		folder1FromMap.removeAll(folder1Files);

		equalByNameFiles.addAll(folder1FromMap);

	}

//	File f = new File (((File)foldersList.get(0)).listFiles()[0].getAbsolutePath());
//
//	StringBuilder stringBuilder = new StringBuilder();
//
//	FileReader fr = new FileReader(f);
//	//создаем BufferedReader с существующего FileReader для построчного считывания
//	BufferedReader reader = new BufferedReader(fr);
//	// считаем сначала первую строку
//	String line = reader.readLine();
//while (line != null) {
//		System.out.println(line);
//		stringBuilder.append(line);
//		// считываем остальные строки в цикле
//		line = reader.readLine();
//	}
//
//stringBuilder.toString()
}


