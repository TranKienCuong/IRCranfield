package irpackage;

import java.util.ArrayList;
import java.util.HashMap;
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
		return Math.log10(numOfAllDocs / this.numberOfDocs);
	}
	
	public void calculateWordWeightForDoc(HashMap<Integer, Double> docAndNorm) {
		for (int i = 0; i < this.postings.size(); ++i) {
			postings.get(i).calculateWeight(idf, docAndNorm);
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
	
	public void updateIdf(double idf) {
		this.idf = idf;
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
