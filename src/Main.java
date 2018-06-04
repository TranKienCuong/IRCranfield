import irpackage.*;

import java.io.*;
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
            System.out.println("Doc index: " + doc.getDocIndex());
            System.out.println("Relevant value: " + doc.getRelevantValue());
		}
//		List<List<Double>> averageRecallAndPrecision  = irEngine.evaluate();
//		for (int i = 0; i < averageRecallAndPrecision.size(); ++i) {
//			for (int j = 0; j < averageRecallAndPrecision.get(i).size(); ++j) {
//				System.out.println(averageRecallAndPrecision.get(i).get(j));
//			}
//		}
        FileReader reader = null;
        try {
            reader = new FileReader("web/TEST/query.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(reader);
        String rawQuery = null;

        PrintWriter writer = null;
        //System.out.println(q);
        for (int i = 1; i <= 225; i++) {

            try {
                writer = new PrintWriter("results/" + i + ".txt", "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                rawQuery = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String q = rawQuery.substring(2, rawQuery.length() - 1);

            query = irEngine.new Query(q);
            relevantDocs = irEngine.search(query);
            for (Doc doc : relevantDocs) {
                writer.println(doc.getDocIndex());
            }
            writer.close();
        }
	}
}
