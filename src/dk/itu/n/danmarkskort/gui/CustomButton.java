package dk.itu.n.danmarkskort.gui;

<<<<<<< HEAD
import dk.itu.n.danmarkskort.Extras.AlphaImageIcon;

import javax.swing.*;
=======
import javax.swing.*;

import dk.itu.n.danmarkskort.extras.AlphaImageIcon;

>>>>>>> branch 'develop' of https://github.itu.dk/fand/BFST17_DK_Kort
import java.awt.*;

public class CustomButton extends JButton {

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
