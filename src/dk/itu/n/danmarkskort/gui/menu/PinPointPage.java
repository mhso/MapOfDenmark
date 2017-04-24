package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.gui.map.PinPoint;

import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class PinPointPage extends JPanel {

	public static PinPointPage instance;
	public JPanel panel;
	private Style style;
	private JPanel panelRow;
	
//	public PinPointPage() {
//		setLayout(new BorderLayout(0, 0));
//		
//		JLabel lblDineInteressepunkter = new JLabel("Dine interessepunkter");
//		lblDineInteressepunkter.setHorizontalAlignment(SwingConstants.CENTER);
//		add(lblDineInteressepunkter, BorderLayout.NORTH);
//		
//		panel = new JPanel();
//		add(panel, BorderLayout.CENTER);
//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//		
//		JScrollPane scrollPane = new JScrollPane(panel);
//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		add(scrollPane, BorderLayout.SOUTH);
//		
//		for(PinPoint pinPoint : Main.pinPointManager.getPinPoints()) {
//			PinPointRow row = new PinPointRow(pinPoint);
//			row.setPreferredSize(new Dimension(getWidth(), 30));
//			row.setBounds(0, 0, getWidth(), 30);
//			panel.add(row);
//		}
//		
//		instance = this;
//
//	}
	
	public PinPointPage() {
    	style = new Style();
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        
        JPanel panelPage = new JPanel();
        panelPage.setLayout(new BorderLayout(0, 0));
        add(panelPage);
        
        JPanel panelHeadline = new JPanel();
        panelHeadline.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelHeadline.setBackground(style.menuContentBG());
        panelPage.add(panelHeadline, BorderLayout.NORTH);
        JLabel lblPageHeadline = new JLabel("Dine interessepunkter");
        lblPageHeadline.setFont(new Font("Tahoma", Font.BOLD, 18));
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
        panelPage.add(panelCenter, BorderLayout.CENTER);
        panelCenter.setLayout(new BorderLayout(0, 0));
        initContentPanel(panelCenter);
    }
	
	public void initContentPanel(JPanel panel) {
//		setLayout(new BorderLayout(0, 0));
//		
		panelRow = new JPanel();
		panel.add(panelRow);
		panelRow.setLayout(new BoxLayout(panelRow, BoxLayout.Y_AXIS));
		panelRow.setBackground(style.menuContentBG());
//		
//		JScrollPane scrollPane = new JScrollPane(panel);
//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		add(scrollPane, BorderLayout.SOUTH);
//		
		for(PinPoint pinPoint : Main.pinPointManager.getPinPoints()) {
			PinPointRow row = new PinPointRow(pinPoint);
			row.setPreferredSize(new Dimension(getWidth(), 30));
			row.setBounds(0, 0, getWidth(), 30);
			panelRow.add(row);
		}
		
		instance = this;
	}

	public static void update() {
		if(instance == null) return;
		instance.panelRow.removeAll();
		for(PinPoint pinPoint : Main.pinPointManager.getPinPoints()) {
			PinPointRow row = new PinPointRow(pinPoint);
			row.setPreferredSize(new Dimension(instance.getWidth(), 30));
			row.setBounds(0, 0, instance.getWidth(), 30);
			instance.panelRow.add(row);
		}
	}
	
	public static void removeRow(PinPointRow row) {
		instance.panelRow.remove(row);
	}
	
}
