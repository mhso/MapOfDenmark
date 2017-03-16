package dk.itu.n.danmarkskort.address;

public class AddressValidator {
	public AddressValidator(){
	}
	
	public String cleanAddress(String inputStr){
		inputStr = inputStr.replaceAll("\\s{2,}"," ") //replace spaces etc.
				.replaceAll("[\\t\\u002C\\u002E]"," ") //replace tabs, Comma, dots
				.replaceAll("[^0-9a-zA-ZæøåÆØÅáÁéÉèÈöÖ\\u002D\\s]","?")
				.replaceAll("\\s{2,}"," ").trim();
		System.out.println("Clean 1: "+inputStr);
		inputStr = parseAddressFloor(inputStr);
		System.out.println("Clean Floor: "+inputStr);
		inputStr = cleanAddressFloor(inputStr);
		System.out.println("Clean Floor: "+inputStr);
		inputStr = parseAddressSide(inputStr);
		System.out.println("Clean Side: "+inputStr);
		inputStr = cleanAddressSide(inputStr);
		System.out.println("Clean Side: "+inputStr);
		inputStr = insertDotAfterSingleChar(inputStr);
		inputStr = cleanSpaces(inputStr);
		System.out.println("Clean up: "+inputStr);
		return inputStr;
	}
	
	public String cleanSpaces(String inputStr){
		return inputStr.replaceAll("\\s{2,}"," ").trim(); //replace spaces etc.
	}
	
	public String insertDotAfterSingleChar(String inputStr){
		return inputStr.replaceAll("^([0-9]+)\\s", "$1. ") //replace single digit  "* " with "*. "
				.replaceAll("^([a-zA-ZæøåÆØÅáÁéÉèÈöÖ]{1})\\s", "$1. ") //replace single letter "* " with "*. "
				.replaceAll("\\s([a-zA-ZæøåÆØÅáÁéÉèÈöÖ]{1})\\s", " $1. ") //replace single letter " * " with " *. "
				.replaceAll("\\s([a-zA-ZæøåÆØÅáÁéÉèÈöÖ]{1})\\s", " $1. ") //replace single letter " * " with " *. "
				.replaceAll("\\b([a-zA-ZæøåÆØÅáÁéÉèÈöÖ]{2})\\b", " $1. "); //replace single letter " * " with " *. "
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
		return inputStr.replaceAll("(?i)\\b(kl|kælder|kælderen)\\b", "kl. ")
				.replaceAll("(?i)\\b(st|stue|stuen)\\b", "st. ")
				.replaceAll("([0-9])+(\\s{0,})(?i)(sal)\\b", "$1\\. sal ")
				.replaceAll("([0-9]+\\s[0-9]+)(\\s{0,})([a-zA-Z]{2})\\b", "$1\\. sal $3");
	}
	
	public String cleanAddressFloor(String inputStr){
		return inputStr.replaceAll("kl.", "")
				.replaceAll("st. ","")
				.replaceAll("([0-9])+(\\. sal) ","");
	}
}