package dk.itu.n.danmarkskort.models;

import java.io.Serializable;

public class UserPreferences implements Serializable {
	private static final long serialVersionUID = -447699241270042144L;
	
	private String currentMapTheme;
	private String currentGUITheme;
	private boolean maximizeOnStartup;
	
	private String defaultTheme = "Basic";
	private String defaultMapFile = "map.bin";
	
	public void setCurrentMapTheme(String currentMapTheme) { this.currentMapTheme = currentMapTheme; }
	public void setCurrentGUITheme(String currentGUITheme) { this.currentGUITheme = currentGUITheme; }
	public void setMaximizeOnStartup(boolean maximizeOnStartup) { this.maximizeOnStartup = maximizeOnStartup; }
	public void setDefaultTheme(String defaultTheme) { this.defaultTheme = defaultTheme; }
	public void setDefaultMapFile(String defaultMapFile) { this.defaultMapFile = defaultMapFile; }
	
	public String getCurrentMapTheme() { return currentMapTheme; }
	public String getCurrentGUITheme() { return currentGUITheme; }
	public boolean isMaximizeOnStartup() { return maximizeOnStartup; }
	public String getDefaultTheme() { return defaultTheme; }
	public String getDefaultMapFile() { return defaultMapFile; }
}
