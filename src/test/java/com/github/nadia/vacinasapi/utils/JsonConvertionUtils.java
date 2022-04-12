package com.github.nadia.vacinasapi.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonConvertionUtils {

    public static String asJsonString(Object object){
        try {
            //Configurações de Serealização/Deserealização do ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
            objectMapper.registerModule(new JavaTimeModule());

            //Transformação do Objeto em String Serealizada
            return objectMapper.writeValueAsString(object);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
