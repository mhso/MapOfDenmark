package dk.itu.n.danmarkskort.GUI;

import dk.itu.n.danmarkskort.address.AddressController;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.Set;

public class TopPanel extends JPanel {

    private Style style;
    private DropdownAddressSearch das;
    private DropdownMenu dropMenu;
    private JTextField input;
    private JPanel searchInputWrapper, topParent;
    private JButton menu;

    public TopPanel(Style style) {

        this.style = style;
        setOpaque(false);

        topParent = new JPanel(new BorderLayout());
        topParent.setBorder(BorderFactory.createLineBorder(style.panelBG(), style.topPanelBorderWidth(), false));
        topParent.setOpaque(false);

        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(style.panelBG());
        top.setBorder(null);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 3, 0, 3);
        gbc.weighty = 1;

        menu = style.menuButton();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(menu, gbc);

        input = new JTextField(30);
        input.setBorder(BorderFactory.createEmptyBorder(style.smallMargin(), style.smallMargin(), style.smallMargin(), style.smallMargin()));
        input.setFont(new Font("sans serif", Font.PLAIN, 14));
        input.setForeground(style.panelTextColor());
        input.setOpaque(false);
        input.setCaretColor(style.panelTextColor());

        JButton search = style.searchButton();

        searchInputWrapper = new JPanel(new GridBagLayout());
        searchInputWrapper.setBackground(style.inputFieldBG());
        GridBagConstraints gbc2 = new GridBagConstraints();

        searchInputWrapper.add(input, gbc2);
        searchInputWrapper.add(search, gbc2);

        gbc.insets = new Insets(0, 6, 0, 6);
        gbc.weightx = 1;
        gbc.gridx = 1;
        top.add(searchInputWrapper, gbc);

        JButton route = style.routeButton();

        gbc.insets = new Insets(0, 3, 0, 3);
        gbc.gridx = 2;
        top.add(route, gbc);

        topParent.add(top);
        add(topParent);


        das = new DropdownAddressSearch(this);
        dropMenu = new DropdownMenu(this, style);

        menu.addActionListener(e -> {
            dropMenu.showDropdown(menu);
        });

        // adding drop down functionality
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new TopPanel.SearchFilter());
    }

    public void populateList(Set<String> items) {
        das.hideDropdown();
        das.clearElements();
        int i = 0;
        for(String st : items){
            das.addElement(st);
            if(++i > 10) break;
        }
        das.showDropdown(input);
    }

    public int getInputFieldWidth() {
        return searchInputWrapper.getPreferredSize().width;
    }

    public int getMenuWidth() {
        return menu.getPreferredSize().width + style.topPanelBorderWidth() + 12;
    } // 6 is from insets

    public Dimension getTopPanelDimension() {
        return topParent.getPreferredSize();
    }

    public JTextField getInputField() {
        return input;
    }

    private class SearchFilter extends DocumentFilter {
        @Override
        public void replace(FilterBypass fb, int offset, int length, String newText,
                            AttributeSet attr) throws BadLocationException {

            super.replace(fb, offset, length, newText, attr);

            if(offset > 2) populateList(AddressController.getInstance().getAddrSearchResults(input.getText()));

            revalidate();
            repaint();
        }
    }
}
