package dk.itu.n.danmarkskort.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.backend.OSMParserListener;
import dk.itu.n.danmarkskort.models.ParsedObject;

import java.awt.Component;

import javax.imageio.ImageIO;
import javax.swing.Box;

public class WindowParsingLoadscreenNew extends JFrame implements OSMParserListener {

	private JPanel contentPane;
	private JPanel panel;
	private JLabel labelStatus;
	private BufferedImage mainImage;
	private long fileSize;
	private boolean showObjectString = true;
	private double currentPercent;
	private JLabel labelPercent;
	
	public static void main(String[] args) {
		WindowParsingLoadscreen frame = new WindowParsingLoadscreen();
	}
	
	public void initialize(String fileName) {
		getFileSize(fileName);
		setTitle("Parsing Data");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new BGImage();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 63));
		setContentPane(contentPane);
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(120, 5, 5, 100));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 5));
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new EmptyBorder(5, 30, 5, 5));
		statusPanel.setLayout(new BorderLayout(5, 5));
		statusPanel.setOpaque(false);
		contentPane.add(statusPanel, BorderLayout.SOUTH);
		
		JPanel percentPanel = new JPanel();
		percentPanel.setLayout(new BorderLayout(5, 5));
		percentPanel.setOpaque(false);
		panel.add(percentPanel, BorderLayout.EAST);
		
		labelPercent = new JLabel("0 %");
		labelPercent.setForeground(Color.RED);
		labelPercent.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPercent.setFont(new Font("Monotype Corsiva", Font.PLAIN, 50));
		percentPanel.add(labelPercent, BorderLayout.NORTH);
		
		labelStatus = new JLabel("Preparing To Parse File...");
		labelStatus.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(labelStatus, BorderLayout.WEST);
		
		setPreferredSize(new Dimension(mainImage.getWidth(), mainImage.getHeight()+80));
		
		pack();
		
		setPreferredSize(new Dimension(mainImage.getWidth(), mainImage.getHeight()+80));
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void getFileSize(String fileName) {
		File file = new File(fileName);
		fileSize = Util.getFileSize(file);
		long kb = fileSize/1024;
		long mb = kb/1024;
		Main.log("File Name: " + file.getName()); 
		Main.log("File Size: " + mb + " MB");
	}
	
	private void setProgressPercent() {
		labelPercent.setText((int)currentPercent + " %");
		contentPane.repaint();
	}
	
	@Override
	public void onParsingStarted() {
		
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
		currentPercent++;
		setProgressPercent();
		showObjectString = true;
	}

	@Override
	public void onParsingFinished() {
		dispose();
	}
	
	private class BGImage extends JPanel {
		private BufferedImage[] subImagesBlue = new BufferedImage[74];
		private BufferedImage[] subImagesRed = new BufferedImage[74];
		
		public BGImage() {
			createSubImages();
		}
		
		private void createSubImages() {
			BufferedImage copyImage = null;
			try {
				mainImage = ImageIO.read(new File("resources/parsingBG.png"));
				copyImage = new BufferedImage(mainImage.getColorModel(), mainImage.copyData(null), 
						mainImage.isAlphaPremultiplied(), null);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			subImagesBlue[0] = mainImage.getSubimage(0, 0, 
					mainImage.getWidth(), 3);
			int y = mainImage.getHeight()-4;
			for(int i = 73; i >= 0; i--) {
				subImagesBlue[i] = mainImage.getSubimage(0, y, 
						mainImage.getWidth(), 4);
				BufferedImage redImage = copyImage.getSubimage(0, y, 
						mainImage.getWidth(), 4);
				for(int j = 0; j < redImage.getWidth(); j++) {
					for(int k = 0; k < redImage.getHeight(); k++) {
						if(redImage.getRGB(j, k) != Color.WHITE.getRGB()) redImage.setRGB(j, k, Color.RED.getRGB());
					}
				}
				subImagesRed[i] = redImage;
				y -= 8;
			}
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			double subImagesAmount = currentPercent* 74/100;
			g.drawImage(mainImage, 0, 0, null);
			int y = mainImage.getHeight()-4;
			for(int i = 0; i < (int)subImagesAmount; i++) {
				g.drawImage(subImagesRed[73-i], 0, y, null);
				y -= 8;
			}
		}
	}
}

