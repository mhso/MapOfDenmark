package dk.itu.n.danmarkskort.address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressParser {
	public AddressParser() {
		
	}
	
	public Address parse(String inputAddress){
		final String RGX_ALPHA = "[\\.\\u002D\\u0027a-zA-ZæøåÆØÅáÁéÉèÈöÖüÜëË ]";
		final String RGX_POSTCODE = "(?<postcode>[0-9]{4})";
		final String RGX_MULTIPLEHOUSENUMBER = "([0-9]{1,3}[a-zA-Z]{1}\\-[0-9]{1,3}[a-zA-Z]{1})|([0-9]{1,3}\\-[0-9]{1,3})";
		final String RGX_HOUSENUMBER = RGX_MULTIPLEHOUSENUMBER + "|([0-9]{1,3}[a-zA-Z]{1})|([0-9]{1,3})";
		
		final Pattern PAT_POSTCODE = Pattern.compile(RGX_POSTCODE);
		final Pattern PAT_STREET_HOUSE = 
				Pattern.compile("^(?<street>([0-9]{1,3}+\\s"+RGX_ALPHA+"+)|("+RGX_ALPHA+"+))"
						+ "\\s(?<housenumber>("+RGX_HOUSENUMBER+"\\s)|("+RGX_HOUSENUMBER+"$))");
		final Pattern PAT_POSTCODE_CITY = Pattern.compile(""+RGX_POSTCODE+"\\s(?<city>"+RGX_ALPHA+"+$)");
		final Pattern PAT_STREET = Pattern.compile("^(?<street>([0-9]{1,3}\\s"+RGX_ALPHA+"+)|("+RGX_ALPHA+"+))");
		final Pattern PAT_CITY = Pattern.compile("(?<city>"+RGX_ALPHA+"+$)");
		final Pattern PAT_FLOOR = Pattern.compile("(?<floor>(kld.|st.|([0-9])+(\\.\\s(sal))))");
		final Pattern PAT_DOORSIDE = Pattern.compile("(?<side>(tv.|mf.|th.))");
		
		Address buildAddr = new Address();
		AddressValidator addrVal = new AddressValidator();
		inputAddress = addrVal.cleanAddress(inputAddress);
		Matcher doorSidePat = PAT_DOORSIDE.matcher(inputAddress);
		if(doorSidePat.find()){
			inputAddress = addrVal.cleanAddressSide(inputAddress);
			//System.out.println("Clean side: "+inputAddress);
		}
		
		Matcher floorPat = PAT_FLOOR.matcher(inputAddress);
		if(floorPat.find()){			
			inputAddress = addrVal.cleanAddressFloor(inputAddress);
			//System.out.println("Clean floor: "+inputAddress);
		}
		
		Matcher streetHousePat = PAT_STREET_HOUSE.matcher(inputAddress);
		if(streetHousePat.find()){
			Matcher allMatch = streetHousePat;
			String streetMatch = allMatch.group("street").trim();
			String housenumberMatch = allMatch.group("housenumber").trim();
			
			//System.out.println("PAT_STREET_HOUSE Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());
			
			streetMatch = addrVal.insertDotAfterSingleChar(streetMatch);
			
			buildAddr.setStreet(streetMatch);
			buildAddr.setHousenumber(housenumberMatch);
		}
		
		Matcher streetPat = PAT_STREET.matcher(inputAddress);
		if(streetPat.find()){
			Matcher allMatch = streetPat;
			String streetMatch = allMatch.group("street").trim();
			
			//System.out.println("PAT_STREET Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());

			streetMatch = addrVal.insertDotAfterSingleChar(streetMatch);
			buildAddr.setStreet(streetMatch);
		}
		
		Matcher postcodePat = PAT_POSTCODE.matcher(inputAddress);
		if(postcodePat.find()){
			Matcher allMatch = postcodePat;
			String postcodeMatch = allMatch.group("postcode").trim();
			String cityMatch = null;
			
			//System.out.println("PAT_POSTCODE Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());
			
			buildAddr.setPostcode(postcodeMatch);
			buildAddr.setCity(cityMatch);
		}
		
		Matcher cityPat = PAT_CITY.matcher(inputAddress);
		if(cityPat.find()){
			Matcher allMatch = cityPat;
			String cityMatch = allMatch.group("city").trim();
			
			//System.out.println("PAT_CITY Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());		
			buildAddr.setCity(cityMatch);
		}
		
		Matcher postcodeCityPat = PAT_POSTCODE_CITY.matcher(inputAddress);
		if(postcodeCityPat.find()){
			Matcher allMatch = postcodeCityPat;
			String postcodeMatch = allMatch.group("postcode").trim();
			String cityMatch = allMatch.group("city").trim();
			
			//System.out.println("PAT_POSTCODE_CITY Match: ["+allMatch.group()+"] Start Index: "+allMatch.start()+" End Index: "+allMatch.end());
			
			buildAddr.setPostcode(postcodeMatch);
			buildAddr.setCity(cityMatch);
		}
		
		return buildAddr;
	}
}