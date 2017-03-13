package dk.itu.n.danmarkskort.GUI;

import dk.itu.n.danmarkskort.Extras.AlphaImageIcon;

import javax.swing.*;
import java.awt.*;

public class Style {

    private int topWidth, topHeight, margin, smallMargin, topPanelBorderWidth;
    private float alphaFloat, alphaRollover;
    private Color panelBG, inputFieldBG, panelTextColor, iconBorderColor, zoomButtonBG, dropDownBG;
    private JButton searchButton, menuButton,routeButton, zoomInButton, zoomOutButton;
    private ImageIcon scaleIndicator;
    private AlphaImageIcon searchHoverIcon, menuHoverIcon, routeHoverIcon, zoomInHoverIcon, zoomOutHoverIcon;

    public Style() {
        // Default style sheet
        topWidth = 450; // does this actually do anything?
        topHeight = 35;
        margin = 10;
        smallMargin = 10;
        topPanelBorderWidth = 8;

        alphaFloat = 0.5f;
        alphaRollover = 0.8f;

        panelBG = new Color(30, 40, 50);
        inputFieldBG = new Color(50, 60, 70);
        panelTextColor = new Color(210, 210, 210);
        iconBorderColor = new Color(90, 100, 110);
        zoomButtonBG = new Color(30, 40 ,50);

        searchButton = new JButton(new AlphaImageIcon(new ImageIcon("resources/icons/searchw.png"), alphaFloat));
        menuButton = new JButton(new AlphaImageIcon(new ImageIcon("resources/icons/menuw.png"), alphaFloat));
        routeButton = new JButton(new AlphaImageIcon(new ImageIcon("resources/icons/routew.png"), alphaFloat));
        zoomInButton = new JButton(new AlphaImageIcon(new ImageIcon("resources/icons/zoominw.png"), alphaFloat));
        zoomOutButton = new JButton(new AlphaImageIcon(new ImageIcon("resources/icons/zoomoutw.png"), alphaFloat));

        searchHoverIcon = new AlphaImageIcon(new ImageIcon("resources/icons/searchw.png"), alphaRollover);
        menuHoverIcon = new AlphaImageIcon(new ImageIcon("resources/icons/menuw.png"), alphaRollover);
        routeHoverIcon = new AlphaImageIcon(new ImageIcon("resources/icons/routew.png"), alphaRollover);
        zoomInHoverIcon = new AlphaImageIcon(new ImageIcon("resources/icons/zoominw.png"), alphaRollover);
        zoomOutHoverIcon = new AlphaImageIcon(new ImageIcon("resources/icons/zoomoutw.png"), alphaRollover);

        scaleIndicator = new ImageIcon("resources/scale.png");
    }

    // Getters
    public int topWidth() { return topWidth; }
    public int topHeight() { return topHeight; }
    public int margin() { return margin; }
    public int smallMargin() { return smallMargin; }
    public int topPanelBorderWidth() { return topPanelBorderWidth; }
    public float alphaFloat() { return alphaFloat; }

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

    public AlphaImageIcon searchHoverIcon() { return searchHoverIcon; }
    public AlphaImageIcon menuHoverIcon() { return menuHoverIcon; }
    public AlphaImageIcon routeHoverIcon() { return routeHoverIcon; }
    public AlphaImageIcon zoomInHoverIcon() { return zoomInHoverIcon; }
    public AlphaImageIcon zoomOutHoverIcon() { return zoomOutHoverIcon; }

    public ImageIcon scaleIndicator() { return scaleIndicator; }
}
