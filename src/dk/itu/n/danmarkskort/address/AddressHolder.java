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
	
	public static int count(){
		int size = 0;
		for(Postcode pc : postcodes.values()){
			size += pc.count();
		}
		return size;
	}
	
	public static int count(List<Postcode> list){
		int size = 0;
		for(Postcode pc : list){
			size += pc.count();
		}
		return size;
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
	
	public static Map<String, Postcode> postcodeContains(Map<String, Postcode> inputList, String postcode){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		if(postcode == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(entry.getKey().contains(postcode.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
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
	
	public static Map<String, Postcode> postcodeStartsWith(Map<String, Postcode> inputList, String postcode){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		if(postcode == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(entry.getKey().startsWith(postcode.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
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
	
	public static Map<String, Postcode> cityContains(Map<String, Postcode> inputList, String city){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		if(inputList == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().contains(city.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
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
	
	public static Map<String, Postcode> cityStartsWith(Map<String, Postcode> inputList, String city){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		if(inputList == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().startsWith(city.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
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
	
	public static Map<String, Postcode> cityEquals(Map<String, Postcode> inputList, String city){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		if(inputList == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().equalsIgnoreCase(city)) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	public static Map<String, Postcode> searchPostcode(Map<String, Postcode> inputList, Address addr, SearchEnum streetType, SearchEnum cityType){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
			switch(streetType){
			case CONTAINS:
				list = postcodeContains(inputList, addr.getPostcode());
				break;
			case EQUALS:
				if(getPostcode(addr.getPostcode()) != null){
					Postcode pc = getPostcode(addr.getPostcode());
					list.put(pc.getPostcode(), pc);
				}
				break;
			case NOT_IN_USE:
				list = inputList;
				break;
			case STARTSWITH:
				list = postcodeStartsWith(inputList, addr.getPostcode());
				break;
			default:
				break;
			}
			
			list = searchCity(list, addr, cityType);
			
		return list;
	}
	
	public static Map<String, Postcode> searchCity(Map<String, Postcode> inputList, Address addr, SearchEnum cityType){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
			switch(cityType){
			case CONTAINS:
				list = postcodeContains(inputList, addr.getPostcode());
				break;
			case EQUALS:
				if(getPostcode(addr.getPostcode()) != null){
					Postcode pc = getPostcode(addr.getPostcode());
					list.put(pc.getPostcode(), pc);
				}
				break;
			case NOT_IN_USE:
				list = inputList;
				break;
			case STARTSWITH:
				list = postcodeStartsWith(inputList, addr.getPostcode());
				break;
			default:
				break;
		}
		return list;
	}
	
	public static List<Postcode> search(Address addr,
			SearchEnum streetType, SearchEnum housenumberType, SearchEnum postcodeType, SearchEnum cityType){
		
			for(Postcode pc : searchPostcode(postcodes, addr, postcodeType, cityType).values()){
				for(Street st : pc.search(pc.getStreets(), addr, streetType).values()){
					for(Housenumber hn : st.search(st.getHousenumbers(), addr, housenumberType).values()) {
						System.out.println(hn.toString());
					}
				}
			}
		return null;
	}
	
	public static List<Postcode> search3(Address addr, SearchEnum StreetType, SearchEnum housenumberType, SearchEnum postcodeType, SearchEnum cityType){
		List<Postcode> searchPostcodes = new ArrayList<Postcode>();
		List<Postcode> searchPostcodeWithStreets = new ArrayList<Postcode>();
		//System.out.println("search addr input: " + addr.toString());
		searchPostcodes = checkPostcodeCity(addr, postcodes, postcodeType, cityType);
		searchPostcodeWithStreets = streetCheck(addr, searchPostcodes, StreetType);
		searchPostcodeWithStreets = housenumberCheck(addr, searchPostcodeWithStreets, housenumberType);
		
		return searchPostcodeWithStreets;
	}
	
	private static List<Postcode> checkPostcodeCity(Address addr, Map<String, Postcode> inputList, SearchEnum postcodeType, SearchEnum cityType){
		List<Postcode> outputList = new ArrayList<Postcode>();
		if(postcodeType == SearchEnum.NOT_IN_USE && cityType == SearchEnum.NOT_IN_USE) {
			for(Postcode postcode : inputList.values()){
				outputList.add(postcode);
			}
		}else{
				switch(postcodeType){
				case EQUALS:
					if(getPostcode(addr.getPostcode()) != null) outputList.add(getPostcode(addr.getPostcode()));
					break;
				case STARTSWITH:
					outputList.addAll(postcodeStartsWith(addr.getPostcode()));
					break;
				case CONTAINS:
					outputList.addAll(postcodeContains(addr.getPostcode()));
					break;
				case NOT_IN_USE:
					break;
				default:
					break;
				}
				
				switch(cityType){
				case EQUALS:
					outputList.addAll(cityEquals(addr.getPostcode()));
					break;
				case STARTSWITH:
					outputList.addAll(cityStartsWith(addr.getPostcode()));
					break;
				case CONTAINS:
					outputList.addAll(cityContains(addr.getPostcode()));
					break;
				case NOT_IN_USE:
					break;
				default:
					break;
				}
		}
		//System.out.println("Total Address count: " + AddressHolder.count(outputList));
		//System.out.println("checkPostcodeCity output count: " + AddressHolder.count(outputList));
		return outputList;
	}
	
	private static List<Postcode> streetCheck(Address addr, List<Postcode> inputList, SearchEnum StreetType){
		List<Postcode> outputList = new ArrayList<Postcode>();
		if(StreetType == SearchEnum.NOT_IN_USE) {
			for(Postcode postcode : inputList){
				outputList.add(postcode);
			}
		}else{
			Postcode pc;
			for(Postcode postcode : inputList){
				if(StreetType == SearchEnum.EQUALS){
					if(postcode.getStreet(addr.getStreet()) != null){
						Street street = postcode.getStreet(addr.getStreet());
						pc = new Postcode(postcode.getPostcode(), postcode.getCity());
						pc.getStreets().put(street.getStreet(), street);
						outputList.add(pc);
						break;
					}
				}else{
					
					switch(StreetType){
						case STARTSWITH:
							pc = new Postcode(postcode.getPostcode(), postcode.getCity());
							for(Street street : postcode.streetStartsWith(addr.getStreet())){
								pc.getStreets().put(street.getStreet(), street);
								outputList.add(pc);
							}
							break;
						case CONTAINS:
							pc = new Postcode(postcode.getPostcode(), postcode.getCity());
							for(Street street : postcode.streetContains(addr.getStreet())){
								pc.getStreets().put(street.getStreet(), street);
								outputList.add(pc);
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
		//System.out.println("streetCheck input count: " + AddressHolder.count(inputList));
		//System.out.println("streetCheck output count: " + AddressHolder.count(outputList));
	return outputList;
	}

	private static List<Postcode> housenumberCheck(Address addr, List<Postcode> inOutList, SearchEnum housenumberType){
		List<Postcode> outputList = new ArrayList<Postcode>();
		if(housenumberType == SearchEnum.NOT_IN_USE) {

		}else{
			Postcode pc;
		for(Postcode postcode : inOutList){
			for(Street street : postcode.getStreets().values()){
				List<Housenumber> list = new  ArrayList<Housenumber>();	
				if(housenumberType == SearchEnum.EQUALS){
					if(street.getHousenumber(addr.getHousenumber()) != null){
						pc = new Postcode(postcode.getPostcode(), postcode.getCity());
						Street st = new Street(pc, street.getStreet());
						Housenumber hn = street.getHousenumber(addr.getHousenumber());
						st.addHousenumber(hn.getHousenumber(), hn.getLonLat());	
						outputList.add(pc);
						break;
					}
				}else{
					
					pc = new Postcode(postcode.getPostcode(), postcode.getCity());
					Street st = new Street(pc, street.getStreet());
					
					switch(housenumberType){
					case STARTSWITH:
						for(Housenumber hn : street.housenumberStartsWith(addr.getHousenumber())){
							st.addHousenumber(hn.getHousenumber(), hn.getLonLat());							
						}
						pc.getStreets().put(street.getStreet(), street);
						outputList.add(pc);
						break;
					case CONTAINS:
						for(Housenumber hn : street.housenumberContains(addr.getHousenumber())){
							st.addHousenumber(hn.getHousenumber(), hn.getLonLat());							
						}
						pc.getStreets().put(street.getStreet(), street);
						outputList.add(pc);
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
		inOutList.parallelStream().distinct().limit(10l).collect(Collectors.toList());
	return inOutList;
	}
}
