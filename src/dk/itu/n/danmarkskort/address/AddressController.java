package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.TimerUtil;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.ParsedAddress;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	
	public List<String> getSearchSuggestions(String find, long limitAmountOfResults){ 
		return addressSuggestion.searchSuggestions(find, limitAmountOfResults);
	}
	public List<String> getSearchSuggestions(Point2D.Float find){
		List<String> result = new ArrayList<String>();
		Housenumber hn = searchHousenumberKDTree(find);
		if(hn != null) result.add(hn.toString());
		return result;
	}
	
	
	public Address getSearchResult(String find){
		Address addrBuild = AddressParser.parse(find, false);
		if(addrBuild == null) return null;
		
			Map<String, Postcode> list;
			list = addressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS);
			if(list.size() < 1) { list = addressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY);
			}
			if(list.size() < 1) { list = addressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.EQUALS);
			}
			if(list.size() < 1) { list = addressHolder.search(addrBuild,
					SearchEnum.LEVENSHTEIN, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.EQUALS);
			}
			if(list.size() < 1) { list = addressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.LEVENSHTEIN);
			}
			
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

	public Address getSearchResult(Point2D.Float find){
		Housenumber hn = searchHousenumberKDTree(find);
		if(hn == null) return null;
		Address addr = AddressParser.parse(hn.toString(), false);
		addr.setLonLat(hn.getLonLat());
		return addr;
	}
	
	private Housenumber searchHousenumberKDTree(Point2D.Float lonLat){		
		if(lonLat != null) {
			Housenumber hn = housenumberTree.nearest(lonLat);
			if(hn != null) { Main.log("RouteController found house: " + hn.toString()); }
			else { Main.log("No edge found"); }
			return hn;
		}
		return null;
	}
	
	public void addAddress(Point2D.Float lonLat, String street, String housenumber, String postcode, String city){
		Postcode pc = addressHolder.postcodes.get(postcode);
		if(pc == null) pc = new Postcode(postcode, city);
		pc.addAddress(street, housenumber, lonLat);
		addressHolder.postcodes.put(postcode, pc);
		postcodeCityBestMatch.add(postcode, city);
	}
	
	public int getAddressSize() { return addressHolder.count(); }
	
	public void addressParsed(ParsedAddress addr) {
        if(addr != null) {
			if(AddressValidator.isAddressMinimum(addr.getStreet(), addr.getHousenumber(), addr.getPostcode())){
				if(AddressValidator.isCityname(addr.getCity())) {
					addAddress(addr, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), addr.getCity());
				}else{
					addAddress(addr, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), null);
				}
			}else if(AddressValidator.isAddressMinimum(
	        			AddressValidator.prepStreetname(addr.getStreet()),
	        			AddressValidator.prepHousenumber(addr.getHousenumber()),
	        			AddressValidator.prepPostcode(addr.getPostcode())
	        				)) {
					addAddress(addr, AddressValidator.prepStreetname(addr.getStreet()),
					AddressValidator.prepHousenumber(addr.getHousenumber()),
					AddressValidator.prepPostcode(addr.getPostcode()), null);
			} else {
					addressesNotAcceptedCount++;
			}
        }
    }

	public void onLWParsingFinished() {
		postcodeCityBestMatch();
		makeTree();
		
		Main.log("Addresses (accepted): " + getAddressSize());
		Main.log("Addresses (not accepted): " + addressesNotAcceptedCount);	
		Main.log("Postcodes size: " + addressHolder.postcodes.size());
	}

	private void postcodeCityBestMatch() {
		for(Entry<String, Postcode> entry : addressHolder.postcodes.entrySet()){
			entry.getValue().setCity(postcodeCityBestMatch.getMatch(entry.getKey()));
		}
		postcodeCityBestMatch.cleanup();
	}
	
	private void makeTree(){
		List<Housenumber> housenumbers = addressHolder.getHousenumbers();
		Main.log("AddressController -> MakeTree size: " + housenumbers.size());
		housenumberTree = new KDTreeNode<>(housenumbers, 1000);
	}
}