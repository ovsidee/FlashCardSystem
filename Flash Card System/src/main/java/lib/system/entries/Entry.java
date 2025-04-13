package lib.system.entries;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private String translationEnglish;

    @Column(nullable = false)
    private String translationGerman;

    @Column(nullable = false)
    private String translationPolish;

    public Entry() {}

    public Entry(String translationEnglish, String translationGerman, String translationPolish) {
        this.translationEnglish = translationEnglish;
        this.translationGerman = translationGerman;
        this.translationPolish = translationPolish;
    }

    public String getTranslationGerman() {
        return translationGerman;
    }

    public String getTranslationPolish() {
        return translationPolish;
    }

    public String getTranslationEnglish() {
        return translationEnglish;
    }

    public Long getID() {
        return ID;
    }

    public void setTranslationPolish(String translationPolish) {
        this.translationPolish = translationPolish;
    }

    public void setTranslationEnglish(String translationEnglish) {
        this.translationEnglish = translationEnglish;
    }

    public void setTranslationGerman(String translationGerman) {
        this.translationGerman = translationGerman;
    }

    @Override
    public String toString() {
        return translationEnglish + " - " + translationGerman + ", " + translationPolish;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entry entry = (Entry) obj;
        return  translationEnglish.equalsIgnoreCase(entry.translationEnglish) &&
                translationGerman.equalsIgnoreCase(entry.translationGerman) &&
                translationPolish.equalsIgnoreCase(entry.translationPolish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translationEnglish.toLowerCase(), translationGerman.toLowerCase(), translationPolish.toLowerCase());
    }
}