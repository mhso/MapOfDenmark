package dk.itu.n.danmarkskort.gui.components;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.extras.AlphaImageIcon;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.io.IOException;

/**
 * A Class that provides additional visual functionality to a JButton by providing transparency methods for
 * an ImageIcon.
 * 
 * @author Team N ITU
 */
public class CustomButton extends JButton {
	private static final long serialVersionUID = 8833927450795701033L;
	
	private ImageIcon icon;
	private float alpha;
	private float alphaHover;
	private Color bg;

	public CustomButton(String filename, float alpha, float alphaHover) {
        this(filename, alpha, alphaHover, null);
    }

    public CustomButton(String filename, float alpha, float alphaHover, Color bg) {
    	this.alpha = alpha;
    	this.alphaHover = alphaHover;
    	this.bg = bg;
        if(Main.production) {
			try {
				icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/"+filename)));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		else icon = new ImageIcon(filename);
        setAlphaIcon(icon);
    }
    
    public void setAlphaIcon(ImageIcon newIcon) {
    	icon = newIcon;
    	super.setIcon(new AlphaImageIcon(icon, alpha));
    	setRolloverIcon(new AlphaImageIcon(icon, alphaHover));
        setBorder(BorderFactory.createEmptyBorder());
        if(bg == null) {
            setOpaque(false);
            setContentAreaFilled(false);
        } else {
            setBackground(bg);
        }
    }
}
