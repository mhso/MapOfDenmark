package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;
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
		style = Main.style;
		this.pinPoint = pinPoint;
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		panel.setBackground(style.menuContentBG());
		PinPointRow self = this;
		
		btnPan = style.pinPointPanButton();
		btnPan.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Main.pinPointManager.panToLocation(pinPoint);
			}
		});
		panel.add(btnPan);
		
		btnDelete = style.pinPointDeleteButton();
		btnDelete.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Main.pinPointManager.removePinPoint(pinPoint);
				Main.mainPanel.repaint();
				PinPointPage.removeRow(self);
			}
		});

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
