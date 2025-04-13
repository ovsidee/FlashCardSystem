package lib.system.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import lib.system.entries.Entry;
import lib.system.repository.SpringDataEntryRepository;
import lib.system.services.WordFormat;

import java.util.*;

@Controller
public class FlashcardsController {
    public SpringDataEntryRepository springDataEntryRepository;
    public WordFormat wordFormat;
    public Random random;
    public Scanner scanner;

    public FlashcardsController( SpringDataEntryRepository springDataEntryRepository, WordFormat wordFormat, Random random, Scanner scanner) {
        this.springDataEntryRepository = springDataEntryRepository;
        this.wordFormat = wordFormat;
        this.random = random;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println(
                          "\nType \"1\" to get the list of all flashcards.\n" +
                            "Type \"2\" to add a new flashcard.\n" +
                            "Type \"3\" to get a test.\n" +
                            "Type \"4\" to modify a flashcard.\n" +
                            "Type \"5\" to remove a flashcard.\n" +
                            "Type \"6\" to sort flashcards.\n" +
                            "Type \"7\" to find a flashcard.\n" +
                            "Type \"8\" to exit."
            );
            String command = scanner.next();
            switch (command) {
                case "1" ->  printAllEntries();
                case "2" ->  userAddEntry();
                case "3" -> {
                    System.out.println("Random word test:");
                    Entry randomEntry = springDataEntryRepository.getRandomEntry();

                    int randomNumber = random.nextInt(3) + 1;
                    switch (randomNumber) {
                        // 1 - English (user must enter German + Polish)
                        case 1 -> userWriteGermanPolishWords(randomEntry);
                        // 2 - German (user must enter English + Polish)
                        case 2 -> userWriteEnglishPolishWords(randomEntry);
                        // 3 - Polish (user must enter English + German)
                        case 3 -> userWriteEnglishGermanWords(randomEntry);
                    }
                }
                case "4" -> modifyEntry();
                case "5" -> removeFlashCard();
                case "6" -> sortFlashCards();
                case "7" -> searchFlashcards();
                case "8" -> {
                    System.out.println("Bye bye...");
                    System.exit(1);
                }
                default -> System.out.println("Incorrect input.");
            }
        }
    }

    public void printAllEntries() {
        for (Entry entry : springDataEntryRepository.findAll()) {
            System.out.println(
                    "ID: \"" + entry.getID() + "\"," +
                            " English: \"" + wordFormat.printFormattedWord(entry.getTranslationEnglish()) + "\"," +
                            " German: \"" + wordFormat.printFormattedWord(entry.getTranslationGerman()) + "\"," +
                            " Polish: \"" + wordFormat.printFormattedWord(entry.getTranslationPolish()) + "\""
            );
        }
    }

    public void userAddEntry() {
        System.out.println("Write the word in English");
        String wordInEnglish = scanner.next();

        System.out.println("Write the word in German");
        String wordInGerman = scanner.next();

        System.out.println("Write the word in Polish");
        String wordInPolish = scanner.next();

        Entry newEntry = new Entry(wordInEnglish, wordInGerman, wordInPolish);
        if (springDataEntryRepository.findAll().contains(newEntry)) {
            System.out.println("This flashcard already exists");
        } else {
            springDataEntryRepository.save(newEntry);
            System.out.println("Added a new flashcard: " + wordFormat.printFormattedWordFromEntry(newEntry));
        }
    }

    public void removeFlashCard() {
        printAllEntries();
        System.out.println("Write an ID of the flashcard you want to remove: ");
        Long idToRemove = scanner.nextLong();

        Optional<Entry> optionalEntry = springDataEntryRepository.findById(idToRemove);
        if (optionalEntry.isEmpty()){
            System.out.println("Error: Entry not found.");
        } else {
            springDataEntryRepository.deleteById(idToRemove);
            System.out.println("Entry deleted successfully.");
        }
    }

    public void sortFlashCards() {
        String fieldName = chooseLanguageFindFieldName();

        System.out.println("Choose order to sort words: \"ASC\" - ascendingly, \"DESC\" - descendingly");
        String orderChoice = scanner.next();

        List<Entry> sortedEntries;
        switch (orderChoice){
            case "ASC" ->{
                sortedEntries = springDataEntryRepository.findAll(
                        Sort.by(Sort.Direction.ASC, fieldName)
                );
            }
            case "DESC" -> {
                sortedEntries = springDataEntryRepository.findAll(
                        Sort.by(Sort.Direction.DESC, fieldName)
                );
            }
            default -> {
                System.out.println("Incorrect input.");
                return;
            }
        }

        for (Entry entry : sortedEntries){
            System.out.println("English: " + wordFormat.printFormattedWord(entry.getTranslationEnglish()) +
                    ", German: " + wordFormat.printFormattedWord(entry.getTranslationGerman()) +
                    ", Polish: " + wordFormat.printFormattedWord(entry.getTranslationPolish()));
        }
    }

    public void modifyEntry() {
        printAllEntries();
        System.out.println("Write an ID of the Flashcard you want to modify: ");
        Long idToModify = scanner.nextLong();
        scanner.nextLine();

        Optional<Entry> optionalEntry = springDataEntryRepository.findById(idToModify);

        if (optionalEntry.isEmpty()) {
            System.out.println("Error: Entry not found.");
        } else {
            Entry entryToUpdate = optionalEntry.get();

            System.out.println("Enter new English translation (current: " + wordFormat.printFormattedWord(entryToUpdate.getTranslationEnglish()) + "): ");
            String newEnglish = scanner.nextLine();

            System.out.println("Enter new German translation (current: " + wordFormat.printFormattedWord(entryToUpdate.getTranslationGerman()) + "): ");
            String newGerman = scanner.nextLine();

            System.out.println("Enter new Polish translation (current: " + wordFormat.printFormattedWord(entryToUpdate.getTranslationPolish()) + "): ");
            String newPolish = scanner.nextLine();

            entryToUpdate.setTranslationEnglish(newEnglish);
            entryToUpdate.setTranslationGerman(newGerman);
            entryToUpdate.setTranslationPolish(newPolish);

            springDataEntryRepository.save(entryToUpdate);
            System.out.println("Modify entry successfully.");
        }
    }

    public void searchFlashcards() {
        String fieldName = chooseLanguageFindFieldName();
        if (fieldName == null) return;

        System.out.println("Enter word that you want to search: ");
        String word = scanner.next();

        List<Entry> searchResults;

        switch (fieldName) {
            case "translationEnglish" -> searchResults = springDataEntryRepository.searchByEnglishWord("%" + word + "%");
            case "translationGerman" -> searchResults = springDataEntryRepository.searchByGermanWord("%" + word + "%");
            case "translationPolish" -> searchResults = springDataEntryRepository.searchByPolishWord("%" + word + "%");
            default ->{
                System.out.println("Invalid search field.");
                return;
            }
        }

        if (!searchResults.isEmpty()) {
            for (Entry entry : searchResults) {
                System.out.println(
                        "ID: \"" + entry.getID() + "\"," +
                                " English: \"" + wordFormat.printFormattedWord(entry.getTranslationEnglish()) + "\"," +
                                " German: \"" + wordFormat.printFormattedWord(entry.getTranslationGerman()) + "\"," +
                                " Polish: \"" + wordFormat.printFormattedWord(entry.getTranslationPolish()) + "\""
                );
            }
        } else {
            System.out.println("No flashcards found.");
        }
    }

    public void userWriteEnglishGermanWords(Entry randomEntry) {
        System.out.println("Translate this word to English: \"" + wordFormat.printFormattedWord(randomEntry.getTranslationPolish()) + "\"(polish)");
        String wordInEnglish = scanner.next();

        if (wordInEnglish.equalsIgnoreCase(randomEntry.getTranslationEnglish())) {
            System.out.println("Correct! Now write it in German:");
        } else {
            System.out.println("Incorrect! The correct answer was: \"" + wordFormat.printFormattedWord(randomEntry.getTranslationEnglish()) + "\"" + ". Now write it in German:");
        }

        String wordInGerman = scanner.next();
        if (wordInGerman.equalsIgnoreCase(randomEntry.getTranslationGerman())) {
            System.out.println("Great job! All Correct!");
        } else {
            System.out.println("Incorrect! The correct answer was: \"" + wordFormat.printFormattedWord(randomEntry.getTranslationGerman()) + "\"");
        }
    }

    public void userWriteGermanPolishWords(Entry randomEntry) {
        System.out.println("Translate this word to German: \"" + wordFormat.printFormattedWord(randomEntry.getTranslationEnglish()) + "\"(english)");
        String wordInGerman = scanner.next();

        if (wordInGerman.equalsIgnoreCase(randomEntry.getTranslationGerman())) {
            System.out.println("Correct! Now write it in Polish:");
        } else {
            System.out.println("Incorrect! The correct answer was: \"" + wordFormat.printFormattedWord(randomEntry.getTranslationGerman()) + "\"" + ". Now write it in Polish:");
        }

        String wordInPolish = scanner.next();
        if (wordInPolish.equalsIgnoreCase(randomEntry.getTranslationPolish())) {
            System.out.println("Great job! All Correct!");
        } else {
            System.out.println("Incorrect! The correct answer was: \"" + wordFormat.printFormattedWord(randomEntry.getTranslationPolish()) + "\"");
        }
    }

    public void userWriteEnglishPolishWords(Entry randomEntry) {
        System.out.println("Translate this word to English: \"" + wordFormat.printFormattedWord(randomEntry.getTranslationGerman()) + "\"(german)");
        String wordInEnglish = scanner.next();

        if (wordInEnglish.equalsIgnoreCase(randomEntry.getTranslationEnglish())) {
            System.out.println("Correct! Now write it in Polish:");
        } else {
            System.out.println("Incorrect! The correct answer was: \"" + wordFormat.printFormattedWord(randomEntry.getTranslationEnglish()) + "\"" + ". Now write it in Polish:");
        }

        String wordInPolish = scanner.next();
        if (wordInPolish.equalsIgnoreCase(randomEntry.getTranslationPolish())) {
            System.out.println("Great job! All Correct!");
        } else {
            System.out.println("Incorrect! The correct answer was: \"" + wordFormat.printFormattedWord(randomEntry.getTranslationPolish()) + "\"");
        }
    }

    public String chooseLanguageFindFieldName() {
        System.out.println("Choose a language to sort by (english, german, polish): ");
        String languageChoice = scanner.next().toLowerCase();

        String fieldName;
        switch (languageChoice) {
            case "english" -> fieldName = "translationEnglish";
            case "german" -> fieldName = "translationGerman";
            case "polish" -> fieldName = "translationPolish";
            default -> {
                System.out.println("Invalid language choice.");
                return null;
            }
        }
        return fieldName;
    }
}