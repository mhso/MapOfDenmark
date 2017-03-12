package dk.itu.n.danmarkskort.GUI;

import dk.itu.n.danmarkskort.Extras.AlphaImageIcon;

import javax.swing.*;
import java.awt.*;

public class Style {

    private int topWidth, topHeight, margin, smallMargin, alphaInt;

    private float alphaFloat;

    private Color panelBorderColor, panelBG, inputFieldBG, panelTextColor, iconBorderColor, zoomButtonBG;

    private JButton searchButton, menuButton,routeButton, zoomInButton, zoomOutButton;

    private ImageIcon scaleIndicator;

    public Style() {
        // Default style sheet
        topWidth = 450; // does this actually do anything?
        topHeight = 35;
        margin = 10;
        smallMargin = 10;
        alphaInt = 255;

        alphaFloat = 0.6f;

        panelBorderColor = new Color(60, 70, 80, alphaInt);
        panelBG = new Color(60, 70, 80, alphaInt);
        inputFieldBG = new Color(80, 90, 100);
        panelTextColor = new Color(180, 180, 180);
        iconBorderColor = new Color(90, 100, 110);
        zoomButtonBG = new Color(40, 50 ,60);

        searchButton = new JButton(new AlphaImageIcon(new ImageIcon("resources/searchw.png"), alphaFloat));
        menuButton = new JButton(new AlphaImageIcon(new ImageIcon("resources/menuw.png"), alphaFloat));
        routeButton = new JButton(new AlphaImageIcon(new ImageIcon("resources/routew.png"), alphaFloat));
        zoomInButton = new JButton(new AlphaImageIcon(new ImageIcon("resources/zoominw.png"), alphaFloat));
        zoomOutButton = new JButton(new AlphaImageIcon(new ImageIcon("resources/zoomoutw.png"), alphaFloat));

        scaleIndicator = new ImageIcon("resources/scale.png");
    }

    // Getters
    public int topWidth() { return topWidth; }
    public int topHeight() { return topHeight; }
    public int margin() { return margin; }
    public int smallMargin() { return smallMargin; }
    public int alphaInt() { return alphaInt; }
    public float alphaFloat() { return alphaFloat; }
    public Color panelBorderColor() { return panelBorderColor; }
    public Color panelBG() { return panelBG; }
    public Color inputFieldBG() { return inputFieldBG; }
    public Color panelTextColor() { return panelTextColor; }
    public Color iconBorderColor() {return iconBorderColor; }
    public Color zoomButtonBG() { return zoomButtonBG; }

    public JButton searchButton() { return searchButton; }
    public JButton menuButton() { return menuButton; }
    public JButton routeButton() { return routeButton; }
    public JButton zoomInButton() { return zoomInButton; }
    public JButton zoomOutButton() { return zoomOutButton; }
    public ImageIcon scaleIndicator() { return scaleIndicator; }
}
