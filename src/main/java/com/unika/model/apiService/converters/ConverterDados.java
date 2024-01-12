package com.unika.model.apiService.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.Serializable;
import java.util.List;

public class ConverterDados implements IConverterDados, Serializable {
    private static final long serialVersionUID = -4582920413398912145L;
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
            } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> obterLista(String json, Class<T> classe) {
        CollectionType lista = mapper.getTypeFactory()
                .constructCollectionType(List.class, classe);
        try {
            return mapper.readValue(json, lista);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String obterJason(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
