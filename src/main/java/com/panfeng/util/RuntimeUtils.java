package com.panfeng.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RuntimeUtils {
	String command = "";
	Process process;

	public RuntimeUtils(String command) {
		super();
		this.command = command;
	}

	public void start() {
		try {
			process = Runtime.getRuntime().exec(command);
			new Thread(() -> {
				InputStream inputStream = process.getErrorStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream));
				String res;
				try {
					while ((res = bufferedReader.readLine()) != null) {
						System.err.println(res);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						bufferedReader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			new Thread(() -> {
				InputStream inputStream = process.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream));
				String res;
				try {
					while ((res = bufferedReader.readLine()) != null) {
						System.out.println(res);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						bufferedReader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();

			process.waitFor();
			process.destroy();
			process = null;
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
}
