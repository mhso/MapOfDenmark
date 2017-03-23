package dk.itu.n.danmarkskort.backend;

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
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;

public class ThemeCreator extends JFrame {

	private JPanel contentPane;
	private JPanel cardPanel;
	private JPanel themeEditPanel;
	private List<String> fileNames;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ThemeCreator frame = new ThemeCreator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ThemeCreator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);
		
		cardPanel = new JPanel();
		cardPanel.setLayout(new CardLayout());
		contentPane.add(cardPanel, BorderLayout.CENTER);
		
		JPanel pickThemeMainPanel = new JPanel();
		pickThemeMainPanel.setLayout(new BorderLayout(0, 15));
		cardPanel.add(pickThemeMainPanel, "name_846857261777111");
		
		fileNames = new ArrayList<String>();
		
		JLabel lblChooseATheme = new JLabel("Choose a Theme to edit");
		lblChooseATheme.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblChooseATheme.setHorizontalAlignment(SwingConstants.CENTER);
		pickThemeMainPanel.add(lblChooseATheme, BorderLayout.NORTH);
		
		JPanel themePanel = new JPanel();
		themePanel.setLayout(new GridLayout(0, 1, 10, 10));
		themePanel.setBackground(Color.WHITE);
		
		JScrollPane scrollPane = new JScrollPane(themePanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getThemes(themePanel);
		pickThemeMainPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel themeEditPanelMain = new JPanel();
		themeEditPanelMain.setLayout(new BorderLayout(0, 15));
		cardPanel.add(themeEditPanelMain, "name_846857261777222");
		
		themeEditPanel = new JPanel();
		themeEditPanel.setLayout(new GridLayout(0, 1, 10, 10));
		themeEditPanel.setBackground(Color.WHITE);
		
		JScrollPane editThemeScrollPane = new JScrollPane(themeEditPanel);
		editThemeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editThemeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		themeEditPanelMain.add(editThemeScrollPane, BorderLayout.CENTER);
		
		setPreferredSize(new Dimension(320, 500));
		
		pack();	
		
		setLocationRelativeTo(null);
	}

	private void getThemes(JPanel themePanel) {
		Path dir = Paths.get("resources");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.XML")) {
			int i = 0;
	           for (Path entry: stream) {
	        	   String fileName = entry.toString().substring(10, entry.toString().length()-4);
	        	   if(!fileName.startsWith("Theme")) continue;
	        	   JButton button = new JButton(fileName);
	        	   button.addActionListener(new ButtonListener(i));
	        	   button.setBackground(Color.WHITE);
	        	   button.setHorizontalAlignment(SwingConstants.CENTER);
	        	   button.setFont(new Font("Tahoma", Font.PLAIN, 18));
	        	   themePanel.add(button);
	        	   fileNames.add(fileName);
	        	   i++;
	           }
	       } catch (IOException | DirectoryIteratorException ex) {
	           ex.printStackTrace();
	       }
	}
	
	private void themeSelected(String fileName) {
		CardLayout cl = (CardLayout)cardPanel.getLayout();
		populateThemePanel(fileName);
		cl.show(cardPanel, "name_846857261777222");
	}

	private void populateThemePanel(String fileName) {
		String filePath = "resources/"+fileName+".XML";
		parseThemeContent(filePath);
	}
	
	private Color selectColor() {
		return JColorChooser.showDialog(this, "test", Color.BLACK);
	}

	private void parseThemeContent(String filePath) {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new ThemeContentHandler());
			reader.parse(new InputSource(filePath));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parse a color from Hex to RGB.
	 * 
	 * @param hex A Hexadecimal String representation of a color.
	 * @return A Java RGB Color matching the inputed Hex color.
	 */
	private static Color parseColor(String hex) {
		if(hex.charAt(0) == '#') hex = hex.substring(1);
		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);
		return new Color(r, g, b);
	}
	
	private class ThemeContentHandler extends SAXAdapter {
		private String elementType;
		private JPanel superElementPanel;
		private JPanel elementPanel;
		private Color defaultFontColor;
		private int defaultFontSize;
		
		@Override
		public void endElement(String uri, String localname, String qName) throws SAXException {
			switch(qName) {
			case "line": 
			case "area":
				themeEditPanel.add(superElementPanel);
			break;
			}		
		}

		@Override
		public void startElement(String uri, String localname, String qName, Attributes atts) 
				throws SAXException {
			final int BUTTON_SIZE = 50;
			switch(qName) {
			case "defaultfont":
				defaultFontSize = Integer.parseInt(atts.getValue("fontsize"));
				defaultFontColor = parseColor(atts.getValue("fontcolor"));
			break;
			case "type":
				JLabel label = new JLabel(atts.getValue("name"));
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
				colorPicker.addActionListener(new ColorChanger(colorPicker, elementType));
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
				colorPicker.setPreferredSize(new Dimension(BUTTON_SIZE, colorPicker.getHeight()));
				colorPicker.setBackground(parseColor(atts.getValue("color")));
				outerColorPanel.add(colorPicker, BorderLayout.EAST);
				
				elementPanel.add(outerColorPanel);
			break;
			case "lineproperties":
				JPanel lineWidthPanel = new JPanel();
				lineWidthPanel.setBackground(Color.WHITE);
				lineWidthPanel.setLayout(new BorderLayout(BUTTON_SIZE, 5));
				
				JLabel widthLabel = new JLabel("Line Width:");
				widthLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
				lineWidthPanel.add(widthLabel, BorderLayout.WEST);
				
				JButton lineWidthPicker = new JButton(atts.getValue("linewidth"));
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
					lineDashPanel.setPreferredSize(new Dimension(BUTTON_SIZE, lineDashPanel.getHeight()));
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
		private String elementName;

		public ColorChanger(JButton sourceButton, String elementName) {
			this.sourceButton = sourceButton;
			this.elementName = elementName;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Color color = selectColor();
		}
		
	}

	private class ButtonListener implements ActionListener {
		private int fileIndex;
		
		public ButtonListener(int fileIndex) {
			this.fileIndex = fileIndex;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			themeSelected(fileNames.get(fileIndex));
		}
	}
}
