package dk.itu.n.danmarkskort.gui;

import dk.itu.n.danmarkskort.extras.AlphaImageIcon;
import javax.swing.*;

import java.awt.*;

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
        icon = new ImageIcon(filename);
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
