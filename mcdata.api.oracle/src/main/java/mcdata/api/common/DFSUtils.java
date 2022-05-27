/**
 * 
 */
package mcdata.api.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DFSUtils {
	public static boolean isFilelocked(File file) {
		try {
			try (FileInputStream in = new FileInputStream(file)) {
				in.read();
				return false;
			}
		} catch (FileNotFoundException e) {
			return file.exists();
		} catch (IOException ioe) {
			return true;
		}
	}

	public static boolean lockFile(File file) throws Exception {
		try {
			if (isFilelocked(file)) {
				return false;
			}
			if (!file.exists()) {
				//String path = file.getParentFile().toString();
				file.getParentFile().mkdirs();
			}
			rndAccessFile(file).getChannel().lock();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}

	}

	private static RandomAccessFile rndAccessFile(File file) throws FileNotFoundException {
		return new RandomAccessFile(file, "rw");
	}
}
