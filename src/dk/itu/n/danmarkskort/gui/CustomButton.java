package dk.itu.n.danmarkskort.gui;

import dk.itu.n.danmarkskort.extras.AlphaImageIcon;
import javax.swing.*;

import java.awt.*;

public class CustomButton extends JButton {
	private static final long serialVersionUID = 8833927450795701033L;

	public CustomButton(String filename, float alpha, float alphaHover) {
        this(filename, alpha, alphaHover, null);
    }

    public CustomButton(String filename, float alpha, float alphaHover, Color bg) {
        ImageIcon icon = new ImageIcon(filename);
        setIcon(new AlphaImageIcon(icon, alpha));
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
