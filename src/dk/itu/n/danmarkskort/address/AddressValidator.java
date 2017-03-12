package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddressValidator {
	public AddressValidator(){
	}
	
	public String cleanAddress(String inputStr){
		System.out.println("Replace Side: "+cleanAddressSide(inputStr));
		inputStr = inputStr.replaceAll("\\s{2,}"," ") //replace spaces etc.
				.replaceAll("[\\t\\u002C\\u002E]"," ") //replace tabs, Comma, dots
				.replaceAll("[^0-9a-zA-ZÊ¯Â∆ÿ≈·¡È…Ë»ˆ÷\\u002D\\s]","?")
				.replaceAll("\\s{2,}"," ").trim();
		inputStr = parseAddressSide(inputStr);
		inputStr = parseAddressFloor(inputStr);
		return inputStr;
	}
	
	public String cleanSpaces(String inputStr){
		return inputStr.replaceAll("\\s{2,}"," ").trim(); //replace spaces etc.
	}
	
	public String insertDotAfterSingleChar(String inputStr){
		return inputStr.replaceAll("^([0-9]+)\\s", "$1. ") //replace single digit  "* " with "*. "
				.replaceAll("^([a-zA-ZÊ¯Â∆ÿ≈·¡È…Ë»ˆ÷]{1})\\s", "$1. ") //replace single letter "* " with "*. "
				.replaceAll("\\s([a-zA-ZÊ¯Â∆ÿ≈·¡È…Ë»ˆ÷]{1})\\s", " $1. ") //replace single letter " * " with " *. "
				.replaceAll("\\s([a-zA-ZÊ¯Â∆ÿ≈·¡È…Ë»ˆ÷]{1})\\s", " $1. "); //replace single letter " * " with " *. "
	}
	
	public String parseAddressSide(String inputStr){
		return inputStr.replaceAll("(?i)\\b(tv|til venstre|venstre)\\b", "tv. ")
				.replaceAll("(?i)\\b(mf|midt for|midt)\\b", "mf. ")
				.replaceAll("(?i)\\b(th|til h¯jre|h¯jre)\\b", "th. ");
	}
	
	public String cleanAddressSide(String inputStr){
		return inputStr.replaceAll("tv.", "")
				.replaceAll("mf.", "")
				.replaceAll("th.", "");
	}
	
	public String parseAddressFloor(String inputStr){
		return inputStr.replaceAll("(?i)\\b(kl|kÊlder|kÊlderen)\\b", "kl. ")
				.replaceAll("(?i)\\b(st|stue|stuen)\\b", "st. ")
				.replaceAll("([0-9])+(\\s{0,})(?i)(sal)\\b", "$1\\. sal ");
	}
	
	public String cleanAddressFloor(String inputStr){
		return inputStr.replaceAll("kl.", "")
				.replaceAll("st. ","")
				.replaceAll("([0-9])+(\\. sal) ","");
	}

	public boolean confirmStreetname(String streetMatch) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean comparePostalcodeToCity(String postcodeMatch, String cityMatch) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getCityFromPostalcode(String postcodeMatch) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPostalcodeFromCity(String cityMatch) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> searchCityname(String city) {
		// TODO Auto-generated method stub
		return null;
	}
}
