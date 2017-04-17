package dk.itu.n.danmarkskort.mapgfx;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.border.MatteBorder;

import dk.itu.n.danmarkskort.gui.CustomButton;
import dk.itu.n.danmarkskort.models.WayType;

import javax.swing.JSlider;

public class ThemeAddElement extends JFrame {
	private static final long serialVersionUID = 880226387273529455L;
	private JPanel contentPane;
	private JTextField fieldName;
	private JTextField fieldLayer;
	private JPanel panelProperties;
	private JTextField textFieldLineDash;
	private JTextField textFieldLineWidth;
	private String elementType;
	private Color innerColor;
	private Color outerColor;
	private String wayType;
	private List<String> elements;
	private JLabel errorLabel;
	private ThemeCreator tc;
	private JSlider zoomSlider;

	/**
	 * Create the frame.
	 */
	public ThemeAddElement(ThemeCreator tc, List<String> elements) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.tc = tc;
		this.elements = elements;
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		centerPanel.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel wayTypePanel = new JPanel();
		topPanel.add(wayTypePanel);
		wayTypePanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		wayTypePanel.setLayout(new GridLayout(0, 3, 0, 0));
		
		JPanel panelType = new JPanel();
		wayTypePanel.add(panelType);
		panelType.setBorder(new EmptyBorder(35, 40, 40, 40));
		panelType.setLayout(new BorderLayout(20, 15));
		
		JLabel labelType = new JLabel("Type");
		panelType.add(labelType, BorderLayout.NORTH);
		labelType.setHorizontalAlignment(SwingConstants.CENTER);
		
		JComboBox<String> comboBoxType = new JComboBox<>(new String[]{"Line", "Area", "Label", "Icon"});
		comboBoxType.setSelectedItem(null);
		comboBoxType.addActionListener(e -> {
			elementType = (String)comboBoxType.getSelectedItem();
			if(elementType.equals("Line")) lineSelected();
			else if(elementType.equals("Area")) areaSelected();
		});
		panelType.add(comboBoxType, BorderLayout.CENTER);
		
		JPanel panelWaytype = new JPanel();
		wayTypePanel.add(panelWaytype);
		panelWaytype.setBorder(new EmptyBorder(35, 10, 40, 10));
		panelWaytype.setLayout(new BorderLayout(25, 15));
		
		JLabel labelWaytypeName = new JLabel("Waytype Name");
		labelWaytypeName.setHorizontalAlignment(SwingConstants.CENTER);
		panelWaytype.add(labelWaytypeName, BorderLayout.NORTH);
		
		fieldName = new JTextField();
		fieldName.setPreferredSize(new Dimension(50, 20));
		fieldName.setEditable(false);
		fieldName.setHorizontalAlignment(SwingConstants.CENTER);
		panelWaytype.add(fieldName);
		
		JButton buttonSeeWaytypes = new CustomButton("resources/icons/menu.png", 0.5f, 0.8f, Color.BLACK);
		buttonSeeWaytypes.setPreferredSize(new Dimension(30, 25));
		buttonSeeWaytypes.addActionListener(e -> showWaytypesDialog());
		panelWaytype.add(buttonSeeWaytypes, BorderLayout.EAST);
		
		JPanel panelLayer = new JPanel();
		panelLayer.setBorder(new EmptyBorder(35, 10, 40, 10));
		wayTypePanel.add(panelLayer);
		panelLayer.setLayout(new BorderLayout(25, 15));
		
		JLabel labelLayer = new JLabel("Layer");
		labelLayer.setHorizontalAlignment(SwingConstants.CENTER);
		panelLayer.add(labelLayer, BorderLayout.NORTH);
		
		fieldLayer = new JTextField();
		fieldLayer.setPreferredSize(new Dimension(50, 20));
		fieldLayer.setHorizontalAlignment(SwingConstants.CENTER);
		panelLayer.add(fieldLayer);
		
		JPanel panelZoom = new JPanel();
		panelZoom.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		topPanel.add(panelZoom, BorderLayout.SOUTH);
		panelZoom.setLayout(new BorderLayout(5, 0));
		
		zoomSlider = new JSlider();
		zoomSlider.setPaintLabels(true);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setSnapToTicks(true);
		zoomSlider.setValue(10);
		zoomSlider.setMaximum(20);
		zoomSlider.setMinorTickSpacing(1);
		zoomSlider.setMajorTickSpacing(5);
		zoomSlider.setBorder(new EmptyBorder(5, 0, 5, 10));
		panelZoom.add(zoomSlider);
		
		JLabel lblZoomLevel = new JLabel("Zoom Level:");
		lblZoomLevel.setBorder(new EmptyBorder(5, 10, 5, 0));
		panelZoom.add(lblZoomLevel, BorderLayout.WEST);
		
		panelProperties = new JPanel();
		panelProperties.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panelProperties.setLayout(new CardLayout());
		centerPanel.add(panelProperties);
		
		JPanel emptyPanel = new JPanel();
		panelProperties.add(emptyPanel, "name_846857261777234");
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(new BorderLayout(50, 0));
		
		JButton buttonConfirm = new JButton("Confirm");
		buttonConfirm.addActionListener(e -> confirm());
		buttonConfirm.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bottomPanel.add(buttonConfirm, BorderLayout.EAST);
		
		JButton buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(e -> dispose());
		buttonCancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bottomPanel.add(buttonCancel, BorderLayout.WEST);
		
		errorLabel = new JLabel("");
		errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		errorLabel.setForeground(Color.RED);
		bottomPanel.add(errorLabel, BorderLayout.CENTER);
		
		createLinePanel();
		createAreaPanel();
		
		setPreferredSize(new Dimension(500, 475));
		
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void showError() {
		errorLabel.setText("Fill Out All the Required Information.");
	}

	private void confirm() {
		wayType = fieldName.getText();
		if(wayType == null || wayType.isEmpty() || elementType == null) showError();
		else if(elementType.equals("Line")) addLine();
		else if(elementType.equalsIgnoreCase("Area")) addArea();
	}
	
	private void addLine() {
		String layer = fieldLayer.getText();
		String lineWidth = textFieldLineWidth.getText();
		String lineDash = textFieldLineDash.getText();
		int zoomValue = zoomSlider.getValue();
		if(zoomValue == 0) zoomValue = 1;
		if(lineDash != null && lineDash.isEmpty()) lineDash = null;
		if(isMissingInfo(layer, innerColor, outerColor) || !tc.isNumerical(layer) || lineWidth == null || lineWidth.isEmpty() 
				|| !tc.isNumerical(lineWidth)) showError();
		else {
			tc.addLineElement(wayType, layer, innerColor, outerColor, lineWidth, lineDash, zoomValue);
			dispose();
		}
	}
	
	private void addArea() {
		String layer = fieldLayer.getText();
		int zoomValue = zoomSlider.getValue();
		if(zoomValue == 0) zoomValue = 1;
		if(isMissingInfo(layer, innerColor, outerColor)) showError();
		else {
			tc.addAreaElement(wayType, layer, innerColor, outerColor, zoomValue);
			dispose();
		}
	}
	
	private boolean isMissingInfo(String layer, Color innerColor, Color outerColor) {
		if(layer == null || layer.isEmpty() || innerColor == null || outerColor == null) return true;
		return false;
	}

	private void showWaytypesDialog() {
		JDialog dialog = new JDialog(this);
		dialog.setModal(true);
		JPanel dialogContentPane = new JPanel();
		dialogContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialogContentPane.setLayout(new BorderLayout(0, 0));
		dialog.setContentPane(dialogContentPane);
		
		WayType[] wayTypes = WayType.values();
		List<WayType> unusedWaytypes = new ArrayList<WayType>();
		for(int i = 0; i < wayTypes.length; i++) {
			if(!elements.contains(wayTypes[i].toString())) unusedWaytypes.add(wayTypes[i]);
		}
		JList<WayType> list = new JList<>(unusedWaytypes.toArray(new WayType[unusedWaytypes.size()]));
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					fieldName.setText(unusedWaytypes.get(list.getSelectedIndex()).toString());
					dialog.dispose();
				}
			}			
		});
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER && !list.isSelectionEmpty()) {
					fieldName.setText(unusedWaytypes.get(list.getSelectedIndex()).toString());
					dialog.dispose();
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		dialogContentPane.add(scrollPane);
		
		dialog.setPreferredSize(new Dimension(300, 400));
		dialog.pack();
		
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	private void showElementsDialog() {
		JDialog dialog = new JDialog(this);
		dialog.setModal(true);
		JPanel dialogContentPane = new JPanel();
		dialogContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialogContentPane.setLayout(new BorderLayout(0, 0));
		dialog.setContentPane(dialogContentPane);
		
		JList<String> list = new JList<>(elements.toArray(new String[elements.size()]));
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					fieldLayer.setText(list.getSelectedValue());
					dialog.dispose();
				}
			}			
		});
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER && !list.isSelectionEmpty()) {
					fieldLayer.setText(list.getSelectedValue());
					dialog.dispose();
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		dialogContentPane.add(scrollPane);
		
		dialog.setPreferredSize(new Dimension(300, 400));
		dialog.pack();
		
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private void createLinePanel() {
		JPanel linePanel = new JPanel();
		
		linePanel.setLayout(new GridLayout(2, 2, 0, 0));
		
		linePanel.add(getInnerColorPanel());
		
		linePanel.add(getOuterColorPanel());
		
		JPanel panelLineWidth = new JPanel();
		panelLineWidth.setBorder(new EmptyBorder(40, 20, 40, 25));
		linePanel.add(panelLineWidth);
		panelLineWidth.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblLineWidth = new JLabel("Line Width:");
		lblLineWidth.setHorizontalAlignment(SwingConstants.CENTER);
		panelLineWidth.add(lblLineWidth);
		
		textFieldLineWidth = new JTextField();
		textFieldLineWidth.setHorizontalAlignment(SwingConstants.CENTER);
		panelLineWidth.add(textFieldLineWidth);
		textFieldLineWidth.setColumns(10);
		
		JPanel panelLineDash = new JPanel();
		panelLineDash.setBorder(new EmptyBorder(0, 0, 0, 5));
		linePanel.add(panelLineDash);
		panelLineDash.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panelDashSub1 = new JPanel();
		panelLineDash.add(panelDashSub1);
		panelDashSub1.setLayout(new BorderLayout(0, 0));
		
		JCheckBox chckbxDottedLine = new JCheckBox("Dotted Line");
		chckbxDottedLine.setHorizontalAlignment(SwingConstants.CENTER);
		panelDashSub1.add(chckbxDottedLine);
		
		JPanel panelDashSub2 = new JPanel();
		panelDashSub2.setBorder(new EmptyBorder(5, 5, 40, 5));
		panelLineDash.add(panelDashSub2);
		panelDashSub2.setLayout(new BorderLayout(0, 20));
		
		JLabel labelDashValues = new JLabel("Dash Values");
		labelDashValues.setForeground(Color.GRAY);
		labelDashValues.setHorizontalAlignment(SwingConstants.CENTER);
		labelDashValues.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelDashSub2.add(labelDashValues, BorderLayout.NORTH);
		
		textFieldLineDash = new JTextField();
		textFieldLineDash.setEnabled(false);
		textFieldLineDash.setHorizontalAlignment(SwingConstants.CENTER);
		panelDashSub2.add(textFieldLineDash, BorderLayout.CENTER);
		textFieldLineDash.setColumns(10);
		
		chckbxDottedLine.addActionListener(e -> {
			if(chckbxDottedLine.isSelected()) {
				labelDashValues.setForeground(Color.BLACK);
				textFieldLineDash.setEnabled(true);
			}
			else {
				labelDashValues.setForeground(Color.GRAY);
				textFieldLineDash.setEnabled(false);
			}
		});
		
		panelProperties.add(linePanel, "name_846857261777193");
	}
	
	private void createAreaPanel() {
		JPanel areaPanel = new JPanel();
		areaPanel.setLayout(new GridLayout(1, 2, 0, 0));
		areaPanel.setBorder(new EmptyBorder(50, 0, 50, 0));
		
		areaPanel.add(getInnerColorPanel());
		
		areaPanel.add(getOuterColorPanel());
		
		panelProperties.add(areaPanel, "name_84685726177666");
	}
	
	private JPanel getInnerColorPanel() {
		JPanel panelInnerColor = new JPanel();
		panelInnerColor.setBorder(new EmptyBorder(30, 30, 30, 60));
		panelInnerColor.setLayout(new BorderLayout(40, 0));
		
		JLabel labelInnerColor = new JLabel("Inner Color:");
		labelInnerColor.setBorder(null);
		panelInnerColor.add(labelInnerColor, BorderLayout.WEST);
		
		JButton buttonInnerColor = new JButton();
		buttonInnerColor.addActionListener(e -> {
			innerColor = JColorChooser.showDialog(this, "Choose Inner Color", Color.BLACK);
			if(innerColor != null) buttonInnerColor.setBackground(innerColor);
		});
		panelInnerColor.add(buttonInnerColor, BorderLayout.CENTER);
		return panelInnerColor;
	}
	
	private JPanel getOuterColorPanel() {
		JPanel panelOuterColor = new JPanel();
		panelOuterColor.setBorder(new EmptyBorder(30, 30, 30, 60));
		panelOuterColor.setLayout(new BorderLayout(40, 0));
		
		JLabel labelOuterColor = new JLabel("Outer Color:");
		labelOuterColor.setBorder(null);
		panelOuterColor.add(labelOuterColor, BorderLayout.WEST);
		
		JButton buttonOuterColor = new JButton("");
		buttonOuterColor.addActionListener(e -> {
			outerColor = JColorChooser.showDialog(this, "Choose Outer Color", Color.BLACK);
			if(outerColor != null) buttonOuterColor.setBackground(outerColor);
		});
		panelOuterColor.add(buttonOuterColor, BorderLayout.CENTER);
		return panelOuterColor;
	}
	
	private void lineSelected() {
		CardLayout cl = (CardLayout)panelProperties.getLayout();
		cl.show(panelProperties, "name_846857261777193");
	}
	
	private void areaSelected() {
		CardLayout cl = (CardLayout)panelProperties.getLayout();
		cl.show(panelProperties, "name_84685726177666");
	}
}
