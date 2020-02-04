package com.github.owl4soul.xjc_gen;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileMover {

	void moveFiles() {
		String srcPath = Constants.ROOT_PATH + "\\src\\";
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
				if (javaFile.getName().equals("ObjectFactory.java") || javaFile.getName().equals("Response.java")) {
					// Достаем контент, который будем заменять вместе с названием файла
				}
			}
		}

	}
}
