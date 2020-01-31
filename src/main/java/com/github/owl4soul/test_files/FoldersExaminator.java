package com.github.owl4soul.test_files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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


	/**
	 * Путь к корню, папки внутри которого мы будем рассматривать.
	 * Передается в качестве аргумента.
	 */
	private static String rootFolderPath;

	/**
	 * Размапленные папки внутри переданной в качестве аргумента корневой директории.
	 */
	private static Map<File, File[]> mappedTargetFolders;

	private static Set<File> equalByNameFiles;

	private static Set<File> notEqualByName;

	private static List<File> allTargetFolders = new ArrayList<>();

	private static List<File> differentByContentFromEqualByNameFiles = new ArrayList<>();

	public static void main(String... paths) throws IOException {
		// Принимаем аргумент, который должен указать корневую директорию с папками, которые будем проверять
		rootFolderPath = paths[0];

		// Получение всех путей к целевым папкам - по задаче внутри рутовой папки хранятся файлы через двойную вложенность
		initAllTargetFolders();

		// Маппинг целевых папок - ключ: целевая папка, значение: массив файлов.
		initFilesPerTargetFoldersMap();

		equalByNameFiles = new HashSet<>();
		notEqualByName = new HashSet<>();

		List<File> foldersList = new ArrayList<>(mappedTargetFolders.keySet());

		for (int i = 0; i < foldersList.size() - 1; i++) {
			// сравниваем этот каталог со следующим, находим файлы с одинаквыми названиями и суем в сет
			addSameNamedFilesToSet(i, foldersList.get(i), foldersList.get(i + 1));
		}

		for (File commonFile : equalByNameFiles) {
			List<File> filesToCheck = new ArrayList<>();

			for (Map.Entry<File, File[]> mappedFolderEntry : mappedTargetFolders.entrySet()) {
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

		System.out.println("Not equal named COUNT = " + notEqualByName.size());
		System.out.println("Not equal named files: ");
		if (notEqualByName.isEmpty()) {
			System.out.println("NOT FOUND");
		} else {
			for (File uncommonFile : notEqualByName) {
				System.out.println(uncommonFile.getName());
			}
		}

		System.out.println("--------------------------------------------------------------");

		System.out.println("Equal named COUNT = " + equalByNameFiles.size());
		System.out.println("Equal named files: ");
		if (equalByNameFiles.isEmpty()) {
			System.out.println("NOT FOUND");
		} else {
			for (File commonFile : equalByNameFiles) {
				System.out.println(commonFile.getName());
			}
		}

		System.out.println("--------------------------------------------------------------");

		System.out.println("Files with different content from equal by name COUNT = " + differentByContentFromEqualByNameFiles.size());
		System.out.println("Files with different content from equal by name files: ");
		if (differentByContentFromEqualByNameFiles.isEmpty()) {
			System.out.println("NOT FOUND");
		} else {
			for (File differentContentedFile : differentByContentFromEqualByNameFiles) {
				System.out.println(differentContentedFile.getName());
			}
		}

		System.out.println("--------------------------------------------------------------");

		Set<String> namesOfDifferentContentFiles = new HashSet<>();
		differentByContentFromEqualByNameFiles.forEach(file -> namesOfDifferentContentFiles.add(file.getName()));
		System.out.println("Different NAMES of same named files with different content COUNT = " + namesOfDifferentContentFiles.size());
		System.out.println("Different NAMES of same named files with different content: ");
		for (String nameOfFile :  namesOfDifferentContentFiles) {
			System.out.println(nameOfFile);
		}
	}

	/**
	 * Инициализация всех целевых папок, внутри которых будем проверять файлы на уникальность контента.
	 */
	private static void initAllTargetFolders() {
		List<File> rootFoldersContent = Arrays.asList(new File(rootFolderPath).listFiles());

		for (File contentInRoot : rootFoldersContent) {
			if (contentInRoot.isDirectory()){
				allTargetFolders.addAll(Arrays.asList(contentInRoot.listFiles()));
			}
		}
	}

	/**
	 * Заполнение мапы mappedTargetFolders - где ключ: целевая папка, значение: массив файлов в ней.
	 */
	private static void initFilesPerTargetFoldersMap() {
		mappedTargetFolders = new HashMap<>();
		for (File targetFolder : allTargetFolders) {
			mappedTargetFolders.put(targetFolder, targetFolder.listFiles());
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

	public static void addSameNamedFilesToSet(int step, File folder1, File folder2) {
		System.out.println("----on step " + step + " checking folders: " + folder1.getParent() + " & " + folder2.getParent());

		// достанем из мапы все файлы данного каталога
		Set<File> folder1Files = new HashSet<>();
		Set<File> folder2Files = new HashSet<>();

		Collections.addAll(folder1Files, mappedTargetFolders.get(folder1));
		Collections.addAll(folder2Files, mappedTargetFolders.get(folder2));

		folder1Files.retainAll(folder2Files);

		notEqualByName.addAll(folder1Files);

		Set<File> folder1FromMap = new HashSet<>();
		Collections.addAll(folder1FromMap, mappedTargetFolders.get(folder1));

		folder1FromMap.removeAll(folder1Files);

		equalByNameFiles.addAll(folder1FromMap);

	}
}


