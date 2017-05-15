package dk.itu.n.danmarkskort.gui;


import javax.imageio.ImageIO;
import javax.swing.*;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.components.CustomButton;
import dk.itu.n.danmarkskort.gui.components.CustomToggleButton;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Style {

    private int margin, smallMargin, topPanelBorderWidth, menuItemInsets;
    private float alphaFloat, alphaHover, menuAlpha, menuAlphaHover;
    private Color panelBG, inputFieldBG, panelTextColor, zoomButtonBG, menuContentBG, menuItemsBG, scrollBarThumb, scrollBarThumbActive,
                dropdownItemBG, dropdownItemBGActive, dropdowItemTextColor, dropdownItemTextColorActive, launcherVersionText, launcherSelectionBG,
                routeOuterBG, routeInnerBG, routeText, routeTextLight;
    private CustomButton searchButton, menuButton, routeButton, centerViewButton, zoomInButton, zoomOutButton,
            menuLayerButton, menuSaveButton, menuOpenButton, menuSettingsButton, menuRouteButton, menuInfoButton, 
            menuPinPointButton, pinPointPanButton, pinPointDeleteButton, arrowUpDownButton;
    private ImageIcon scaleIndicator, basicThemePreview, nightThemePreview, bwThemePreview, logo, launcherOptionsIcon, launcherLoadIcon;
    private Image frameIcon;
    private Font normalText, smallHeadline, mediumHeadline, largeHeadline, largeHeadlineSpacing, defaultLabelText, defaultHeadline, mapLabelFont;

    
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

        launcherVersionText = new Color(160, 160, 160);
        launcherSelectionBG = new Color(130, 170, 200);

        routeOuterBG = panelBG;
        routeInnerBG = inputFieldBG;
        routeText = new Color(180, 180, 180);
        routeTextLight = new Color(255, 255, 255);

        searchButton = new CustomButton("resources/icons/search.png", alphaFloat, alphaHover);
        menuButton = new CustomButton("resources/icons/menu.png", alphaFloat, alphaHover);
        routeButton = new CustomButton("resources/icons/route.png", alphaFloat, alphaHover);
        
        menuLayerButton = new CustomButton("resources/icons/layers.png", menuAlpha, menuAlphaHover);
        menuSaveButton = new CustomButton("resources/icons/save.png", menuAlpha, menuAlphaHover);
        menuOpenButton = new CustomButton("resources/icons/open.png", menuAlpha, menuAlphaHover);
        menuSettingsButton = new CustomButton("resources/icons/settings.png", menuAlpha, menuAlphaHover);
        menuRouteButton = new CustomButton("resources/icons/menuroute.png", menuAlpha, menuAlphaHover);
        menuInfoButton = new CustomButton("resources/icons/info.png", menuAlpha, menuAlphaHover);
        menuPinPointButton = new CustomButton("resources/icons/pinpointmenu.png", menuAlpha, menuAlphaHover);
        
        menuOpenButton = new CustomButton("resources/icons/open.png", menuAlpha, menuAlphaHover);

        arrowUpDownButton = new CustomButton("resources/icons/arrowupdown.png", menuAlpha, menuAlphaHover);
        
        centerViewButton = new CustomButton("resources/icons/target.png", alphaFloat, alphaHover, zoomButtonBG);
        zoomInButton = new CustomButton("resources/icons/zoomin.png", alphaFloat, alphaHover, zoomButtonBG);
        zoomOutButton = new CustomButton("resources/icons/zoomout.png", alphaFloat, alphaHover, zoomButtonBG);
        
        basicThemePreview = getImageIcon("resources/icons/previewthemebasic.png");
        nightThemePreview = getImageIcon("resources/icons/previewthemenight.png");
        bwThemePreview = getImageIcon("resources/icons/previewthemebw.png");
        
        scaleIndicator = getImageIcon("resources/scale.png");
        
        arrowUpDownButton = new CustomButton("resources/icons/arrowupdown.png", menuAlpha, menuAlphaHover);
        
        if(Main.production) frameIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icons/map-icon.png"));
        else frameIcon = Toolkit.getDefaultToolkit().getImage("resources/icons/map-icon.png");

        logo = getImageIcon("resources/icons/map-icon.png");
        logo = new ImageIcon(logo.getImage().getScaledInstance(128, 128, BufferedImage.SCALE_SMOOTH));
        launcherOptionsIcon = getImageIcon("resources/icons/settings.png");
        launcherLoadIcon = getImageIcon("resources/icons/open.png");


        try {
        	if(Main.production) {
        	    defaultLabelText = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/Roboto-Medium.ttf")).deriveFont(13f);
        		normalText = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/Roboto-Regular.ttf")).deriveFont(16f);
                smallHeadline = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/Roboto-Medium.ttf")).deriveFont(18f);
                defaultHeadline = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/Roboto-Medium.ttf")).deriveFont(20f);
                mediumHeadline = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/Roboto-Light.ttf")).deriveFont(20f);
                largeHeadline = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/Roboto-Light.ttf")).deriveFont(42f);
                mapLabelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/Roboto-Medium.ttf"));
        	}
        	else {
                defaultLabelText = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Roboto-Medium.ttf")).deriveFont(13f);
                normalText = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Roboto-Regular.ttf")).deriveFont(16f);
                smallHeadline = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Roboto-Medium.ttf")).deriveFont(18f);
                defaultHeadline = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Roboto-Medium.ttf")).deriveFont(20f);
                mediumHeadline = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Roboto-Light.ttf")).deriveFont(20f);
                largeHeadline = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Roboto-Light.ttf")).deriveFont(42f);
                mapLabelFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Roboto-Medium.ttf"));
        	}
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.TRACKING, 0.1);
        largeHeadlineSpacing = largeHeadline.deriveFont(attributes);
    }
    
    private ImageIcon getImageIcon(String fileName) {
    	if(Main.production)
			try {
				return new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/"+fileName)));
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		else return new ImageIcon(fileName);
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
    public CustomButton centerViewButton() { return centerViewButton; }
    public CustomButton zoomInButton() { return zoomInButton; }
    public CustomButton zoomOutButton() { return zoomOutButton; }
    public CustomButton menuLayerButton() { return menuLayerButton; }
    public CustomButton menuSaveButton() { return menuSaveButton; }
    public CustomButton menuOpenButton() { return menuOpenButton; }
    public CustomButton menuSettingsButton() { return menuSettingsButton; }
    public CustomButton menuRouteButton() { return menuRouteButton; }
    public CustomButton menuInfoButton() { return menuInfoButton; }
    public CustomButton menuPinPointButton() { return menuPinPointButton; }
    public CustomButton pinPointPanButton() { return pinPointPanButton; }
    public CustomButton pinPointDeleteButton() { return pinPointDeleteButton; }
    public CustomButton arrowUpDownButton() { return arrowUpDownButton; }
    
    public CustomToggleButton toggleButton() { return new CustomToggleButton(1.0f, 0.8f); }
    
    public ImageIcon basicThemePreview() { return basicThemePreview; }
    public ImageIcon nightThemePreview() { return nightThemePreview; }
    public ImageIcon bwThemePreview() { return bwThemePreview; }
    
    public ImageIcon scaleIndicator() { return scaleIndicator; }
    
    public Image frameIcon() { return frameIcon; }

    public Font defaultLabelText() { return defaultLabelText; }
    public Font normalText() { return normalText; }
    public Font smallHeadline() { return smallHeadline; }
    public Font defaultHeadline() { return defaultHeadline; }
    public Font mediumHeadline() { return mediumHeadline; }
    public Font largeHeadline() { return largeHeadline; }
    public Font mapLabelFont() { return mapLabelFont; }
    public Font largeHeadlineSpacing() { return largeHeadlineSpacing; }

    public Color launcherVersionText() { return launcherVersionText; }
    public ImageIcon logo() { return logo; }
    public ImageIcon launcherOptionsIcon() { return launcherOptionsIcon; }
    public ImageIcon launcherLoadIcon() { return launcherLoadIcon; }
    public Color launcherSelectionBG() { return launcherSelectionBG; }

    public Color routeOuterBG() { return routeOuterBG; }
    public Color routeInnerBG() { return routeInnerBG; }
    public Color routeText() { return routeText; }
    public Color routeTextLight() { return routeTextLight; }
}
