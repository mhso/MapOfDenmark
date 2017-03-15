package dk.itu.n.danmarkskort;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Util {

	public static float getRAMUsageInMB() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000F;
	}

	public static File getCurrentDirectory() {
		return new File(getCurrentDirectoryPath());
	}
	
	public static String getCurrentDirectoryPath() {
		return System.getProperty("user.dir");
	}
	
	public static String getCurrentOSMFolderPath() {
		return Util.getCurrentDirectoryPath() + "/parsedOSMFiles/" + Main.osmParser.getChecksum();
	}
	
	public static String getFileChecksum(MessageDigest digest, File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);

		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}
		
		fis.close();
		byte[] bytes = digest.digest();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
	
	public static String getFileChecksumMD5(File file) throws NoSuchAlgorithmException, IOException {
		return getFileChecksum(MessageDigest.getInstance("MD5"), file);
	}

	public static void printMap(Map<?,?> map) {
		for (Object name : map.keySet()) {
			String key = name.toString();
			String value = map.get(name).toString();
			System.out.println(key + " " + value);
		}
	}
	
	public static int getNumberOfLines(File file) {
		try {
			LineNumberReader  lnr = new LineNumberReader(new FileReader(file));
			lnr.skip(Long.MAX_VALUE);
			lnr.close();
			return lnr.getLineNumber() + 1;
		} catch (Exception e) {
			return 0;
		}
	}
	
}
