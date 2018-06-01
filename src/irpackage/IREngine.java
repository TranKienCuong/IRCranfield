package irpackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IREngine {
    private String lemmatizedWordsDirectory;
    private String stopwordsPath;
    private Lemmatization lemmatization;
    private BufferedReader bufferedReader;
    private FileReader fileReader;
    private HashMap<String, Boolean> stopwordHashMap;
    private HashMap<String, WordProperties> vocabulary;
    private List<HashMap<String, Integer>> wordAndTFByDocs;
    private HashMap<Integer, Double> docAndNorm;

    public IREngine(String lemmatizedWordsDirectory, String stopwordsPath) {
        this.lemmatizedWordsDirectory = lemmatizedWordsDirectory;
        this.stopwordsPath = stopwordsPath;
        lemmatization = new Lemmatization();
        vocabulary = new HashMap<String, WordProperties>();
        wordAndTFByDocs = new ArrayList();
        docAndNorm = new HashMap<Integer, Double>();
        bufferedReader = null;
        fileReader = null;
    }

    public void start() {
        indexing();
        loadStopwords();
    }

    private void indexing() {
        try {
            int numberOfAllDocs = 1400;

            for (int i = 1; i <= numberOfAllDocs; ++i) {
                String filePath = lemmatizedWordsDirectory + i + ".txt";
                fileReader = new FileReader(filePath);
                bufferedReader = new BufferedReader(fileReader);
                String word = bufferedReader.readLine();
                wordAndTFByDocs.add(new HashMap<String, Integer>());
                
                while (word != null) {
                	// This word is not in vocabulary
                    if (vocabulary.get(word) == null) {
                        Posting posting = new Posting(i, 1);
                        WordProperties wordProperties = new WordProperties(1, 1, 0, posting);
                        vocabulary.put(word, wordProperties);
                    } else {
                    	WordProperties wordProperties = vocabulary.get(word);
                        int docIndex = wordProperties.getLastPosting().getDocIndex();
                        // The posting list of this word has contained this doc already.
                        if (docIndex == i) {
                            // Increase 'fre' in wordProperties by 1
                            wordProperties.Add(0, 1);
                            Posting currentPosting = wordProperties.getLastPosting();
                            // Increase 'tf' in posting by 1
                            currentPosting.Add(1);
                        } else {

                            Posting newPosting = new Posting(i, 1);
                            // Increase 'fre' in wordProperties by 1 and add the newPosting
                            wordProperties.Add(1, 1, newPosting);
                        }
                    }

                    int currentDocIndex = i - 1;
                    if (wordAndTFByDocs.get(currentDocIndex).containsKey(word)) {
                    	// Increase 'tf' of word by 1
                        int tf = wordAndTFByDocs.get(currentDocIndex).get(word);
                        wordAndTFByDocs.get(i - 1).put(word, tf + 1);
                    } else {
                    	wordAndTFByDocs.get(currentDocIndex).put(word, 1);
                    }
                    
                    word = bufferedReader.readLine();
                }
                
            }
            
            // Calculate Norm
            for (int docIndex = 1; docIndex <= wordAndTFByDocs.size(); ++docIndex) {
            	double norm = 0;
            	HashMap<String, Integer>wordAndTF = wordAndTFByDocs.get(docIndex - 1);
            	
				for (String word: wordAndTF.keySet()) {
					int tf = wordAndTF.get(word);
					WordProperties wordProperties = vocabulary.get(word);
	                double idf = wordProperties.calculateIdf(numberOfAllDocs);
	                wordProperties.updateIdf(idf);
					norm += Math.pow(tf*idf, 2);
				}
				norm = Math.sqrt(norm);
				
				docAndNorm.put(docIndex, norm);
            }

            //Calculate idf and (tf.idf)/ Norm
            for (String word: vocabulary.keySet()) {
                WordProperties wordProperties = vocabulary.get(word);
                wordProperties.calculateWordWeightForDoc(docAndNorm);
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            try {

                if (bufferedReader != null)
                    bufferedReader.close();

                if (fileReader != null)
                    fileReader.close();

            } catch (IOException ex) {

                ex.printStackTrace();
            }
        }
    }

    private void loadStopwords() {
        stopwordHashMap = new HashMap<String, Boolean>();

        try {
            fileReader = new FileReader(stopwordsPath);
            bufferedReader = new BufferedReader(fileReader);
            String stopword = bufferedReader.readLine();
            while (stopword != null) {
                stopwordHashMap.put(stopword, true);
                stopword = bufferedReader.readLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Doc> search(Query query) {
        if (query == null)
            return new ArrayList<>();

        query.calculateWeights();

        HashMap<Integer, Vector<Double>> docWordWeightHashMap = new HashMap<>();
        Vector<Double> queryWordWeights = new Vector<>();

        int lengthOfQueryWordWeight = 0;
        // Create queryWordWeight of query and docWordWeightHashMap of docs contain words in query
        for (String queryWord: query.wordWeights.keySet()) {
            lengthOfQueryWordWeight += 1;
            // Create queryWordWeight (vector)
            Double weight = query.wordWeights.get(queryWord);
            queryWordWeights.addElement(weight);

            // This queryWord isn't in postingList
            WordProperties wordProperties = vocabulary.get(queryWord);
            if (wordProperties != null) {
                List<Posting> postingList = wordProperties.getPostingList();
                // Create docWordWeightHashMap
                for (int postingListIndex = 0; postingListIndex < postingList.size(); ++postingListIndex) {
                    Posting posting = postingList.get(postingListIndex);
                    int docIndex = posting.getDocIndex();

                    Vector<Double> wordWeights = docWordWeightHashMap.get(docIndex);
                    if (wordWeights == null) {
                        wordWeights = new Vector<Double>();
                        for (int count = 0; count < lengthOfQueryWordWeight - 1; ++count) {
                            wordWeights.addElement(null);
                        }
                        docWordWeightHashMap.put(docIndex, wordWeights);
                    }
                    Double weightOfWord = posting.getWeightOfWord();
                    wordWeights.addElement(weightOfWord);
                }
            }

            // Add null to vacant positions of wordWeights (vector) to keep a same size with queryWordWeight (vector)
            for (Integer docIndex: docWordWeightHashMap.keySet()) {
                Vector<Double> wordWeights = docWordWeightHashMap.get(docIndex);
                if (wordWeights.size() == lengthOfQueryWordWeight) {
                    continue;
                }
                for (int count = wordWeights.size(); count < lengthOfQueryWordWeight; ++count) {
                    wordWeights.add(null);
                }
            }
        }

        List<Doc> relevantDocs = new ArrayList<Doc>();
        // Calculate Cosine similarity
        for (Integer docIndex: docWordWeightHashMap.keySet()) {
            Vector<Double> docWordWeights = docWordWeightHashMap.get(docIndex);
            double cosine = 0;
            double queryMagnitude = 0;
            double docMagnitude = 0;
            for (int i = 0; i < docWordWeights.size(); ++i) {
                double queryWordWeight = queryWordWeights.get(i);
                double docWordWeight = docWordWeights.get(i) == null ? 0 : docWordWeights.get(i);
                cosine += queryWordWeight * docWordWeight;
                queryMagnitude += queryWordWeight * queryWordWeight;
                docMagnitude += docWordWeight * docWordWeight;
            }
            cosine = cosine / (Math.sqrt(queryMagnitude) * Math.sqrt(docMagnitude));

            Doc relevantDoc = new Doc(docIndex, cosine);
            relevantDocs.add(relevantDoc);
        }

        Collections.sort(relevantDocs);
        return relevantDocs;
    }

    // This class represents a query used for a specific instance of IREngine
    public class Query {
        private String query;
        private List<String> lemmaWords;
        public HashMap<String, Double> wordWeights;

        public Query(String query) {
            this.query = query;
            wordWeights = new HashMap<>();
            lemmaWords = lemmatization.lemmatize(query);
        }

        private void calculateWeights() {
            // Calculate 'tf' for each query-word
            for (String queryWord: lemmaWords) {
                // This queryWord is not in vocabulary
                if (vocabulary.get(queryWord) == null) {
                    continue;
                }
                // This queryWord is a stopword
                if (stopwordHashMap.get(queryWord) != null) {
                    continue;
                }
                if (wordWeights.get(queryWord) == null) {
                    Double tf = 1d;
                    // Increase 'tf' by 1
                    wordWeights.put(queryWord, tf);
                    continue;
                }
                Double tf = wordWeights.get(queryWord);
                tf += 1;
            }

            // Calculate tf.idf for each query-word
            for (String queryWord: wordWeights.keySet()) {
                WordProperties wordProperties = vocabulary.get(queryWord);
                // If this word isn't in vocabulary;
                if (wordProperties == null) {
                    wordWeights.put(queryWord, 0.0);
                    continue;
                }

                Double idf = wordProperties.getIdf();
                Double weight = wordWeights.get(queryWord); // tf
                // Normalize tf
                weight = 1 + Math.log(lemmaWords.size() / weight) ;
                // Calculate tf.idf
                weight = weight * idf;
                wordWeights.put(queryWord, weight);
            }
        }

        @Override
        public String toString() {
            return query;
        }
    }
}
