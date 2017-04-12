package dk.itu.n.danmarkskort.address;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressParser {
	public AddressParser() {
		
	}
	
	private final static String RGX_ALPHA = "[\\.\\u002D\\u0027a-zA-ZæøåÆØÅáÁéÉèÈöÖüÜëË ]";
	private final static String RGX_POSTCODE = "(?<postcode>[0-9]{4})";
	private final static String RGX_MULTIPLEHOUSENUMBER = "([0-9]{1,3}[A-Z]{1}\\-[0-9]{1,3}[A-Z]{1})|([0-9]{1,3}[A-Z]{1}\\-[A-Z]{1})|([0-9]{1,3}\\-[0-9]{1,3})";
	private final static String RGX_HOUSENUMBER = RGX_MULTIPLEHOUSENUMBER + "|([0-9]{1,3}[a-zA-Z]{1})|([0-9]{1,3})";
	
	private final static String PAT_POSTCODE = RGX_POSTCODE;
	private final static String PAT_HOUSENUMBER = "\\s(?<housenumber>("+RGX_HOUSENUMBER+"\\s)|("+RGX_HOUSENUMBER+"$))";
	private final static String PAT_STREET_HOUSENUMBER = "^(?<street>([0-9]{1,3}+\\s"+RGX_ALPHA+"+)|("+RGX_ALPHA+"+))"
			+ "\\s(?<housenumber>("+RGX_HOUSENUMBER+"\\s)|("+RGX_HOUSENUMBER+"$))";
	private final static String PAT_POSTCODE_CITY =""+RGX_POSTCODE+"\\s(?<city>"+RGX_ALPHA+"+$)";
	private final static String PAT_STREET = "^(?<street>([0-9]{1,3}\\s"+RGX_ALPHA+"+)|("+RGX_ALPHA+"+))";
	private final static String PAT_CITY = "(?<city>"+RGX_ALPHA+"+$)";
	private final static String PAT_FLOOR = "(?<floor>(kld.|st.|([0-9])+(\\.\\s(sal))))";
	private final static String PAT_DOORSIDE = "(?<side>(tv.|mf.|th.))";
	
	private static String combineRegex(String ... args) {
		StringBuilder sb = new StringBuilder();
		for (String arg : args) {
			if (sb.length() > 0) sb.append(" *,? *");
			sb.append(arg);
		}
		return sb.toString();
	}

	private final static String[] REGEXS = {
			combineRegex(PAT_FLOOR),
			combineRegex(PAT_DOORSIDE),
			combineRegex(PAT_STREET),
			combineRegex(PAT_HOUSENUMBER),
			combineRegex(PAT_POSTCODE),
			combineRegex(PAT_CITY),
			combineRegex(PAT_STREET_HOUSENUMBER),
			combineRegex(PAT_POSTCODE_CITY),
			combineRegex(PAT_STREET, PAT_POSTCODE_CITY),
			combineRegex(PAT_STREET_HOUSENUMBER, PAT_POSTCODE),
			combineRegex(PAT_STREET_HOUSENUMBER, PAT_CITY),
			combineRegex(PAT_STREET_HOUSENUMBER, PAT_POSTCODE_CITY)
	};

	private final static Pattern[] PATTERNS =
			Arrays.stream(REGEXS).map(Pattern::compile).toArray(Pattern[]::new);

	private static void consumeIfMatchGroup(Consumer<String> consumer, Matcher matcher, String group) {
		try {
			String match = matcher.group(group);
			if (match != null) {
				consumer.accept(matcher.group(group).trim());
			}
		} catch (IllegalArgumentException e) {
			// do nothing
		}
	}

	public static Address parse(String inputAddress, boolean searchMode) {
		String input = AddressValidator.cleanAddress(inputAddress);
		Address addrBuild = new Address();
		for (Pattern pattern : PATTERNS) {
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches()) {
				consumeIfMatchGroup(addrBuild::setStreet, matcher, "street");
				consumeIfMatchGroup(addrBuild::setHousenumber, matcher, "housenumber");
				consumeIfMatchGroup(addrBuild::setPostcode, matcher, "postcode");
				consumeIfMatchGroup(addrBuild::setCity, matcher, "city");
				if(!searchMode) return addrBuild;
			}
		}
		//throw new IllegalArgumentException("Cannot parse Address: " + input);
		//System.out.println("Cannot parse Address: " + inputAddress);
		//System.out.println("Clean parse Address: " + input);
		if(searchMode) return addrBuild;
		return null;
	}
}