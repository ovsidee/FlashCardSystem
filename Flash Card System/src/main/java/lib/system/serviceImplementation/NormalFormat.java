package lib.system.serviceImplementation;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import lib.system.entries.Entry;
import lib.system.services.WordFormat;

@Service
@Profile("original")
public class NormalFormat implements WordFormat {
    @Override
    public String printFormattedWord(String string) {
        return string;
    }

    @Override
    public String printFormattedWordFromEntry(Entry entry) {
        return entry.toString();
    }
}
