package com.texthandling;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Ильнар on 22.04.2015.
 */
public class Main {

//    JUST FOR TESTING
    public static String TEST_FIRST_WORD = "100";
    public static String TEST_SECOND_WORD = "100";
    public static String TEST_THIRD_WORD = "100";


    public static void main(String[] args) throws Exception {
            String ssa = "Привет потомки, я пьяная!    Воу-воу-воу! Нееее, так не пойдет... Так точно, Ватсон, именно так, и никак иначе. Это - \"аналоговое телевидение\", оно идет под номером 666";
        String ss = "1 1 1 1 3 1";
        LanguageModel lm = LanguageModel.getNewLanguageModel(ss, 3);
        lm.writeLanguageModel(new File("text.txt"));
        LanguageModel myNewLm = LanguageModel.readLanguageModel(new File("text.txt"));

        /*for (Token s : lm.invertedBaseCollection.getChilds().keySet()) {
            for(Token sss : lm.invertedBaseCollection.getChilds().get(s).getChilds().keySet()) {
                HashMap<Coll, Coll> third = lm.invertedBaseCollection.getChilds().get(s).getChilds().get(sss).getChilds();
                for(Token sssss : third.keySet()){
                    System.out.println(s.getString() + " " + sss.getString() + " " + sssss.getString() + " " + lm.invertedBaseCollection.getChilds().get(s).get().get(sss).occurance);
                }

            }
        }*/


        System.out.println(lm.c2.get(new Token("1")).occurance);
        System.out.println(lm.c2.get(new Token("1")).getChilds().get(new Coll(new Token("1"))).occurance);


        int z = 0;









    }

}
