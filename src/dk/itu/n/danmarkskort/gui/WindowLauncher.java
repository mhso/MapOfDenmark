package dk.itu.n.danmarkskort.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import dk.itu.n.danmarkskort.Main;

import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import java.awt.Color;

public class WindowLauncher extends JFrame {
	private JPanel contentPane;
	private List<Path> binFiles;
	private JLabel labelSelectedFile;
	private String selectedFilePath;
	private JButton buttonLaunch;
	
	public WindowLauncher() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		Style style = new Style();
		setTitle(Main.APP_NAME + " Launcher");
		setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icons/map-icon.png"));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(style.panelBG());
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(style.menuItemsBG());
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel labelHeaderName = new JLabel(Main.APP_NAME);
		labelHeaderName.setForeground(Color.WHITE);
		labelHeaderName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		labelHeaderName.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(labelHeaderName);
		
		JLabel labelHeaderVersion = new JLabel("Version " + Main.APP_VERSION);
		labelHeaderVersion.setForeground(Color.WHITE);
		labelHeaderVersion.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelHeaderVersion.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(labelHeaderVersion);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(style.menuItemsBG());
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 5));
		
		JPanel panelParsedFiles = new JPanel();
		panelParsedFiles.setBackground(style.menuItemsBG());
		centerPanel.add(panelParsedFiles, BorderLayout.CENTER);
		panelParsedFiles.setLayout(new BorderLayout(0, 5));
		
		JLabel labelParsedMaps = new JLabel("Parsed Map Files");
		labelParsedMaps.setForeground(new Color(215, 215, 215));
		panelParsedFiles.add(labelParsedMaps, BorderLayout.NORTH);
		labelParsedMaps.setHorizontalAlignment(SwingConstants.CENTER);
		labelParsedMaps.setFont(new Font("Tahoma", Font.PLAIN, 17));
		
		labelSelectedFile = new JLabel();
		
		JList<String> binFilesList = new JList(getParsedFiles());
		binFilesList.setFont(new Font("Tahoma", Font.PLAIN, 15));
		binFilesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		binFilesList.setBackground(style.menuContentBG());
		DefaultListCellRenderer dlcr = (DefaultListCellRenderer)binFilesList.getCellRenderer();
		dlcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		binFilesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				labelSelectedFile.setText(binFilesList.getSelectedValue());
				selectedFilePath = binFiles.get(binFilesList.getSelectedIndex()).toString();
				if(!buttonLaunch.isEnabled()) enableLaunchButton(true);
			}
		});
		
		JScrollPane scroll = new JScrollPane(binFilesList);
		scroll.setBackground(style.menuItemsBG());
		panelParsedFiles.add(scroll);
		
		JPanel panelCurrentFile = new JPanel();
		panelCurrentFile.setBackground(style.menuItemsBG());
		centerPanel.add(panelCurrentFile, BorderLayout.NORTH);
		panelCurrentFile.setLayout(new BorderLayout(0, 5));
		
		JLabel labelSelectedFileHeader = new JLabel("Selected File");
		labelSelectedFileHeader.setForeground(new Color(215, 215, 215));
		labelSelectedFileHeader.setHorizontalAlignment(SwingConstants.CENTER);
		labelSelectedFileHeader.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panelCurrentFile.add(labelSelectedFileHeader, BorderLayout.NORTH);
		
		buttonLaunch = new JButton("Launch");
		
		if (binFiles.isEmpty()) {
			labelSelectedFileHeader.setText("No map files found, load a new one from 'Configure' menu.");
			enableLaunchButton(false);
		}
		else if(selectedFilePath == null) {
			labelSelectedFile.setText("Default Map Not Found.");
			enableLaunchButton(false);
		}
		
		labelSelectedFile.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		labelSelectedFile.setOpaque(true);
		labelSelectedFile.setBackground(style.menuContentBG());
		labelSelectedFile.setFont(new Font("Tahoma", Font.BOLD, 15));
		labelSelectedFile.setHorizontalAlignment(SwingConstants.CENTER);
		panelCurrentFile.add(labelSelectedFile);
		
		JPanel panelBottom = new JPanel();
		panelBottom.setBorder(new EmptyBorder(2, 2, 2, 2));
		panelBottom.setBackground(style.menuItemsBG());
		panelBottom.setPreferredSize(new Dimension(10, 50));
		contentPane.add(panelBottom, BorderLayout.SOUTH);
		panelBottom.setLayout(new BorderLayout(0, 0));
		
		buttonLaunch.addActionListener(e -> {
			dispose();
			Main.launch(new String[]{selectedFilePath});
		});
		buttonLaunch.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panelBottom.add(buttonLaunch, BorderLayout.EAST);
		
		JButton buttonConfigure = new JButton("Configure");
		buttonConfigure.addActionListener(e -> new WindowLauncherConfigure(this));
		buttonConfigure.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panelBottom.add(buttonConfigure, BorderLayout.WEST);
		
		setPreferredSize(new Dimension(550, 500));
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setSelectedFile(String fileName) {
		labelSelectedFile.setText(fileName);
		selectedFilePath = fileName;
		if(!buttonLaunch.isEnabled()) enableLaunchButton(true);
	}
	
	private void enableLaunchButton(boolean enable) {
		buttonLaunch.setEnabled(enable);
		if(!enable) buttonLaunch.setToolTipText("Load or Select a map to Launch.");
		else buttonLaunch.setToolTipText("Click to Launch the Application!");
	}
	
	private String[] getParsedFiles() {
		binFiles = new ArrayList<>();
		try {
			for(Path entry : Files.newDirectoryStream(Paths.get("parsedOSMFiles"))) {
				for(Path binFile : Files.newDirectoryStream(entry)) {
					if(!binFile.toFile().getName().equals("pinpoints.bin")) {
						binFiles.add(binFile);
						if(binFile.toFile().getName().equals(Main.userPreferences.getDefaultMapFile())) {
							selectedFilePath = binFile.toString();
							labelSelectedFile.setText(binFile.toFile().getName() + " (default)");
						}
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		String[] results = new String[binFiles.size()];
		for(int i = 0; i < binFiles.size(); i++) {
			results[i] = binFiles.get(i).getFileName().toString();
		}
		return results;
	}
}
