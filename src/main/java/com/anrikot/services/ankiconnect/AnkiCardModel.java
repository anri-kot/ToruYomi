package com.anrikot.services.ankiconnect;

import java.util.List;
import java.util.Map;

public class AnkiCardModel {
    String modelName;
    List<String> inOrderFields;
    Map<String, String> cardTemplates;

    

    public AnkiCardModel(String modelName, List<String> inOrderFields, Map<String, String> cardTemplates) {
        this.modelName = modelName;
        this.inOrderFields = inOrderFields;
        this.cardTemplates = cardTemplates;
    }
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public List<String> getInOrderFields() {
        return inOrderFields;
    }
    public void setInOrderFields(List<String> inOrderFields) {
        this.inOrderFields = inOrderFields;
    }
    public Map<String, String> getCardTemplates() {
        return cardTemplates;
    }
    public void setCardTemplates(Map<String, String> cardTemplates) {
        this.cardTemplates = cardTemplates;
    }
    
}
