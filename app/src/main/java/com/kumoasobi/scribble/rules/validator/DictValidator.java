package com.kumoasobi.scribble.rules.validator;

import java.util.Set;

import com.kumoasobi.scribble.exceptions.WordNotValidException;
import com.kumoasobi.scribble.models.WordInfo;

public class DictValidator {
    public static void isValidWord(WordInfo wordInfo, Set<String> dict) {
        String word = wordInfo.getWord();
        if (!dict.contains(word)) {
            throw new WordNotValidException("The word '" + word + "' is not valid!");
        }
    }
}
