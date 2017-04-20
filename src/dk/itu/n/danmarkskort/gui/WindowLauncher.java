package dk.itu.n.danmarkskort.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dk.itu.n.danmarkskort.Main;

import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.Font;

public class WindowLauncher extends JFrame {

	private JPanel contentPane;

	public WindowLauncher() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel labelHeaderName = new JLabel(Main.APP_NAME);
		labelHeaderName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		labelHeaderName.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(labelHeaderName);
		
		JLabel labelHeaderVersion = new JLabel("Version " + Main.APP_VERSION);
		labelHeaderVersion.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelHeaderVersion.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(labelHeaderVersion);
		
		setPreferredSize(new Dimension(500, 400));
		setLocationRelativeTo(null);
		
		pack();
		setVisible(true);
	}

}
