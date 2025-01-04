//package org.flatscrew.latte.ansi;
//
//
//public class TextWrapper {
//    private static final char NBSP = '\u00A0';  // non-breaking space
//
//    public static String wrap(String text, int limit, String breakpoints) {
//        if (limit < 1 || text == null) {
//            return text;
//        }
//
//        State state = new State();
//        ParserState pstate = ParserState.GROUND;
//
//        for (int i = 0; i < text.length(); i++) {
//            int cp = text.codePointAt(i);
//            if (Character.isSupplementaryCodePoint(cp)) {
//                i++; // Skip the second char of the surrogate pair
//            }
//            String grapheme = new String(Character.toChars(cp));
//
//            // Handle ANSI escape sequences
//            if (pstate == ParserState.GROUND && grapheme.charAt(0) == '\u001B') {
//                pstate = ParserState.ESCAPE;
//                state.word.append(grapheme);
//                continue;
//            }
//
//            if (pstate == ParserState.ESCAPE) {
//                state.word.append(grapheme);
//                if (grapheme.charAt(0) == '[') {
//                    pstate = ParserState.CSI;
//                } else {
//                    pstate = ParserState.GROUND;
//                }
//                continue;
//            }
//
//            if (pstate == ParserState.CSI) {
//                state.word.append(grapheme);
//                if (Character.isLetter(grapheme.charAt(0))) {
//                    pstate = ParserState.GROUND;
//                }
//                continue;
//            }
//
//            // Handle regular text
//            if (grapheme.equals("\n")) {
//                if (state.wordLen == 0) {
//                    if (state.curWidth + state.space.length() > limit) {
//                        state.curWidth = 0;
//                    } else {
//                        // preserve whitespaces
//                        state.buf.append(state.space);
//                    }
//                    state.space.setLength(0);
//                }
//                state.addWord();
//                state.addNewline();
//            } else if (Character.isWhitespace(grapheme.charAt(0)) && grapheme.charAt(0) != NBSP) {
//                state.addWord();
//                state.space.append(grapheme);
//            } else if (grapheme.equals("-") || breakpoints.contains(grapheme)) {
//                state.addSpace();
//                if (state.curWidth + state.wordLen >= limit) {
//                    // We can't fit the breakpoint in the current line, treat
//                    // it as part of the word.
//                    state.word.append(grapheme);
//                    state.wordLen++;
//                } else {
//                    state.addWord();
//                    state.buf.append(grapheme);
//                    state.curWidth++;
//                }
//            } else {
//                if (state.curWidth == limit) {
//                    state.addNewline();
//                }
//
//                int charWidth = getCharWidth(cp);
//
//                if (state.wordLen + charWidth > limit) {
//                    // Hardwrap the word if it's too long
//                    state.addWord();
//                }
//
//                state.word.append(grapheme);
//                state.wordLen += charWidth;
//
//                if (state.curWidth + state.wordLen + state.space.length() > limit) {
//                    state.addNewline();
//                }
//            }
//        }
//
//        if (state.wordLen == 0) {
//            if (state.curWidth + state.space.length() > limit) {
//                state.curWidth = 0;
//            } else {
//                // preserve whitespaces
//                state.buf.append(state.space);
//            }
//            state.space.setLength(0);
//        }
//
//        state.addWord();
//        return state.buf.toString();
//    }
//
//    private static int getCharWidth(int codePoint) {
//        // Handle supplementary characters (like emojis)
//        if (Character.isSupplementaryCodePoint(codePoint)) {
//            return 2;
//        }
//
//        // Handle CJK characters
//        Character.UnicodeBlock block = Character.UnicodeBlock.of(codePoint);
//        if (block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
//                block == Character.UnicodeBlock.HIRAGANA ||
//                block == Character.UnicodeBlock.KATAKANA ||
//                block == Character.UnicodeBlock.HANGUL_SYLLABLES) {
//            return 2;
//        }
//
//        return 1;
//    }
//
//    private static class State {
//        StringBuilder buf = new StringBuilder();
//        StringBuilder word = new StringBuilder();
//        StringBuilder space = new StringBuilder();
//        int curWidth = 0;  // written width of the line
//        int wordLen = 0;   // word buffer len without ANSI escape codes
//
//        void addSpace() {
//            curWidth += space.length();
//            buf.append(space);
//            space.setLength(0);
//        }
//
//        void addWord() {
//            if (word.isEmpty()) {
//                return;
//            }
//            addSpace();
//            curWidth += wordLen;
//            buf.append(word);
//            word.setLength(0);
//            wordLen = 0;
//        }
//
//        void addNewline() {
//            buf.append('\n');
//            curWidth = 0;
//            space.setLength(0);
//        }
//    }
//
//    private enum ParserState {
//        GROUND,
//        ESCAPE,
//        CSI
//    }
//}