package dk.itu.n.danmarkskort.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dk.itu.n.danmarkskort.Main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WindowLauncher extends JFrame {
	private JPanel contentPane;
	private List<Path> binFiles;
	private JLabel labelSelectedFile;
	private String selectedFilePath;
	private JButton buttonLaunch;
	private JLabel labelSelectedFileHeader;
	
	public WindowLauncher() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Style style = new Style();
		setTitle("Launcher");
		setIconImage(style.frameIcon());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(style.margin(), 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		GridBagConstraints gc = new GridBagConstraints();

		gc.gridy = 0;
		JLabel labelHeaderName = new JLabel(Main.APP_NAME);
		labelHeaderName.setFont(style.largeHeadlineSpacing());
		labelHeaderName.setHorizontalAlignment(SwingConstants.CENTER);
        labelHeaderName.setIcon(style.logo());
        labelHeaderName.setIconTextGap(10);
		panel.add(labelHeaderName, gc);

		gc.gridy = 1;
		JLabel labelHeaderVersion = new JLabel("Version " + Main.APP_VERSION);
		labelHeaderVersion.setFont(style.mediumHeadline());
		labelHeaderVersion.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(labelHeaderVersion, gc);


		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 15));
		
		JPanel panelParsedFiles = new JPanel();
		centerPanel.add(panelParsedFiles, BorderLayout.CENTER);
		panelParsedFiles.setLayout(new BorderLayout(0, 5));
		
		JLabel labelParsedMaps = new JLabel("Parsed Map Files");
		panelParsedFiles.add(labelParsedMaps, BorderLayout.NORTH);
		labelParsedMaps.setHorizontalAlignment(SwingConstants.CENTER);
		labelParsedMaps.setFont(style.mediumHeadline());
		
		labelSelectedFile = new JLabel();
		
		JList<String> binFilesList = new JList(getParsedFiles());
		binFilesList.setFont(style.normalText());
		binFilesList.setSelectionBackground(style.launcherSelectionBG());
		binFilesList.setSelectionForeground(style.panelBG());
		binFilesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultListCellRenderer dlcr = (DefaultListCellRenderer)binFilesList.getCellRenderer();
		dlcr.setHorizontalAlignment(SwingConstants.CENTER);
		dlcr.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		binFilesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				labelSelectedFile.setText(binFilesList.getSelectedValue());
				selectedFilePath = binFiles.get(binFilesList.getSelectedIndex()).toString();
				if(!buttonLaunch.isEnabled()) enableLaunchButton(true);
				if(e.getClickCount() == 2) launch();
			}
		});
		binFilesList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(!binFilesList.isSelectionEmpty()) launch();
			}
		});
		
		JScrollPane scroll = new JScrollPane(binFilesList);
		scroll.setBorder(null);
		panelParsedFiles.add(scroll);
		
		JPanel panelCurrentFile = new JPanel();
		panelCurrentFile.setBorder(new EmptyBorder(5, 0, 0, 0));
		centerPanel.add(panelCurrentFile, BorderLayout.SOUTH);
		panelCurrentFile.setLayout(new BorderLayout(0, 5));
		
		labelSelectedFileHeader = new JLabel("Selected File");
		labelSelectedFileHeader.setHorizontalAlignment(SwingConstants.CENTER);
		labelSelectedFileHeader.setFont(style.normalText());
		panelCurrentFile.add(labelSelectedFileHeader, BorderLayout.NORTH);
		
		buttonLaunch = new JButton("Launch");
		if(selectedFilePath == null) {
			selectedFilePath = "resources/default.bin";
			labelSelectedFile.setText("default.bin (default)");
		}
		
		labelSelectedFile.setOpaque(true);
		labelSelectedFile.setFont(style.mediumHeadline());
		labelSelectedFile.setHorizontalAlignment(SwingConstants.CENTER);
		panelCurrentFile.add(labelSelectedFile);
		
		JPanel panelBottom = new JPanel();
		panelBottom.setBorder(BorderFactory.createEmptyBorder(30, 0 ,0 ,0));
		panelBottom.setPreferredSize(new Dimension(10, 70));
		contentPane.add(panelBottom, BorderLayout.SOUTH);
		panelBottom.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		buttonLaunch.addActionListener(e -> launch());
		buttonLaunch.setFont(style.mediumHeadline());
        buttonLaunch.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, style.panelBG()));
        buttonLaunch.setPreferredSize(new Dimension(buttonLaunch.getPreferredSize().width, 40));
        buttonLaunch.setBackground(style.launcherSelectionBG());
        buttonLaunch.setForeground(Color.BLACK);
        buttonLaunch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.gridx = 2;
		panelBottom.add(buttonLaunch, gbc);
		
		JButton buttonConfigure = new JButton("Options");
		buttonConfigure.addActionListener(e -> new WindowLauncherConfigure(this));
		buttonConfigure.setFont(style.mediumHeadline());
		buttonConfigure.setIcon(style.launcherOptionsIcon());
		buttonConfigure.setBackground(style.inputFieldBG());
		buttonConfigure.setForeground(style.panelTextColor());
		buttonConfigure.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonConfigure.setBorder(BorderFactory.createMatteBorder(1, 0, 0,0, style.panelBG()));
        buttonConfigure.setPreferredSize(new Dimension(buttonConfigure.getPreferredSize().width, 40));
        gbc.gridx = 0;
		panelBottom.add(buttonConfigure, gbc);

		JButton buttonLoadFile = new JButton("Load New File");
        gbc.gridx = 1;
		panelBottom.add(buttonLoadFile, gbc);
		buttonLoadFile.addActionListener(e -> loadFile());
		buttonLoadFile.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonLoadFile.setFont(style.mediumHeadline());
		buttonLoadFile.setIcon(style.launcherLoadIcon());
		buttonLoadFile.setBackground(style.inputFieldBG());
		buttonLoadFile.setForeground(style.panelTextColor());
		buttonLoadFile.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonLoadFile.setPreferredSize(new Dimension(buttonLoadFile.getPreferredSize().width, 40));
        buttonLoadFile.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, style.panelBG()));

        buttonLoadFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonLoadFile.setBackground(style.panelBG());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                buttonLoadFile.setBackground(style.inputFieldBG());
            }
        });

        buttonConfigure.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonConfigure.setBackground(style.panelBG());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                buttonConfigure.setBackground(style.inputFieldBG());
            }
        });

        buttonLaunch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonLaunch.setBackground(style.scrollBarThumbActive());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                buttonLaunch.setBackground(style.launcherSelectionBG());
            }
        });

		// Colors and stuff

		final Color outerColor = style.inputFieldBG();
		final Color middleColor = style.menuItemsBG();
		final Color innerColor = style.panelBG();
		
		contentPane.setBackground(outerColor);
		panel.setBackground(outerColor);
		centerPanel.setBackground(outerColor);
		panelParsedFiles.setBackground(outerColor);
		binFilesList.setBackground(innerColor);
		scroll.setBackground(innerColor);
		panelCurrentFile.setBackground(outerColor);
		labelSelectedFile.setBackground(middleColor);
		panelBottom.setBackground(outerColor);
		
		labelHeaderName.setForeground(style.panelTextColor());
		labelHeaderVersion.setForeground(style.launcherVersionText());
		labelParsedMaps.setForeground(style.launcherVersionText());
		labelSelectedFileHeader.setForeground(style.launcherVersionText());
		binFilesList.setForeground(new Color(200, 200, 200));
		labelSelectedFile.setForeground(new Color(200, 200, 200));
		
		setPreferredSize(new Dimension(550, 550));
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void launch() {
		dispose();
		Main.launch(new String[]{selectedFilePath});
	}

	public void setSelectedFile(String fileName) {
		labelSelectedFileHeader.setText("Selected File");
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
		if(Main.production) {
			selectedFilePath = "/resources/default.bin";
			labelSelectedFile.setText("default.bin (default)");
		}		
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


	private void loadFile() {
		JFileChooser fc = viewFileChooser("Load View To File", "Load");

		int fcVal = fc.showOpenDialog(this);
		if (fcVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			setSelectedFile(file.getAbsolutePath());
		}
	}

	private JFileChooser viewFileChooser(String dialogTitle, String approveBtnTxt){
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(dialogTitle);
		fc.setApproveButtonText(approveBtnTxt);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileNameExtensionFilter("Map Files", "osm", "zip", "bin"));

		fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		return fc;
	}
}
