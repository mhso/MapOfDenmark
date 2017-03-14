package dk.itu.n.danmarkskort.GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DropdownMenu extends JPopupMenu{

    private JPanel menuItems, wrapper;
    private GridBagConstraints gbcContainer;
    private JScrollPane contentScrollPanel;
    private TopPanel topPanel;
    private Style style;

    public DropdownMenu(TopPanel topPanel, Style style) {

        this.topPanel = topPanel;
        this.style = style;

        setFocusable(false);
        setBorderPainted(false);
        setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        wrapper = new JPanel(new GridBagLayout());
        gbcContainer = new GridBagConstraints();

        addMenuItems();
        addContentPanel();

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
        menuItems.add(style.menuRouteButton(), gbcMenuItems);

        // open
        gbcMenuItems.gridy = 1;
        menuItems.add(style.menuOpenButton(), gbcMenuItems);

        // save
        gbcMenuItems.gridy = 2;
        menuItems.add(style.menuSaveButton(), gbcMenuItems);

        // layers/filters
        gbcMenuItems.gridy = 3;
        menuItems.add(style.menuLayerButton(), gbcMenuItems);

        // settings
        gbcMenuItems.gridy = 4;
        menuItems.add(style.menuSettingsButton(), gbcMenuItems);

        // about us
        gbcMenuItems.gridy = 5;
        menuItems.add(style.menuInfoButton(), gbcMenuItems);
        menuItems.setPreferredSize(new Dimension(topPanel.getMenuWidth(), menuItems.getPreferredSize().height));
        gbcContainer.gridx = 0;
        wrapper.add(menuItems, gbcContainer);
    }

    private void addContentPanel() {
        JPanel content = new JPanel();
        content.setPreferredSize(new Dimension(topPanel.getTopPanelDimension().width - topPanel.getMenuWidth(), menuItems.getPreferredSize().height));
        content.setBackground(style.menuContentBG());
        contentScrollPanel = new JScrollPane(content);

        gbcContainer.gridx = 1;
        wrapper.add(content, gbcContainer);
    }

    /**
     * Hide the Dropdown-Menu.
     */
    public void hideDropdown() {
        setVisible(false);
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
