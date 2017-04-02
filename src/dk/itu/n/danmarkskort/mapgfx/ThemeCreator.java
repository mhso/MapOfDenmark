package dk.itu.n.danmarkskort.mapgfx;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import dk.itu.n.danmarkskort.SAXAdapter;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ThemeCreator extends JFrame {

	private JPanel contentPane;
	private JPanel themeEditPanel;
	private String selectedFile;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem mntmAddElement;
	private JMenuItem mntmChangeFile;
	private List<String> elements = new ArrayList<>();
	private Map<String, Integer> zoomMap = new HashMap<>();

	/**
	 * Create the frame.
	 */
	public ThemeCreator(String selectedFile) {
		super("Graphic Theme Editor");
		this.selectedFile = selectedFile;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("");
		menuBar.add(menu);
		
		mntmAddElement = new JMenuItem("Add Element");
		mntmAddElement.addActionListener(e -> addElement());
		menuBar.add(mntmAddElement);
		
		mntmChangeFile = new JMenuItem("Change File");
		menuBar.add(mntmChangeFile);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);
		
		JPanel themeEditPanelMain = new JPanel();
		themeEditPanelMain.setLayout(new BorderLayout(0, 15));
		contentPane.add(themeEditPanelMain);
		
		themeEditPanel = new JPanel();
		themeEditPanel.setLayout(new GridLayout(0, 1, 10, 10));
		themeEditPanel.setBackground(Color.WHITE);
		
		JScrollPane editThemeScrollPane = new JScrollPane(themeEditPanel);
		editThemeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editThemeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		themeEditPanelMain.add(editThemeScrollPane, BorderLayout.CENTER);
		
		parseThemeContent();
		
		setPreferredSize(new Dimension(350, 500));
		
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void addLineElement(String wayType, String layer, Color innerColor, Color outerColor, String lineWidth, String lineDash, 
			int zoomValue) {
		Document doc = getDocument(selectedFile);
		
		NodeList lineNodes = doc.getElementsByTagName("lines");
		Element newElement = doc.createElement("line");
		lineNodes.item(0).appendChild(newElement);
		
		Element type = doc.createElement("type");
		type.setAttribute("name", wayType);
		newElement.appendChild(type);
		
		Element eInnerColor = doc.createElement("innercolor");
		eInnerColor.setAttribute("color", parseHex(innerColor));
		newElement.appendChild(eInnerColor);
		
		Element eOuterColor = doc.createElement("outercolor");
		eOuterColor.setAttribute("color", parseHex(outerColor));
		newElement.appendChild(eOuterColor);
		
		Element lineProperties = doc.createElement("lineproperties");
		lineProperties.setAttribute("linewidth", lineWidth);
		if(lineDash != null) lineProperties.setAttribute("linedash", lineDash);
		newElement.appendChild(lineProperties);
		
		Element layerValue = doc.createElement("layer");
		layerValue.setAttribute("layer", layer);
		newElement.appendChild(layerValue);
		
		saveDocument(doc, selectedFile);
		
		addToZoomDocument(wayType, zoomValue);
	}

	public void addAreaElement(String wayType, String layer, Color innerColor, Color outerColor, int zoomValue) {
		Document doc = getDocument(selectedFile);
		
		NodeList lineNodes = doc.getElementsByTagName("areas");
		Element newElement = doc.createElement("area");
		lineNodes.item(0).appendChild(newElement);
		
		Element type = doc.createElement("type");
		type.setAttribute("name", wayType);
		newElement.appendChild(type);
		
		Element eInnerColor = doc.createElement("innercolor");
		eInnerColor.setAttribute("color", parseHex(innerColor));
		newElement.appendChild(eInnerColor);
		
		Element eOuterColor = doc.createElement("outercolor");
		eOuterColor.setAttribute("color", parseHex(outerColor));
		newElement.appendChild(eOuterColor);

		
		Element layerValue = doc.createElement("layer");
		layerValue.setAttribute("layer", layer);
		newElement.appendChild(layerValue);
		
		saveDocument(doc, selectedFile);
		addToZoomDocument(wayType, zoomValue);
	}

	private void addToZoomDocument(String wayType, int zoomValue) {
		String zoomFile = "resources/ZoomValues.XML";
		Document doc = getDocument(zoomFile);
		
		NodeList zoomNodes = doc.getElementsByTagName("zoomlevel");
		Element zoomElement = (Element)zoomNodes.item(zoomValue-1);
		
		Element newElement = doc.createElement("type");
		newElement.setAttribute("name", wayType);
		zoomElement.appendChild(newElement);
		
		saveDocument(doc, zoomFile);
	}
	
	private void addElement() {
		ThemeAddElement addElementFrame = new ThemeAddElement(this, elements);
	}

	private Color selectColor(Color initColor) {
		return JColorChooser.showDialog(this, "test", initColor);
	}
	
	private Document getDocument(String fileName) {		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			return docBuilder.parse(fileName);
		} catch (SAXException | ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void changeLayer(String wayType, String elementType, int newLayer) {
		Document doc = getDocument(selectedFile);
		elementType = elementType.toLowerCase();
		
		NodeList lineNodes = doc.getElementsByTagName(elementType);
		for(int i = 0; i < lineNodes.getLength(); i++) {
			Element lineNode = (Element)lineNodes.item(i);
			if(lineNode.getElementsByTagName("type").item(0).getAttributes().getNamedItem("name").getNodeValue().equals(wayType)) {	
				Element finalNode = (Element)lineNode.getElementsByTagName("layer").item(0);
				NamedNodeMap nnm = finalNode.getAttributes();
				Node nodeAttr = nnm.getNamedItem("layer");
				nodeAttr.setTextContent(""+newLayer);
				break;
			}
		}
		saveDocument(doc, selectedFile);
	}
	
	private void changeZoomLevel(String wayType, int oldZoom, int newZoom) {
		String zoomFile = "resources/ZoomValues.XML";
		Document doc = getDocument(zoomFile);
		
		NodeList zoomNodes = doc.getElementsByTagName("zoomlevel");
		Element zoomElement = (Element)zoomNodes.item(oldZoom-1);
		NodeList typeNodes = zoomElement.getElementsByTagName("type");
		for(int i = 0; i < typeNodes.getLength(); i++) {
			Element typeNode = (Element)typeNodes.item(i);
			if(typeNode.getAttributes().getNamedItem("name").getNodeValue().equals(wayType)) {
				typeNode.getParentNode().removeChild(typeNode);
			}
		}
		saveDocument(doc, zoomFile);
		addToZoomDocument(wayType, newZoom);
	}
	
	private void changeWidth(String wayType, String newWidth) {
		Document doc = getDocument(selectedFile);
		
		NodeList lineNodes = doc.getElementsByTagName("line");
		for(int i = 0; i < lineNodes.getLength(); i++) {
			Element lineNode = (Element)lineNodes.item(i);
			if(lineNode.getElementsByTagName("type").item(0).getAttributes().getNamedItem("name").getNodeValue().equals(wayType)) {	
				Element finalNode = (Element)lineNode.getElementsByTagName("lineproperties").item(0);
				NamedNodeMap nnm = finalNode.getAttributes();
				Node nodeAttr = nnm.getNamedItem("linewidth");
				nodeAttr.setTextContent(newWidth);
				break;
			}
		}
		saveDocument(doc, selectedFile);
	}
	
	private void changeColor(String colorType, String elementType, String wayType, Color newColor) {				
		String hex = parseHex(newColor);
		elementType = elementType.toLowerCase();
		
		Document doc = getDocument(selectedFile);
		
		NodeList lineNodes = doc.getElementsByTagName(elementType);
		for(int i = 0; i < lineNodes.getLength(); i++) {
			Element lineNode = (Element)lineNodes.item(i);
			if(lineNode.getElementsByTagName("type").item(0).getAttributes().getNamedItem("name").getNodeValue().equals(wayType)) {	
				Element finalNode = (Element)lineNode.getElementsByTagName(colorType).item(0);
				NamedNodeMap nnm = finalNode.getAttributes();
				Node nodeAttr = nnm.getNamedItem("color");
				nodeAttr.setTextContent(hex);
				break;
			}
		}
		saveDocument(doc, selectedFile);
	}
	
	private void saveDocument(Document doc, String fileName) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
	}

	private void parseThemeContent() {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new ZoomHandler());
			reader.parse(new InputSource("resources/ZoomValues.XML"));
			
			reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new ThemeContentHandler());
			reader.parse(new InputSource(selectedFile));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isNumerical(String width) {
		for(int i = 0; i < width.length(); i++) {
			if(!Character.isDigit(width.charAt(i))) return false;
		}
		return true;
	}
	
	/**
	 * Parse a color from Hex to RGB.
	 * 
	 * @param hex A Hexadecimal String representation of a color.
	 * @return A Java RGB Color matching the inputed Hex color.
	 */
	private Color parseColor(String hex) {
		if(hex.charAt(0) == '#') hex = hex.substring(1);
		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);
		return new Color(r, g, b);
	}
	
	private String parseHex(Color color) {
		String r = Integer.toHexString(color.getRed()).toUpperCase();
		if(r.length() == 1) r = "0" + r;
		String g = Integer.toHexString(color.getGreen()).toUpperCase();
		if(g.length() == 1) g = "0" + g;
		String b = Integer.toHexString(color.getBlue()).toUpperCase();
		if(b.length() == 1) b = "0" + b;
		return r+g+b;
	}
	
	private class ZoomHandler extends SAXAdapter {
		private int currentZoomValue;
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
			switch(qName) {
				case "zoomlevel":
					currentZoomValue = Integer.parseInt(atts.getValue("level"));
				break;
				case "type":
					zoomMap.put(atts.getValue("name"), currentZoomValue);
				break;
			}
		}
	}
	
	private class ThemeContentHandler extends SAXAdapter {
		private String elementType;
		private String wayType;
		private JPanel superElementPanel;
		private JPanel elementPanel;
		private Color defaultFontColor;
		private int defaultFontSize;
		final int BUTTON_SIZE = 55;
		
		@Override
		public void endElement(String uri, String localname, String qName) throws SAXException {
			switch(qName) {
			case "line": 
			case "area":
				JPanel zoomPanel = new JPanel();
				zoomPanel.setBackground(Color.WHITE);
				zoomPanel.setLayout(new BorderLayout(5, 5));
				
				JLabel zoomLabel = new JLabel("Zoom Level:");
				zoomLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
				zoomPanel.add(zoomLabel, BorderLayout.WEST);
				
				JButton zoomPicker = new JButton(""+zoomMap.get(wayType));
				zoomPicker.setFont(new Font("Tahoma", Font.PLAIN, 18));
				zoomPicker.addActionListener(new ZoomChanger(zoomPicker, wayType));
				zoomPicker.setPreferredSize(new Dimension(BUTTON_SIZE, zoomPicker.getHeight()));
				zoomPanel.add(zoomPicker, BorderLayout.EAST);
				
				elementPanel.add(zoomPanel);				
				themeEditPanel.add(superElementPanel);
			break;
			}		
		}

		@Override
		public void startElement(String uri, String localname, String qName, Attributes atts) 
				throws SAXException {
			switch(qName) {
			case "defaultfont":
				defaultFontSize = Integer.parseInt(atts.getValue("fontsize"));
				defaultFontColor = parseColor(atts.getValue("fontcolor"));
			break;
			case "type":
				wayType = atts.getValue("name");
				elements.add(wayType);
				JLabel label = new JLabel(wayType);
				label.setForeground(Color.RED);
				label.setFont(new Font("Tahoma", Font.PLAIN, 20));
				superElementPanel.add(label, BorderLayout.NORTH);
			break;
			case "line":
				createPanel();
			case "area":
				createPanel();
			break;
			case "lines":
				elementType = "Line";
				createSplitPanel();
			break;
			case "areas":
				elementType = "Area";
				createSplitPanel();
			break;
			case "label":
				elementType = "Label";
			case "icon":
				elementType = "Icon";
			break;
			case "innercolor":
				JPanel innerColorPanel = new JPanel();
				innerColorPanel.setBackground(Color.WHITE);
				innerColorPanel.setLayout(new BorderLayout(5, 5));
				
				JLabel colorLabel = new JLabel("Inner Color:");
				colorLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
				innerColorPanel.add(colorLabel, BorderLayout.WEST);
				
				JButton colorPicker = new JButton();
				colorPicker.addActionListener(new ColorChanger(colorPicker, elementType, wayType, ColorChanger.INNER_COLOR));
				colorPicker.setPreferredSize(new Dimension(BUTTON_SIZE, colorPicker.getHeight()));
				colorPicker.setBackground(parseColor(atts.getValue("color")));
				innerColorPanel.add(colorPicker, BorderLayout.EAST);
				
				elementPanel.add(innerColorPanel);
			break;
			case "outercolor":
				JPanel outerColorPanel = new JPanel();
				outerColorPanel.setBackground(Color.WHITE);
				outerColorPanel.setLayout(new BorderLayout(BUTTON_SIZE, 5));
				
				colorLabel = new JLabel("Outer Color:");
				colorLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
				outerColorPanel.add(colorLabel, BorderLayout.WEST);
				
				colorPicker = new JButton();
				colorPicker.addActionListener(new ColorChanger(colorPicker, elementType, wayType, ColorChanger.OUTER_COLOR));
				colorPicker.setPreferredSize(new Dimension(BUTTON_SIZE, colorPicker.getHeight()));
				colorPicker.setBackground(parseColor(atts.getValue("color")));
				outerColorPanel.add(colorPicker, BorderLayout.EAST);
				
				elementPanel.add(outerColorPanel);
			break;
			case "layer":
				JPanel layerPanel = new JPanel();
				layerPanel.setBackground(Color.WHITE);
				layerPanel.setLayout(new BorderLayout(BUTTON_SIZE, 5));
				
				JLabel layerLabel = new JLabel("Layer:");
				layerLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
				layerPanel.add(layerLabel, BorderLayout.WEST);
				
				JButton layerPicker = new JButton(atts.getValue("layer"));
				layerPicker.addActionListener(new LayerChanger(layerPicker, elementType, wayType));
				layerPicker.setPreferredSize(new Dimension(BUTTON_SIZE, layerPicker.getHeight()));
				layerPanel.add(layerPicker, BorderLayout.EAST);
				
				elementPanel.add(layerPanel);
			break;
			case "lineproperties":
				JPanel lineWidthPanel = new JPanel();
				lineWidthPanel.setBackground(Color.WHITE);
				lineWidthPanel.setLayout(new BorderLayout(BUTTON_SIZE, 5));
				
				JLabel widthLabel = new JLabel("Line Width:");
				widthLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
				lineWidthPanel.add(widthLabel, BorderLayout.WEST);
				
				JButton lineWidthPicker = new JButton(atts.getValue("linewidth"));
				lineWidthPicker.addActionListener(new WidthChanger(lineWidthPicker, wayType));
				lineWidthPicker.setPreferredSize(new Dimension(BUTTON_SIZE, lineWidthPicker.getHeight()));
				lineWidthPicker.setBackground(Color.WHITE);
				lineWidthPanel.add(lineWidthPicker, BorderLayout.EAST);
				
				elementPanel.add(lineWidthPanel);
				
				if(atts.getValue("linedash") != null) {
					JPanel lineDashPanel = new JPanel();
					lineDashPanel.setBackground(Color.WHITE);
					lineDashPanel.setLayout(new BorderLayout(BUTTON_SIZE, 5));
					
					JLabel dashLabel = new JLabel("Line Dash:");
					dashLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
					lineDashPanel.add(dashLabel, BorderLayout.WEST);
					
					JButton lineDashPicker = new JButton(atts.getValue("linedash"));
					lineDashPicker.setPreferredSize(new Dimension(BUTTON_SIZE, lineDashPanel.getHeight()));
					lineDashPicker.setBackground(Color.WHITE);
					lineDashPanel.add(lineDashPicker, BorderLayout.EAST);
					
					elementPanel.add(lineDashPanel);
				}
			break;
			case "fontcolor":
				JPanel fontColorPanel = new JPanel();
				fontColorPanel.setBackground(Color.WHITE);
				fontColorPanel.setLayout(new BorderLayout(BUTTON_SIZE, 5));
				
				JLabel fontColorLabel = new JLabel("Line Width:");
				fontColorLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
				fontColorPanel.add(fontColorLabel, BorderLayout.WEST);
				
				JButton fontColorPicker = new JButton("Font Color");
				fontColorPicker.addActionListener(new ColorChanger(fontColorPicker, elementType, wayType, ColorChanger.FONT_COLOR));
				fontColorPicker.setPreferredSize(new Dimension(BUTTON_SIZE, fontColorPicker.getHeight()));
				fontColorPicker.setBackground(defaultFontColor);
				if(atts.getValue("fontcolor") != null) {
					fontColorPicker.setBackground(parseColor(atts.getValue("fontcolor")));
				}
				fontColorPanel.add(fontColorPicker, BorderLayout.EAST);
				
				elementPanel.add(fontColorPanel);
			break;
			case "fontsize":
				JPanel fontSizePanel = new JPanel();
				fontSizePanel.setBackground(Color.WHITE);
				fontSizePanel.setLayout(new BorderLayout(BUTTON_SIZE, 5));
				
				JLabel fontSizeLabel = new JLabel("Font Size:");
				fontSizeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
				fontSizePanel.add(fontSizeLabel, BorderLayout.WEST);
				
				JButton fontSizePicker = new JButton(""+defaultFontSize);
				fontSizePicker.setPreferredSize(new Dimension(BUTTON_SIZE, fontSizePicker.getHeight()));
				fontSizePicker.setBackground(defaultFontColor);
				if(atts.getValue("fontsize") != null) {
					fontSizePicker.setText(atts.getValue("fontcolor"));
				}
				fontSizePanel.add(fontSizePicker, BorderLayout.EAST);
				
				elementPanel.add(fontSizePanel);
			break;
			}
		}
		
		private void createSplitPanel() {
			JPanel splitPanel = new JPanel();
			splitPanel.setLayout(new BorderLayout(5, 5));
			splitPanel.setBackground(Color.WHITE);
			
			JLabel splitLabel = new JLabel("____________________");
			splitLabel.setForeground(Color.BLUE);
			splitLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
			splitPanel.add(splitLabel, BorderLayout.NORTH);
			
			JLabel typeLabel = new JLabel(elementType);
			typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
			typeLabel.setForeground(Color.BLUE);
			typeLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
			splitPanel.add(typeLabel, BorderLayout.CENTER);
			
			themeEditPanel.add(splitPanel);
		}
		
		private void createPanel() {
			superElementPanel = new JPanel();
			superElementPanel.setLayout(new BorderLayout(5, 10));
			superElementPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
			superElementPanel.setOpaque(false);
			
			elementPanel = new JPanel();
			elementPanel.setPreferredSize(new Dimension(elementPanel.getWidth(), 120));
			elementPanel.setLayout(new GridLayout(0, 1));
			elementPanel.setOpaque(false);
			superElementPanel.add(elementPanel, BorderLayout.CENTER);
		}
	}
	
	private class ColorChanger implements ActionListener {
		private JButton sourceButton;
		private String elementType;
		private String elementName;
		private int colorType;
		public static final int OUTER_COLOR = 0;
		public static final int INNER_COLOR = 1;
		public static final int FONT_COLOR = 2;

		public ColorChanger(JButton sourceButton, String elementType, String elementName, int colorType) {
			this.sourceButton = sourceButton;
			this.elementType = elementType;
			this.elementName = elementName;
			this.colorType = colorType;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Color color = selectColor(sourceButton.getBackground());
			if(color == null) return;
			String colorDef = "";
			if(colorType == OUTER_COLOR) colorDef = "outercolor";
			else if(colorType == INNER_COLOR) colorDef = "innercolor";
			else if(colorType == FONT_COLOR) colorDef = "fontcolor";
			changeColor(colorDef, elementType, elementName, color);
			sourceButton.setBackground(color);
		}
	}
	
	private class LayerChanger implements ActionListener {
		private JButton sourceButton;
		private String elementName;
		private String elementType;

		public LayerChanger(JButton sourceButton, String elementType, String elementName) {
			this.sourceButton = sourceButton;
			this.elementName = elementName;
			this.elementType = elementType;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String layer = JOptionPane.showInputDialog(ThemeCreator.this, "Input New Layer Value", sourceButton.getText());
			if(layer == null || !isNumerical(layer)) return;
			changeLayer(elementName, elementType, Integer.parseInt(layer));
			sourceButton.setText(layer);
		}
	}
	
	private class WidthChanger implements ActionListener {
		private JButton sourceButton;
		private String elementName;

		public WidthChanger(JButton sourceButton, String elementName) {
			this.sourceButton = sourceButton;
			this.elementName = elementName;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String width = JOptionPane.showInputDialog(ThemeCreator.this, "Input New Line Width", sourceButton.getText());
			if(width == null || !isNumerical(width)) return;
			changeWidth(elementName, width);
			sourceButton.setText(width);
		}
	}
	
	private class ZoomChanger implements ActionListener {
		private JButton sourceButton;
		private String elementName;

		public ZoomChanger(JButton sourceButton, String elementName) {
			this.sourceButton = sourceButton;
			this.elementName = elementName;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int oldZoom = zoomMap.get(elementName);
			String newZoom = JOptionPane.showInputDialog(ThemeCreator.this, "Input New Zoom Value", sourceButton.getText());
			if(newZoom == null || !isNumerical(newZoom)) return;
			changeZoomLevel(elementName, oldZoom, Integer.parseInt(newZoom));
			sourceButton.setText(newZoom);
		}
		
		private boolean isNumerical(String width) {
			for(int i = 0; i < width.length(); i++) {
				if(!Character.isDigit(width.charAt(i))) return false;
			}
			return true;
		}
	}
}
