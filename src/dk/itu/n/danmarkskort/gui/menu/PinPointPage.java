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

public class PinPointPage extends JPanel {

	public PinPointPage() {
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblDineInteressepunkter = new JLabel("Dine interessepunkter");
		lblDineInteressepunkter.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblDineInteressepunkter, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		for(PinPoint pinPoint : Main.pinPointManager.getPinPoints()) {
			PinPointRow row = new PinPointRow(pinPoint);
			row.setPreferredSize(new Dimension(getWidth(), 30));
			row.setBounds(0, 0, getWidth(), 30);
			panel.add(row);
		}

	}

}
