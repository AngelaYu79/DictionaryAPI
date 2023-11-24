package algonquin.cst2335.dictionaryapi;

public class Definition {
    private String word;
    private String definition;

    public Definition(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }
}
