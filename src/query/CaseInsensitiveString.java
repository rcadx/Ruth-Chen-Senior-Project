package query;

public class CaseInsensitiveString implements Comparable<String> {
	
	final private String s;
	
	public CaseInsensitiveString(String s) {
		this.s = s;
	}

	@Override
	public int compareTo(String arg0) {
		return this.s.toLowerCase().compareTo(arg0.toLowerCase());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((s == null) ? 0 : s.toLowerCase().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaseInsensitiveString other = (CaseInsensitiveString) obj;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.toLowerCase().equals(other.s.toLowerCase()))
			return false;
		return true;
	}
	
	public int indexOf(CaseInsensitiveString other) {
		return s.toLowerCase().indexOf(other.s.toLowerCase());
		
	}
	
	public String toString() {
		return s;
	}
	

}
