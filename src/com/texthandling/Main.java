package com.texthandling;

/**
 * Created by Ильнар on 22.04.2015.
 */
public class Main {

    public static void main(String[] args) {
        LanguageModel languageModel = new LanguageModel();
        languageModel.create(null);

//        System.out.println("\n\n" + languageModel.finishGram("мы UNKNOWN сообщить").toString());

        System.out.println("\n\n" + languageModel.generateSentence());

        System.out.println("\n\n" + languageModel.restoreOrder("привычке подействовало тревожно физически"));
    }
}
