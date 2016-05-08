package query;

public class RankedString implements Comparable<RankedString>{
	/*
	 * RankedString consists of a tweet and its rank, determined by RankingFilter
	 * RankedStrings are compared based on their rank
	 */
	
	final public String string;
	final public int rank;
	
	public RankedString(String s) {
		this(s, 0);
	}
	
	public RankedString(String s, int rank) {
		this.string = s;
		this.rank = rank;
	}

	@Override
	public int compareTo(RankedString other) {
		int thisRank = new Integer(this.rank);
		int otherRank = new Integer(other.rank);
		
		if(thisRank < otherRank) {
			return 1;
		}
		else if (thisRank > otherRank) {
			return -1;
		}
		else {
			return this.string.compareTo(other.string);
		}
	}
	
	@Override
	public String toString() {
		return string;
	}


}
