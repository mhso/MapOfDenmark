package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddressValidator {
	private Map<String,String> postalcodeToCity = new HashMap<String,String>();
	private Map<String,String> cityToPostalcode = new HashMap<String,String>();
	private List<String> streetnames = new ArrayList<String>();
	
	public AddressValidator(){
		//postalcodeCityMapping();
	}
	
	public String cleanAddress(String inputStr){
		System.out.println("Replace Side: "+cleanAddressSide(inputStr));
		inputStr = inputStr.replaceAll("\\s{2,}"," ") //replace spaces etc.
				.replaceAll("[\\t\\u002C\\u002E]"," ") //replace tabs, Comma, dots
				.replaceAll("[^0-9a-zA-ZæøåÆØÅáÁéÉèÈöÖ\\u002D\\s]","?")
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
				.replaceAll("^([a-zA-ZæøåÆØÅáÁéÉèÈöÖ]{1})\\s", "$1. ") //replace single letter "* " with "*. "
				.replaceAll("\\s([a-zA-ZæøåÆØÅáÁéÉèÈöÖ]{1})\\s", " $1. ") //replace single letter " * " with " *. "
				.replaceAll("\\s([a-zA-ZæøåÆØÅáÁéÉèÈöÖ]{1})\\s", " $1. "); //replace single letter " * " with " *. "
	}
	
	public String parseAddressSide(String inputStr){
		return inputStr.replaceAll("(?i)\\b(tv|til venstre|venstre)\\b", "tv. ")
				.replaceAll("(?i)\\b(mf|midt for|midt)\\b", "mf. ")
				.replaceAll("(?i)\\b(th|til højre|højre)\\b", "th. ");
	}
	
	public String cleanAddressSide(String inputStr){
		return inputStr.replaceAll("tv.", "")
				.replaceAll("mf.", "")
				.replaceAll("th.", "");
	}
	
	public String parseAddressFloor(String inputStr){
		return inputStr.replaceAll("(?i)\\b(kl|kælder|kælderen)\\b", "kl. ")
				.replaceAll("(?i)\\b(st|stue|stuen)\\b", "st. ")
				.replaceAll("([0-9])+(\\s{0,})(?i)(sal)\\b", "$1\\. sal ");
	}
	
	public String cleanAddressFloor(String inputStr){
		return inputStr.replaceAll("kl.", "")
				.replaceAll("st. ","")
				.replaceAll("([0-9])+(\\. sal) ","");
	}
	
	public String getCityFromPostalcode(String postalcode){
		return postalcodeToCity.get(postalcode);
	}
	
	public String getPostalcodeFromCity(String cityName){
		return cityToPostalcode.get(cityName);
	}
	
	public boolean comparePostalcodeToCity(String postalcode, String cityName){
		if(getPostalcodeFromCity(cityName) != null && postalcode.compareTo(getPostalcodeFromCity(cityName)) == 0){
			if(getCityFromPostalcode(postalcode) != null && cityName.compareTo(getCityFromPostalcode(postalcode)) == 0){
				return true;
			}
		}
		return false;
	}
	
	/*
	private void postalcodeCityMapping(){
		for(String str : readfile("resources/postalcode_city.txt")){
			String[] strArr = str.split("_");
			if(strArr.length == 2){
				postalcodeToCity.put(strArr[0], strArr[1]);
				cityToPostalcode.put(strArr[1], strArr[0]);
				//System.out.println("Mapping: "+strArr[0]+"->"+strArr[1]);
			}
		}
	}
	*/
	
	public List<String> searchCityname(String cityname){
			return postalcodeToCity.values().stream().filter(it -> it.contains(cityname)).collect(Collectors.toList());
	}
	
	public List<String> getStreetnames(){
		if(streetnames.size() == 0){
			//streetnames = readfile("resources/streetnames.txt");
		}
		return streetnames;
	}
	
	public boolean confirmStreetname(String streetname){
		List<String> matches = getStreetnames().stream().filter(it -> it.contentEquals(streetname)).collect(Collectors.toList());
	    if(matches != null && matches.size() == 1){
	    	return true;
	    }
		return false;
	}
	
	public List<String> searchStreetname(String streetname){
		return getStreetnames().stream().filter(it -> it.contains(streetname)).collect(Collectors.toList());
	}
}
