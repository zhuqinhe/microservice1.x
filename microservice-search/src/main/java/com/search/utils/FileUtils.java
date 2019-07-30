package com.search.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class FileUtils {

	public static String readFile(String filePath) throws Exception {
		StringBuilder sb = new StringBuilder("");
		BufferedReader reader = null;
		File file = new File(filePath);
		if (!file.exists() || file.isDirectory()) {
			return null;
		}
		reader = new BufferedReader(new FileReader(file));
		String line = null;
		// 一次读入一行，直到读入null为文件结束
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		reader.close();
		return sb.toString().trim();
	}

	public static void write(String path, String fileName, String content) throws Exception {

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		String filePath = path + "/" + fileName;
		FileOutputStream out = null;
		out = new FileOutputStream(filePath);
		out.write(content.getBytes());
		out.flush();
		out.close();
	}

}
