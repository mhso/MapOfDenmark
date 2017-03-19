package dk.itu.n.danmarkskort.gui;

import javax.swing.*;
import java.awt.*;

public class Style {

    private int margin, smallMargin, topPanelBorderWidth, menuItemInsets;
    private float alphaFloat, alphaHover, menuAlpha, menuAlphaHover;
    private Color panelBG, inputFieldBG, panelTextColor, zoomButtonBG, menuContentBG, menuItemsBG, scrollBarThumb, scrollBarThumbActive,
                dropdownItemBG, dropdownItemBGActive, dropdowItemTextColor, dropdownItemTextColorActive;
    private CustomButton searchButton, menuButton,routeButton, zoomInButton, zoomOutButton,
            menuLayerButton, menuSaveButton, menuOpenButton, menuSettingsButton, menuRouteButton, menuInfoButton;
    private ImageIcon scaleIndicator;

    public Style() {
        // top panel
        margin = 10;
        smallMargin = 10;
        topPanelBorderWidth = 8;
        menuItemInsets = 6;
        menuAlpha = 0.7f;
        menuAlphaHover = 1f;
        alphaFloat = 0.5f;
        alphaHover = 0.8f;

        panelBG = new Color(30, 40, 50);
        inputFieldBG = new Color(50, 60, 70);
        panelTextColor = new Color(210, 210, 210);
        zoomButtonBG = new Color(30, 40 ,50);
        menuItemsBG = new Color(60, 70, 80);
        menuContentBG = new Color(200, 210, 220);
        scrollBarThumb = new Color(150, 160, 170);
        scrollBarThumbActive = new Color(20, 130 , 200);
        dropdownItemBG = new Color(255, 255, 255);
        dropdownItemBGActive = new Color(20, 130, 200);
        dropdowItemTextColor = new Color(0,0,0);
        dropdownItemTextColorActive = new Color(255, 255, 255);


        searchButton = new CustomButton("resources/icons/search.png", alphaFloat, alphaHover);
        menuButton = new CustomButton("resources/icons/menu.png", alphaFloat, alphaHover);
        routeButton = new CustomButton("resources/icons/route.png", alphaFloat, alphaHover);

        menuLayerButton = new CustomButton("resources/icons/layers.png", menuAlpha, menuAlphaHover);
        menuSaveButton = new CustomButton("resources/icons/save.png", menuAlpha, menuAlphaHover);
        menuOpenButton = new CustomButton("resources/icons/open.png", menuAlpha, menuAlphaHover);
        menuSettingsButton = new CustomButton("resources/icons/settings.png", menuAlpha, menuAlphaHover);
        menuRouteButton = new CustomButton("resources/icons/menuroute.png", menuAlpha, menuAlphaHover);
        menuInfoButton = new CustomButton("resources/icons/info.png", menuAlpha, menuAlphaHover);

        zoomInButton = new CustomButton("resources/icons/zoomin.png", alphaFloat, alphaHover, zoomButtonBG);
        zoomOutButton = new CustomButton("resources/icons/zoomout.png", alphaFloat, alphaHover, zoomButtonBG);

        scaleIndicator = new ImageIcon("resources/scale.png");

    }

    // Getters
    public int margin() { return margin; }
    public int smallMargin() { return smallMargin; }
    public int topPanelBorderWidth() { return topPanelBorderWidth; }
    public int menuItemInsets() { return menuItemInsets; }
    public float alphaFloat() { return alphaFloat; }

    public Color panelBG() { return panelBG; }
    public Color inputFieldBG() { return inputFieldBG; }
    public Color panelTextColor() { return panelTextColor; }
    public Color zoomButtonBG() { return zoomButtonBG; }
    public Color menuItemsBG() { return menuItemsBG; }
    public Color menuContentBG() { return menuContentBG; }
    public Color scrollBarThumb() { return scrollBarThumb; }
    public Color scrollBarThumbActive() { return scrollBarThumbActive; }
    public Color dropdownItemBG() { return dropdownItemBG; }
    public Color dropdownItemBGActive() { return dropdownItemBGActive; }
    public Color dropdowItemTextColor() { return dropdowItemTextColor; }
    public Color dropdownItemTextColorActive() { return dropdownItemTextColorActive; }

    public CustomButton searchButton() { return searchButton; }
    public CustomButton menuButton() { return menuButton; }
    public CustomButton routeButton() { return routeButton; }
    public CustomButton zoomInButton() { return zoomInButton; }
    public CustomButton zoomOutButton() { return zoomOutButton; }
    public CustomButton menuLayerButton() { return menuLayerButton; }
    public CustomButton menuSaveButton() { return menuSaveButton; }
    public CustomButton menuOpenButton() { return menuOpenButton; }
    public CustomButton menuSettingsButton() { return menuSettingsButton; }
    public CustomButton menuRouteButton() { return menuRouteButton; }
    public CustomButton menuInfoButton() { return menuInfoButton; }

    public ImageIcon scaleIndicator() { return scaleIndicator; }

}
