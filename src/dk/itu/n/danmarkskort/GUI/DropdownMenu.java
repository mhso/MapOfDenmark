package dk.itu.n.danmarkskort.GUI;

import javax.swing.*;
import java.awt.*;

public class DropdownMenu extends JPopupMenu{

    JPanel menuItems;
    JScrollPane content;
    TopPanel topPanel;
    Style style;

    public DropdownMenu(TopPanel topPanel, Style style) {

        this.topPanel = topPanel;
        this.style = style;

        setFocusable(false);
        setBorderPainted(false);

        addMenuItems();
        addContentPanel();
    }

    private void addMenuItems() {
        menuItems = new JPanel();
        menuItems.setBackground(Color.BLACK);
        //menuItems.setPreferredSize(new Dimension(400, 500));
        menuItems.setPreferredSize(new Dimension(topPanel.getMenuWidth(), menuItems.getPreferredSize().height));
        add(menuItems);
    }

    private void addContentPanel() {
        JPanel something = new JPanel();
        something.setPreferredSize(new Dimension(topPanel.getTopPanelDimension().width - topPanel.getMenuWidth(), menuItems.getPreferredSize().height));
        content = new JScrollPane(something);
        add(menuItems);
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
        show(source, loc.x - style.topPanelBorderWidth(), loc.y + source.getHeight() + style.topPanelBorderWidth());
        System.out.println(loc.x + ", " + loc.y);
    }

}
