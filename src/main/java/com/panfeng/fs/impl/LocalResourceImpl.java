package com.panfeng.fs.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.paipianwang.pat.common.util.FileUtils;
import com.panfeng.fs.ResourceInterface;

@Component
public class LocalResourceImpl implements ResourceInterface {

	@Override
	public byte[] getBytes(String dir, String name) {
		File file = new File(dir, name);
		if (!file.exists())
			return null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			byte[] bytearray = new byte[inputStream.available()];
			inputStream.read(bytearray);
			return bytearray;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}

	@Override
	public InputStream getInputStream(String dir, String name) {
		File file = new File(dir, name);
		if (!file.exists())
			return null;
		try {
			InputStream inputStream = new FileInputStream(file);
			return inputStream;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public File getFile(String dir, String name) {
		File file = new File(dir, name);
		if (!file.exists())
			return null;
		else
			return file;
	}

	@Override
	public boolean writeFile(byte[] files, String dir, String name) {
		checkDir(dir);
		File file = new File(dir, name);
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			os.write(files);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return false;
	}

	@Override
	public boolean writeFile(InputStream inputStream, String dir, String name) {
		checkDir(dir);
		return FileUtils.saveFileByInputStream(inputStream, dir + name);
	}

	@Override
	public boolean writeFile(File file, String dir, String name) {
		checkDir(dir);
		FileUtils.saveFileByFile(file, dir + name);
		return false;
	}

	@Override
	public void removeFile(String path, String name) {
		File file = new File(path, name);
		if (file.exists()) {
			file.delete();
		}
	}

	private void checkDir(String path) {
		File file = new File(path);
		file.mkdirs();
	}

	@Override
	public void removeDir(String dir, boolean removeThis) {
		File root = new File(dir);
		if (root.exists() && root.isDirectory()) {
			File[] files = root.listFiles();
			for (File file : files) {
				file.delete();
			}
			if (removeThis)
				root.delete();
		}
	}

	@Override
	public List<File> getFileList(String dir) {
		File root = new File(dir);
		if (root.exists() && root.isDirectory()) {
			File[] files = root.listFiles();
			return Arrays.asList(files);
		}
		return null;
	}

}
