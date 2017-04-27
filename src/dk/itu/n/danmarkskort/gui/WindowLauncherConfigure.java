package dk.itu.n.danmarkskort.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;

public class WindowLauncherConfigure extends JDialog {

	private JPanel contentPane;
	private WindowLauncher parent;

	public WindowLauncherConfigure(WindowLauncher parent) {
		super(parent);
		this.parent = parent;
		Style style = new Style();
		setModal(true);
		setBounds(100, 100, 450, 300);
		setIconImage(style.frameIcon());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);
		
		JLabel labelLaunchConfigurations = new JLabel("Launch Configurations");
		labelLaunchConfigurations.setForeground(Color.WHITE);
		labelLaunchConfigurations.setOpaque(true);
		labelLaunchConfigurations.setFont(new Font("Tahoma", Font.PLAIN, 25));
		labelLaunchConfigurations.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelLaunchConfigurations, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(new Color(130, 173, 198));
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		JPanel panelBinary = new JPanel();
		panelBinary.setPreferredSize(new Dimension(10, 50));
		panelBinary.setBackground(style.panelBG());
		panelBinary.setBorder(new EmptyBorder(0, 5, 0, 5));
		centerPanel.add(panelBinary);
		panelBinary.setLayout(new BorderLayout(0, 0));
		
		JLabel labelBinary = new JLabel("Enable Binary Save/Load");
		labelBinary.setForeground(new Color(200, 200, 200));
		labelBinary.setBorder(null);
		labelBinary.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelBinary.setHorizontalAlignment(SwingConstants.CENTER);
		panelBinary.add(labelBinary, BorderLayout.WEST);
		
		CustomToggleButton buttonBinary = style.toggleButton();
		buttonBinary.addActionListener(e -> Main.binaryfile = !buttonBinary.isOn());
		buttonBinary.setOn(Main.binaryfile);
		panelBinary.add(buttonBinary, BorderLayout.EAST);
		
		JPanel panelReparse = new JPanel();
		panelReparse.setPreferredSize(new Dimension(10, 50));
		panelReparse.setBackground(style.panelBG());
		panelReparse.setBorder(new EmptyBorder(0, 5, 0, 5));
		centerPanel.add(panelReparse);
		panelReparse.setLayout(new BorderLayout(0, 0));
		
		JLabel labelReparse = new JLabel("Force Reparsing");
		labelReparse.setForeground(new Color(200, 200, 200));
		labelReparse.setBorder(null);
		labelReparse.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelReparse.setHorizontalAlignment(SwingConstants.CENTER);
		panelReparse.add(labelReparse, BorderLayout.WEST);
		
		CustomToggleButton buttonReparse = style.toggleButton();
		buttonReparse.addActionListener(e -> {
			Main.userPreferences.setForceParsing(!buttonReparse.isOn());
			Main.forceParsing = !buttonReparse.isOn();
			Util.writeObjectToFile(Main.userPreferences, DKConstants.USERPREF_PATH);
		});
		buttonReparse.setOn(Main.userPreferences.isForcingParsing());
		panelReparse.add(buttonReparse, BorderLayout.EAST);
		
		centerPanel.add(Box.createRigidArea(new Dimension(centerPanel.getWidth(), 10)));
		
		JPanel panelBottom = new JPanel();
		panelBottom.setBackground(style.menuItemsBG());
		panelBottom.setPreferredSize(new Dimension(10, 55));
		centerPanel.add(panelBottom);
		
		contentPane.setBackground(new Color(130, 173, 198));
		labelLaunchConfigurations.setBackground(style.menuItemsBG());
		
		setPreferredSize(new Dimension(400, 320));
		setLocationRelativeTo(null);
		
		pack();
		setVisible(true);
	}
}
