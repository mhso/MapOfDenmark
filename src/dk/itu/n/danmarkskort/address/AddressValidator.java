package dk.itu.n.danmarkskort.address;

public class AddressValidator {
	private final String allowedAlphaSet = "a-zA-ZæøåÆØÅáÁéÉèÈöÖüÜëË";
	private final String allowedCharSet = "\\u002D\\u0027"+allowedAlphaSet;
	
	public AddressValidator(){
	}

	public String cleanAddress(String inputStr){
		inputStr = cleanSpaces(inputStr);
		inputStr = inputStr.replaceAll("[\\t\\u002C\\u002E]"," ") //replace tabs, Comma, dots
				.replaceAll("[^0-9"+allowedCharSet+"\\u002D\\s]","?"); //replace all non allowed char with "?"
		inputStr = cleanSpaces(inputStr);
		//if(inputStr.contains("Batteri")) System.out.println(inputStr);
		inputStr = parseAddressFloor(inputStr);
		inputStr = cleanAddressFloor(inputStr);
		inputStr = parseAddressSide(inputStr);
		inputStr = cleanAddressSide(inputStr);
		inputStr = insertDotAfterSingleChar(inputStr);
		inputStr = insertDotAfterDoubleChar(inputStr);
		inputStr = removeEndingDot(inputStr);
		inputStr = cleanSpaces(inputStr);
		return inputStr;
	}

	public String cleanSpaces(String inputStr){
		return inputStr.replaceAll("\\s{2,}"," ").trim(); //replace spaces etc.
	}

	public String insertDotAfterSingleChar(String inputStr){
		return inputStr.replaceAll("^([0-9]+)\\s", "$1. ") //replace single digit  "* " with "*. "
				.replaceAll("\\b(["+allowedAlphaSet+"]{1})\\s", " $1. "); //replace single letter " * " with " *. "
	}
	
	public String insertDotAfterDoubleChar(String inputStr){
		return inputStr
				.replaceAll("\\b(["+allowedAlphaSet+"]{2})\\s", " $1. "); //replace double letter " * " with " *. "
	}
	
	public String removeEndingDot(String inputStr){
		inputStr = cleanSpaces(inputStr);
		if(inputStr.endsWith("\\u002E")) return inputStr.substring(0, inputStr.length()-1);
		return inputStr;
	}

	public String parseAddressSide(String inputStr){
		return inputStr.replaceAll("(?i)\\b(tv|til venstre|venstre)\\b", "tv. ")
				.replaceAll("(?i)\\b((mf|midt for|midt))\\b", "mf. ")
				.replaceAll("(?i)\\b(th|til højre|højre)\\b", "th. ");
	}

	public String cleanAddressSide(String inputStr){
		return inputStr.replaceAll("tv\\.", "")
				.replaceAll("mf\\.", "")
				.replaceAll("th\\.", "");
	}

	public String parseAddressFloor(String inputStr){
		return inputStr.replaceAll("(?i)\\b(kl|kld|kælder|kælderen)\\b", "kld. ")
				.replaceAll("(?i)\\b(st|stue|stuen)\\b", "st. ")
				.replaceAll("([0-9])+(\\s{0,})(?i)(sal)\\b", "$1\\. sal ")
				.replaceAll("([0-9]+\\s[0-9]+)(\\s{0,})([a-zA-Z]{2})\\b", "$1\\. sal $3");
	}

	public String cleanAddressFloor(String inputStr){
		return inputStr.replaceAll("kld\\.", "")
				.replaceAll("st\\. ","")
				.replaceAll("([0-9])+(\\. sal) ","");
	}
}