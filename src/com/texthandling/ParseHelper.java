package com.texthandling;

import java.io.*;
import java.util.*;

/**
 * Created by Ильнар on 28.04.2015.
 */
public class ParseHelper {

    private static String REGEX = "(?= - )|(?=\\b)|(?=\\\\)|(?=, )|(?=\\[)|(?=\\])|\\s+";

    public static List<String> getSequence(String s) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File("Text.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String content = "";
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                content += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parse(content);
    }


    public static List<String> parse(String s) {
        List<String> list = new ArrayList<String>();
        for (String s1 : s.split(REGEX)) {
            if (!s1.trim().equals("")) {
                list.add(s1);
            }
        }
        return list;
    }
}
