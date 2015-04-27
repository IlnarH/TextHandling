package com.texthandling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ильнар on 21.04.2015.
 */
public class NGram {

    private List<Token> words;

    private int occurrenceCount;

    public NGram(Token... words) {
        setWords(words);
        occurrenceCount = 1;
    }

    public NGram(String... words) {
        setWords(words);
        occurrenceCount = 1;
    }

    public NGram(List<Token> words) {
        this.words = new ArrayList<Token>(words);
        occurrenceCount = 1;
    }

    public List<Token> getWords() {
        return words;
    }

    public Token getWord(Integer position) {
        if (position >= words.size()) {
            throw new RuntimeException("Gram has not word at this position");
        }
        return words.get(position);
    }

    public void setWords(Token... words) {
        this.words = Arrays.asList(words);
    }
    public void setWords(String... words) {
        this.words = new ArrayList<Token>();
        for (String word : words) {
            this.words.add(new Token(word));
        }
    }

    public int getOccurrenceCount() {
        return occurrenceCount;
    }

    public void incrementCount() {
        occurrenceCount++;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Token word : words) {
            stringBuilder.append(word.getString());
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().trim();
    }
}
