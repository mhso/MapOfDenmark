package dk.itu.n.danmarkskort.address;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.util.Map;
import java.util.Set;

public class DEMOAddressMain extends JFrame {

	private JPanel contentPane;
	private JTextField txtInputAddress;
	private JTextArea textArea;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DEMOAddressMain frame = new DEMOAddressMain();
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
	public DEMOAddressMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1142, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblInputAddress = new JLabel("Input Address:");
		GridBagConstraints gbc_lblInputAddress = new GridBagConstraints();
		gbc_lblInputAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblInputAddress.anchor = GridBagConstraints.EAST;
		gbc_lblInputAddress.gridx = 1;
		gbc_lblInputAddress.gridy = 1;
		panel.add(lblInputAddress, gbc_lblInputAddress);
		
		txtInputAddress = new JTextField();
		txtInputAddress.setText("H\u00E5ndv\u00E6rkerv\u00E6nget 12 st. 3400 Hiller\u00F8d");
		GridBagConstraints gbc_txtInputAddress = new GridBagConstraints();
		gbc_txtInputAddress.gridwidth = 3;
		gbc_txtInputAddress.insets = new Insets(0, 0, 5, 5);
		gbc_txtInputAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtInputAddress.gridx = 1;
		gbc_txtInputAddress.gridy = 2;
		panel.add(txtInputAddress, gbc_txtInputAddress);
		txtInputAddress.setColumns(10);
		
		JButton btnParseAddress = new JButton("Parse Address");
		btnParseAddress.addActionListener(a ->  {
			btnParseAddressaction(txtInputAddress.getText());
		});
		GridBagConstraints gbc_btnParseAddress = new GridBagConstraints();
		gbc_btnParseAddress.insets = new Insets(0, 0, 0, 5);
		gbc_btnParseAddress.gridwidth = 3;
		gbc_btnParseAddress.fill = GridBagConstraints.BOTH;
		gbc_btnParseAddress.gridx = 1;
		gbc_btnParseAddress.gridy = 3;
		panel.add(btnParseAddress, gbc_btnParseAddress);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setText("Here the results will be...");
		textArea.setEditable(false);
		textArea.setColumns(65);
		textArea.setRows(22);
		scrollPane.setViewportView(textArea);
		
	}
	
	private void btnParseAddressaction(String inputText){
		DEMOAddressParser dEMOAddressParser = new DEMOAddressParser("resources/map.osm");
		AddressManager addressManager = AddressManager.getInstance();
		/*
		addressManager.addOsmAddress(4616395489l, 55.6715887, 12.5832270, "addr:housenumber", "1");
		addressManager.addOsmAddress(4616395489l, 55.6715887, 12.5832270, "addr:postcode", "1411");
		addressManager.addOsmAddress(4616395489l, 55.6715887, 12.5832270, "addr:street", "Langebrogade");
		
		addressManager.addOsmAddress(340527455, 55.6702390, 12.5810040, "addr:city", "København K");
		addressManager.addOsmAddress(340527455, 55.6702390, 12.5810040, "addr:country", "DK");
		addressManager.addOsmAddress(340527455, 55.6702390, 12.5810040, "addr:housenumber", "2");
		addressManager.addOsmAddress(340527455, 55.6702390, 12.5810040, "addr:postcode", "1411");
		addressManager.addOsmAddress(340527455, 55.6702390, 12.5810040, "addr:street", "Langebrogade");
		
		addressManager.addOsmAddress(340486650, 55.6708970, 12.5859510, "addr:country", "DK");
		addressManager.addOsmAddress(340486650, 55.6708970, 12.5859510, "addr:housenumber", "15");
		addressManager.addOsmAddress(340486650, 55.6708970, 12.5859510, "addr:postcode", "1411");
		addressManager.addOsmAddress(340486650, 55.6708970, 12.5859510, "addr:city", "København K");
		addressManager.addOsmAddress(340486650, 55.6708970, 12.5859510, "addr:street", "Applebys Plads");
		
		addressManager.addOsmAddress(296957928, 55.6651852, 12.5812276, "addr:city", "København S");
		addressManager.addOsmAddress(296957928, 55.6651852, 12.5812276, "addr:housenumber", "2-4");
		addressManager.addOsmAddress(296957928, 55.6651852, 12.5812276, "addr:postcode", "2300");
		addressManager.addOsmAddress(296957928, 55.6651852, 12.5812276, "addr:street", "Leifsgade");
		
		
		Address osmAddr = addressManager.getAddressFromNodeId(4616395489l);
		*/
		
		AddressParser addressParser = new AddressParser();
		Address address = addressParser.parse(inputText);
		Map<Long, Address> adresses = addressManager.getAddresses();
		String result = "Read Input: "+inputText+"\n";
		if(address != null)	result += "Parsed Results:\n"+address.toString()+"\n";
		result += "Adresses found: "+adresses.size()+"\n";
		
		//for(Address addr : adresses.values()){ result += addr.toString()+"\n"; }
		
		textArea.setText(result);
		
		testPrint(addressManager);
	}
	
	private void testPrint(AddressManager addressManager){
		System.out.println("-- Addresses mapped: "+addressManager.getAddresses().size()+" --");
		System.out.println("-- Postcodes mapped: "+addressManager.getPostcodes().size()+" --");
		//for(String pc : addressManager.getPostcodes().keySet()){ System.out.println(pc); }
		//System.out.println("-- Citys mapped: --");
		//for(Postcode s : addressManager.getPostcodes().values()){ System.out.println(s.getCity()); }
		System.out.println("-- Streets mapped: "+addressManager.getStreets().size()+" --");
		/*
		for(String s : addressManager.getStreets().keySet()){ System.out.println(s); }
		*/
		
		Set<String> findStreets = addressManager.streetSearch("An");
		System.out.println("-- Street search: "+findStreets.size()+" --");
		for(String s : findStreets){
			Postcode postcode = addressManager.getPostcodeFromStreet(s);
			System.out.println("Street: "+s);
			System.out.println("....Postcode: "+postcode.getPostcode()+ " City: "+postcode.getCity());
		}
	}
}
