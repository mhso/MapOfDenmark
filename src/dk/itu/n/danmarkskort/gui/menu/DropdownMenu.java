package dk.itu.n.danmarkskort.gui.menu;

import dk.itu.n.danmarkskort.gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DropdownMenu extends CustomDropdown {

    private JPanel menuItems, wrapper;
    private JPanel routePage, aboutUsPage, settingsPage, mapLayersPage, loadPage, savePage;
    private GridBagConstraints gbcContainer;
    private JScrollPane contentPane;
    private TopPanel topPanel;
    private Style style;
    private boolean popUpBlocked;

    public DropdownMenu(TopPanel topPanel, Style style) {
        super();

        this.topPanel = topPanel;
        this.style = style;

        routePage = new RoutePage(this, "TEMP_VALUE");
        loadPage = new LoadPage();
        savePage = new SavePage();
        settingsPage = new SettingsPage();
        mapLayersPage = new MapLayersPage();
        aboutUsPage = new AboutUsPage();

        wrapper = new JPanel(new GridBagLayout());
        gbcContainer = new GridBagConstraints();

        addMenuItems();
        addcontentPane();

        add(wrapper);
    }

    private void addMenuItems() {
        GridBagConstraints gbcMenuItems = new GridBagConstraints();
        gbcMenuItems.anchor = GridBagConstraints.NORTH;
        gbcMenuItems.insets = new Insets(style.menuItemInsets(), style.menuItemInsets(), style.menuItemInsets(), style.menuItemInsets());

        menuItems = new JPanel(new GridBagLayout());
        menuItems.setBackground(style.menuItemsBG());
        menuItems.setBorder(BorderFactory.createEmptyBorder(style.menuItemInsets(), style.menuItemInsets(), style.menuItemInsets(), style.menuItemInsets()));

        // route
        CustomButton routeButton = style.menuRouteButton();
        routeButton.addActionListener(e -> addToContentPane(routePage));
        menuItems.add(routeButton, gbcMenuItems);

        // open
        gbcMenuItems.gridy = 1;
        CustomButton openButton = style.menuOpenButton();
        openButton.addActionListener(e -> addToContentPane(loadPage));
        menuItems.add(openButton, gbcMenuItems);

        // save
        gbcMenuItems.gridy = 2;
        CustomButton saveButton = style.menuSaveButton();
        saveButton.addActionListener(e -> addToContentPane(savePage));
        menuItems.add(saveButton, gbcMenuItems);

        // layers/filters
        gbcMenuItems.gridy = 3;
        CustomButton layerButton = style.menuLayerButton();
        layerButton.addActionListener(e -> addToContentPane(mapLayersPage));
        menuItems.add(layerButton, gbcMenuItems);

        // settings
        gbcMenuItems.gridy = 4;
        CustomButton settingsButton = style.menuSettingsButton();
        settingsButton.addActionListener(e -> addToContentPane(settingsPage));
        menuItems.add(settingsButton, gbcMenuItems);

        // about us
        gbcMenuItems.gridy = 5;
        CustomButton aboutUsButton = style.menuInfoButton();
        aboutUsButton.addActionListener(e -> addToContentPane(aboutUsPage));
        menuItems.add(aboutUsButton, gbcMenuItems);

        menuItems.setPreferredSize(new Dimension(topPanel.getMenuWidth(), menuItems.getPreferredSize().height));
        gbcContainer.gridx = 0;
        wrapper.add(menuItems, gbcContainer);
    }

    private void addcontentPane() {
        contentPane = new JScrollPane();
        contentPane.setBorder(null);

        contentPane.setPreferredSize(new Dimension(topPanel.getTopPanelDimension().width - topPanel.getMenuWidth(), menuItems.getPreferredSize().height));
        contentPane.getViewport().setBackground(style.menuContentBG());
        contentPane.getVerticalScrollBar().setUnitIncrement(6);
        contentPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(style));
        
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        gbcContainer.gridx = 1;
        wrapper.add(contentPane, gbcContainer);
    }

    public void addToContentPane(JPanel newContent) {
        contentPane.getViewport().removeAll();
        contentPane.getViewport().add(newContent);
    }
    
    public boolean isPopUpBlocked() {
    	return popUpBlocked;
    }
    
    public void blockVisibility(boolean blocked) {
		popUpBlocked = blocked;
	}
    
    public void setVisible(boolean visible) {
		if(popUpBlocked) return;
		else super.setVisible(visible);
	}

    /**
     * Show the Dropdown-Menu at the specified Component.
     * @param source The component under which this menu should be shown.
     */
    public void showDropdown(Component source) {
        Point loc = source.getLocation();

        // Not quite sure why the - 6, + 1 is needed. I suppose the 6 is for the insets in the toppanel, maybe
        show(source,
                loc.x - style.topPanelBorderWidth() - 6,
                loc.y + source.getHeight() + style.topPanelBorderWidth() + 1);
    }
}
