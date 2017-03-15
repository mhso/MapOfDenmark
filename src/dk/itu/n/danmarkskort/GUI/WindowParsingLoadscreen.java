package dk.itu.n.danmarkskort.GUI;

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

import dk.itu.n.danmarkskort.backend.OSMParserListener;
import dk.itu.n.danmarkskort.models.ParsedObject;

import java.awt.Component;
import javax.swing.Box;

public class WindowParsingLoadscreen extends JFrame implements OSMParserListener {

	private JPanel contentPane;
	private JProgressBar progressBar;
	private JPanel panel;
	private Component rigidArea;
	private JLabel labelStatus;
	private int lineCountHundreds;
	private long fileSizeMB;
	private boolean showObjectString = true;
	
	public static void main(String[] args) {
		WindowParsingLoadscreen frame = new WindowParsingLoadscreen();
	}
	
	public void initialize(String fileName) {
		setFileSize(fileName);
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
		
		labelStatus = new JLabel("  ");
		labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(labelStatus, BorderLayout.SOUTH);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		contentPane.add(rigidArea, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void setFileSize(String fileName) {
		File file = new File(fileName);
		long b = file.length();
		long kb = b/1024;
		fileSizeMB = kb/1024;
		System.out.println("File Size: " + fileSizeMB + " MB");
	}
	
	private void setProgressPercent() {
		double currentMB = (lineCountHundreds * 0.006);
		double percent = (currentMB/fileSizeMB) * 100;
		progressBar.setValue((int)percent);
	}
	
	@Override
	public void onParsingStarted() {
		progressBar.setIndeterminate(false);
	}

	@Override
	public void onParsingGotObject(ParsedObject parsedObject) {
		if(showObjectString) {
			labelStatus.setText(parsedObject.toString());
			showObjectString = false;
		}
	}

	@Override
	public void onLineCountHundred() {
		lineCountHundreds++;
		setProgressPercent();
		showObjectString = true;
	}

	@Override
	public void onParsingFinished() {
		System.out.println("Done");
		dispose();
	}
}
