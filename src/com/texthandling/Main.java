package com.texthandling;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Ильнар on 22.04.2015.
 */
public class Main {

    public static void main(String[] args) throws Exception {
//        LanguageModel languageModel = LanguageModel.getNewLanguageModel(LanguageModel.getInput(new File("Text.txt")), 3);
//        LanguageModel languageModel = LanguageModel.readLanguageModel(new File("ololo"));
//
//        languageModel.writeLanguageModel(new File("ololo"));

//        LanguageModel ll = LanguageModel.readLanguageModel(new File("ololo"));

//        System.out.println("\n\n" + languageModel.finishGram("мы UNKNOWN сообщить").toString());

//        System.out.println("\n\n" + languageModel.generateSentence());

//        System.out.println("\n\n" + languageModel.restoreOrder("привычке подействовало тревожно физически"));
        Scanner sc = new Scanner(System.in);
        LanguageModel lm = null;
        while(sc.hasNext()){
            String str = sc.nextLine();
            if(str.equals("end"))
                break;
            if(str.split(" ") != null && str.split(" ")[0].equals("get")){
                lm = LanguageModel.getNewLanguageModel(LanguageModel.getInput(new File(str.split(" ")[1])), 3);
            }
            if(str.split(" ") != null && str.split(" ")[0].equals("read")){
                lm = LanguageModel.readLanguageModel(new File(str.split(" ")[1]));
            }
            if(str.split(" ") != null && str.split(" ")[0].equals("Task1")){
                System.out.println(lm.finishGram(str.substring(6)));
            }
            if(str.equals("Task2")){
                System.out.println(lm.generateSentence());
            }
            if(str.split(" ") != null && str.split(" ")[0].equals("Task3")){
                System.out.println(lm.finishGram(str.substring(6)));
            }

        }


    }
}
