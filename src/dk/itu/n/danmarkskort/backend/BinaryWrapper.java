package dk.itu.n.danmarkskort.backend;

import java.io.Serializable;

import dk.itu.n.danmarkskort.address.AddressHolder;

public class BinaryWrapper implements Serializable {
	private static final long serialVersionUID = -7588730983362489371L;
	
	private OSMParser model;
	private AddressHolder addressHolder;
	
	public void setModel(OSMParser model) {
		this.model = model;
	}
	
	public OSMParser getModel() {
		return model;
	}
	
	public void setAddressHolder(AddressHolder addressHolder){
		this.addressHolder = addressHolder;
	}
	
	public AddressHolder getAddressHolder(){
		return addressHolder;
	}
}
