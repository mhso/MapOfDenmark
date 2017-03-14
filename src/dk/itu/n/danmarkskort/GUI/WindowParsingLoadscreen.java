package dk.itu.n.danmarkskort.GUI;

import java.awt.BorderLayout;
import java.util.Random;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.File;

import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import java.awt.Component;
import javax.swing.Box;

public class WindowParsingLoadscreen extends JFrame {

	private JPanel contentPane;
	private JProgressBar progressBar;
	private JPanel panel;
	private Component rigidArea;
	private JLabel labelStatus;
	public static void main(String[] args) {
		WindowParsingLoadscreen frame = new WindowParsingLoadscreen();
		frame.testProgressBar();
	}
	
	/**
	 * Create the frame.
	 */
	public WindowParsingLoadscreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(224, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 63));
		setContentPane(contentPane);
		
		JLabel labelHeader = new JLabel("Parsing Data...");
		labelHeader.setForeground(Color.RED);
		labelHeader.setHorizontalAlignment(SwingConstants.CENTER);
		labelHeader.setFont(new Font("Monotype Corsiva", Font.PLAIN, 50));
		contentPane.add(labelHeader, BorderLayout.NORTH);
		
		panel = new JPanel();
		panel.setOpaque(false);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 5));
		
		progressBar = new JProgressBar();
		panel.add(progressBar);
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(146, 144));
		progressBar.setBackground(new Color(176, 224, 230));
		progressBar.setForeground(new Color(0, 128, 0));
		progressBar.setBorder(new LineBorder(new Color(0, 0, 0)));
		progressBar.setIndeterminate(true);
		
		labelStatus = new JLabel("  ");
		labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(labelStatus, BorderLayout.SOUTH);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		contentPane.add(rigidArea, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void testProgressBar() {
		/*  
		 * Testing the progress bar with random values.
		 * Real values would be calculated by: 
		 * 1. Getting file size in MB, f.x. small.osm = 182 MB.
		 * 2. Getting a current line number while parsing the XML file (using .sax location listeners).
		 * 3. Getting current MB parsed = currentLine * ~ 0,00006.
		 * 4. Getting percent of total parsed data = currentMB/fileSize*100.
		 * 5. ???
		 * 6. PROFIT!!!
		 * map = 3.013.103 lines, 182 MB
		 * ratio = 6,0402847164534368722210956611838e-5
		 * small = 156.721 lines, 9,42 MB
		 * ratio = 6,0106814019818658635409421838809e-5
		 */
		
		File file = new File("map.osm");
		long b = file.length();
		long kb = b/1024;
		long mb = kb/1024;
		System.out.println("File Size: " + mb + " MB");
		
		Runnable r = new ProgressTest();
		r.run();
	}
	
	private class ProgressTest implements Runnable {
		private String[] statues = {"Reading Nodes...", "Loading Icons...", "Loading Addresses...", "Saving Addresses...", "Calculating Roads..."};
		private int value = 0;
		
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			progressBar.setIndeterminate(false);
			Random random = new Random();
			while(value < 100) {
				value++;
				progressBar.setValue(value);
				if(random.nextInt(100) < 15) labelStatus.setText(statues[random.nextInt(statues.length)]);
				try {
					Thread.sleep(100 + random.nextInt(300));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(WindowParsingLoadscreen.this, "Parsing Finished!", "Finished.", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
