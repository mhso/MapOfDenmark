package dk.itu.n.danmarkskort.backend;

import java.io.Serializable;

import dk.itu.n.danmarkskort.models.UserPreferences;

public class BinaryWrapper implements Serializable {
	private static final long serialVersionUID = -7588730983362489371L;
	
	private UserPreferences userPreferences;
	private OSMParser model;
	
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}
	
	public void setModel(OSMParser model) {
		this.model = model;
	}
	
	public OSMParser getModel() {
		return model;
	}
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}
}
