package dk.itu.n.danmarkskort.address;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class AddressSearchPredicates {
	    public static Predicate<Address> streetStartsWith(String strInput) {
	    	
	        return p ->  p.getStreet() != null && p.getStreet().toLowerCase().startsWith(strInput.toLowerCase());
	    }
	    
	    public static Predicate<Address> streetContains(String strInput) {
	        return p -> p.getStreet() != null && p.getStreet().toLowerCase().contains(strInput.toLowerCase());
	    }
	     
	    public static Predicate<Address> streetEquals(String strInput) {
	        return p -> p.getStreet() != null && p.getStreet().equalsIgnoreCase(strInput);
	    }
	     
	    public static Predicate<Address> streetLevenshteinDistance(String strInput, int minValue, int maxValue) {
	        return p -> p.getStreet() != null 
	        		&& StringUtils.getLevenshteinDistance(
	        				p.getStreet().toLowerCase(), strInput.toLowerCase()) > minValue   		
	        		&& StringUtils.getLevenshteinDistance(
	        				p.getStreet().toLowerCase(), strInput.toLowerCase()) < maxValue;
	    }
	    
	    public static Predicate<Address> streetHousenumberLevenshteinDistance(Address addr, 
	    		int minValueStreet, int maxValueStreet, 
	    		int minValueHousenumber, int maxValueHouseNumber) {
	        return p -> p.getStreet() != null 
	        		&& StringUtils.getLevenshteinDistance(
	        				p.getStreet().toLowerCase(), addr.getStreet().toLowerCase()) > minValueStreet   		
	        		&& StringUtils.getLevenshteinDistance(
	        				p.getStreet().toLowerCase(), addr.getStreet().toLowerCase()) < maxValueStreet
	        		&& p.getHousenumber() != null 
	    	        && StringUtils.getLevenshteinDistance(
	    	        		p.getHousenumber().toLowerCase(), addr.getHousenumber().toLowerCase()) > minValueHousenumber   		
	    	        && StringUtils.getLevenshteinDistance(
	    	        		p.getHousenumber().toLowerCase(), addr.getHousenumber().toLowerCase()) < maxValueHouseNumber;
	    }
	    
	    public static Predicate<Address> streetEqualsHousenumberStartsWith(Address addr) {
	        return p -> p.getStreet() != null 
	        		&& p.getStreet().equalsIgnoreCase(addr.getStreet())
	        		&& p.getHousenumber() != null 
	        		&& p.getHousenumber().toLowerCase().contains((addr.getHousenumber()));
	    }
	    
	    public static Predicate<Address> streetEqualsHousenumberContains(Address addr) {
	        return p -> p.getStreet() != null 
	        		&& p.getStreet().equalsIgnoreCase(addr.getStreet())
	        		&& p.getHousenumber() != null 
	        		&& p.getHousenumber().toLowerCase().startsWith((addr.getHousenumber()));
	    }
	    
	    public static Predicate<Address> streetHousenumberEquals(Address addr) {
	        return p -> p.getStreet() != null 
	        		&& p.getStreet().equalsIgnoreCase(addr.getStreet())
	        		&& p.getHousenumber() != null 
	        		&& p.getHousenumber().equalsIgnoreCase(addr.getHousenumber().toLowerCase());
	    }
	    
	    public static Predicate<Address> postcodeEquals(String strInput) {
	        return p -> p.getPostcode() != null
	        		&& p.getPostcode().equalsIgnoreCase(strInput);
	    }
	    
	    public static Predicate<Address> toStringShortContains(String strInput) {
	        return p -> p.toStringShort() != null 
	        		&& p.toStringShort().toLowerCase().contains(strInput.toLowerCase());
	    }
	    
	    public static Predicate<Address> toStringShortEquals(Address addr) {
	        return p -> p.toStringShort() != null 
	        		&& p.toStringShort().equalsIgnoreCase((addr.toStringShort()));
	    }
	     
	    public static List<Address> filterAddresses(Map<float[], Address> addresses, 
	    		Predicate<Address> predicate, long limit) {
			    	if(addresses != null && predicate != null ) {
			    		return addresses.values().stream().filter(predicate).limit(limit)
			    				.collect(Collectors.<Address>toList());
			        }
			    	return null;
	    }
	    
	    public static List<String> toStringShort(List<Address> addresses){
	    	return addresses.stream().map(Address::toStringShort).collect(Collectors.<String>toList());
	    }
	    
	    public static List<String> filterToStringShort(Map<float[], Address> addresses, 
	    		Predicate<Address> predicate, long limit){
	    			return toStringShort(filterAddresses(addresses, predicate, limit));
	    }
	    
	    public static boolean confirmStreetExist(Map<float[], Address> addresses, String find){
			return (filterAddresses(addresses, streetEquals(find), 1) != null);
		}
	    
	    public static Address addressEquals(Map<float[], Address> addresses, Address addr){
	    	//System.out.println(addr.toStringShort());
			List<Address> result = (filterAddresses(addresses, 
					toStringShortEquals(addr) , 1l));
			if(result != null && result.size() > 0) return result.get(0);
			return null;
	}
}
