package query;

abstract public class WordPenalty implements RankingFilter {
	
	final protected String word;
	final protected int amount;
	
	public WordPenalty(String word, int amount) {
		this.word = word;
		this.amount = amount;
	}
	
	abstract public RankedString filter(RankedString s);
	
	public String toString(){
		return word;
	}
	

}
