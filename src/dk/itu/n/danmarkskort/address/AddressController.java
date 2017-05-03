package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.TimerUtil;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.ParsedAddress;
import dk.itu.n.danmarkskort.routeplanner.RouteEdge;
import dk.itu.n.danmarkskort.routeplanner.RouteGraph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AddressController {
	private AddressHolder addressHolder = new AddressHolder();
	private int addressesNotAcceptedCount;
	private TimerUtil timerUtilA = new TimerUtil();
	private TimerUtil timerUtilB = new TimerUtil();
	private PostcodeCityBestMatch postcodeCityBestMatch;
	private AddressSuggestion addressSuggestion;
	private KDTree<Housenumber> housenumberTree;
	private boolean debug = false;
	
	public AddressController(){
		postcodeCityBestMatch = new PostcodeCityBestMatch();
		addressSuggestion = new AddressSuggestion();
	}
	
	public AddressHolder getAddressHolder() {
		return addressHolder;
	}
	
	public void setAddressHolder(AddressHolder holder) {
		addressHolder = holder;
	}
	
	public List<String> getSearchSuggestions(String find, long limitAmountOfResults){ return addressSuggestion.searchSuggestions(find, limitAmountOfResults); }
	public List<String> getSearchSuggestions(Point2D.Float find){
		List<String> result = new ArrayList<String>();
		Housenumber hn = searchHousenumberKDTree(find);
		if(hn != null) result.add(hn.toString());
		return result;
	}
	
	
	public Address getSearchResult(String find){
		Address addrBuild = AddressParser.parse(find, false);
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
	
	public Housenumber searchHousenumberKDTree(Point2D.Float lonLat){		
		if(lonLat != null) {
			Housenumber hn = housenumberTree.nearest(lonLat);
			if(hn != null) { System.out.println("RouteController found house: " + hn.toString()); }
			else { System.out.println("No edge found"); }
			return hn;
		}
		return null;
	}
	
	public void addAddress(Point2D.Float lonLat, String street, String housenumber, String postcode, String city){
		Postcode pc = addressHolder.postcodes.get(postcode);
		if(pc == null) pc = new Postcode(postcode, city);
		pc.addAddress(street, housenumber, lonLat);
		addressHolder.postcodes.put(postcode, pc);
		postcodeCityBestMatch.add(postcode,  city);
	}
	
	private int acceptLvl1 = 0, acceptLvl2 = 0, acceptNot = 0;
	public void addressParsed(ParsedAddress addr) {
		timerUtilA.on();
        if(addr != null) {
        	Point2D.Float lonLat = new Point2D.Float(addr.getFirstLon(), addr.getFirstLat());
	
			if(AddressValidator.isAddressMinimum(addr.getStreet(), addr.getHousenumber(), addr.getPostcode())){
				if(AddressValidator.isCityname(addr.getCity())) {
					addAddress(lonLat, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), addr.getCity());
				} else {
					addAddress(lonLat, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), null);
				}
				acceptLvl1++;
			}else if(AddressValidator.isAddressMinimum(
        				AddressValidator.prepStreetname(addr.getStreet()),
        				AddressValidator.prepHousenumber(addr.getHousenumber()),
        				AddressValidator.prepPostcode(addr.getPostcode())
        				)) {
        	addAddress(lonLat, AddressValidator.prepStreetname(addr.getStreet()),
    				AddressValidator.prepHousenumber(addr.getHousenumber()),
    				AddressValidator.prepPostcode(addr.getPostcode()), null);
        	acceptLvl2++;
			} else {
					acceptNot++;
					addressesNotAcceptedCount++;
         }        	
			lonLat = null;
        }
    }

	public void onLWParsingFinished() {
		timerUtilA.off();
		if(debug) System.out.print("acceptLvl1: " + acceptLvl1 + ", acceptLvl2: " + acceptLvl2 + ", acceptNot: " + acceptNot);
		Main.log("Addresses parse first to last time: " + timerUtilA.toString());
		timerUtilB.on();
		for(Entry<String, Postcode> entry : addressHolder.postcodes.entrySet()){
			entry.getValue().setCity(postcodeCityBestMatch.getMatch(entry.getKey()));
		}
		postcodeCityBestMatch.cleanup();
		
		makeTree();
		
		timerUtilB.off();
		Main.log("Addresses PostcodeCityCombination time: " + timerUtilB.toString());
		Main.log("Addresses (accepted): " + getAddressSize());
		Main.log("Addresses (not accepted): " + addressesNotAcceptedCount);		
	}
	
	public int getAddressSize() {
		return addressHolder.count();
	}
	
	private void makeTree(){
		List<Housenumber> housenumbers = addressHolder.getHousenumbers();
		System.out.println("AddressController -> MakeTree size: " + housenumbers.size());
		housenumberTree = new KDTreeNode<>(housenumbers);
	}
}