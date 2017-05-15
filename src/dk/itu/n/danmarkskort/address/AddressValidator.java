package dk.itu.n.danmarkskort.address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;

public class AddressValidator {
	private final static String allowedAlphaSet = "a-zA-ZæøåÆØÅáÁéÉèÈöÖüÜëËÿŸäÄ";
	private final static String allowedCharSet = "\\u002D\\u0027\\u002F"+allowedAlphaSet;

	private final static String RGX_ALPHA = "\\.\\u002D\\u0027"+allowedAlphaSet+"\\s";
	private final static String RGX_ALPHA_NO = "[0-9]{0,3}\\.\\u002D\\u0027\\u002F"+allowedAlphaSet+"\\s";
	
	private final static String RGX_STREET = "(["+RGX_ALPHA_NO+"]{1,40})";
	private final static String RGX_MULTIPLEHOUSENUMBER = "([0-9]{1,3}[A-Z]{1}\\-[0-9]{1,3}[A-Z]{1})|([0-9]{1,3}[A-Z]{1}\\-[A-Z]{1})|([0-9]{1,3}\\-[0-9]{1,3})";
	private final static String RGX_HOUSENUMBER = RGX_MULTIPLEHOUSENUMBER + "|([0-9]{1,3}[A-Z]{1})|([0-9]{1,3})";
	
	private final static String RGX_POSTCODE = "(^[0-9]{4}$)";
	private final static String RGX_CITY = "(^(["+RGX_ALPHA+"]{1,34})$)";
	
	private final static Pattern PAT_STREET = Pattern.compile(RGX_STREET);
	private final static Pattern PAT_HOUSENUMBER = Pattern.compile(RGX_HOUSENUMBER);
	private final static Pattern PAT_POSTCODE = Pattern.compile(RGX_POSTCODE);
	private final static Pattern PAT_CITY = Pattern.compile(RGX_CITY);

	/**
	 * Method takes and a unclean string, and perform different actions to ensure that the string,
	 * conforms to a specific pattern.
	 * @param inputStr unclean string
	 * @return a cleaned address string
	 */
	public static String cleanAddress(String inputStr){
		inputStr = cleanExcessSpaces(inputStr);
		inputStr = inputStr.replaceAll("[\\t\\u002C\\u002E]"," ") //replace tabs, Comma, dots
				.replaceAll("[^0-9"+allowedCharSet+"\\s]","?"); //replace all non allowed char with "?"
		inputStr = replaceDashSpaces(inputStr);
		inputStr = cleanExcessSpaces(inputStr);
		inputStr = parseAddressFloor(inputStr);
		inputStr = cleanAddressFloor(inputStr);
		inputStr = parseAddressSide(inputStr);
		inputStr = cleanAddressSide(inputStr);
		inputStr = replaceSpaceInHouseNumber(inputStr);
		inputStr = insertDotAfterSingleChar(inputStr);
		inputStr = insertDotAfterDoubleChar(inputStr);
		inputStr = removeEndingDot(inputStr);
		inputStr = capitalizeFully(inputStr);
		inputStr = cleanExcessSpaces(inputStr);
		return inputStr;
	}

	private static String cleanExcessSpaces(String inputStr){
		return inputStr.replaceAll("\\s{2,}"," ").trim(); //replace spaces etc.
	}
	
	private static String removeSpaces(String inputStr){
		return inputStr.replaceAll("\\s",""); //replace spaces etc.
	}

	private static String insertDotAfterSingleChar(String inputStr){
		return inputStr.replaceAll("^([0-9]{1,3})\\s", "$1. ") //replace single digit  "* " with "*. "
				.replaceAll("\\s(["+allowedAlphaSet+"]{1})\\s", " $1. ") //replace single letter " * " with " *. "
				.replaceAll("^(["+allowedAlphaSet+"]{1})\\s", " $1. "); //replace single letter " * " with " *. "
	}
	
	private static String replaceDashSpaces(String inputStr){
		return inputStr = inputStr.replaceAll("([\\s]*\\-[\\s]*)","-");
	}
	
	private static String replaceContractNumAlpha(String inputStr){
		return inputStr
				.replaceAll("([0-9]{1,3})([\\s]*)([A-Z\\-]{1})", "$1$3"); //Remove spaces, etc. "4 A" -> "4A"
	}
	
	private static String removeEverythingAfterFirstSpace(String inputStr){
		if(inputStr.contains(" ")) inputStr = inputStr.substring(0,  inputStr.indexOf(" "));
		return inputStr;
	}
	
	private static String replaceSpaceInHouseNumber(String inputStr){
		return inputStr
				.replaceAll("(\\s[0-9]{1,3})\\s([a-zA-Z\\-]{1,4}\\s)", "$1$2") //Remove spaces, etc. "4 A" -> "4A"
				.replaceAll("(\\s[0-9]{1,3})\\s([0-9]{1,3}\\s)", "$1\\-$2"); //Remove spaces, etc. "14 16" -> "14-16"
	}
	
	private static String insertDotAfterDoubleChar(String inputStr){
		return inputStr
				.replaceAll("\\b(["+allowedAlphaSet+"]{2})\\s", " $1. "); //replace double letter " * " with " *. "
	}
	
	private static String removeEndingDot(String inputStr){
		inputStr = cleanExcessSpaces(inputStr);
		if(inputStr.endsWith("\\u002E")) return inputStr.substring(0, inputStr.length()-1);
		return inputStr;
	}

	private static String parseAddressSide(String inputStr){
		return inputStr.replaceAll("(?i)\\b(tv|til venstre|venstre)\\b", "tv. ")
				.replaceAll("(?i)\\b((mf|midt for|midt))\\b", "mf. ")
				.replaceAll("(?i)\\b(th|til højre|højre)\\b", "th. ");
	}

	private static String cleanAddressSide(String inputStr){
		return inputStr.replaceAll("tv\\.", "")
				.replaceAll("mf\\.", "")
				.replaceAll("th\\.", "");
	}

	private static String parseAddressFloor(String inputStr){
		return inputStr.replaceAll("(?i)\\b(kl|kld|kælder|kælderen)\\b", "kl. ")
				.replaceAll("(?i)\\b(st|stue|stuen)\\b", "st. ")
				.replaceAll("([0-9])+(\\s{0,})(?i)(sal|floor|etage)\\b", "$1\\. sal ")
				.replaceAll("(([0-9][a-zA-z]{0,3})+\\s[0-9]+)(\\s{0,})([a-zA-Z]{2})\\b", "$1\\. sal $3");
	}

	private static String cleanAddressFloor(String inputStr){
		return inputStr.replaceAll("kl\\.", "")
				.replaceAll("st\\. ","")
				.replaceAll("([0-9]{1,2})+(\\. sal) ","");
	}
	
	private static final Pattern PAT_EXTRACTPOSTCODE = Pattern.compile("(.*)(RGX_POSTCODE)(.*)");
	private static String extractPostcode(String inputStr){
		return PAT_EXTRACTPOSTCODE.matcher(inputStr).replaceAll("$2");
	}
	
	private static final Pattern PAT_FINDPOSTCODE = Pattern.compile("(.*)(?<postcode>[0-9]{4})(.*)");
	public static String findPostcode(String inputStr){
		Matcher matcher = PAT_FINDPOSTCODE.matcher(inputStr);
		String postcode = null;
		while(matcher.find()){
			postcode = matcher.group("postcode");
		}
		return postcode;
	}
	
	/**
	 * Capitalize first letter in every word in a string.
	 * 
	 * @param inputStr
	 * @return Capitalize first letter in words 
	 */
	private static String capitalizeFully(String inputStr){
		return WordUtils.capitalizeFully(inputStr);
	}
	
	private static final Pattern PAT_SPLITUPPERCASELETTERS = Pattern.compile("([A-Z]+)([A-Z]+)");
	private static String splitUppercaseLetters(String inputStr){
		return PAT_SPLITUPPERCASELETTERS.matcher(inputStr).replaceAll("$1\\-$2");
	}
	
	public static String prepStreetname(String street){
		if(street != null && !street.isEmpty()) return cleanAddress(street);
		return null;
	}
	
	public static String prepHousenumber(String number){
		String prepStr = number;
		if(number != null){
			prepStr = prepStr.toUpperCase();
			if(prepStr.matches("[0-9]{1,3}[A-Z]{0,1}")){
				return prepStr;
			}
		
			if(prepStr != null && !prepStr.isEmpty()) {
				prepStr = prepStr.toUpperCase();
				prepStr = prepStr.replaceAll("[^0-9A-Z\\s\\-]", " ");
				prepStr = cleanExcessSpaces(prepStr);
				prepStr = parseAddressSide(prepStr);
				prepStr = cleanAddressSide(prepStr);
				prepStr = parseAddressFloor(prepStr);
				prepStr = cleanAddressFloor(prepStr);
				prepStr = replaceContractNumAlpha(prepStr);
				prepStr = removeEverythingAfterFirstSpace(prepStr);
				return splitUppercaseLetters(prepStr);
			}
		}
		return null;
	}
	
	public static String prepPostcode(String postcode){
		if(postcode != null && !postcode.isEmpty()) return removeSpaces(extractPostcode(postcode));
		return null;
	}
	
	public static String prepCityname(String city){
		if(city != null && !city.isEmpty()) return capitalizeFully(cleanExcessSpaces(city));
		return null;
	}
	
	public static boolean isAddressComplete(String street, String number, String postcode, String city){
		if(isPostcode(postcode) && isCityname(city) && isStreetname(street) && isHousenumber(number)) return true;
		return false;
	}
	
	public static boolean isAddressMinimum(String street, String number, String postcode){
		if(isPostcode(postcode) && isStreetname(street) && isHousenumber(number)) return true;
		return false;
	}
	
	private static boolean isStreetname(String street){
		if(street != null && !street.isEmpty()){
			Matcher matcher = PAT_STREET.matcher(street);
			if(matcher.matches()) return true;
		}
		return false;
	}
	
	private static boolean isHousenumber(String number){
		if(number != null && !number.isEmpty()){
			Matcher matcher = PAT_HOUSENUMBER.matcher(number);
			if(matcher.matches()) return true;
		}
		return false;
	}
	
	private static boolean isPostcode(String postcode){
		if(postcode != null && !postcode.isEmpty()) {
			Matcher matcher = PAT_POSTCODE.matcher(postcode);
			if(matcher.matches()) { 
				return true; }
		}
		return false;
	}
	
	public static boolean isCityname(String city){
		if(city != null && !city.isEmpty()){
			Matcher matcher = PAT_CITY.matcher(city);
			if(matcher.matches()) return true;
		} 
		return false;
	}
}