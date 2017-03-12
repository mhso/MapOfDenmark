package dk.itu.n.danmarkskort.GUI;

import dk.itu.n.danmarkskort.Extras.AlphaImageIcon;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {

    Style style;

    public TopPanel(Style style) {

        this.style = style;
        setOpaque(false);

        JPanel topParent = new JPanel(new BorderLayout());
        topParent.setBorder(BorderFactory.createLineBorder(style.panelBorderColor(), 10, true));
        topParent.setOpaque(false);

        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(style.panelBG());
        top.setBorder(null);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 3, 0, 3);
        gbc.weighty = 1;

        JButton menu = style.menuButton();
        menu.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, style.iconBorderColor()));
        menu.setBackground(style.panelBG());
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menu.setToolTipText("Display the menu");

        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(menu, gbc);

        JTextField input = new JTextField(19);
        input.setBorder(BorderFactory.createEmptyBorder(style.smallMargin(), style.smallMargin(), style.smallMargin(), style.smallMargin()));
        input.setFont(new Font("sans serif", Font.PLAIN, 14));
        input.setForeground(style.panelTextColor());
        input.setOpaque(false);
        input.setCaretColor(style.panelTextColor());

        JButton search = style.searchButton();
        search.setBorder(null);
        search.setOpaque(false);
        search.setContentAreaFilled(false);
        search.setCursor(new Cursor(Cursor.HAND_CURSOR));
        search.setToolTipText("Search with the current input");

        JPanel searchInputWrapper = new JPanel(new GridBagLayout());
        searchInputWrapper.setBackground(style.inputFieldBG());
        GridBagConstraints gbc2 = new GridBagConstraints();
        searchInputWrapper.add(input, gbc2);
        searchInputWrapper.add(search, gbc2);

        gbc.insets = new Insets(0, 6, 0, 6);
        gbc.weightx = 1;
        gbc.gridx = 1;
        top.add(searchInputWrapper, gbc);

        JButton route = style.routeButton();
        route.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, style.iconBorderColor()));
        route.setBackground(style.panelBG());
        route.setToolTipText("Display the route finding tool");
        route.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.insets = new Insets(0, 3, 0, 3);
        gbc.gridx = 2;
        top.add(route, gbc);

        topParent.add(top);
        add(topParent);
    }
}
