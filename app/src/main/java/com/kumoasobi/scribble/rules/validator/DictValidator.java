package com.kumoasobi.scribble.rules.validator;

import java.util.Set;

import com.kumoasobi.scribble.exceptions.WordNotValidException;
import com.kumoasobi.scribble.models.WordInfo;

/**
 * 
 * @author Yicheng Ying, Chuhui Gu
 * @version 1.0
 */
public class DictValidator {
    public static void isValidWord(WordInfo wordInfo, Set<String> dict) {
        String word = wordInfo.getWord();
        if (!dict.contains(word)) {
            throw new WordNotValidException("The word '" + word + "' does not exist!");
        }
    }
}
