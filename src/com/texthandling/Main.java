package com.texthandling;

/**
 * Created by Ильнар on 22.04.2015.
 */
public class Main {

//    JUST FOR TESTING
    public static String TEST_FIRST_WORD = "100";
    public static String TEST_SECOND_WORD = "100";
    public static String TEST_THIRD_WORD = "100";


    public static void main(String[] args) {
        LanguageModel languageModel = new LanguageModel();
        languageModel.create(null);

        System.out.println("\n\n======================================================== RESULT ========================================================\n" +
                languageModel.finishGram(/*TEST_FIRST_WORD + " " + TEST_SECOND_WORD + " ?????"*/
                "100 ????? 100"));
    }
}
