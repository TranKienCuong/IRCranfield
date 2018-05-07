package irpackage;

import java.util.ArrayList;
import java.util.List;

public class WordProperties {
	int numberOfDocs = 0;
	int fre = 0;
	double idf = 0;
	List<Posting> postings;
	
	public WordProperties() {
		numberOfDocs = 0;
		fre = 0;
		idf = 0;
		postings = new ArrayList<Posting>();
	}
	
	public WordProperties(int numberOfDocs, int fre, double idf, Posting posting) {
		this.numberOfDocs = numberOfDocs;
		this.fre = fre;
		this.idf = idf;
		postings = new ArrayList<Posting>();
		postings.add(posting);
	}
	
	public void Add(int numberOfDocs, int fre, Posting posting) {
		this.numberOfDocs += numberOfDocs;
		this.fre += fre;
		this.postings.add(posting);
	}
	
	public void Add(int numberOfDocs, int fre) {
		this.numberOfDocs += numberOfDocs;
		this.fre += fre;
	}
	
	public void Add(int fre) {
		this.fre += fre;
	}
	
	public double calculateIdf(int numOfAllDocs) {
		this.idf = 1 + Math.log10(numOfAllDocs / this.numberOfDocs);
		return this.idf;
	}
	
	public void calculateWordWeightForDoc(double idf) {
		for (int i = 0; i < this.postings.size(); ++i) {
			postings.get(i).calculateWeight(idf);
		}
	}
	
	public double getIdf() {
		return idf;
	}
	
	public List<Posting> getPostingList() {
		return this.postings;
	}
	
	public Posting getLastPosting() {
		return postings.get(postings.size() - 1); 
	}
	
//	Posting searchFor(int index) {
//		for (int i = 0; i < postings.size(); ++i) {
//			int docIndex = this.postings.get(i).getDocIndex();
//			if (index == docIndex) {
//				return this.postings.get(i);
//			}
//		}
//		return null;
//	}
}
