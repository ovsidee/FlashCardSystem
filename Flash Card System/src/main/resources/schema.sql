CREATE TABLE IF NOT EXISTS Entry(
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    translationEnglish VARCHAR(255) NOT NULL,
    translationGerman VARCHAR(255) NOT NULL,
    translationPolish VARCHAR(255) NOT NULL
);
