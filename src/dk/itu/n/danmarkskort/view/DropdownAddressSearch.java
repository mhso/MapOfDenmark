package dk.itu.n.danmarkskort.view;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JFrame;

public class DropdownAddressSearch extends JPopupMenu {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new DropdownAddressSearch();
			}
		});
	}
	
	private void createTestFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pane = (JPanel)frame.getContentPane();
		pane.setLayout(new BorderLayout());
		
		JButton activateButton = new JButton("Test");
		activateButton.addActionListener(e -> showMenu(activateButton));
		pane.add(activateButton);
		
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	/**
	 * Create the panel.
	 */
	public DropdownAddressSearch() {
		createTestFrame();
		
		setPopupSize(new Dimension(150, 200));
		System.out.println(getSize());
		
		JButton btnResult = new JButton("Result1");
		setSharedButtonAttributes(btnResult);
		add(btnResult);
		
		JButton btnResult_1 = new JButton("Result2");
		setSharedButtonAttributes(btnResult_1);
		add(btnResult_1);
		
		JButton btnLongresult = new JButton("LongResult123");
		setSharedButtonAttributes(btnLongresult);
		add(btnLongresult);
	}
	
	private void setSharedButtonAttributes(JButton dropdownButton) {
		dropdownButton.setFocusPainted(false);
		dropdownButton.setBackground(getBackground());
		dropdownButton.setSize(getWidth(), dropdownButton.getHeight());
	}

	public void showMenu(Component source) {
		Point loc = source.getLocation();
		show(source, loc.x, loc.y);
	}
}
