package dk.itu.n.danmarkskort.gui.routeplanner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.models.RouteEnum;

public class RouteImageSplit {
	private BufferedImage mainImage;
	private final static int NUMBER_OF_SUBIMAGES = 26;
	private static BufferedImage[] subImages = new BufferedImage[NUMBER_OF_SUBIMAGES];
	
	public RouteImageSplit() {
		createSubImages();
	}
	
	private void createSubImages() {
		try {
			if(Main.production) mainImage = ImageIO.read(getClass().getResourceAsStream("/resources/routeplanner/routing-sprite-osm_medium.png"));
			else mainImage = ImageIO.read(new File("resources/routeplanner/routing-sprite-osm_medium.png"));
			int subImageWidth = mainImage.getWidth() / (NUMBER_OF_SUBIMAGES);
			int subImageX = 0;
			for(int i = 0; i < NUMBER_OF_SUBIMAGES; i++) {
				subImages[i] = 
						mainImage.getSubimage(
						subImageX, 
						0, 
						subImageWidth, mainImage.getHeight());
				subImageX = subImageX + subImageWidth;
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ImageIcon getSubImage(int imgNumber){
		if(imgNumber > 0 && imgNumber  <= NUMBER_OF_SUBIMAGES) {
			BufferedImage img = subImages[imgNumber - 1];
			return new ImageIcon(img);
		} else {
			 throw new IllegalArgumentException();
		}
	}
	
	public ImageIcon getStepIcon(RouteEnum routeEnum){
		int subImageNumber = 1;
		switch(routeEnum) {
		case AT_DESTINATION: subImageNumber = 15;
			break;
		case CONTINUE_ON: subImageNumber = 1;
			break;
		case ROUNDABOUND_1_ROAD: subImageNumber = 11;
			break;
		case ROUNDABOUND_2_ROAD: subImageNumber = 11;
			break;
		case ROUNDABOUND_3_ROAD: subImageNumber = 11;
			break;
		case ROUNDABOUND_4_ROAD: subImageNumber = 11;
			break;
		case ROUNDABOUND_5_ROAD: subImageNumber = 11;
			break;
		case START_AT: subImageNumber = 9;
			break;
		case TURN_LEFT:  subImageNumber = 7;
			break;
		case TURN_RIGHT: subImageNumber = 3;
			break;
		default:
			break;
			
		}
		return getSubImage(subImageNumber);
	}

}
