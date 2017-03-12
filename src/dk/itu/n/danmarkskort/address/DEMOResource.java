package dk.itu.n.danmarkskort.address;

import java.io.InputStream;
import java.net.URL;

public class DEMOResource {
	public static InputStream loadStream(String path){
		InputStream input = DEMOResource.class.getResourceAsStream(path);
		if(input == null){
			input = DEMOResource.class.getResourceAsStream("/"+path);
		}
		return input;
	}
	
	public static URL loadURL(String path){
		URL input = DEMOResource.class.getResource(path);
		if(input == null){
			input = DEMOResource.class.getResource("/"+path);
		}
		return input;
	}
}
