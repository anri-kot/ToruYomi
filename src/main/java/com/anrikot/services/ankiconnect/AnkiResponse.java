package com.anrikot.services.ankiconnect;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AnkiResponse {
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();
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

    public <T> List<T> getResultAsList(Class<T> theClass) {
        return OBJECT_MAPPER.convertValue(this.result, new TypeReference<List<T>>() {});
    }

    public <K, V> Map<K, V> getResultAsMap(Class<K> keyClass, Class<V> valueClass) {
        return OBJECT_MAPPER.convertValue(this.result, new TypeReference<Map<K, V>>() {});
    }

    
}
