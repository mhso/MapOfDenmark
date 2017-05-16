package dk.itu.n.danmarkskort.gui.components;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import dk.itu.n.danmarkskort.Main;

/**
 * A Custom JButton that provides extra visual functionality. The button changes appearance to indicate
 * whether it is on or off, like a ToggleButton.
 * 
 * @author Team N ITU
 */
public class CustomToggleButton extends CustomButton {
	private static final long serialVersionUID = -3440659920912017562L;
	
	private boolean switchedOn = true;

	public CustomToggleButton(float alpha, float alphaHover) {
        super("resources/icons/onbutton.png", alpha, alphaHover, null);
        addActionListener(e -> swap());
    }

    public CustomToggleButton(float alpha, float alphaHover, Color bg) {
    	super("resources/icons/onbutton.png", alpha, alphaHover, bg);
    	addActionListener(e -> swap());
    }
    
    public void setSelected(boolean on) {
    	if(switchedOn != on) swap();
    }
    
    public boolean isSelected() {
    	return switchedOn;
    }
    
    /**
     * Invoked when the button is pressed, changes the ImageIcon of the button.
     */
    private void swap() {
    	if(switchedOn) {
    		if(Main.production)
				try {
					setAlphaIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/offbutton.png"))));
				} catch (IOException e) {
					e.printStackTrace();
				}
			else setAlphaIcon(new ImageIcon("resources/icons/offbutton.png"));
    	}
		else {
			if(Main.production)
				try {
					setAlphaIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/onbutton.png"))));
				} catch (IOException e) {
					e.printStackTrace();
				}
			else setAlphaIcon(new ImageIcon("resources/icons/onbutton.png"));
		}
		switchedOn = !switchedOn;
    }
}
