import java.util.List;
import java.io.*;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		Lemmatization l = new Lemmatization();
		Stemmer s = new Stemmer();
		String punctuations = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
		String filePath = "/Users/dai/Downloads/Stopwords/stopwords-en.txt";
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		
		try {
			fileReader = new FileReader(filePath);
			bufferedReader = new BufferedReader(fileReader);
			
			HashMap<String, Boolean> stopwordHashMap = new HashMap<String, Boolean>();
			String stopword = bufferedReader.readLine();
			while (stopword != null) {
				stopwordHashMap.put(stopword, true);
				stopword = bufferedReader.readLine();
			}

			for (int i = 1; i <= 1400; ++i) {
				filePath = "/Users/dai/Downloads/Cranfield/" + i + ".txt";
				
				fileReader = new FileReader(filePath);
				bufferedReader = new BufferedReader(fileReader);
				
				//filePath = "/Users/dai/Downloads/LemmatizedWords/" + i + ".txt";
				filePath = "/Users/dai/Downloads/StemmedWords/" + i + ".txt";
				fileWriter = new FileWriter(filePath);
				bufferedWriter = new BufferedWriter(fileWriter);
				
				String documentText = bufferedReader.readLine();
//				List<String> lemmatizedWords = l.lemmatize(documentText);
//								
//				for (String word: lemmatizedWords) {
//					Boolean isStopword = stopwordHashMap.get(word);
//					if (isStopword == null) {
//						//System.out.println(word);
//						bufferedWriter.write(word);
//						bufferedWriter.newLine();
//					}
//				}
				String[] words = documentText.split("\\s+");
				for (String word: words) {
					String stemmedWord;
					String lastCharacter = word.substring(word.length() - 1);
					// if the last character is a punctuation
					// then get substring from 0 to length - 2
					if (punctuations.contains(lastCharacter)) {
						if (word.length() == 1) {
							continue;
						}
						stemmedWord = s.stem(word.substring(0, word.length() - 2));
					} else {
						stemmedWord = s.stem(word);
					}
	
					Boolean isStopword = stopwordHashMap.get(stemmedWord);
					if (isStopword == null) {
						bufferedWriter.write(stemmedWord);
						bufferedWriter.newLine();
					}
				}
				
				bufferedWriter.close();
				fileWriter.close();
			}
			
			

			
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bufferedReader != null)
					bufferedReader.close();

				if (fileReader != null)
					fileReader.close();
				
				if (fileWriter != null)
					fileWriter.close();
				
				if (bufferedWriter != null)
					bufferedWriter.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

}
