package dk.itu.n.danmarkskort.GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GUIManager extends JPanel{

    public final static int
            TOP_PANEL_WIDTH = 450,
            TOP_PANEL_HEIGHT = 50,
            STANDARD_MARGIN = 20,
            SMALL_MARGIN = 10,
            STANDARD_OPACITY = 150;

    private final Color panelBorderColor = new Color(0, 0, 0, STANDARD_OPACITY),
                        //panelBackground = new Color(255, 255, 255, STANDARD_OPACITY),
                        panelBackground = new Color(60, 70, 80, STANDARD_OPACITY);

    public GUIManager() {
        //setBounds(0, 0, MainCanvas.WIDTH, MainCanvas.HEIGHT);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(STANDARD_MARGIN, STANDARD_MARGIN, STANDARD_MARGIN, STANDARD_MARGIN));
        addTopPanel();
        addBottomPanel();
        setOpaque(false);
    }

    public void addTopPanel() {
        JPanel topParent = new JPanel(new GridBagLayout());
        topParent.setOpaque(false);

        JPanel top = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        top.setBackground(panelBackground);
        top.setPreferredSize(new Dimension(TOP_PANEL_WIDTH, TOP_PANEL_HEIGHT));
        top.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, panelBorderColor),
                BorderFactory.createEmptyBorder(SMALL_MARGIN, SMALL_MARGIN, SMALL_MARGIN, SMALL_MARGIN)));


        JButton menu = new JButton(new ImageIcon("resources/menu.png"));
        menu.setBorder(null);
        menu.setOpaque(false);
        menu.setContentAreaFilled(false);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(menu, gbc);

        JTextField input = new JTextField(19);
        input.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1;
        gbc.gridx = 1;
        top.add(input, gbc);

        JButton search = new JButton(new ImageIcon("resources/search.png"));
        search.setBorder(null);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.gridx = 2;
        top.add(search, gbc);

        JButton route = new JButton(new ImageIcon("resources/route.png"));
        route.setBorder(null);
        gbc.gridx = 3;
        top.add(route, gbc);

        topParent.add(top);
        add(topParent, BorderLayout.NORTH);
    }

    public void addBottomPanel() {
        JPanel bottom = new JPanel();
        bottom.setBackground(Color.YELLOW);
        bottom.setPreferredSize(new Dimension(300, 100));
        add(bottom, BorderLayout.SOUTH);

    }
}
