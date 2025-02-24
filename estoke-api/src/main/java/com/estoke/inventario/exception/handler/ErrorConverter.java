package com.estoke.inventario.exception.handler;

import com.estoke.inventario.exception.ErroDaApi;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ErrorConverter {

    public Map<String, Object> criarJsonDeErro(ErroDaApi erroDaApi, String msgDeErro) {
        return criarMapDeErro(erroDaApi, msgDeErro);
    }

    public Map<String, Object> criarMapDeErro(ErroDaApi erroDaApi, String msgDeErro) {
        Map<String, Object> body = new LinkedHashMap<>();

        List<Map<String, Object>> detalhes = new ArrayList<>();

        Map<String, Object> detalhe = new LinkedHashMap<>();
        detalhe.put("mensagem", msgDeErro);
        detalhe.put("codigo", erroDaApi.getCodigo());

        detalhes.add(detalhe);
        body.put("erros", detalhes);

        return body;
    }
}
