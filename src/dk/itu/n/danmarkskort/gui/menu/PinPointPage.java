package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.map.PinPoint;

import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

public class PinPointPage extends JPanel {

	public static PinPointPage instance;
	public JPanel panel;
	
	public PinPointPage() {
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblDineInteressepunkter = new JLabel("Dine interessepunkter");
		lblDineInteressepunkter.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblDineInteressepunkter, BorderLayout.NORTH);
		
		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.SOUTH);
		
		for(PinPoint pinPoint : Main.pinPointManager.getPinPoints()) {
			PinPointRow row = new PinPointRow(pinPoint);
			row.setPreferredSize(new Dimension(getWidth(), 30));
			row.setBounds(0, 0, getWidth(), 30);
			panel.add(row);
		}
		
		instance = this;

	}

	public static void update() {
		if(instance == null) return;
		instance.panel.removeAll();
		for(PinPoint pinPoint : Main.pinPointManager.getPinPoints()) {
			PinPointRow row = new PinPointRow(pinPoint);
			row.setPreferredSize(new Dimension(instance.getWidth(), 30));
			row.setBounds(0, 0, instance.getWidth(), 30);
			instance.panel.add(row);
		}
	}
	
	public static void removeRow(PinPointRow row) {
		instance.panel.remove(row);
	}
	
}
