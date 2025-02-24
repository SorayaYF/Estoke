package com.estoke.inventario.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class MapConverter implements Serializable{

    private static final long serialVersionUID = 1L;

    private final ObjectMapper conversor;

    public MapConverter(ObjectMapper conversor) {
        this.conversor = conversor;
    }

    public Map<String, Object> toJsonList(Page<?> page, String... exclusoes) {
        Map<String, Object> pageMap = new LinkedHashMap<>();

        if (!page.getContent().isEmpty()) {
            List<Map<String, Object>> listagem = new ArrayList<>();
            for (Object obj : page.getContent()) {
                listagem.add(toJsonMap(obj, exclusoes));
            }
            pageMap.put("listagem", listagem);

            pageMap.put("paginaAtual", page.getNumber());
            pageMap.put("totalDeItens", page.getTotalElements());
            pageMap.put("totalDePaginas", page.getTotalPages());
        } else {
            pageMap.put("listagem", Collections.emptyList());
            pageMap.put("paginaAtual", 0);
            pageMap.put("totalDeItens", 0);
            pageMap.put("totalDePaginas", 0);
        }

        return pageMap;
    }

    public List<Map<String, Object>> toJsonList(List<?> list, String... exclusoes){

        List<Map<String, Object>> listagem = new ArrayList<Map<String,Object>>();

        for (Object obj : list) {
            listagem.add(toJsonMap(obj, exclusoes));
        }

        return listagem;
    }

    public Set<Map<String, Object>> toJsonSet(Set<?> list, String... exclusoes){

        Set<Map<String, Object>> listagem = new HashSet<Map<String,Object>>();

        for (Object obj : list) {
            listagem.add(toJsonMap(obj, exclusoes));
        }

        return listagem;
    }

    public Map<String, Object> toJsonMap(Object obj, String... exclusoes) {
        try {
            String jsonPlano = conversor.writeValueAsString(obj);

            @SuppressWarnings("unchecked")
            Map<String, Object> mapConvertido = conversor.readValue(jsonPlano, LinkedHashMap.class);

            removeEmptyAndNullFields(mapConvertido, exclusoes);

            return mapConvertido;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    @SuppressWarnings("unchecked")
    private void removeEmptyAndNullFields(Object valor, String... exclusoes) {
        if (valor instanceof Map) {
            Map<String, Object> mapa = (Map<String, Object>) valor;
            for (Iterator<Map.Entry<String, Object>> it = mapa.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = it.next();
                String key = entry.getKey();
                Object childVal = entry.getValue();

                if (childVal == null || isLiberadoParaRemocao(key, exclusoes)) {
                    it.remove();
                } else {
                    removeEmptyAndNullFields(childVal, exclusoes);
                }
            }
        } else if (valor instanceof List) {
            List<Object> lista = (List<Object>) valor;
            for (Object item : lista) {
                removeEmptyAndNullFields(item, exclusoes);
            }
        }
    }

    private boolean isLiberadoParaRemocao(String key, String... exclusoes) {
        for (String outraKey : exclusoes) {
            if (key.equals(outraKey)) {
                return true;
            }
        }
        return false;
    }
}