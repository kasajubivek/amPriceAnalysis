package features;

import java.util.Arrays;

public class WordCompletion {
    public static String[] words;

    public boolean searchWord(String word) {
        int index = binarySearch(words, word);
        return index >= 0;
    }

    public void autoComplete(String prefix) {
//        int index = Arrays.binarySearch(words, prefix);
        int index = binarySearch(words, prefix);

        // If the prefix is not found, convert the insertion point to a positive index
        if (index < 0) {
            index = -index - 1;
        }

        // Display all words starting with the given prefix
        for (int i = index; i < words.length && words[i].startsWith(prefix); i++) {
            System.out.println(words[i]);
        }
    }


    public int binarySearch(String[] myArr, String target){
        int low = 0;
        int high = myArr.length-1;

        while(low<=high){
            int mid = low + (high - low) / 2;

            int _val_ = target.compareTo(myArr[mid]);

            if(_val_ == 0){
                return mid;
            }

            if(_val_ > 0){
                low = mid + 1;
            }else{
                high = mid-1;
            }

        }

        return -1;

    }



    public static void main(String[] args) {

        String[] wordArray = {"apple", "appetizer", "application", "banana"};
        Arrays.sort(wordArray);

        words = wordArray;

        WordCompletion _obj_ = new WordCompletion();

        System.out.println(_obj_.searchWord("apple"));  // true
        System.out.println(_obj_.searchWord("app"));    // false

        System.out.println("Words starting with 'app':");
        _obj_.autoComplete("app");
    }
}
