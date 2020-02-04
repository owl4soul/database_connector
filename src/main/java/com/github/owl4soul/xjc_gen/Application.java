package com.github.owl4soul.xjc_gen;

import java.util.List;

public class Application {

	public static void main(String[] args) throws IllegalAccessException, InstantiationException {

		// Сгенерируем список команд для xcj
		List<String> xjcCommands = XjcComandsInitializer.class.newInstance().generateXjcCommands();

		// Выполним генерацию классов с помощью команд для xcj
		CmdExecutor.class.newInstance().executeCommandsFixingUnic(xjcCommands);
	}
}
