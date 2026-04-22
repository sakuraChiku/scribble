package com.kumoasobi.scribble.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Copy right notice for resources/assets/full_wordlist.dat
 * 
 * Custom wordlist generated from http://app.aspell.net/create using SCOWL with
 * parameters: diacritic: strip max_size: 80 max_variant: 1 special: hacker
 * roman-numerals spelling: US  *
 * Copyright 2000-2014 by Kevin Atkinson
 *
 * Permission to use, copy, modify, distribute and sell these word lists, the
 * associated scripts, the output created from the scripts, and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appears in all copies and that both that copyright
 * notice and this permission notice appear in supporting documentation. Kevin
 * Atkinson makes no representations about the suitability of this array for any
 * purpose. It is provided "as is" without express or implied warranty.
 *
 * Copyright (c) J Ross Beresford 1993-1999. All Rights Reserved.
 *
 * The following restriction is placed on the use of this publication: if The UK
 * Advanced Cryptics Dictionary is used in a software package or redistributed
 * in any form, the copyright notice must be prominently displayed and the text
 * of this document must be included verbatim.
 *
 * Copy right notice for resources/assets/stan_dict.txt
 * 
 * This word list is sourced from:
 * https://inventwithpython.com/dictionary.txt
 *
 * The original content is provided by Al Sweigart (Invent with Python).
 *
 * This file is used in this project ("Scribble") for educational and 
 * non-commercial purposes only.
 *
 * All rights to the original content belong to its respective author.
 * No copyright infringement is intended.
 *
 * If the copyright holder has any concerns regarding the use of this file,
 * please contact the project author and the content will be removed promptly.
 * 
 * @author Yicheng Ying, Junyu Li
 * @version 1.0
 */

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
