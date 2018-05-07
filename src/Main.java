import irpackage.*;

import java.util.List;
import java.util.Scanner;

// This class is only used for console testing
public class Main {
	public static void main(String[] args) {
        System.out.println("Please type a query: ");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        scanner.close();
        IREngine irEngine = new IREngine("web/LemmatizedWords/", "web/Stopwords/stopwords-en.txt");
        irEngine.start();
        IREngine.Query query = irEngine.new Query(s);
        List<Doc> relevantDocs = irEngine.search(query);
        for (String queryWord: query.wordWeights.keySet()) {
            System.out.println("Word: " + queryWord);
            System.out.println("Word weight: " + query.wordWeights.get(queryWord));
        }
		for (Doc doc: relevantDocs) {
            System.out.println("Doc index: " + doc.doxIndex);
            System.out.println("Relevant value: " + doc.relevantValue);
		}
	}
}
