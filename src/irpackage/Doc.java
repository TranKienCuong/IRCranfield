package irpackage;

public class Doc implements Comparable<Doc> {
	public int docIndex;
	public double relevantValue;
	
	public Doc() {
		this.docIndex = 0;
		this.relevantValue = 0;
	}
	
	public Doc(int docIndex, double relevantValue) {
		this.docIndex = docIndex;
		this.relevantValue = relevantValue;
	}
	
	@Override
	public int compareTo(Doc o) {
		if (relevantValue == o.relevantValue) {
			return 0;
		}
		return relevantValue < o.relevantValue ? 1 : -1;
	}
	
	public int getDocIndex() {
		return this.docIndex;
	}
	
	public double getRelevantValue() {
		return relevantValue;
	}
}
