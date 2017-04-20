package dk.itu.n.danmarkskort.backend;

import java.io.Serializable;

import dk.itu.n.danmarkskort.address.AddressHolder;
import dk.itu.n.danmarkskort.models.UserPreferences;

public class BinaryWrapper implements Serializable {
	private static final long serialVersionUID = -7588730983362489371L;
	
	private UserPreferences userPreferences;
	private OSMParser model;
	private AddressHolder addressHolder;
	
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}
	
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
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}
}
