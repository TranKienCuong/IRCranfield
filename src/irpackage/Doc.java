package irpackage;

public class Doc implements Comparable<Doc> {
	public int doxIndex;
	public double relevantValue;
	
	public Doc() {
		this.doxIndex = 0;
		this.relevantValue = 0;
	}
	
	public Doc(int docIndex, double relevantValue) {
		this.doxIndex = docIndex;
		this.relevantValue = relevantValue;
	}
	
	@Override
	public int compareTo(Doc o) {
		if (relevantValue == o.relevantValue) {
			return 0;
		}
		return relevantValue < o.relevantValue ? 1 : -1;
	}
}
