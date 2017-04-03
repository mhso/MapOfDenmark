package dk.itu.n.danmarkskort.mapgfx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ThemeCreatorMain extends JFrame {
	private static final long serialVersionUID = 8618838356916069134L;
	private JPanel contentPane;
	private int themeCount;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ThemeCreatorMain frame = new ThemeCreatorMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public ThemeCreatorMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Graphic Theme Editor");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);
		
		JPanel pickThemeMainPanel = new JPanel();
		pickThemeMainPanel.setLayout(new BorderLayout(0, 15));
		contentPane.add(pickThemeMainPanel);
		
		JLabel lblChooseATheme = new JLabel("Choose a Theme to edit");
		lblChooseATheme.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblChooseATheme.setHorizontalAlignment(SwingConstants.CENTER);
		pickThemeMainPanel.add(lblChooseATheme, BorderLayout.NORTH);
		
		JPanel themePanel = new JPanel();
		themePanel.setLayout(new GridLayout(0, 1, 10, 10));
		themePanel.setBackground(Color.WHITE);
		
		JScrollPane scrollPane = new JScrollPane(themePanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getThemes(themePanel);
		pickThemeMainPanel.add(scrollPane, BorderLayout.CENTER);
		
		setPreferredSize(new Dimension(320, 500));
		
		pack();	
		
		setLocationRelativeTo(null);
	}
	
	private void getThemes(JPanel themePanel) {
		Path dir = Paths.get("resources");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.XML")) {
			int i = 0;
	           for (Path entry: stream) {
	        	   String fileName = entry.toString().substring(10, entry.toString().length()-4);
	        	   if(!fileName.startsWith("Theme")) continue;
	        	   JButton button = new JButton(fileName);
	        	   button.addActionListener(new ButtonListener(button));
	        	   button.setBackground(Color.WHITE);
	        	   button.setHorizontalAlignment(SwingConstants.CENTER);
	        	   button.setFont(new Font("Tahoma", Font.PLAIN, 18));
	        	   themePanel.add(button);
	        	   i++;
	           }
	           themeCount = i;
	       } catch (IOException | DirectoryIteratorException ex) {
	           ex.printStackTrace();
	       }
	}
	
	private void themeSelected(String fileName) {
		String selectedFile = "resources/"+fileName+".XML";
		dispose();
		new ThemeCreator(selectedFile);
	}
	
	public int getThemeCount() {
		return themeCount;
	}
	
	private class ButtonListener implements ActionListener {
		private JButton sourceButton;
		
		public ButtonListener(JButton sourceButton) {
			this.sourceButton = sourceButton;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			themeSelected(sourceButton.getText());
		}
	}
}
