package com.texthandling;

import java.io.File;

/**
 * Created by Ильнар on 22.04.2015.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        LanguageModel languageModel = LanguageModel.getNewLanguageModel(LanguageModel.getInput(new File("Text.txt")), 3);
//        LanguageModel languageModel = LanguageModel.readLanguageModel(new File("ololo"));
//
//        languageModel.writeLanguageModel(new File("ololo"));

//        LanguageModel ll = LanguageModel.readLanguageModel(new File("ololo"));

//        System.out.println("\n\n" + languageModel.finishGram("мы UNKNOWN сообщить").toString());

        System.out.println("\n\n" + languageModel.generateSentence());

//        System.out.println("\n\n" + languageModel.restoreOrder("привычке подействовало тревожно физически"));
    }
}
