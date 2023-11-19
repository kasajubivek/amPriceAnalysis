package features;

import java.util.*;

public class InvertedIndexing {
    private Map<String, List<Integer>> index;

    public InvertedIndexing() {
        this.index = new HashMap<>();
    }

    public void addDocument(int docId, String content) {
        String[] terms = content.toLowerCase().split("\\s+"); // Tokenize the content

        for (String term : terms) {
            if (!index.containsKey(term)) {
                index.put(term, new ArrayList<>());
            }
            index.get(term).add(docId);
        }
    }

    public List<Integer> search(String query) {
        String[] queryTerms = query.toLowerCase().split("\\s+");

        List<Integer> result = new ArrayList<>();

        if (index.containsKey(queryTerms[0])) {
            result.addAll(index.get(queryTerms[0]));
        }

        for (int i = 1; i < queryTerms.length; i++) {
            String term = queryTerms[i];
            if (index.containsKey(term)) {
                result.retainAll(index.get(term));
            } else {
                return Collections.emptyList(); // No results for the query
            }
        }

        return result;
    }

    public static void main(String[] args) {
        InvertedIndexing invertedIndex = new InvertedIndexing();

        // Add sample documents
        invertedIndex.addDocument(1, "apple banana cherry");
        invertedIndex.addDocument(2, "apple cherry date");
        invertedIndex.addDocument(3, "banana cherry date");

        // Search for documents containing "apple" and "cherry"
        List<Integer> searchResult = invertedIndex.search("apple cherry");

        if (!searchResult.isEmpty()) {
            System.out.println("Documents containing the query:");
            for (Integer docId : searchResult) {
                System.out.println("Document " + docId);
            }
        } else {
            System.out.println("No documents found for the query.");
        }
    }
}

