package dk.itu.n.danmarkskort.GUI;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {

    Style style;
    GridBagConstraints gbcParent;

    public BottomPanel(Style style) {

        this.style = style;

        setLayout(new GridBagLayout());
        setOpaque(false);

        gbcParent = new GridBagConstraints();
        gbcParent.anchor = GridBagConstraints.SOUTH;

        addLeft();
        addCenter();
        addRight();
    }

    private void addLeft() {

        JPanel leftPanel = new JPanel(new GridBagLayout());
        gbcParent.gridx = 0;
        gbcParent.gridy = 1;

        GridBagConstraints gbc = new GridBagConstraints();
        JLabel dummy = new JLabel();
        gbc.weightx = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1;
        leftPanel.add(dummy, gbc);

        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel scale = new JLabel(style.scaleIndicator());

        leftPanel.add(scale, gbc);

        add(leftPanel, gbcParent);
    }

    private void addCenter() {
        // the idea is that this panel is gonna contain a box at the bottom center,
        // which displays the longitude-latitude where the mouse is, and a the name
        // of the nearest street

        //right now it contains nothing

        JLabel dummy = new JLabel();
        gbcParent.gridx = 1;
        gbcParent.weightx = 1;

        add(dummy, gbcParent);
    }

    private void addRight() {

        JPanel rightParent = new JPanel(new GridBagLayout());
        rightParent.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel dummy = new JLabel();
        gbc.weighty = 1;
        gbc.weightx = 1;
        rightParent.add(dummy, gbc);

        JButton zoomIn = style.zoomInButton();
        zoomIn.setBackground(style.zoomButtonBG());
        zoomIn.setToolTipText("Zoom in");
        zoomIn.setBorder(null);
        zoomIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        zoomIn.setRolloverIcon(style.zoomInHoverIcon());

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.weightx = 0.0;
        rightParent.add(zoomIn, gbc);

        JButton zoomOut = style.zoomOutButton();
        zoomOut.setBackground(style.zoomButtonBG());
        zoomOut.setToolTipText("Zoom out");
        zoomOut.setBorder(null);
        zoomOut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        zoomOut.setRolloverIcon(style.zoomOutHoverIcon());

        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 0, 0);
        rightParent.add(zoomOut, gbc);

        gbcParent.gridx = 2;
        gbcParent.weightx = 0.0;
        add(rightParent, gbcParent);
    }

}