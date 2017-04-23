package dk.itu.n.danmarkskort.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Dimension;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.backend.InputStreamListener;
import dk.itu.n.danmarkskort.backend.OSMParserListener;

import javax.imageio.ImageIO;

public class WindowParsingLoadscreenNew extends JFrame implements OSMParserListener, InputStreamListener {
	private static final long serialVersionUID = 4049548769311961507L;
	private static final Color BAR_STATIC_COLOR = Color.LIGHT_GRAY;
	private static final Color BAR_LOADING_COLOR = new Color(0, 153, 0);
	
	private JPanel contentPane;
	private JPanel panel;
	private JLabel labelStatus;
	private BufferedImage mainImage;
	private long fileSize;
	private double currentPercent;
	private JLabel labelPercent;
	private int objectCount = 1000;
	private String fileName;
	
	public WindowParsingLoadscreenNew(String fileName) {
		this.fileName = fileName;
	}
	
	public void initialize() {
		setTitle("Parsing Data");
		setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icons/map-icon.png"));
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
	
	public void run() {
		getFileSize(fileName);
		initialize();
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
	public void onStreamStarted() {
		labelStatus.setText("Parsing File...");
	}

	@Override
	public void onParsingGotItem(Object parsedItem) {
		objectCount++;
		if(objectCount >= 1000) {
			labelStatus.setText(parsedItem.toString());
			objectCount = 0;
		}
	}
	
	@Override
	public void onPercent(int percentAmounth) {
		currentPercent += percentAmounth;
		setProgressPercent();
	}
	
	@Override
	public void onStreamEnded() {
		labelStatus.setText("Converting Data To KD-Trees...");
	}
	
	@Override
	public void onSetupDone() {
		dispose();
	}

	@Override
	public void onParsingFinished() {
		labelStatus.setText("Saving Data To Binary Format...");
	}
	
	private class BGImage extends JPanel {
		private BufferedImage[] subImages = new BufferedImage[74];
		private BufferedImage[] subImagesRed = new BufferedImage[74];
		
		public BGImage() {
			createSubImages();
		}
		
		private void createSubImages() {
			BufferedImage copyImage = null;
			try {
				if(Main.production) mainImage = ImageIO.read(getClass().getResourceAsStream("/parsingBG.png"));
				else mainImage = ImageIO.read(new File("resources/parsingBG.png"));
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			copyImage = new BufferedImage(mainImage.getColorModel(), mainImage.copyData(null), 
					mainImage.isAlphaPremultiplied(), null);
			subImages[0] = mainImage.getSubimage(0, 0, 
					mainImage.getWidth(), 3);
			int y = mainImage.getHeight()-4;
			for(int i = 73; i >= 0; i--) {
				subImages[i] = mainImage.getSubimage(0, y, 
						mainImage.getWidth(), 4);
				for(int j = 0; j < subImages[i].getWidth(); j++) {
					for(int k = 0; k < subImages[i].getHeight(); k++) {
						if(subImages[i].getRGB(j, k) != Color.WHITE.getRGB()) subImages[i].setRGB(j, k, BAR_STATIC_COLOR.getRGB());
					}
				}
				BufferedImage redImage = copyImage.getSubimage(0, y, 
						mainImage.getWidth(), 4);
				for(int j = 0; j < redImage.getWidth(); j++) {
					for(int k = 0; k < redImage.getHeight(); k++) {
						if(redImage.getRGB(j, k) != Color.WHITE.getRGB()) redImage.setRGB(j, k, BAR_LOADING_COLOR.getRGB());
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

