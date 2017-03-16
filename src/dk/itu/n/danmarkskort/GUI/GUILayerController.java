package dk.itu.n.danmarkskort.GUI;

import javax.swing.*;

public class GUILayerController extends JPanel{

    public GUILayerController() {
        setLayout(new OverlayLayout(this));
    }

	private static final long serialVersionUID = -7114065204222405886L;
	/*private ArrayList<Container> layers = new ArrayList<Container>();
	private Container parent;
	
	public GUILayerController(Container parent) {
		this.parent = parent;
		setLayout(null);
		setBounds(0, 0, parent.getWidth(), parent.getHeight());
		Main.window.addComponentListener(this);
	}
	
	public void addLayer(Container panel) {
		panel.setBounds(0, 0, getPreferredSize().width, getPreferredSize().height);
		add(panel);
		layers.add(panel);
		Main.window.pack();
	}
	
	public void setPreferredSize(Dimension d) {
		super.setPreferredSize(d);
		setBounds(0, 0, d.width, d.height);
		for(Container c : layers) {
			c.setPreferredSize(d);
			c.setBounds(0, 0, d.width, d.height);
		}
		Main.window.revalidate();
		Main.window.repaint();
	}
	
	public Container getParent() {
		return parent;
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		
		this.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}*/
	
}
