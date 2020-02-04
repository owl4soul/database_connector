package com.github.owl4soul.xjc_gen;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Application {

	public static void main(String[] args) throws IllegalAccessException, InstantiationException, IOException {

		// Шаг нулевой: созданние  пустых директорий по именам каждой xsd под будущую раскладку
		sparkGenerateCreateDirs();

		// Шаг первый: генерация списка команд для генерации классов каждой xsd в отдельную директорию из предыдущего шага
		List<String> xjcCommandsToGenEachToUnicDirs = XjcComandsInitializer.class.newInstance().generateXjcCommands(XjcComandsInitializer.GeneratingPathMode.UNIC_DIR_FOR_EACH);

		// Шаг второй: выполнение команд генерации из первого шага для создания java-классов внутри папок под именами xsd
		CmdExecutor.class.newInstance().executeCommands(xjcCommandsToGenEachToUnicDirs, CmdExecutor.ExecutorMode.AS_IS);

		// Шаг третий: генерация списка команд xjc для заполнения общей директории всеми xsd
		List<String> xjcCommandsToGenAllToOneDir = XjcComandsInitializer.class.newInstance().generateXjcCommands(XjcComandsInitializer.GeneratingPathMode.COMMON_DIR_FOR_ALL);

		// Шаг четвертый: выполнение команд генерации из второго шага - схлопывание классов в общей директории
		CmdExecutor.class.newInstance().executeCommands(xjcCommandsToGenAllToOneDir, CmdExecutor.ExecutorMode.AS_IS);


		// Шаг пятый: удаление из общей директории того, что является уникальным контентом и должно быть добавлено в след.шаге корректно (с измениением имени и контента) - ObjectFactory, Response, IncludeInList(?).
		ClassFixer.class.newInstance().deleteTargetFilesFromCommonDir();

		// Шаг шестой: переносим файлы с уникальным контентом, фикся внутри и переименовывая сам файл с использованием префикса папки, в которой он находился (= имени xsd, по которой был сгенерирован)
		ClassFixer.class.newInstance().moveFiles();


//		// Сгенерируем список команд для xcj
//		List<String> xjcCommands = XjcComandsInitializer.class.newInstance().generateXjcCommands();
//
//		// Выполним генерацию классов с помощью команд для xcj
//		CmdExecutor.class.newInstance().executeCommands(xjcCommands);

//		ClassFixer.class.newInstance().moveFiles();
	}


	/**
	 * Обрезанный метод Саши.
	 * @throws IOException
	 */
	static void sparkGenerateCreateDirs() throws IOException {

		String sparkFilesDir = "D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\" + Constants.CURRENT_SPARK_DIR_NAME;
		File sourceDir = new File(sparkFilesDir);

		File srcDir = new File(sparkFilesDir + "\\src");
		srcDir.mkdir();

		for (File file : sourceDir.listFiles()) {
			if (!file.isDirectory()) {

				String fileName = file.getName();
				String newDirName = fileName.substring(0, fileName.indexOf("."));
//
//				if (fileName.contains("MonGetEventsList") || fileName.contains("GetPurchases") ||
//						fileName.contains("IdentifyCompany") || fileName.contains("MonGetCompanyEvents") ||
//						fileName.contains("ValidatePassport") || fileName.contains("MonPersonGetEventsList") ||
//						fileName.contains("MonGetEntrepreneursEventsList") || fileName.contains("MonGetEntrepreneurEvents") ||
//						fileName.contains("CheckCompany115FederalLaw")) {
//					newDirName = newDirName + "1";
//				}


				//CALL xjc -encoding UTF-8 -d 20150819\src\BankAccountingReport101 20150819\BankAccountingReport101.xsd

				File newDir = new File(sparkFilesDir + "\\src\\" + newDirName);
				newDir.mkdir();
			}
		}
	}
}
