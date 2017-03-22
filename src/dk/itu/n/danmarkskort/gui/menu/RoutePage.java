package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import dk.itu.n.danmarkskort.gui.Style;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RoutePage extends JPanel {

    Style style;
    private JTextField txtAddrFrom;
    private JTextField txtAddreTo;
    public RoutePage(String txtAddreToSetField) {
    	
    	style = new Style();
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        
        JPanel panelPage = new JPanel();
        panelPage.setLayout(new BorderLayout(0, 0));
        add(panelPage);
        
        JPanel panelHeadline = new JPanel();
        panelHeadline.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelHeadline.setBackground(style.menuContentBG());
        panelPage.add(panelHeadline, BorderLayout.NORTH);
        JLabel lblPageHeadline = new JLabel("Find Route Page");
        lblPageHeadline.setFont(new Font("Tahoma", Font.BOLD, 18));
        panelHeadline.add(lblPageHeadline);
        
        JPanel panelSouth = new JPanel();
        panelSouth.setBackground(style.menuContentBG());
        panelPage.add(panelSouth, BorderLayout.SOUTH);
        
        JPanel panelWest = new JPanel();
        panelWest.setBackground(style.menuContentBG());
        panelPage.add(panelWest, BorderLayout.WEST);
        
        JPanel panelEast = new JPanel();
        panelEast.setBackground(style.menuContentBG());
        panelPage.add(panelEast, BorderLayout.EAST);
        
        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(style.menuContentBG());
        panelPage.add(panelCenter, BorderLayout.CENTER);
        initContentPanel(panelCenter);
        GridBagLayout gbl_panelCenter = new GridBagLayout();
        gbl_panelCenter.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelCenter.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_panelCenter.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panelCenter.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelCenter.setLayout(gbl_panelCenter);
        
        JLabel lblFrom = new JLabel("From:");
        GridBagConstraints gbc_lblFrom = new GridBagConstraints();
        gbc_lblFrom.anchor = GridBagConstraints.EAST;
        gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
        gbc_lblFrom.gridx = 0;
        gbc_lblFrom.gridy = 1;
        panelCenter.add(lblFrom, gbc_lblFrom);
        
        txtAddrFrom = new JTextField();
        txtAddrFrom.setText("AddrFrom");
        GridBagConstraints gbc_txtAddrfrom = new GridBagConstraints();
        gbc_txtAddrfrom.gridwidth = 3;
        gbc_txtAddrfrom.insets = new Insets(0, 0, 5, 5);
        gbc_txtAddrfrom.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtAddrfrom.gridx = 1;
        gbc_txtAddrfrom.gridy = 1;
        panelCenter.add(txtAddrFrom, gbc_txtAddrfrom);
        txtAddrFrom.setColumns(10);
        
        JButton btnS = new JButton();
        btnS.addActionListener(e -> swapToFromFields());
        
        JLabel lblAddrfromconfirmed = new JLabel();
        lblAddrfromconfirmed.setIcon(new ImageIcon("resources/icons/checked_checkbox.png"));
        GridBagConstraints gbc_lblAddrfromconfirmed = new GridBagConstraints();
        gbc_lblAddrfromconfirmed.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddrfromconfirmed.gridx = 4;
        gbc_lblAddrfromconfirmed.gridy = 1;
        panelCenter.add(lblAddrfromconfirmed, gbc_lblAddrfromconfirmed);
        
        JLabel lblAddrToConfirmed = new JLabel();
        lblAddrToConfirmed.setIcon(new ImageIcon("resources/icons/unchecked_checkbox.png"));
        GridBagConstraints gbc_lblAddrToConfirmed = new GridBagConstraints();
        gbc_lblAddrToConfirmed.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddrToConfirmed.gridx = 4;
        gbc_lblAddrToConfirmed.gridy = 2;
        panelCenter.add(lblAddrToConfirmed, gbc_lblAddrToConfirmed);
        
        btnS.setIcon(style.arrowUpDownButton());
        GridBagConstraints gbc_btnS = new GridBagConstraints();
        gbc_btnS.fill = GridBagConstraints.BOTH;
        gbc_btnS.gridheight = 2;
        gbc_btnS.insets = new Insets(0, 0, 5, 5);
        gbc_btnS.gridx = 5;
        gbc_btnS.gridy = 1;
        panelCenter.add(btnS, gbc_btnS);
        
        JLabel lblTo = new JLabel("To:");
        GridBagConstraints gbc_lblTo = new GridBagConstraints();
        gbc_lblTo.anchor = GridBagConstraints.EAST;
        gbc_lblTo.insets = new Insets(0, 0, 5, 5);
        gbc_lblTo.gridx = 0;
        gbc_lblTo.gridy = 2;
        panelCenter.add(lblTo, gbc_lblTo);
        
        txtAddreTo = new JTextField();
        txtAddreTo.setText(txtAddreToSetField);
        GridBagConstraints gbc_txtAddreto = new GridBagConstraints();
        gbc_txtAddreto.gridwidth = 3;
        gbc_txtAddreto.insets = new Insets(0, 0, 5, 5);
        gbc_txtAddreto.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtAddreto.gridx = 1;
        gbc_txtAddreto.gridy = 2;
        panelCenter.add(txtAddreTo, gbc_txtAddreto);
        txtAddreTo.setColumns(10);
        
        JButton btnFind = new JButton("Find Route");
        GridBagConstraints gbc_btnFind = new GridBagConstraints();
        gbc_btnFind.insets = new Insets(0, 0, 0, 5);
        gbc_btnFind.gridx = 3;
        gbc_btnFind.gridy = 3;
        panelCenter.add(btnFind, gbc_btnFind);
    }
    
    private void swapToFromFields() {
    	String addrFromTemp = txtAddrFrom.getText();
    	txtAddrFrom.setText(txtAddreTo.getText());
    	txtAddreTo.setText(addrFromTemp);
	}

	private void initContentPanel(JPanel panel){
    	
    }
}
