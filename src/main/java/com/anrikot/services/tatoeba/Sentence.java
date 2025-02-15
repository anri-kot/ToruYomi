package com.anrikot.services.tatoeba;

public class Sentence {
    private String sentence;
    private String translation;
    private String transcription;

    public Sentence(String sentence, String translation, String transcription) {
        this.sentence = sentence;
        this.translation = translation;
        this.transcription = transcription;
    }
    
    @Override
    public String toString() {
        return "ExampleSentence [sentence=" + sentence + ", translation=" + translation + ", transcription="
                + transcription + "]";
    }

    public String getSentence() {
        return sentence;
    }
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }
    public String getTranscription() {
        return transcription;
    }
    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    
    
}
