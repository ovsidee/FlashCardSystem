package lib.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import lib.system.entries.Entry;

import java.util.List;
import java.util.Random;

public interface SpringDataEntryRepository extends JpaRepository<Entry, Long> {

    default Entry getRandomEntry() {
        List<Entry> entries = findAll();
        if (entries.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return entries.get(random.nextInt(entries.size()));
    }

    @Query("SELECT e FROM Entry e WHERE LOWER(e.translationEnglish) LIKE LOWER(:word)")
    List<Entry> searchByEnglishWord(@Param("word") String word);

    @Query("SELECT e FROM Entry e WHERE LOWER(e.translationGerman) LIKE LOWER(:word)")
    List<Entry> searchByGermanWord(@Param("word") String word);

    @Query("SELECT e FROM Entry e WHERE LOWER(e.translationPolish) LIKE LOWER(:word)")
    List<Entry> searchByPolishWord(@Param("word") String word);
}