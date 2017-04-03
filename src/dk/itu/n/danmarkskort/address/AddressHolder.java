package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AddressHolder {
	public static HashMap<String, Postcode> postcodes = new HashMap<String, Postcode>();
	
	public AddressHolder(){
	}
	
	public static Postcode getPostcode(String postcode) {
		if(postcode == null) return null;
		return postcodes.get(postcode.toLowerCase());
	}

	public static List<Postcode> postcodeContains(String postcode){
		List<Postcode> list = new ArrayList<Postcode>();
		if(postcode == null) return list;
		for(Entry<String, Postcode> entry : postcodes.entrySet()){
			if(entry.getKey().contains(postcode.toLowerCase())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	public static List<Postcode> postcodeStartsWith(String postcode){
		List<Postcode> list = new ArrayList<Postcode>();
		if(postcode == null) return list;
		for(Entry<String, Postcode> entry : postcodes.entrySet()){
			if(entry.getKey().startsWith(postcode.toLowerCase())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	public static List<Postcode> cityContains(String city){
		List<Postcode> list = new ArrayList<Postcode>();
		if(city == null) return list;
		for(Entry<String, Postcode> entry : postcodes.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().contains(city.toLowerCase())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	public static List<Postcode> cityStartsWith(String city){
		List<Postcode> list = new ArrayList<Postcode>();
		if(city == null) return list;
		for(Entry<String, Postcode> entry : postcodes.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().startsWith(city.toLowerCase())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	public static List<Postcode> cityEquals(String city){
		List<Postcode> list = new ArrayList<Postcode>();
		if(city == null) return list;
		for(Entry<String, Postcode> entry : postcodes.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().equalsIgnoreCase(city)) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	public static List<Postcode> search(Address addr, Enums StreetType, Enums housenumberType, Enums postcodeType, Enums cityType){
		
		List<Postcode> searchPostcodes = new ArrayList<Postcode>();
		List<Postcode> searchPostcodeWithStreets = new ArrayList<Postcode>();
		
		System.out.println("searchPostcodes size: " + searchPostcodes.size());
		System.out.println("searchPostcodeWithStreets size: " + searchPostcodeWithStreets.size());
		
		if(postcodeType == Enums.NOT_IN_USE && cityType == Enums.NOT_IN_USE) {
			for(Postcode postcode : postcodes.values()){
				searchPostcodes.add(postcode);
			}
		}else{
				switch(postcodeType){
				case EQUALS:
						if(getPostcode(addr.getPostcode()) != null) searchPostcodes.add(getPostcode(addr.getPostcode()));
						System.out.println("postcodeType EQUALS");
					break;
				case STARTSWITH:
						searchPostcodes.addAll(postcodeStartsWith(addr.getPostcode()));
						System.out.println("postcodeType STARTSWITH");
					break;
				case CONTAINS:
						searchPostcodes.addAll(postcodeContains(addr.getPostcode()));
						System.out.println("postcodeType CONTAINS");
					break;
				case NOT_IN_USE:
					break;
				default:
					break;
				}
				
				switch(cityType){
				case EQUALS:
						searchPostcodes.addAll(cityEquals(addr.getPostcode()));
						System.out.println("postcodeType EQUALS");
					break;
				case STARTSWITH:
						searchPostcodes.addAll(cityStartsWith(addr.getPostcode()));
						System.out.println("postcodeType STARTSWITH");
					break;
				case CONTAINS:
						searchPostcodes.addAll(cityContains(addr.getPostcode()));
						System.out.println("postcodeType CONTAINS");
					break;
				case NOT_IN_USE:
					break;
				default:
					break;
				}
		}
		
		System.out.println("searchPostcodes size: " + searchPostcodes.size());
		System.out.println("searchPostcodeWithStreets size: " + searchPostcodeWithStreets.size());
		if(StreetType == Enums.NOT_IN_USE) {
			for(Postcode postcode : searchPostcodes){
				searchPostcodeWithStreets.add(postcode);
			}
		}else{
			for(Postcode postcode : searchPostcodes){
				if(StreetType == Enums.EQUALS){
					if(postcode.getStreet(addr.getStreet()) != null){
						Street street = postcode.getStreet(addr.getStreet());
						Postcode pc = new Postcode(postcode.getPostcode(), postcode.getCity());
						pc.getStreets().put(street.getStreet(), street);
						searchPostcodeWithStreets.add(pc);
						
					break;
				}else{
					switch(StreetType){
					case STARTSWITH:
						for(Street street : postcode.streetStartsWith(addr.getStreet())){
							Postcode pc = new Postcode(postcode.getPostcode(), postcode.getCity());
							pc.getStreets().put(street.getStreet(), street);
							searchPostcodeWithStreets.add(pc);
						}
						break;
					case CONTAINS:
						for(Street street : postcode.streetContains(addr.getStreet())){
							Postcode pc = new Postcode(postcode.getPostcode(), postcode.getCity());
							pc.getStreets().put(street.getStreet(), street);
							searchPostcodeWithStreets.add(pc);
						}
						break;
					case NOT_IN_USE:
						break;
					default:
						break;
					}
				}
			}
		}
			System.out.println("searchPostcodes size: " + searchPostcodes.size());
			System.out.println("searchPostcodeWithStreets size: " + searchPostcodeWithStreets.size());
			if(housenumberType == Enums.NOT_IN_USE) {

			}else{
				for(Postcode postcode : searchPostcodeWithStreets){
					
					for(Street street : postcode.getStreets().values()){
						List<Housenumber> list = new  ArrayList<Housenumber>();	
						if(housenumberType == Enums.EQUALS){
							if(street.getHousenumber(addr.getHousenumber()) != null){
								street.getHousenumbers().clear();	
							break;
						}else{
							switch(housenumberType){
							case STARTSWITH:
								list.addAll(street.housenumberStartsWith(addr.getHousenumber()));
								break;
							case CONTAINS:
								list.addAll(street.housenumberContains(addr.getHousenumber()));
								break;
							case NOT_IN_USE:
								break;
							default:
								break;
							}
							
							street.getHousenumbers().clear();
							for(Housenumber hn : list){
								street.getHousenumbers().put(hn.getHousenumber(), hn);
							}
						}
					}
				}
			}
		}
	}
		return searchPostcodeWithStreets;
	}
	
	public enum Enums {
		NOT_IN_USE, EQUALS, STARTSWITH, CONTAINS
	}
}
