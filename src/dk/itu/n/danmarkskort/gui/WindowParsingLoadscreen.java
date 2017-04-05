package dk.itu.n.danmarkskort.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.io.File;

import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.LineBorder;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.backend.OSMParserListener;

import java.awt.Component;

import javax.swing.Box;

public class WindowParsingLoadscreen extends JFrame implements OSMParserListener {
	private static final long serialVersionUID = -6160623215890001556L;
	private JPanel contentPane;
	private JProgressBar progressBar;
	private JPanel panel;
	private Component rigidArea;
	private JLabel labelStatus;
	private int lineCountHundreds;
	private long fileLength;
	private boolean showObjectString = true;
	
	public static void main(String[] args) {
		new WindowParsingLoadscreen();
	}
	
	public void initialize(String fileName) {
		getFileLength(fileName);
		setTitle("Parsing Data");
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
		
		labelStatus = new JLabel("Preparing To Parse File...");
		labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(labelStatus, BorderLayout.SOUTH);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		contentPane.add(rigidArea, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void getFileLength(String fileName) {
		File file = new File(fileName);
		fileLength = Util.getNumberOfLines(file);
		long b = file.length();
		long kb = b/1024;
		long mb = kb/1024;
		Main.log("File Name: " + file.getName()); 
		Main.log("File Size: " + mb + " MB");
	}
	
	private void setProgressPercent() {
		double currentLine = lineCountHundreds * 100;
		double percent = (currentLine/fileLength) * 100;
		progressBar.setValue((int)percent);
	}
	
	@Override
	public void onParsingStarted() {
		progressBar.setIndeterminate(false);
	}

	@Override
	public void onLineCountThousand() {
		lineCountHundreds++;
		setProgressPercent();
		showObjectString = true;
	}

	@Override
	public void onParsingFinished() {
		dispose();
	}

	@Override
	public void onParsingGotItem(Object parsedItem) {
		if(showObjectString) {
			labelStatus.setText(parsedItem.toString());
			showObjectString = false;
		}
	}
}
