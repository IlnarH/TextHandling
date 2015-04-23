package com.texthandling;

import java.util.*;

/**
 * Created by Ильнар on 21.04.2015.
 */
public class LanguageModel {

    //    мерность n-граммов
    public static int N = 3;

    //    список всех встречающихся слов
    Map<String, Integer> wordMap = new LinkedHashMap<String, Integer>();

    //    список всех n-граммов
    Map<Long, NGram> grams = new HashMap<Long, NGram>();

    public void create(String input) {
//        String test = "a d d f g d b f d r s i f w f d b d b f s a f r s f h d s p";


//        JUST CREATING TEST VALUES
//==================================================================================================================//
        String first = "";
        String second = "";
        String third = "";
        int pp = -1;
        int p = -1;
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 3000000; i++) {
            int a = random.nextInt(3000000) + 100;
//            int a = i;
            builder.append(a);
//            System.out.print(a + " ");
            if ((i + 1) % 50 == 0) {
//                System.out.println();
            }
            builder.append(" ");
            if (p == Integer.valueOf(Main.TEST_FIRST_WORD) && a == Integer.valueOf(Main.TEST_SECOND_WORD)) {
                first += pp + " ";
            }
            if (pp == Integer.valueOf(Main.TEST_FIRST_WORD) && a == Integer.valueOf(Main.TEST_SECOND_WORD)) {
                second += p + " ";
            }
            if (pp == Integer.valueOf(Main.TEST_FIRST_WORD) && p == Integer.valueOf(Main.TEST_SECOND_WORD)) {
                third += a + " ";
            }
            pp = p;
            p = a;
        }

//        System.out.println("\nIn test generation occurred words as first: " + (first.equals("") ? "none" : first));
        System.out.println("\nIn test generation occurred words as second: " + (second.equals("") ? "none" : second));
//        System.out.println("\nIn test generation occurred words as third: " + (third.equals("") ? "none" : third));
//==================================================================================================================//


        String test = builder.toString();
        String[] words = test.split(" ");

//        заполение списка слов
        for (String word : words) {
            if (!wordMap.containsKey(word)) {
                wordMap.put(word, wordMap.size());
            }
        }
//        построение модели
        initGrams(words);
        System.out.println("\nLanguage model created");
    }

    /**
     * Завершает высказывание исходя из языковой модели (пустое слово помечено как '?????')
     *
     * @param template
     * @return
     */
    public String finishGram(String template) {
        NGram mostMatchingGram = null;
        for (NGram gram : getGramsByTemplate(template)) {
            if (mostMatchingGram == null) {
                mostMatchingGram = gram;
            } else if (gram.getOccurrenceCount() > mostMatchingGram.getOccurrenceCount()) {
                mostMatchingGram = gram;
            }
        }
        return mostMatchingGram != null ? mostMatchingGram.toString() : "Gram not found";
    }

    /**
     * Находит все n-граммы подходящие под данное высказывание
     *
     * @param template
     * @return
     */
    private List<NGram> getGramsByTemplate(String template) {
        String[] words = template.split(" ");
        int unknownWordPosition = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("?????")) {
                unknownWordPosition = i;
                break;
            }
        }

        List<NGram> filteredGrams = new ArrayList<NGram>();
        long firstMatchingGram = getGramIndex(Arrays.asList(words));
        long digit = Utils.pow(wordMap.size(), unknownWordPosition);
        for (int i = 0; i < wordMap.size(); i++) {
            if (grams.containsKey(firstMatchingGram + i * digit)) {
                NGram foundGram = grams.get(firstMatchingGram + i * digit);
                filteredGrams.add(foundGram);
                System.out.println("----> " + foundGram + ", occurrence - " + foundGram.getOccurrenceCount());
            }
        }
        return filteredGrams;
    }

    //    todo harder logic (sentences)
    private void initGrams(String[] words) {
        List<String> gram = new LinkedList<String>();
        if (words.length < N) {
//        todo sentences again
            throw new RuntimeException("Gram length can not be less than words count");
        }
        for (int i = 0; i < N; i++) {
            gram.add(words[i]);
        }
        for (int i = N; i <= words.length; i++) {
            long gramIndex = getGramIndex(gram);
            if (!grams.containsKey(gramIndex)) {
                grams.put(gramIndex, new NGram(gram));
            } else {
                grams.get(gramIndex).incrementCount();
            }
            gram.remove(0);

            if (i == words.length) {
                break;
            }

            gram.add(words[i]);
        }
    }

    /**
     * Note: if template has unknown word, index of first gram matching the template will be returned
     *
     * @param words
     * @return
     */
    private long getGramIndex(List<String> words) {
        int n = 0;
        long index = 0;
        for (String word : words) {
//            unknown word
            if (word.equals("?????")) {
                n++;
                continue;
            }
            try {
                index += wordMap.get(word) * Utils.pow(wordMap.size(), n);
            } catch (NullPointerException e) {
//                todo not exists
                throw new NoSuchWordException("Word '" + word + "' doesn't exist in language model");
            }
            n++;
        }
        return index;
    }
}