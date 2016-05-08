package query;

public class HasWord extends WordPenalty {
	/*
	 * Checks to see if a tweet has a word we want to analyze, if so, increases the rank
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
