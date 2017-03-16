package dk.itu.n.danmarkskort.address;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class AddressSearchPredicates {
	    public static Predicate<Address> streetStartsWith(String strInput) {
	    	System.out.println("String: "+strInput);
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
	        		&& StringUtils.getLevenshteinDistance(p.getStreet().toLowerCase(), strInput.toLowerCase()) > minValue
	        		&& StringUtils.getLevenshteinDistance(p.getStreet().toLowerCase(), strInput.toLowerCase()) < maxValue;
	    }
	    
	    public static Predicate<Address> streetEqualsHousenumberContains(Address addr) {
	        return p -> p.getStreet() != null && p.getStreet().equalsIgnoreCase(addr.getStreet())
	        		&& p.getHousenumber() != null && p.getHousenumber().toLowerCase().contains((addr.getHousenumber()));
	    }
	    
	    public static Predicate<Address> streetHousenumberEquals(Address addr) {
	        return p -> p.getStreet() != null && p.getStreet().equalsIgnoreCase(addr.getStreet())
	        		&& p.getHousenumber() != null && p.getHousenumber().equalsIgnoreCase(addr.getHousenumber().toLowerCase());
	    }
	    
	    public static Predicate<Address> postcodeEquals(String strInput) {
	        return p -> p.getPostcode() != -1 && Integer.toString(p.getPostcode()).equalsIgnoreCase(strInput);
	    }
	    
	    public static Predicate<Address> toStringShortContains(String strInput) {
	        return p -> p.toStringShort() != null && p.toStringShort().toLowerCase().contains(strInput.toLowerCase());
	    }
	    
	    public static Predicate<Address> toStringShortEquals(Address addr) {
	        return p -> p.toStringShort() != null && p.toStringShort().equalsIgnoreCase((addr.toStringShort()));
	    }
	     
	    public static List<Address> filterAddresses(Map<Long, Address> addresses, Predicate<Address> predicate, long limit) {
	    	if(addresses != null && predicate != null ) {
	    		return addresses.values().stream().filter(predicate).limit(limit).collect(Collectors.<Address>toList());
	        }
	        return null;
	    }
	    
	    public static List<String> toStringShort(List<Address> addresses){
	    	return addresses.stream().map(Address::toStringShort).collect(Collectors.<String>toList());
	    }
	    
	    public static List<String> filterToStringShort(Map<Long, Address> addresses, Predicate<Address> predicate, long limit){
	    	return toStringShort(filterAddresses(addresses, predicate, limit));
	    }
	    
	    public static boolean confirmStreetExist(Map<Long, Address> addresses, String find){
			return (filterAddresses(addresses, streetEquals(find), 1) != null);
		}
}
