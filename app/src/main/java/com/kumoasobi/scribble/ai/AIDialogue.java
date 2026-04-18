package com.kumoasobi.scribble.ai;

public class AIDialogue {
    
    public static String getSuccessLine(AIDifficulty diff) {
        switch (diff) {
            case EASY -> {
                return pick(
                "Oh! That’s actually pretty good!",
                        "Did you see that? Not bad, right~",
                        "Lucky me!",
                        "Hehe~ that worked!"
                        );
            }
            case MEDIUM -> {
                return pick(
               "This seems appropriate.",
                       "A reasonable outcome.",
                       "That should suffice.",
                       "Proceeding steadily"
                );
            }
            case HARD -> {
                return pick(
                "As expected.",
                       "This is decisive.",
                       "You left an opening.",
                       "Your defences are weak"
                );
            }
        }
        return "";
    }

    public static String getFailLine(AIDifficulty diff) {
        switch (diff) {
            case EASY -> {
                return pick(
                        "Eh? That didn’t count?!",
                        "Aww, I thought that was fine…",
                        "Wait wait, that should work… right?",
                        "Oops~ guess I missed up!"
                );
            }
            case MEDIUM -> {
                return pick(
                        "I must reconsider this move.",
                        "That was not optimal.",
                        "There appears to be an error.",
                        "I will adjust my approach."
                );
            }
            case HARD -> {
                return pick(
                        "Unacceptable.",
                        "I miscalculated…",
                        "That should not have happened.",
                        "…I will correct this."
                );
            }
        }
        return "";
    }

    private static String pick(String... lines) {
        int i = (int)(Math.random() * lines.length);
        return lines[i];
    }
}
