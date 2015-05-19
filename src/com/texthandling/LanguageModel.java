package com.texthandling;

import java.io.*;
import java.util.*;

/**
 * Created by Ильнар on 21.04.2015.
 */
public class LanguageModel implements Serializable {

    public static String UNKNOWN_WORD = "UNKNOWN";
    public static String ANY_OTHER_WORD = "ANY";


    //    мерность n-граммов
    public static int N = 3;

    //    список всех встречающихся слов
    Map<String, Integer> wordMap = new LinkedHashMap<String, Integer>();

    //    список всех n-граммов
    Map<Long, NGram> grams = new HashMap<Long, NGram>();

    private Map<List<String>, Float> combinations = new HashMap<List<String>, Float>();

    private LanguageModel(){}


    public static LanguageModel getNewLanguageModel(String input, int dimension){
        LanguageModel out = new LanguageModel();

        List<String> sequence = ParseHelper.parse(input);

        for(int i = 1; i < 100; ++i){
            System.out.println(sequence.get(sequence.size() * i/100));

        }

//        заполение списка слов
        Map<String, Integer> currencyMap = new HashMap<String, Integer>();
        for (String s : sequence) {
            if (currencyMap.containsKey(s)) {
                currencyMap.put(s, currencyMap.get(s) + 1);
            } else {
                currencyMap.put(s, 1);
            }
        }
        for (Map.Entry<String, Integer> entry : currencyMap.entrySet()) {
            out.wordMap.put(entry.getKey(), out.wordMap.size());
        }
//        wordMap.put(ANY_OTHER_WORD, wordMap.size());
        out.N = dimension;



//        построение модели
        out.initGrams(sequence);
        System.out.println("\nLanguage model with " + sequence.size() + " words, " + out.wordMap.size() + " unique words and " + out.grams.size() +  " unique N-grams created");


        return out;
    }

    public static String getInput(File inputFile, String encodings) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), encodings));
            String out;
            char[] cbuf = new char[((int) inputFile.length())];
            int count = reader.read(cbuf);
            out = new String(cbuf, 0, count);
            return out;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getInput(File inputFile){
        return getInput(inputFile, "CP1251");
    }


    public void writeLanguageModel(File file) throws IOException {

        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.flush();
        oos.close();
        System.out.println("Wroten!");
    }

    public static LanguageModel readLanguageModel(File file) {
        if(!file.exists()){
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            LanguageModel lm = (LanguageModel) ois.readObject();
            System.out.println("Read!");
            return lm;
        } catch (Exception e){
            return null;
        }

    }




    private void create(String input) {

        //todo init
        String inputString = "";

        List<String> sequence = ParseHelper.getSequence(inputString);


//        заполение списка слов
        Map<String, Integer> currencyMap = new HashMap<String, Integer>();
        for (String s : sequence) {
            if (currencyMap.containsKey(s)) {
                currencyMap.put(s, currencyMap.get(s) + 1);
            } else {
                currencyMap.put(s, 1);
            }
        }
        for (Map.Entry<String, Integer> entry : currencyMap.entrySet()) {
            wordMap.put(entry.getKey(), wordMap.size());
        }
//        wordMap.put(ANY_OTHER_WORD, wordMap.size());



//        построение модели
        initGrams(sequence);
        System.out.println("\nLanguage model created");
    }

    /**
     * Завершает высказывание исходя из языковой модели (пустое слово помечено как <unknown>)
     *
     * @param template
     * @return
     */
    public NGram finishGram(String template) {
        NGram mostMatchingGram = null;
        for (NGram gram : getByTemplate(template)) {
            if (mostMatchingGram == null) {
                mostMatchingGram = gram;
            } else if (gram.getOccurrenceCount() > mostMatchingGram.getOccurrenceCount()) {
                mostMatchingGram = gram;
            }
        }


        List<String> l = ParseHelper.parse(template);
        String[] words = new String[l.size()];
        l.toArray(words);
        int unknownWordPosition = -1;
        int otherWordPosition = -1;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(UNKNOWN_WORD)) {
                if (unknownWordPosition == -1) {
                    unknownWordPosition = i;
//                break;
                } else {
                    otherWordPosition = i;
                }
            }
        }
        NGram nGram = new NGram();
        if (unknownWordPosition == 0) {
            if (otherWordPosition == -1) {
                nGram.setWords(mostMatchingGram.getWord(0), words[1], words[2]);
            } else if (otherWordPosition == 1) {
                nGram.setWords(mostMatchingGram.getWord(0), mostMatchingGram.getWord(1), words[2]);
            } else if (otherWordPosition == 2) {
                nGram.setWords(mostMatchingGram.getWord(0), words[1], mostMatchingGram.getWord(2));
            }
        } else if (unknownWordPosition == 1) {
            if (otherWordPosition == -1) {
                nGram.setWords(words[0], mostMatchingGram.getWord(1), words[2]);
            } else if (otherWordPosition == 2) {
                nGram.setWords(words[0], mostMatchingGram.getWord(1), mostMatchingGram.getWord(2));
            }
        } else {
            nGram.setWords(words[0], words[1], mostMatchingGram.getWord(2));
        }
        return nGram;


    }

    private String findWord(int index) {
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            if (entry.getValue() == index) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String restoreOrder(String sequence) {
        List<String> words = ParseHelper.parse(sequence);
        combinations(new ArrayList<String>(), words, 1);
        Map.Entry<List<String>, Float> mostProbable = null;
        for (Map.Entry<List<String>, Float> entry : combinations.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
            if (mostProbable == null) {
                mostProbable = entry;
            } else if (mostProbable.getValue() < entry.getValue()) {
                mostProbable = entry;
            }
        }
        String s = "";
        for (String s1 : mostProbable.getKey()) {
            s += s1 + " ";
        }
        return s;
    }

    private void combinations(List<String> has, List<String> rem, float probability) {
        int[] occurrence = new int[rem.size()];
        int occurrenceSum = 0;
        int i = 0;
        if (has.size() < N - 1) {
            for (int j = 0; j < rem.size(); j++) {
                String s = rem.get(j);
                has.add(s);
                rem.remove(j);
                combinations(has, rem, 1);
                has.remove(s);
                rem.add(j, s);
            }
            return;
        }
        for (String s : rem) {
            List<String> temp = new ArrayList<String>();
            for (int j = 0; j < N - 1; j++) {
                temp.add(has.get(has.size() - N + 1 + j));
            }
            temp.add(s);
            long gramIndex = getGramIndex(temp);
            occurrence[i] = grams.containsKey(gramIndex) ? grams.get(gramIndex).getOccurrenceCount() + 1 : 1;

            occurrenceSum += occurrence[i];
            i++;
        }

        for (int j = 0; j < rem.size(); j++) {
            String s = rem.get(j);
            has.add(s);
            rem.remove(j);
            if (rem.isEmpty()) {
                if (has.size() == N) {
                    combinations.put(new ArrayList<String>(has), probability * occurrence[j]);
                } else {
                    combinations.put(new ArrayList<String>(has), probability);
                }
            } else {
                combinations(has, rem, probability * occurrence[j] / occurrenceSum);
            }
            rem.add(j, s);
            has.remove(s);
        }
    }

    public String  generateSentence() {
        Random random = new Random();
        String a;
        String b;
        while (true) {
            NGram g = null;
            for (NGram gram : grams.values()) {
                if (random.nextInt(100) == 1) {
                    g = gram;
                }
            }
//            while ((g = grams.get(random.nextLong())) == null);
//            a = findWord(random.nextInt(wordMap.size() - 1));
//            b = findWord(random.nextInt(wordMap.size() - 1));
            a = g.getWord(0);
            b = g.getWord(1);
            if (Character.isUpperCase(a.charAt(0)) && finishGram(a + " " + b + " " + UNKNOWN_WORD) != null) {
                break;
            }
        }

        String sentence = a + " " + b;
        for (int i = 0; i < 20; i++) {
            String c = a;
            a = b;
            NGram gram = finishGram(c + " " + b + " " + UNKNOWN_WORD);
            b = gram.getWord(2);
            sentence += " " + b;
            System.out.println(sentence);
        }
        return sentence;
    }

    private List<NGram> getByTemplate(String template) {
        List<String> l = ParseHelper.parse(template);
        String[] words = new String[l.size()];
        l.toArray(words);
        int unknownWordPosition = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(UNKNOWN_WORD)) {
                unknownWordPosition = i;
                break;
            }
        }
        List<NGram> result = getGramsByTemplate(template);
        if (result.isEmpty()) {
            if (unknownWordPosition == 0) {
                result = getGramsByTemplate(UNKNOWN_WORD + " " + UNKNOWN_WORD + " " + words[2]);
                if (result.isEmpty()) {
                    return getGramsByTemplate(UNKNOWN_WORD + " " + words[1] + " " + UNKNOWN_WORD);
                } else {
                    return result;
                }
            } else if (unknownWordPosition == 1) {
                result = getGramsByTemplate(UNKNOWN_WORD + " " + UNKNOWN_WORD + " " + words[2]);
                if (result.isEmpty()) {
                    return getGramsByTemplate(words[0] + " " + UNKNOWN_WORD + " " + UNKNOWN_WORD);
                } else {
                    return result;
                }
            } else {
                result = getGramsByTemplate(words[0] + " " + UNKNOWN_WORD + " " + UNKNOWN_WORD);
                if (result.isEmpty()) {
                    return getGramsByTemplate(UNKNOWN_WORD + " " + words[1] + " " + UNKNOWN_WORD);
                } else {
                    return result;
                }
            }
        } else {
            return result;
        }
    }

    /**
     * Находит все n-граммы подходящие под данное высказывание
     *
     * @param template
     * @return
     */
    private List<NGram> getGramsByTemplate(String template) {
        List<String> l = ParseHelper.parse(template);
        String[] words = new String[l.size()];
        l.toArray(words);
        int unknownWordPosition = -1;
        int otherWordPosition = -1;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(UNKNOWN_WORD)) {
                if (unknownWordPosition == -1) {
                    unknownWordPosition = i;
//                break;
                } else {
                    otherWordPosition = i;
                }
            }
        }

        List<NGram> filteredGrams = new ArrayList<NGram>();
        long firstMatchingGram;
        try {
            firstMatchingGram = getGramIndex(Arrays.asList(words));
        } catch (NoSuchWordException e) {
            return Collections.EMPTY_LIST;
        }
        long digit = Utils.pow(wordMap.size(), unknownWordPosition);
        long otherDigit = Utils.pow(wordMap.size(), otherWordPosition);
        for (int i = 0; i < wordMap.size(); i++) {
            if (otherWordPosition == -1) {
                if (grams.containsKey(firstMatchingGram + i * digit)) {
                    NGram foundGram = grams.get(firstMatchingGram + i * digit);
                    filteredGrams.add(foundGram);
                    //System.out.println("----> " + foundGram + ", occurrence - " + foundGram.getOccurrenceCount());
                }
            } else {
                for (int j = 0; j < wordMap.size(); j++) {
                    if (grams.containsKey(firstMatchingGram + i * digit + j * otherDigit)) {
                        NGram foundGram = grams.get(firstMatchingGram + i * digit + j * otherDigit);
                        filteredGrams.add(foundGram);
                        //System.out.println("----> " + foundGram + ", occurrence - " + foundGram.getOccurrenceCount());
                    }
                }
            }
        }
        return filteredGrams;
    }

    private void initGrams(List<String> words) {
        List<String> gram = new LinkedList<String>();
        if (words.size() < N) {
            throw new RuntimeException("Gram length can not be less than words count");
        }
        for (int i = 0; i < N; i++) {
            gram.add(words.get(i));
        }
        for (int i = N; i <= words.size(); i++) {
            long gramIndex = getGramIndex(gram);
            if (!grams.containsKey(gramIndex)) {
                grams.put(gramIndex, new NGram(gram));
            } else {
                grams.get(gramIndex).incrementCount();
            }
            gram.remove(0);

            if (i == words.size()) {
                break;
            }

            gram.add(words.get(i));
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
            if (word.equals(UNKNOWN_WORD)) {
                n++;
                continue;
            }
            try {
                index += wordMap.get(word) * Utils.pow(wordMap.size(), n);
            } catch (NullPointerException e) {
//                todo not exists
                throw new NoSuchWordException("Word '" + word + "' doesn't exist in language model");

//                index += wordMap.get(ANY_OTHER_WORD) * Utils.pow(wordMap.size(), n);
            }
            n++;
        }
        return index;
    }
}