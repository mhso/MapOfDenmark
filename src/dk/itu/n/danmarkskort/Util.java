
package dk.itu.n.danmarkskort;

import java.awt.AWTException;
import java.awt.Desktop;
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
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dk.itu.n.danmarkskort.backend.BinaryWrapper;
import dk.itu.n.danmarkskort.backend.ProgressMonitor;
import dk.itu.n.danmarkskort.backend.ProgressListener;
import dk.itu.n.danmarkskort.models.Region;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

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
	
	public static long getFileSize(String fileName) {
		return getFileSize(new File(fileName));
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
	
	public static double roundByN(double n, double value){
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
	
	public static double distanceInMeters(Point2D p1, Point2D p2) {
		p1 = toRealCoords(p1);
		p2 = toRealCoords(p2);
		double earthRadius = 6371*1000;
		double latRad1 = Math.toRadians(p1.getY());
		double latRad2 = Math.toRadians(p2.getY());
		double latDeltaRad = Math.toRadians(p2.getY()-p1.getY());
		double lonDeltaRad = Math.toRadians(p2.getX()-p1.getX());
		
		double haversine = Math.sin(latDeltaRad/2) * Math.sin(latDeltaRad/2) + 
				Math.cos(latRad1) * Math.cos(latRad2) * Math.sin(lonDeltaRad/2) * Math.sin(lonDeltaRad/2);
		double formular = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1-haversine));
		
        return earthRadius * formular;
    }
	
	public static Point2D toRealCoords(Point2D fakeCoords) {
		return new Point2D.Float((float)fakeCoords.getX()/Main.model.getLonFactor(), (float)-fakeCoords.getY());
	}
	
	public static Point2D toFakeCoords(Point2D realCoords) {
		return new Point2D.Float((float)realCoords.getX()*Main.model.getLonFactor(), (float)-realCoords.getY());
	}
	
	public static Point2D.Float stringCordsToPointFloat(String inputStr){
		String[] strArr = inputStr.split(", ");
		float lat = Float.parseFloat(strArr[0]);
		float lon = Float.parseFloat(strArr[1]);
		Point2D realCoords = new Point2D.Float(lon, lat);
		Point2D fakeCoords = Util.toFakeCoords(realCoords);
		System.out.println(realCoords);
		System.out.println(fakeCoords);
		return new Point2D.Float((float)fakeCoords.getX(), (float)fakeCoords.getY());
	}
	
	public static String getBinaryFilePath() {
		File file = new File(Main.osmReader.getFileName());
		String simpleFileName = file.getName().substring(0, file.getName().length()-4);
		return "parsedOSMFiles/"+Main.osmReader.getChecksum()+"/" + 
				simpleFileName + ".bin";
	}
	
	public static void extractAllFromBinary(BinaryWrapper binary) {
		Main.model = binary.getModel();
		Main.addressController.setAddressHolder(binary.getAddressHolder());
		Main.addressController.setHousenumberTree(binary.getHousenumberTree());
		Main.routeController.setEdgeTree(binary.getEdgeTree());
		Main.routeController.setGraph(binary.getRouteGraph());
	}
	
	public static void addAllToBinary(BinaryWrapper binary) {
		binary.setModel(Main.model);
        binary.setAddressHolder(Main.addressController.getAddressHolder());
        binary.setEdgeTree(Main.routeController.getEdgeTree());
        binary.setHousenumberTree(Main.addressController.gethousenumberTree());
        binary.setRouteGraph(Main.routeController.getGraph());
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
	
	public static Object readObjectFromFile(String fileName, List<ProgressListener> listeners) {		
		try {
			BufferedInputStream fout;
			if(Main.production && fileName.endsWith("resources/default.bin")) 
				fout = new BufferedInputStream(Util.class.getResourceAsStream(fileName));
			else fout = new BufferedInputStream(new FileInputStream(fileName));
			ProgressMonitor monitor = new ProgressMonitor(fout);
			ObjectInputStream oos = new ObjectInputStream(monitor);
			for(ProgressListener listener : listeners) monitor.addListener(listener);
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

	public static void zoomToRegion(AffineTransform transform, Region region, double currentWidth) {
		Util.pan(transform, -region.x1, -region.y2);
		Util.zoom(transform, currentWidth / (region.x2 - region.x1));
	}
	
	public static void openWebpage(String urlString) {
	    try {
	        Desktop.getDesktop().browse(new URL(urlString).toURI());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static BufferedImage screenshot(Rectangle region) {
		try {
			BufferedImage capture = new Robot().createScreenCapture(region);
			return capture;
		} catch (AWTException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage screenshot(Rectangle region, int delay) {
		ExecutorService service = Executors.newFixedThreadPool(1);
		Future<BufferedImage> imageTask = service.submit(new ScreenshotDelayer(region, delay, true));
        
		try {
			BufferedImage image = imageTask.get();
			service.shutdownNow();
			return image;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		service.shutdownNow();
		return null;
	}
	
	public static BufferedImage screenshot(int delay) {
		Rectangle screenRect = new Rectangle(
				Main.map.getLocationOnScreen().x, 
				Main.map.getLocationOnScreen().y, 
				Main.map.getWidth(), 
				Main.map.getHeight()
			);
		return screenshot(screenRect, delay);
	}
	
	public static BufferedImage screenshot() {
		return screenshot(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}
	
	public static BufferedImage screenshotWithoutGUI(Rectangle region) {
		Main.mainPanel.hideGUI();
		Main.mainPanel.repaint();
		
		try {
			BufferedImage capture = new Robot().createScreenCapture(region);
			Main.mainPanel.showGUI();
			return capture;
		} catch (AWTException e) {
			e.printStackTrace();
			Main.mainPanel.showGUI();
			Main.mainPanel.repaint();
			return null;
		}
	}
	
	public static BufferedImage screenshotWithoutGUI(int delay) {
		ExecutorService service = Executors.newFixedThreadPool(1);
		Future<BufferedImage> imageTask = service.submit(new ScreenshotDelayer(null, delay, false));
        
		try {
			BufferedImage image = imageTask.get();
			service.shutdownNow();
			return image;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		service.shutdownNow();
		return null;
	}
	
	public static BufferedImage screenshotWithoutGUI() {
		if(!Main.map.isVisible()) return new BufferedImage(0, 0, BufferedImage.TYPE_USHORT_GRAY);
		Rectangle screenRect = new Rectangle(
				Main.map.getLocationOnScreen().x, 
				Main.map.getLocationOnScreen().y, 
				Main.map.getWidth(), 
				Main.map.getHeight()
			);
		return screenshotWithoutGUI(screenRect);
	}
	
	public static void zoomToRegionY(AffineTransform transform, Region region, double currentHeight) {
		Util.pan(transform, -region.x1, -region.y2);
		Util.zoom(transform, currentHeight / (region.y2 - region.y1));
	}
	
	private static class ScreenshotDelayer implements Callable<BufferedImage> {
		private boolean gui;
		private Rectangle region;
		
		public ScreenshotDelayer(Rectangle region, int delay, boolean gui) {
			this.gui = gui;
			this.region = region;
		}
		
		@Override
		public BufferedImage call() throws Exception {
			while(!Main.tileController.isTileQueueEmpty()) Thread.sleep(5);
			Thread.sleep(1500);
			if(gui) {
				if(region == null) return screenshot();
				else return screenshot(region);
			}
			else return screenshotWithoutGUI();
		}	
	}
}
