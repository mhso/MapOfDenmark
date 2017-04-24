package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSplitPane;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.border.EmptyBorder;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.gui.map.PinPoint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PinPointRow extends JPanel {
	
	private JButton btnPan;
	private JButton btnDelete;
	private JLabel name;
	private PinPoint pinPoint;
	private Style style;
	
	/**
	 * Create the panel.
	 */
	public PinPointRow(PinPoint pinPoint) {
		style = new Style();
		this.pinPoint = pinPoint;
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		PinPointRow self = this;
		
		btnPan = new JButton("");
		btnPan.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Main.pinPointManager.panToLocation(pinPoint);
			}
		});
		btnPan.setBackground(Color.WHITE);
		btnPan.setIcon(new ImageIcon("/home/wituz/Downloads/placeholder.png"));
		panel.add(btnPan);
		
		btnDelete = new JButton("");
		btnDelete.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Main.pinPointManager.removePinPoint(pinPoint);
				Main.mainPanel.repaint();
				PinPointPage.removeRow(self);
			}
		});
		btnDelete.setBackground(Color.WHITE);
		btnDelete.setIcon(new ImageIcon("/home/wituz/Downloads/trash.png"));
		panel.add(btnDelete);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.setBackground(style.menuContentBG());
		
		name = new JLabel(pinPoint.getName());
		name.setBorder(new EmptyBorder(0, 5, 0, 0));
		panel_1.add(name);

	}

}
