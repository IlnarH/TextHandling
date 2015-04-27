package com.texthandling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ильнар on 21.04.2015.
 */
public class NGram {

    private List<String> words;

    private int occurrenceCount;

    public NGram(String... words) {
        setWords(words);
        occurrenceCount = 1;
    }

    public NGram(List<String> words) {
        this.words = new ArrayList<String>(words);
        occurrenceCount = 1;
    }

    public List<String> getWords() {
        return words;
    }

    public String getWord(Integer position) {
        if (position >= words.size()) {
            throw new RuntimeException("Gram has not word at this position");
        }
        return words.get(position);
    }

    public void setWords(String... words) {
        this.words = Arrays.asList(words);
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
        for (String word : words) {
            stringBuilder.append(word);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().trim();
    }
}
