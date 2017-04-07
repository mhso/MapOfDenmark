package dk.itu.n.danmarkskort.gui;

import java.awt.Color;

import javax.swing.ImageIcon;

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
    
    public void setOn(boolean on) {
    	if(switchedOn != on) swap();
    }
    
    public boolean isOn() {
    	return switchedOn;
    }
    
    private void swap() {
    	if(switchedOn) setAlphaIcon(new ImageIcon("resources/icons/offbutton.png"));
		else setAlphaIcon(new ImageIcon("resources/icons/onbutton.png"));
		switchedOn = !switchedOn;
    }
}
