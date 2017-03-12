package dk.itu.n.danmarkskort.address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressParser {
	public AddressParser() {
		
	}
	
	public Address parse(String inputAddress){
		
		final String RGX_ALPHA = "[\\.\\u002Da-zA-Z�������������� ]";
		final String RGX_POSTCODE = "(?<postcode>[0-9]{4})";
		
		final Pattern PAT_POSTCODE = Pattern.compile(RGX_POSTCODE);
		final Pattern PAT_STREET_HOUSE = Pattern.compile("^(?<street>([0-9]+\\s"+RGX_ALPHA+"+)|("+RGX_ALPHA+"+))\\s(?<housenumber>([0-9]{1,3}[a-zA-B]{1}\\s)|([0-9]{1,3}\\s))");
		final Pattern PAT_POSTCODE_CITY = Pattern.compile(""+RGX_POSTCODE+"\\s(?<city>"+RGX_ALPHA+"+$)");
		final Pattern PAT_STREET = Pattern.compile("^(?<street>([0-9]+\\s"+RGX_ALPHA+"+)|("+RGX_ALPHA+"+))");
		final Pattern PAT_CITY = Pattern.compile("(?<city>"+RGX_ALPHA+"+$)");
		final Pattern PAT_FLOOR = Pattern.compile("(?<floor>(kl.|st.|([0-9])+(\\.\\s(sal))))");
		final Pattern PAT_DOORSIDE = Pattern.compile("(?<side>(tv.|mf.|th.))");
		
		Address buildAddress = new Address();
		Address finalBuildAddress = new Address();
		AddressValidator addrVal = new AddressValidator();
		inputAddress = addrVal.cleanAddress(inputAddress);
		Matcher doorSidePat = PAT_DOORSIDE.matcher(inputAddress);
		if(doorSidePat.find()){
			
			Matcher allMatch = doorSidePat;
			String doorSideMatch = allMatch.group("side").trim();
			
			System.out.println("PAT_SIDE Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());

			finalBuildAddress.setDoorSide(doorSideMatch);
			buildAddress.setDoorSide(doorSideMatch);
			
			inputAddress = addrVal.cleanAddressSide(inputAddress);
			System.out.println("Clean side: "+inputAddress);
		}
		
		Matcher floorPat = PAT_FLOOR.matcher(inputAddress);
		if(floorPat.find()){
			
			Matcher allMatch = floorPat;
			String floorMatch = allMatch.group("floor").trim();
			
			System.out.println("PAT_FLOOR Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());

			finalBuildAddress.setFloor(floorMatch);
			buildAddress.setFloor(floorMatch);
			
			inputAddress = addrVal.cleanAddressFloor(inputAddress);
			System.out.println("Clean floor: "+inputAddress);
		}
		
		Matcher streetHousePat = PAT_STREET_HOUSE.matcher(inputAddress);
		if(streetHousePat.find()){
			
			Matcher allMatch = streetHousePat;
			String streetMatch = allMatch.group("street").trim();
			String housenumberMatch = allMatch.group("housenumber").trim();
			
			System.out.println("PAT_STREET_HOUSE Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());
			
			streetMatch = addrVal.insertDotAfterSingleChar(streetMatch);
			
			if(addrVal.confirmStreetname(streetMatch)){
				finalBuildAddress.setStreet(streetMatch);
			} else {
				streetMatch = streetMatch+"?";
			}
			
			buildAddress.setStreet(streetMatch);
			buildAddress.setHousenumber(housenumberMatch);
		}
		
		Matcher postcodeCityPat = PAT_POSTCODE_CITY.matcher(inputAddress);
		if(postcodeCityPat.find()){
			
			Matcher allMatch = postcodeCityPat;
			String postcodeMatch = allMatch.group("postcode").trim();
			String cityMatch = allMatch.group("city").trim();
			
			System.out.println("PAT_POSTCODE_CITY Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());
			
			if(addrVal.comparePostalcodeToCity(postcodeMatch, cityMatch)){
				finalBuildAddress.setPostcode(postcodeMatch);
				finalBuildAddress.setCity(cityMatch);
			}else{

				if(addrVal.getCityFromPostalcode(postcodeMatch) == null){
					postcodeMatch = postcodeMatch+"?";
				}
				if(addrVal.getPostalcodeFromCity(cityMatch) == null){
					cityMatch = cityMatch+"?";
				}
				if(addrVal.getCityFromPostalcode(postcodeMatch) != null && addrVal.getPostalcodeFromCity(cityMatch) != null){
					finalBuildAddress.setPostcode(postcodeMatch);
					finalBuildAddress.setCity(cityMatch);
				}
			}
			
			buildAddress.setPostcode(postcodeMatch);
			buildAddress.setCity(cityMatch);
		}
		
		Matcher postcodePat = PAT_POSTCODE.matcher(inputAddress);
		if(postcodePat.find()){
			
			Matcher allMatch = postcodePat;
			String postcodeMatch = allMatch.group("postcode").trim();
			String cityMatch = null;
			
			System.out.println("PAT_POSTCODE Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());

				if(addrVal.getCityFromPostalcode(postcodeMatch) != null){
					finalBuildAddress.setPostcode(postcodeMatch);
					cityMatch = addrVal.getCityFromPostalcode(postcodeMatch);
					finalBuildAddress.setCity(cityMatch);
					
				}else{
					postcodeMatch = postcodeMatch+"?";
				}
			
			buildAddress.setPostcode(postcodeMatch);
			buildAddress.setCity(cityMatch);
		}
		
		Matcher cityPat = PAT_CITY.matcher(inputAddress);
		if(cityPat.find()){
			
			Matcher allMatch = cityPat;
			String cityMatch = allMatch.group("city").trim();
			
			System.out.println("PAT_CITY Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());
			
				if(addrVal.getPostalcodeFromCity(cityMatch) == null){
					finalBuildAddress.setCity(cityMatch);
				}
			
			buildAddress.setCity(cityMatch);

		}			
		
		Matcher streetPat = PAT_STREET.matcher(inputAddress);
		if(streetPat.find()){
			
			Matcher allMatch = streetPat;
			String streetMatch = allMatch.group("street").trim();
			
			System.out.println("PAT_STREET Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());

			streetMatch = addrVal.insertDotAfterSingleChar(streetMatch);
			
			if(addrVal.confirmStreetname(streetMatch)){
				finalBuildAddress.setStreet(streetMatch);
			} else {
				if(addrVal.getPostalcodeFromCity(streetMatch) != null){
					finalBuildAddress.setCity(streetMatch);
					streetMatch = null;
				} else {
					streetMatch = streetMatch+"?";
				}
			}
			
			buildAddress.setStreet(streetMatch);
		}
			
		if(finalBuildAddress.getPostcode() != null && !addrVal.comparePostalcodeToCity(finalBuildAddress.getPostcode(), finalBuildAddress.getCity())){
			if(addrVal.getCityFromPostalcode(finalBuildAddress.getPostcode()) != null){
				finalBuildAddress.setCity(addrVal.getCityFromPostalcode(finalBuildAddress.getPostcode()));
			}
		}
		
		if(finalBuildAddress.getPostcode() != null && addrVal.searchCityname(finalBuildAddress.getCity()).size() == 1){
			finalBuildAddress.setCity(addrVal.getCityFromPostalcode(finalBuildAddress.getPostcode()));
		}
		
		if(finalBuildAddress.getStreet() == null){ finalBuildAddress.setStreet(buildAddress.getStreet()); }
		if(finalBuildAddress.getHousenumber() == null){ finalBuildAddress.setHousenumber(buildAddress.getHousenumber()); }
		if(finalBuildAddress.getFloor() == null){ finalBuildAddress.setFloor(buildAddress.getFloor()); }
		if(finalBuildAddress.getDoorSide() == null){ finalBuildAddress.setDoorSide(buildAddress.getDoorSide()); }
		if(finalBuildAddress.getPostcode() == null){ finalBuildAddress.setPostcode(buildAddress.getPostcode()); }
		if(finalBuildAddress.getCity() == null){ finalBuildAddress.setCity(buildAddress.getCity()); }
		
		return finalBuildAddress;
	}
	
	public double compareAdresses(Address addr1, Address addr2){
		int compareValue = 0;
		
		// Primary
		if(addr1.getPostcode().equals(addr2.getPostcode())){ compareValue++; }
		if(addr1.getCity().equals(addr2.getCity())){ compareValue++; }
		if(addr1.getCountry().equals(addr2.getCountry())){ compareValue++; }
		if(addr1.getStreet().equals(addr2.getStreet())){ compareValue++; }
		
		// Secondary
		if(addr1.getHousenumber().equals(addr2.getHousenumber())){ compareValue++; }
		
		// Third
		if(addr1.getHousename().equals(addr2.getHousename())){ compareValue++; }
		if(addr1.getFloor().equals(addr2.getFloor())){ compareValue++; }
		
		return 0;
	}
}