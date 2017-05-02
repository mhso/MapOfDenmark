package dk.itu.n.danmarkskort.models;

import java.io.Serializable;

public class UserPreferences implements Serializable {
	private static final long serialVersionUID = -447699241270042144L;
	
	private String currentMapTheme;
	private String currentGUITheme;
	private boolean maximizeOnStartup;
	private boolean saveToBinary = true;
	private boolean forceParsing = false;
	private boolean highlightNearestRoad = true;
	
	private String defaultTheme = "Basic";
	private String defaultMapFile = "map.bin";
	
	public void setCurrentMapTheme(String currentMapTheme) { this.currentMapTheme = currentMapTheme; }
	public void setCurrentGUITheme(String currentGUITheme) { this.currentGUITheme = currentGUITheme; }
	public void setMaximizeOnStartup(boolean maximizeOnStartup) { this.maximizeOnStartup = maximizeOnStartup; }
	public void setForceParsing(boolean forceParsing) { this.forceParsing = forceParsing; }
	public void setDefaultTheme(String defaultTheme) { this.defaultTheme = defaultTheme; }
	public void setDefaultMapFile(String defaultMapFile) { this.defaultMapFile = defaultMapFile; }
	public void setHighlightNearestRoad(boolean highlightNearestRoad) { this.highlightNearestRoad = highlightNearestRoad; }
	
	public String getCurrentMapTheme() { return currentMapTheme; }
	public String getCurrentGUITheme() { return currentGUITheme; }
	public boolean isMaximizeOnStartup() { return maximizeOnStartup; }
	public boolean isForcingParsing() { return forceParsing; }
	public String getDefaultTheme() { return defaultTheme; }
	public String getDefaultMapFile() { return defaultMapFile; }
	public boolean isHighlightingNearestRoad() { return highlightNearestRoad; }
	public boolean isSavingToBinary() { return saveToBinary; }
	public void setSaveToBinary(boolean saveToBinary) { this.saveToBinary = saveToBinary; }
}
