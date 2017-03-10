package dk.itu.n.danmarkskort.GUI;

import dk.itu.n.danmarkskort.Extras.AlphaContainer;
import dk.itu.n.danmarkskort.Extras.AlphaImageIcon;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.MainCanvas;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUIManager extends JPanel{

    public final static int
            TOP_PANEL_WIDTH = 450,
            TOP_PANEL_HEIGHT = 50,
            STANDARD_MARGIN = 20,
            SMALL_MARGIN = 10,
            STANDARD_OPACITY = 150;

    private float alphaFloat = 0.6f;
    private int alphaLevel = (int) (255 * alphaFloat);

    private Color panelBorderColor = new Color(60, 70, 80, alphaLevel),
            panelBackground = new Color(60, 70, 80, alphaLevel),
            textColor = new Color(255, 255, 255, alphaLevel);

    private MainCanvas maincanvas;

    public GUIManager(MainCanvas mc) {
        maincanvas = mc;

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
        top.setPreferredSize(new Dimension(TOP_PANEL_WIDTH, TOP_PANEL_HEIGHT));
        top.setBorder(BorderFactory.createLineBorder(panelBorderColor, 10, true));

        JButton menu = new JButton(new AlphaImageIcon(new ImageIcon("resources/menu.png"), alphaFloat));
        menu.setBorder(null);
        menu.setContentAreaFilled(false);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(menu, gbc);

        JTextField input = new JTextField(19);
        input.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        input.setFont(new Font("sans serif", Font.BOLD, 18));
        input.setForeground(textColor);
        input.setBackground(panelBackground);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1;
        gbc.gridx = 1;
        top.add(input, gbc);

        JButton search = new JButton(new AlphaImageIcon(new ImageIcon("resources/search.png"), alphaFloat));
        search.setBorder(null);
        search.setContentAreaFilled(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.gridx = 2;
        top.add(search, gbc);

        JButton route = new JButton(new AlphaImageIcon(new ImageIcon("resources/route.png"), alphaFloat));
        route.setBorder(null);
        route.setContentAreaFilled(false);
        gbc.gridx = 3;
        top.add(route, gbc);

        topParent.add(top);

        add(topParent, BorderLayout.NORTH);
    }

    public void addBottomPanel() {
        JPanel bottomParent = new JPanel();
        bottomParent.setBackground(Color.YELLOW);
        bottomParent.setPreferredSize(new Dimension(300, 100));
        add(bottomParent, BorderLayout.SOUTH);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
