package com.texthandling;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.HashSet;
import java.util.Map;

/**
 * Created by Ильнар on 22.04.2015.
 */
public class Main {

//    JUST FOR TESTING
    public static String TEST_FIRST_WORD = "100";
    public static String TEST_SECOND_WORD = "100";
    public static String TEST_THIRD_WORD = "100";


    public static void main(String[] args) {
//        LanguageModel languageModel = new LanguageModel();
//        languageModel.create(null);

//        Coll aa = languageModel.baseCollection;
        String ss = "Привет потомки, я пьяная!    Воу-воу-воу! Нееее, так не пойдет... Так точно, Ватсон, именно так, и никак иначе. Это - \"аналоговое телевидение\", оно идет под номером 666";
        String[] aa = ss.split("(?= - )|(?=[!#$%&'()*+,./:;<=>?@[\\\\]_`{|}~])|\\s+");
        for (String s : aa) {
            System.out.println(s);
        }

        int d = 1;

//        System.out.println("\n\n======================================================== RESULT ========================================================\n" +
//                languageModel.finishGе.am(/*TEST_FIRST_WORD + " " + TEST_SECOND_WORD + " ?????"*/
//                "100 ????? 100"));
//    }














    }

}
