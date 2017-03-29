package dk.itu.n.danmarkskort.address;

public class AddressValidator {
	private final static String allowedAlphaSet = "a-zA-ZæøåÆØÅáÁéÉèÈöÖüÜëË";
	private final static String allowedCharSet = "\\u002D\\u0027"+allowedAlphaSet;
	
	public AddressValidator(){
	}

	public static String cleanAddress(String inputStr){
		inputStr = cleanSpaces(inputStr);
		inputStr = inputStr.replaceAll("[\\t\\u002C\\u002E]"," ") //replace tabs, Comma, dots
				.replaceAll("[^0-9"+allowedCharSet+"\\s]","?")
				.replaceAll(" - ","-"); //replace all non allowed char with "?"
		inputStr = cleanSpaces(inputStr);
		inputStr = parseAddressFloor(inputStr);
		inputStr = cleanAddressFloor(inputStr);
		inputStr = parseAddressSide(inputStr);
		inputStr = cleanAddressSide(inputStr);
		inputStr = replaceSpaceInHouseNumber(inputStr);
		inputStr = insertDotAfterSingleChar(inputStr);
		inputStr = insertDotAfterDoubleChar(inputStr);
		inputStr = removeEndingDot(inputStr);
		inputStr = cleanSpaces(inputStr);
		return inputStr;
	}

	public static String cleanSpaces(String inputStr){
		return inputStr.replaceAll("\\s{2,}"," ").trim(); //replace spaces etc.
	}

	public static String insertDotAfterSingleChar(String inputStr){
		return inputStr.replaceAll("^([0-9]{1,3})\\s", "$1. ") //replace single digit  "* " with "*. "
				.replaceAll("\\t(["+allowedAlphaSet+"]{1})\\s", " $1. "); //replace single letter " * " with " *. "
	}
	
	public static String replaceSpaceInHouseNumber(String inputStr){
		return inputStr
				.replaceAll("(\\s[0-9]{1,3})\\s([a-zA-Z\\-]{1,4}\\s)", "$1$2") //Remove spaces, etc. "4 A" -> "4A"
				.replaceAll("(\\s[0-9]{1,3})\\s([0-9]{1,3}\\s)", "$1\\-$2"); //Remove spaces, etc. "14 16" -> "14-16"
	}
	
	public static String insertDotAfterDoubleChar(String inputStr){
		return inputStr
				.replaceAll("\\b(["+allowedAlphaSet+"]{2})\\s", " $1. "); //replace double letter " * " with " *. "
	}
	
	public static String removeEndingDot(String inputStr){
		inputStr = cleanSpaces(inputStr);
		if(inputStr.endsWith("\\u002E")) return inputStr.substring(0, inputStr.length()-1);
		return inputStr;
	}

	public static String parseAddressSide(String inputStr){
		return inputStr.replaceAll("(?i)\\b(tv|til venstre|venstre)\\b", "tv. ")
				.replaceAll("(?i)\\b((mf|midt for|midt))\\b", "mf. ")
				.replaceAll("(?i)\\b(th|til højre|højre)\\b", "th. ");
	}

	public static String cleanAddressSide(String inputStr){
		return inputStr.replaceAll("tv\\.", "")
				.replaceAll("mf\\.", "")
				.replaceAll("th\\.", "");
	}

	public static String parseAddressFloor(String inputStr){
		return inputStr.replaceAll("(?i)\\b(kl|kld|kælder|kælderen)\\b", "kld. ")
				.replaceAll("(?i)\\b(st|stue|stuen)\\b", "st. ")
				.replaceAll("([0-9])+(\\s{0,})(?i)(sal|floor)\\b", "$1\\. sal ")
				.replaceAll("(([0-9][a-zA-z]{0,3})+\\s[0-9]+)(\\s{0,})([a-zA-Z]{2})\\b", "$1\\. sal $3");
	}

	public static String cleanAddressFloor(String inputStr){
		return inputStr.replaceAll("kld\\.", "")
				.replaceAll("st\\. ","")
				.replaceAll("([0-9]{1,2})+(\\. sal) ","");
	}
}