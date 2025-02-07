package features;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpellChecking {

   static Set<String> _similar_words_ = new HashSet<>();
   static Set<String> _manufacturers_ = new HashSet<>();
   static boolean _match_;


    public static void main(String[] args) {

        getManufacturersList();
        spellChecker();

    }

    public static void getManufacturersList(){
        try{
            Scanner _text_scanner_ =  new Scanner(new File("src/main/resources/manufacturers.txt"));

            while (_text_scanner_.hasNextLine()){
                _manufacturers_.add(_text_scanner_.nextLine());
            }
        }catch (FileNotFoundException e){
            System.out.println(e);
            e.printStackTrace();
        }

    }


    public static void spellChecker(){
        String _usr_inp_ = "";

        try{

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Please enter your favourite car brand\nIf you want to exit the program then type 'quit': ");

                _usr_inp_ = scanner.nextLine();

                if(isValid(_usr_inp_)){
                    if (_usr_inp_.equalsIgnoreCase("quit")) {
                        System.out.println("See you!");
                        break;
                    }

                   spellChecker(_usr_inp_);
                }else {
                    System.out.println("Error! Invalid Input.\n");
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public static void spellChecker(String _usr_inp_){
        try{
            if(isValid(_usr_inp_)){

            for(String manufacturer: _manufacturers_){
                _match_ = editDistanceSimilarity(_usr_inp_, manufacturer);
                if(_match_){
                    break;
                }
            }

            if(!_match_ && !_similar_words_.isEmpty()){
                System.out.println("Oh no! The entered word was not found in our dictionary. But here are some suggestions: ");
                for(String _wrd_: _similar_words_){
                    System.out.println(_wrd_);
                }
                System.out.println();
                _similar_words_.clear();
            }else if(!_match_ && _similar_words_.isEmpty()){
                System.out.println("Oh no! The entered word was not found in our dictionary.\n");
            }

            }else {
                System.out.println("Error! Invalid Input.\n");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }




    public static boolean editDistanceSimilarity(String _word_one_, String _word_two_){
        try{
            char[] _word_one_arr_ = _word_one_.toLowerCase().toCharArray();
            char[] _word_two_arr_ = _word_two_.toLowerCase().toCharArray();


            int[][] _matrix_ = new int[_word_two_.length()+1][_word_one_.length()+1];



            for(int _i_ = 0; _i_<_matrix_[0].length; _i_++){
                _matrix_[0][_i_] = _i_;
            }


            for(int _j_ = 1; _j_<_matrix_.length; _j_++){
                _matrix_[_j_][0] = _j_;
            }



            for(int _row_ = 1; _row_<_matrix_.length; _row_++){
                for(int _col_ = 1; _col_<_matrix_[0].length; _col_++){

                    if(_word_one_arr_[_col_-1] == _word_two_arr_[_row_-1]){
                        _matrix_[_row_][_col_] = _matrix_[_row_ - 1][_col_ - 1];
                    }else {
                        _matrix_[_row_][_col_] = Math.min(Math.min(_matrix_[_row_ - 1][_col_ - 1], _matrix_[_row_][_col_ - 1]), _matrix_[_row_ - 1][_col_]) + 1;
                    }

                }
            }



            if(_matrix_[_matrix_.length - 1][_matrix_[0].length - 1] == 0){
                System.out.println("The spelling of the word "+_word_one_+" is correct\n");
                return true;
            }else if(_matrix_[_matrix_.length - 1][_matrix_[0].length - 1] <= 3 && _word_one_.toLowerCase().charAt(0) == _word_two_.toLowerCase().charAt(0)){
                if(_word_one_.length() > 1 && _word_two_.length() > 1){
                    if(_word_one_.toLowerCase().charAt(1) == _word_two_.toLowerCase().charAt(1)){
                        _similar_words_.add(_word_two_);
                    }
                }else {
                    _similar_words_.add(_word_two_);
                }
            }

        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }

        return false;
    }



    public static boolean isValid(String _usr_inp_) {

        return Pattern.compile("^[a-zA-Z]+$").matcher(_usr_inp_).matches();

    }

}
