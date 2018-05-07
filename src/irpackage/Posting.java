package irpackage;

public class Posting {
	int docIndex = 0;
	double tf = 0;
	double weightOfWord = 0;
	
	public Posting() {
		docIndex = 0;
		tf = 0;
		weightOfWord = 0;
	}
	
	public Posting(int index, int fre) {
		this.docIndex = index;
		this.tf = fre;
	}
	
	public void Add(int fre) {
		this.tf += fre;
	}
	
	void calculateWeight(double idf) {
		this.weightOfWord = tf * idf;
	}
	
	public void normalizeTf(double numOfWords) {
		tf = tf / numOfWords;
	}
	
	int compareTo(int index) {
		if (this.docIndex == index) {
			return 0;
		}
		return this.docIndex > index ? 1 : -1;
	}
	
	public int getDocIndex() {
		return docIndex;
	}
	
	public double getWeightOfWord() {
		return this.weightOfWord;
	}
}
