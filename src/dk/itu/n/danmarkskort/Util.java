package dk.itu.n.danmarkskort;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import dk.itu.n.danmarkskort.backend.InputMonitor;
import dk.itu.n.danmarkskort.backend.InputStreamListener;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Util {
	
	public static final AffineTransform zeroTransform = new AffineTransform();
	
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
		return Util.getCurrentDirectoryPath() + "/parsedOSMFiles/" + Main.osmReader.getChecksum();
	}
	
	public static String getFileChecksum(MessageDigest digest, String filename) throws IOException {
		BufferedInputStream buff = new BufferedInputStream(new FileInputStream(filename));

		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		while ((bytesCount = buff.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}
		
		buff.close();
		byte[] bytes = digest.digest();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
	
	public static String getFileChecksumMD5(String filename) throws NoSuchAlgorithmException, IOException {
		return getFileChecksum(MessageDigest.getInstance("MD5"), filename);
	}

	public static void printMap(Map<?,?> map) {
		for (Object name : map.keySet()) {
			String key = name.toString();
			String value = map.get(name).toString();
			System.out.println(key + " " + value);
		}
	}
	
	public static long getFileSize(File file) {
		return file.length();
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
	
	public static int roundByN(double n, double value){
	    return (int) (Math.round(value/n) * n);
	}
	
	public static int mouseWarp() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point p = MouseInfo.getPointerInfo().getLocation();
		
		try {
			if(p.x == 0) {
				new Robot().mouseMove(screenSize.width - 2, p.y);
				return 0;
			}
			else if(p.x == screenSize.width - 1) {
				new Robot().mouseMove(2, p.y);
				return 1;
			}
			else if(p.y == 0) {
				new Robot().mouseMove(p.x, screenSize.height - 2);
				return 2;
			}
			else if(p.y == screenSize.height - 1) {
				new Robot().mouseMove(p.x, 2);
				return 3;
			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static Point2D toRealCoords(Point2D fakeCoords) {
		return new Point2D.Float((float)fakeCoords.getX()/Main.model.getLonFactor(), (float)-fakeCoords.getY());
	}
	
	public static String getBinaryFilePath() {
		File file = new File(Main.osmReader.getFileName());
		String simpleFileName = file.getName().substring(0, file.getName().length()-4);
		return "parsedOSMFiles/"+Main.osmReader.getChecksum()+"/" + 
				simpleFileName + ".bin";
	}
	
	public static boolean writeObjectToFile(Object object, String filename) {
		try {
			FileOutputStream fout = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(object);
			oos.close();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Object readObjectFromFile(String fileName, List<InputStreamListener> listeners) {		
		try {
			BufferedInputStream fout = new BufferedInputStream(new FileInputStream(fileName));
			InputMonitor monitor = new InputMonitor(fout, fileName);
			ObjectInputStream oos = new ObjectInputStream(monitor);
			for(InputStreamListener listener : listeners) monitor.addListener(listener);
			Object object = oos.readObject();
			oos.close();
			return object;
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
			Main.log("Could not find file: " + fileName);
			return null;
		}
	}
	
	public static Object readObjectFromFile(String filename) {
		try {
			BufferedInputStream fout = new BufferedInputStream(new FileInputStream(filename));
			ObjectInputStream oos = new ObjectInputStream(fout);
			Object object = oos.readObject();
			oos.close();
			return object;
		} catch(Exception e) {
			Main.log("Could not find file: " + filename);
			return null;
		}
	}
	
	public static boolean valueIsBetween(double value, double a, double b) {
		if(a > b) {
			return (value >= b && value <= a);
		} else if (a < b) {
			return (value >= a && value <= b);
		} else {
			return (value == a);
		}
	}
	
	public static void pan(AffineTransform transform, double dx, double dy) {
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
	}
	
	public static void zoom(AffineTransform transform, double scale) {
		transform.preConcatenate(AffineTransform.getScaleInstance(scale, scale));
	}

}
