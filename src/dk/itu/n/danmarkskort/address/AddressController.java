package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.ParsedAddress;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class handles the management of creating and access of the address datatructure. 
 * @author Group N
 *
 */
public class AddressController {
	private AddressHolder addressHolder;
	private PostcodeCityBestMatch postcodeCityBestMatch;
	private AddressSuggestion addressSuggestion;
	private KDTree<Housenumber> housenumberTree;
	private int addressesNotAcceptedCount;
	
	public AddressController(){
		addressHolder = new AddressHolder();
		postcodeCityBestMatch = new PostcodeCityBestMatch();
		addressSuggestion = new AddressSuggestion();
	}
	
	public AddressHolder getAddressHolder() { return addressHolder; }
	public void setAddressHolder(AddressHolder holder) { addressHolder = holder; }
	
	public KDTree<Housenumber> gethousenumberTree(){ return housenumberTree; }
	public void setHousenumberTree(KDTree<Housenumber> housenumberTree){
		this.housenumberTree = housenumberTree;
	}
	
	/**
	 * Returns a list of search suggestions by string input
	 * 
	 * @param find, search string
	 * @param limitAmountOfResults limit number of return suggestions
	 * @return a list of suggestions or empty list if non.
	 */
	public List<String> getSearchSuggestions(String find, long limitAmountOfResults){ 
		return addressSuggestion.searchSuggestions(find, limitAmountOfResults);
	}
	
	/**
	 * Returns nearst address as single result, but in a list form.
	 * 
	 * @param find, search coords
	 * @return a list with nearst address suggestions or empty list if non.
	 */
	public List<String> getSearchSuggestions(Point2D.Float find){
		List<String> result = new ArrayList<String>();
		Housenumber hn = searchHousenumberKDTree(find);
		if(hn != null) result.add(hn.toString());
		return result;
	}
	
	/**
	 * Method used for searching for an address by a string value.
	 * 
	 * @param find, the address string as one line.
	 * @return an build Address Object from the result, null if nothing found.
	 */
	public Address getSearchResult(String find){
		Address addrBuild = AddressParser.parse(find, false);
		if(addrBuild == null) return null;
		
			Map<String, Postcode> list;
			// Look for exact match.
			list = addressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS);
			// Look for exact street, number, postcode, but any citys.
			if(list.size() < 1) { list = addressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY);
			}
			// Look for exact street, number, but any postcode and citys.
			if(list.size() < 1) { list = addressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.EQUALS);
			}
			// Uses levenshtein on street to find a streetname that is close to the spelled input.
			if(list.size() < 1) { list = addressHolder.search(addrBuild,
					SearchEnum.LEVENSHTEIN, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.EQUALS);
			}
			// Uses levenshtein on city as last atemp to find a result that is close to the input.
			if(list.size() < 1) { list = addressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.LEVENSHTEIN);
			}
			
			// Expect only one result
			if(list.size() == 1) {
				for(Postcode pc : list.values()){
					for(Street st : pc.getStreets().values()){
						for(Housenumber hn : st.getHousenumbers().values()){
							Address addr = AddressParser.parse(hn.toString(), false);
							addr.setLonLat(hn.getLonLat());
							return addr;
						}
					}
				}
			}
		return null;
	}

	/**
	 * Used for finding the nearst housenumber object, by (fake) coords.
	 * 
	 * @param find by coords input.
	 * @return an Address object, for the nearst house.
	 */
	public Address getSearchResultByCoords(Point2D.Float find){
		Housenumber hn = searchHousenumberKDTree(find);
		if(hn == null) return null;
		Address addr = AddressParser.parse(hn.toString(), false);
		addr.setLonLat(hn.getLonLat());
		return addr;
	}
	
	private Housenumber searchHousenumberKDTree(Point2D.Float lonLat){		
		if(lonLat != null) {
			Housenumber hn = housenumberTree.nearest(lonLat);
			return hn;
		}
		return null;
	}
	
	/**
	 * Adds an address to the address datastruture.
	 * (as Postcode -> Street -> Housenumber).
	 * 
	 * @param lonLat, address coords
	 * @param street, address streetname
	 * @param housenumber, address housenumber
	 * @param postcode, address postcode
	 * @param city, address cityname
	 */
	public void addAddress(Point2D.Float lonLat, String street, String housenumber, String postcode, String city){
		Postcode pc = addressHolder.postcodes.get(postcode);
		if(pc == null) pc = new Postcode(postcode, city);
		pc.addAddress(street, housenumber, lonLat);
		addressHolder.postcodes.put(postcode, pc);
		postcodeCityBestMatch.add(postcode, city); // Add for later postcode/city match update.
	}
	
	/**
	 * Calls calculations in the address structor to get the total amount of addresses.
	 * @return the total amount of addresses.
	 */
	public int getAddressSize() { return addressHolder.count(); }
	
	
	/**
	 * The method is called i the parsing process, it expects and complete address.
	 * Because it is made up of userinput we need to validate the values.
	 * 
	 * @param addr an object build in the OSM parsing process
	 */
	public void addressParsed(ParsedAddress addr) {
        if(addr != null) {
        	// Checks whether the input atleast contains a valid street, housenumber and postcode.
			if(AddressValidator.isAddressMinimum(addr.getStreet(), addr.getHousenumber(), addr.getPostcode())){
				// If valid city, accept city, else dont save it.
				if(AddressValidator.isCityname(addr.getCity())) {
					addAddress(addr, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), addr.getCity());
				}else{
					addAddress(addr, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), null);
				}
			} // If input is not valid at first, then try to prepare the input, and test again.
			else if(AddressValidator.isAddressMinimum(
	        			AddressValidator.prepStreetname(addr.getStreet()),
	        			AddressValidator.prepHousenumber(addr.getHousenumber()),
	        			AddressValidator.prepPostcode(addr.getPostcode())
	        				)) {
					addAddress(addr, AddressValidator.prepStreetname(addr.getStreet()),
					AddressValidator.prepHousenumber(addr.getHousenumber()),
					AddressValidator.prepPostcode(addr.getPostcode()), null);
			} // If input prep, did not work, discard the address as not valid. 
			else {
					addressesNotAcceptedCount++;
			}
        }
    }

	/**
	 * Method is run after parsing has finish, in this method we call other methods, 
	 * that is required after the parsing has finished.
	 */
	public void onLWParsingFinished() {
		postcodeCityBestMatch();
		makeTree();
		
		Main.log("Addresses (accepted): " + getAddressSize());
		Main.log("Addresses (not accepted): " + addressesNotAcceptedCount);	
		Main.log("Postcodes size: " + addressHolder.postcodes.size());
	}

	/**
	 * This method retrieves the best cityname match for a postcode, and updates the postcode object, with best matches.
	 * (if an address is missing a cityname or there is different spelling of the same city it is corrected here.)
	 */
	private void postcodeCityBestMatch() {
		for(Entry<String, Postcode> entry : addressHolder.postcodes.entrySet()){
			entry.getValue().setCity(postcodeCityBestMatch.getMatch(entry.getKey()));
		}
		postcodeCityBestMatch.cleanUp();
	}
	
	/**
	 * Method build a KDTree of all the addresses in the address structure.
	 */
	private void makeTree(){
		List<Housenumber> housenumbers = addressHolder.getHousenumbers();
		Main.log("AddressController -> MakeTree size: " + housenumbers.size());
		housenumberTree = new KDTreeNode<>(housenumbers, 1000); // Initializes a KDTree with leaf size of 1000 elements.
	}
}