package com.kumoasobi.scribble.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DictionaryLoader {
    public static Set<String> loadDictionarySet(String filepath) {
        BufferedReader br;
        FileReader fr;
        Set<String> dict = new HashSet<>();
        try {
            fr = new FileReader(filepath);
            br = new BufferedReader(fr); 

            String line;
            while ((line = br.readLine()) != null) {
                dict.add(line.trim().toUpperCase());
            }
        } catch (IOException e) {
        }
        return dict;
    }
}
