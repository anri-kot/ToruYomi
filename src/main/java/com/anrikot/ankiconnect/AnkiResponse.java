package com.anrikot.ankiconnect;

public class AnkiResponse {
    private Object result;
    private String error;

    

    @Override
    public String toString() {
        return "AnkiResponse [result=" + getResult() + 
            ", error=" + getError() + "]";
    }
    public Object getResult() {
        return result;
    }
    public void setResult(Object result) {
        this.result = result;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

    
}
