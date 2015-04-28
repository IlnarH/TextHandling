package com.texthandling;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.*;

/**
 * Created by Ильнар on 21.04.2015.
 */
public class LanguageModel implements Serializable {

    //    мерность n-граммов
    private int N = 3;

    //    список всех встречающихся слов
    Map<Token, Integer> wordMap = new LinkedHashMap<Token, Integer>();

    //    список всех n-граммов
    public Map<Long, NGram> grams = new HashMap<Long, NGram>();

    public Coll baseCollection = new Coll();
    public Coll invertedBaseCollection = new Coll();

    public HashMap<Token, Coll> c1 = new HashMap<>();
    public HashMap<Token, Coll> c2 = new HashMap<>();


    private LanguageModel(){}

    private class NoSuchWordException extends RuntimeException {

        public NoSuchWordException(String message) {
            super(message);
        }
    }


    public static LanguageModel getNewLanguageModel(String input, int dimension){
        LanguageModel out = new LanguageModel();

        String[] words = input.split("(?= - )|(?=[!'()*+,.:;<=>?@[\\\\]_`])|\\s+");
        ArrayList<Token> lst = new ArrayList<>();

//        заполение списка слов
        for (String word : words) {
            if (!out.wordMap.containsKey(new Token(word))) {
                out.wordMap.put(new Token(word), out.wordMap.size());
            }
            lst.add(new Token(word));
        }
//        построение модели
        out.initGramsStruct(lst.toArray(new Token[lst.size()]));
        out.initGrams(words);
        out.N = dimension;
        System.out.println("\nLanguage model created");
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


    public void writeLanguageModel(File file) throws IOException{

        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    public static LanguageModel readLanguageModel(File file) {
        if(!file.exists()){
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (LanguageModel) ois.readObject();
        } catch (Exception e){
            return null;
        }

    }


    /**
     * Завершает высказывание исходя из языковой модели (пустое слово помечено как '?????')
     *
     * @return
     */
   /* public String finishGram(String template) {
        NGram mostMatchingGram = null;
        for (NGram gram : getGramsByTemplate(template)) {
            if (mostMatchingGram == null) {
                mostMatchingGram = gram;
            } else if (gram.getOccurrenceCount() > mostMatchingGram.getOccurrenceCount()) {
                mostMatchingGram = gram;
            }
        }
        return mostMatchingGram != null ? mostMatchingGram.toString() : "Gram not found";
    }*/

    /**
     * Находит все n-граммы подходящие под данное высказывание
     *
     */
   /* private List<NGram> getGramsByTemplate(String template) {                            //Переделать, чтобы принимала на вход токены
        String[] words = template.split(" ");
        List<Token> lst = new LinkedList<>();
        for (String word : words) {
            lst.add(new Token(word));
        }

        int unknownWordPosition = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("?????")) {
                lst.set(i, new Token(lst.get(i).getString(), TokenType.template));
                unknownWordPosition = i;
                break;
            }
        }

        List<NGram> filteredGrams = new ArrayList<NGram>();
        long firstMatchingGram = getGramIndex(lst);
        long digit = Utils.pow(wordMap.size(), unknownWordPosition);
        for (int i = 0; i < wordMap.size(); i++) {
            if (grams.containsKey(firstMatchingGram + i * digit)) {
                NGram foundGram = grams.get(firstMatchingGram + i * digit);
                filteredGrams.add(foundGram);
                System.out.println("----> " + foundGram + ", occurrence - " + foundGram.getOccurrenceCount());
            }
        }
        return filteredGrams;
    }*/

    //    todo harder logic (sentences)
    private void initGrams(String[] words) {
        List<Token> gram = new LinkedList<Token>();
        if (words.length < N) {
//        todo sentences again
            throw new RuntimeException("Gram length can not be less than words count");
        }
        for (int i = 0; i < N; i++) {
            gram.add(new Token(words[i]));
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

            gram.add(new Token(words[i]));
        }
    }


    private void initGramsStruct(Token[] words) {
        List<Token> gram = new LinkedList<Token>();
        if (words.length < N) {
//        todo sentences again
            throw new RuntimeException("Gram length can not be less than words count");
        }
        for (int i = 0; i < N; i++) {
            gram.add(words[i]);
        }
        Coll[] cool = new Coll[N];
        Coll[] cool2 = new Coll[N];
        for (int i = N; i <= words.length; i++) {
            cool[0] = new Coll(gram.get(0), null);
            cool2[0] = new Coll(gram.get(N - 1), null);
            for (int j = 1; j < N; j++) {
                cool[j] = new Coll(gram.get(j), cool[j - 1]);
                cool2[j] = new Coll(gram.get(N - j - 1), cool2[j - 1]);
            }
//            if(c1.containsKey(cool[N - 1].getNode())){
//                c1.get(cool[N - 1].getNode()).Unite(cool[N - 1]);
//            } else {
//                c1.put(cool[N - 1].getNode(), cool[N - 1]);
//            }

            if(c2.containsKey(cool2[N - 1].getNode())){
                c2.get(cool2[N - 1].getNode()).Unite(cool2[N - 1]);
            } else {
                c2.put(cool2[N - 1].getNode(), cool2[N - 1]);
            }

//            baseCollection.Unite(cool[N - 1]);
//            invertedBaseCollection.Unite(cool2[N - 1]);

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
     * @return
     */
    private long getGramIndex(List<Token> words) {
        int n = 0;
        long index = 0;
        for (Token word : words) {
//            unknown word
            if (word.getCondition() == TokenType.template) {
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