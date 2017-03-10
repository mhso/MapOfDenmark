package dk.itu.n.danmarkskort.view;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
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

	private JTextField txtField;
	
	private void createTestFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pane = (JPanel)frame.getContentPane();
		pane.setLayout(new BorderLayout());
		
		txtField = new JTextField(5);
		((AbstractDocument) txtField.getDocument()).setDocumentFilter(new SearchFilter());
		txtField.addActionListener(e -> showDropdown(txtField));
		pane.add(txtField);
		
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void addElement(String text) {
		JButton buttonResult = new JButton(text);
		setSharedButtonAttributes(buttonResult);
		add(buttonResult);
	}
	
	public void clearElements() {
		removeAll();
	}
	
	/**
	 * Create the panel.
	 */
	public DropdownAddressSearch() {
		createTestFrame();
		setFocusable(false);
		
		addElement("Result1");
		
		addElement("Result2");
		
		addElement("Long Result123");
	}
	
	private void setSharedButtonAttributes(JButton dropdownButton) {
		dropdownButton.addActionListener(e -> {
			txtField.setText(dropdownButton.getText());
			hideDropdown();
		});
		dropdownButton.setFocusPainted(false);
		dropdownButton.setBorderPainted(false);
		dropdownButton.setBackground(new Color(0, 0, 0, 0));
	}
	
	public void hideDropdown() {
		setVisible(false);
	}

	public void showDropdown(Component source) {
		Point loc = source.getLocation();
		show(source, loc.x, loc.y+source.getHeight());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		// We have to override this method and add a 'repaint()' to it, otherwise transparent
		// JButtons cause trouble.
		super.paintComponent(g);
		repaint();
	}
	
	private class SearchFilter extends DocumentFilter {
		@Override
		public void replace(FilterBypass fb, int offset, int length, String newText, 
				AttributeSet attr) throws BadLocationException {
			
			super.replace(fb, offset, length, newText, attr);
			
			showDropdown(txtField);
		}
	}
}