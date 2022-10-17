package DMEngine;

import generated.CTEDictionary;

import java.util.*;
import java.util.stream.Collectors;

public class EnigmaDictionary {
    private final String ABC;
    private Set<String> words;
    private String excluded;

    public EnigmaDictionary(CTEDictionary xmlDictionary, String abc){
        this.ABC = abc;
        setDictionaryFromXml(xmlDictionary);
    }

    private void setDictionaryFromXml(CTEDictionary xmlDictionary) {
        String xmlWords = xmlDictionary.getWords().toUpperCase().trim();
        this.excluded = xmlDictionary.getExcludeChars().toUpperCase();

        String reg = "[" + this.excluded +"]*";
        xmlWords = xmlWords.replaceAll(reg, "");

        this.words = new HashSet<>(Arrays.asList(xmlWords.split(" ")));

        this.words = this.words.stream().map(String::toUpperCase).collect(Collectors.toSet());


    }

    public boolean isInDictionary(String word) {
        return words.contains(word.toUpperCase());
    }

    public String getExcluded() {
        return excluded;
    }

    public String getWords(){
        String res = "";

        for (String word : this.words) {
            res += word + System.lineSeparator();
        }

        return res;
    }
}
