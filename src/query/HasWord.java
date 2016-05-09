package query;

public class HasWord extends WordPenalty {
	/*
	 * Checks to see if a tweet has a word we want to analyze, if so, increases the rank or decreases the rank accordingly.
	 * 
	 * Irrelevant words will have a negative rank, thus decreasing the RankedString's rank.
	 * Relevant words will have a positive rank, thus increasing the RankedString's rank.
	 * 
	 * The ranks of words are determined by the user in filters.txt.
	 */
	
	public HasWord(String word, int rank) {
		super(word, rank);
	}
	
	public RankedString filter(RankedString s) {
		if (s.string.toLowerCase().indexOf(word.toLowerCase()) > 0) {
			return new RankedString(s.string, s.rank + amount);
		}
		else {
			return s;
		}
	}
}
