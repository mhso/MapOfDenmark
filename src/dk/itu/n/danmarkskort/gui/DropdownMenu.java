package dk.itu.n.danmarkskort.gui;

import javax.swing.*;

import dk.itu.n.danmarkskort.gui.components.CustomScrollBarUI;

import java.awt.*;

public class DropdownMenu extends JPopupMenu{

    private JPanel menuItems, wrapper;
    private GridBagConstraints gbcContainer;
    private JScrollPane contentPane;
    private TopPanel topPanel;
    private Style style;

    public DropdownMenu(TopPanel topPanel, Style style) {

        this.topPanel = topPanel;
        this.style = style;

        setFocusable(false);
        setBorderPainted(false);
        setBorder(BorderFactory.createEmptyBorder());

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

        JPanel test = new JPanel();
        test.setPreferredSize(new Dimension(80, 800));
        test.setBackground(style.menuContentBG());

        addToContentPane(test);

        gbcContainer.gridx = 1;
        wrapper.add(contentPane, gbcContainer);
    }

    public void addToContentPane(JPanel newContent) {
        contentPane.getViewport().add(newContent);
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
