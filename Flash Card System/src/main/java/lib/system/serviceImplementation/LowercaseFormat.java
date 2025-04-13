package lib.system.serviceImplementation;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import lib.system.entries.Entry;
import lib.system.services.WordFormat;

@Service
@Profile("lowerCase")
public class LowercaseFormat implements WordFormat {
    @Override
    public String printFormattedWord(String string) {
        return string.toLowerCase();
    }

    @Override
    public String printFormattedWordFromEntry(Entry entry) {
        return entry.toString().toLowerCase();
    }
}