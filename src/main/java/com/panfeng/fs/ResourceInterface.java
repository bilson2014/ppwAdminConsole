package com.panfeng.fs;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 
 * @author Wang,LM
 *
 */
public interface ResourceInterface {
	byte[] getBytes(String dir, String name);

	InputStream getInputStream(String dir, String name);

	File getFile(String dir, String name);

	boolean writeFile(byte[] file, String dir, String name);

	boolean writeFile(InputStream inputStream, String dir, String name);
	
	boolean writeFile(File file, String dir, String name);

	void removeFile(String dir, String name);
	
	void removeDir(String dir, boolean removeThis);
	
	List<File> getFileList(String dir);

}
