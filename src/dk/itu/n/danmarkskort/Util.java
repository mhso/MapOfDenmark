package dk.itu.n.danmarkskort;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.Point2D;

import dk.itu.n.danmarkskort.models.Coordinate;
import dk.itu.n.danmarkskort.models.ParsedBounds;

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
	
	public static double roundByN(int n, double value){
	    return Math.round(value/n) * n;
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
	
	public static Point2D coordinateToScreen(Coordinate coord) {
		
		ParsedBounds bounds = Main.tileController.getBounds();
		
		double x = coord.getLong() - bounds.minLong;
		double y = coord.getLat() - bounds.minLat;
		double latDist = bounds.getWidth();
		double lonDist = bounds.getHeight();
		double heightDist = bounds.getPixelHeight();
		double widthDist = bounds.getPixelWidth();
		
		double ratioX = widthDist / latDist;
		double ratioY = heightDist / lonDist;
		x *= ratioX;
		y *= -ratioY;
		y += heightDist;
		return new Point2D.Double(x, y);
	}
	
	public static Point2D coordinateToScreen(Coordinate coord, double scale) {
		Point2D p = coordinateToScreen(coord);
		return new Point2D.Double(p.getX() * scale, p.getY() * scale);
	}
	
}
