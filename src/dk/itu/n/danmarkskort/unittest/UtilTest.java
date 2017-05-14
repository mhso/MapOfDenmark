package dk.itu.n.danmarkskort.unittest;

import static org.junit.Assert.*;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Test;

import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.Region;

public class UtilTest {
	
    @After
    public void tearDown() {
    	new File("test/writeObjectToFileTest.bin").delete();
    }
	
	@Test
	public void testFileChecksumMD5() {
		try {
			String expectedChecksum = "ad76d9b408c7c692ffb0c5d31c7df1ba";
			String checksum = Util.getFileChecksumMD5("test/checksum-test.txt");
			assertEquals(expectedChecksum, checksum);
		} catch (NoSuchAlgorithmException e) {
			fail("NoSuchAlgorithmException (The specified algorithm in the method is most likely invalid)");
		} catch (IOException e) {
			fail("IOException (file probably not found)");
		}
	}
	
	@Test
	public void testFileSize() {
		long expectedFileSize = 100;
		long fileSize = Util.getFileSize("test/checksum-test.txt");
		assertEquals(expectedFileSize, fileSize);
	}
	
	@Test
	public void testNumberOfLines() {
		int expectedLineCount = 2;
		int lineCount = Util.getNumberOfLines(new File("test/checksum-test.txt"));
		assertEquals(expectedLineCount, lineCount);
	}
	
	@Test
	public void testRoundByN() {
		double deltaUncertainty = 0;
		double outcome1 = Util.roundByN(10, 14);
		double outcome2 = Util.roundByN(10, 15);
		double outcome3 = Util.roundByN(2.5, 5.1);
		double outcome4 = Util.roundByN(1000, 1421);
		double expectedOutcome1 = 10;
		double expectedOutcome2 = 20;
		double expectedOutcome3 = 5;
		double expectedOutcome4 = 1000;
		assertEquals(expectedOutcome1, outcome1, deltaUncertainty);
		assertEquals(expectedOutcome2, outcome2, deltaUncertainty);
		assertEquals(expectedOutcome3, outcome3, deltaUncertainty);
		assertEquals(expectedOutcome4, outcome4, deltaUncertainty);
	}
	
	@Test
	public void testReadWriteObjectToFile() {
		// Write object to file
		Region region = new Region(0, 0, 1, 1);
		boolean success = Util.writeObjectToFile(region, "test/writeObjectToFileTest.bin");
		assertEquals(true, success);
		assertEquals(true, new File("test/writeObjectToFileTest.bin").exists());
		
		// Read object from file
		region = (Region)Util.readObjectFromFile("test/writeObjectToFileTest.bin");
		assertEquals(0, region.x1, 0);
		assertEquals(0, region.y1, 0);
		assertEquals(1, region.x2, 0);
		assertEquals(1, region.y2, 0);
	}
	
	@Test
	public void testValueIsBetween() {
		assertEquals(true, Util.valueIsBetween(5, 0, 10));
		assertEquals(true, Util.valueIsBetween(5, 10, 0));
		assertEquals(true, Util.valueIsBetween(-5, 0, -10));
		assertEquals(true, Util.valueIsBetween(-5, -10, 0));
	}
	
	@Test
	public void testPan() {
		AffineTransform transform = new AffineTransform();
		Util.pan(transform, 5, 5);
		assertEquals(5, transform.getTranslateX(), 0);
		assertEquals(5, transform.getTranslateY(), 0);
	}
	
	@Test
	public void testZoom() {
		AffineTransform transform = new AffineTransform();
		Util.zoom(transform, 2);
		assertEquals(2, transform.getScaleX(), 0);
		Util.zoom(transform, 2);
		assertEquals(4, transform.getScaleX(), 0);
	}
	
	@Test
	public void testZoomToRegion() {
		AffineTransform transform = new AffineTransform();
		Region region = new Region(50, 50, 250, 250);
		Util.zoomToRegion(transform, region, 100);
		assertEquals(0.5, transform.getScaleX(), 0);
		assertEquals(-25, transform.getTranslateX(), 0);	
	}
	
}
