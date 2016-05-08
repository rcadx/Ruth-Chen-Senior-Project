package query;

public interface RankingFilter {
	
	/*
	 * Implemented by WordPenalty
	 * Allows a chain of filters to be constructed
	 */
	
	public RankedString filter(RankedString s);

}
