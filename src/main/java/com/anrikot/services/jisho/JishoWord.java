package com.anrikot.services.jisho;

import java.util.List;

public class JishoWord {
    private String word;
    private List<String> readings;
    private List<String> meanings;

    public JishoWord(String word, List<String> readings, List<String> meanings) {
        this.word = word;
        this.readings = readings;
        this.meanings = meanings;
    }
    

    @Override
    public String toString() {
        return "JishoWord [word=" + word + ", readings=" + readings + ", meanings=" + meanings.toString() + "]";
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getReadings() {
        return readings;
    }

    public void setReadings(List<String> readings) {
        this.readings = readings;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }
    
}
