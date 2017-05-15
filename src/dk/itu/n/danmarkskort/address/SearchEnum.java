package dk.itu.n.danmarkskort.address;

/**
 * Enum class is used with the address datastructure, to define a search type.
 * @author Group N
 *
 */
public enum SearchEnum {
	ANY, EQUALS, STARTSWITH, CONTAINS, LEVENSHTEIN
}
