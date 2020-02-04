package com.github.owl4soul.xjc_gen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CmdExecutor {

	private ExecutorMode executorMode;

	void executeCommands(List<String> commands, ExecutorMode executorMode) {
		this.executorMode = executorMode;
		for (String command : commands) {
			// xjc -encoding UTF-8 -d D:\work\generateSpark\jaxb-ri-2.2.6_old\bin\20200203\src\AAA D:\work\generateSpark\jaxb-ri-2.2.6_old\bin\20200203\CompanyStructure.xsd
			executeCommand(command);
		}
	}

	private void executeCommand(String command) {
		Process process = null;

		try {
			process = Runtime.getRuntime()
							 .exec(command);

			InputStream in = process.getInputStream();
			InputStream err = process.getErrorStream();

			BufferedReader inputReader = new BufferedReader(new InputStreamReader(in));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(err));
			String line;
			String error = null;
			while ((line = inputReader.readLine()) != null || (error = errorReader.readLine()) != null) {
				System.out.println(line);

				// Если включена починка файлов
				if (executorMode.equals(ExecutorMode.FIXING)) {
					// Если только что выполнилась генерация класса,
					// который должен быть переименован, поскольку обладает уникальным контентом, то "чиним" его
					if (Constants.KNOWN_UNIC_CONTENT_CLASSES.keySet().contains(line)) {
						ClassFixer.class.newInstance().fixClass(line, command);
					}
				}

				if (error != null) {
					System.out.println("[ERROR] " + error);
				}
			}

			in.close();
			err.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} finally {
			process.destroy();
		}
	}

	public enum ExecutorMode {
		AS_IS,
		FIXING;
	}

}
