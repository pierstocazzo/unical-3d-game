package utils;

import java.net.URL;

public class Loader {
	
	public static URL load( String toLoad ) {
		return Loader.class.getClassLoader().getResource( toLoad );
	}
	
}
