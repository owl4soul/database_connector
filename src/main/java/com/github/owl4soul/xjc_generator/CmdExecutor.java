package com.github.owl4soul.xjc_generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

@Deprecated
public class CmdExecutor {
//	xjc -encoding UTF-8 -d D:\work\generateSpark\jaxb-ri-2.2.6_old\bin\20200203\src\ArchivedResult D:\work\generateSpark\jaxb-ri-2.2.6_old\bin\20200203\ArchivedResult.xsd

	public static void main(String... args) {
		Process process = null;
		try {
			process = Runtime.getRuntime()
									 .exec("xjc -encoding UTF-8 -d D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\20200203\\src\\AAA D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\20200203\\ArchivedResult.xsd");

			InputStream in = process.getInputStream();
			InputStream err = process.getErrorStream();
			OutputStream out = process.getOutputStream();

			BufferedReader inputReader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = inputReader.readLine()) != null) {
				System.out.println(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally {

			process.destroy();
		}
	}
}
