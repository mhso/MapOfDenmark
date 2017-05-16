package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.gui.map.PinPoint;

import javax.swing.BoxLayout;

public class PinPointPage extends JPanel {

	public static PinPointPage instance;
	public JPanel panel;
	private Style style;
	private JPanel panelRow;
	
	public PinPointPage() {
    	style = Main.style;
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        
        JPanel panelPage = new JPanel();
        panelPage.setLayout(new BorderLayout(0, 0));
        add(panelPage);
        
        JPanel panelHeadline = new JPanel();
        panelHeadline.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelHeadline.setBackground(style.menuContentBG());
        panelPage.add(panelHeadline, BorderLayout.NORTH);
        
        JLabel lblPageHeadline = new JLabel("Your Point of Interests");
        lblPageHeadline.setFont(style.defaultHeadline());
        panelHeadline.add(lblPageHeadline);
        
        JPanel panelSouth = new JPanel();
        panelSouth.setBackground(style.menuContentBG());
        panelPage.add(panelSouth, BorderLayout.SOUTH);
        
        JPanel panelWest = new JPanel();
        panelWest.setBackground(style.menuContentBG());
        panelPage.add(panelWest, BorderLayout.WEST);
        
        JPanel panelEast = new JPanel();
        panelEast.setBackground(style.menuContentBG());
        panelPage.add(panelEast, BorderLayout.EAST);
        
        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(style.menuContentBG());
        panelPage.add(panelCenter);
        panelCenter.setLayout(new BorderLayout());
        panelCenter.setPreferredSize(getPreferredSize());
        initContentPanel(panelCenter);
    }
	
	public void initContentPanel(JPanel panel) {
		panelRow = new JPanel();
		panel.add(panelRow);
		panelRow.setPreferredSize(panel.getPreferredSize());
		panelRow.setLayout(new BoxLayout(panelRow, BoxLayout.Y_AXIS));
		panelRow.setBackground(style.menuContentBG());
		int i = 0;
		for(PinPoint pinPoint : Main.pinPointManager.getPinPoints()) {
			PinPointRow row = new PinPointRow(pinPoint);
			row.setPreferredSize(new Dimension(getWidth(), 30));
			row.setBounds(0, 0, getWidth(), 30);
			panelRow.add(row);
			i++;
		}
		for(int j = 0; j < 5-i ; j++) {
			JPanel emptyRow = new JPanel();
			emptyRow.setBackground(style.menuContentBG());
			emptyRow.setPreferredSize(new Dimension(getWidth(), 30));
			panelRow.add(emptyRow);
		}
		
		instance = this;
	}

	public static void update() {
		if(instance == null) return;
		instance.panelRow.removeAll();
		int i = 0;
		for(PinPoint pinPoint : Main.pinPointManager.getPinPoints()) {
			PinPointRow row = new PinPointRow(pinPoint);
			row.setPreferredSize(new Dimension(instance.getWidth(), 30));
			row.setBounds(0, 0, instance.getWidth(), 30);
			instance.panelRow.add(row);
			i++;
		}
		for(int j = 0; j < 5-i ; j++) {
			JPanel emptyRow = new JPanel();
			emptyRow.setPreferredSize(new Dimension(instance.getWidth(), 30));
			emptyRow.setBackground(instance.style.menuContentBG());
			instance.panelRow.add(emptyRow);
		}
	}
	
	public static void removeRow(PinPointRow row) {
		instance.panelRow.remove(row);
	}
	
}
