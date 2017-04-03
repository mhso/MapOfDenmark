package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.AbstractListModel;

public class PinPointPage extends JPanel {

	public PinPointPage() {
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblDineInteressepunkter = new JLabel("Dine interessepunkter");
		lblDineInteressepunkter.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblDineInteressepunkter, BorderLayout.NORTH);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"test", "test", "test", "test", "test"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		add(list, BorderLayout.CENTER);

	}

}
