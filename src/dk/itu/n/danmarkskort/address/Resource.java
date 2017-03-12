package dk.itu.n.danmarkskort.address;

import java.io.InputStream;
import java.net.URL;

public class Resource {
	public static InputStream loadStream(String path){
		InputStream input = Resource.class.getResourceAsStream(path);
		if(input == null){
			input = Resource.class.getResourceAsStream("/"+path);
		}
		return input;
	}
	
	public static URL loadURL(String path){
		URL input = Resource.class.getResource(path);
		if(input == null){
			input = Resource.class.getResource("/"+path);
		}
		return input;
	}
}
