package lib.system.services;

import lib.system.entries.Entry;

public interface WordFormat {
    String printFormattedWordFromEntry(Entry entry);
    String printFormattedWord(String string);
}

