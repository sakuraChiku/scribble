package com.kumoasobi.scribble.logic;

import com.kumoasobi.scribble.models.WordInfo;

/**
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
 *
 * @author Yicheng Ying
 */

public class DictValidator {
    public void loadWords(String filename) {

    }

    public boolean isValidWord(WordInfo wordInfo) {
        String word = wordInfo.getWord();
        return true;
    }
}
