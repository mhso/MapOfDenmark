package dk.itu.n.danmarkskort.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import dk.itu.n.danmarkskort.Main;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.Component;

public class WindowLauncherConfigure extends JDialog {

	private JPanel contentPane;
	private WindowLauncher parent;

	public WindowLauncherConfigure(WindowLauncher parent) {
		super(parent);
		this.parent = parent;
		Style style = new Style();
		setModal(true);
		setBounds(100, 100, 450, 300);
		setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icons/map-icon.png"));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);
		
		JLabel lblLaunchConfigurations = new JLabel("Launch Configurations");
		lblLaunchConfigurations.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblLaunchConfigurations.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblLaunchConfigurations, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 0, 50, 0));
		centerPanel.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel labelReparse = new JLabel("Force Reparsing");
		labelReparse.setBorder(null);
		labelReparse.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelReparse.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(labelReparse, BorderLayout.WEST);
		
		CustomToggleButton buttonReparse = style.toggleButton();
		buttonReparse.addActionListener(e -> {
			Main.userPreferences.setForceParsing(!buttonReparse.isOn());
			Main.forceParsing = !buttonReparse.isOn();
		});
		buttonReparse.setOn(Main.userPreferences.isForcingParsing());
		panel.add(buttonReparse, BorderLayout.EAST);
		
		JButton buttonLoadFile = new JButton("Load New File");
		buttonLoadFile.addActionListener(e -> loadFile());
		buttonLoadFile.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonLoadFile.setFont(new Font("Tahoma", Font.PLAIN, 20));
		centerPanel.add(buttonLoadFile);
		
		setPreferredSize(new Dimension(400, 300));
		setLocationRelativeTo(null);
		
		pack();
		setVisible(true);
	}

	private void loadFile() {
		JFileChooser fc = viewFileChooser("Load View To File", "Load");
		
		int fcVal = fc.showOpenDialog(WindowLauncherConfigure.this);
		if (fcVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			parent.setSelectedFile(file.getAbsolutePath());
			dispose();
        }
    }
	
	private JFileChooser viewFileChooser(String dialogTitle, String approveBtnTxt){
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(dialogTitle);
		fc.setApproveButtonText(approveBtnTxt);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileNameExtensionFilter("Map Files", "osm", "zip", "bin"));
		fc.setCurrentDirectory(new File("D:/BFST17_DK_Kort"));
		return fc;
	}
}
