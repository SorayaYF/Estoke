package com.estoke.inventario.controller;

import com.estoke.inventario.entity.ItemInventario;
import com.estoke.inventario.entity.enums.Disponibilidade;
import com.estoke.inventario.entity.enums.Status;
import com.estoke.inventario.service.ItemInventarioService;
import com.google.common.base.Preconditions;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/itens-inventario")
public class ItemInventarioController {

    private MapConverter converter;

    private final ItemInventarioService service;

    public ItemInventarioController(MapConverter converter, ItemInventarioService service) {
        this.converter = converter;
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> inserir(
            @Valid
            @RequestBody
            ItemInventario itemInventario) {
        Preconditions.checkArgument(!itemInventario.isPersistido(),
                "O item não pode possuir o id informado");
        ItemInventario itemInventarioSalvo = service.salvar(itemInventario);
        return ResponseEntity.created(URI.create(
                "/itens-inventario/id/" + itemInventarioSalvo.getId())).build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> alterar(
            @Valid
            @RequestBody
            ItemInventario itemInventario) {
        Preconditions.checkArgument(itemInventario.isPersistido(),
                "O id do item é obrigatório para atualização");
        ItemInventario itemInventarioAtualizado = service.salvar(itemInventario);
        return ResponseEntity.ok(converter.toJsonMap(itemInventarioAtualizado));
    }

    @Transactional
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> excluirPor(
            @PathVariable("id")
            @Positive(message = "O id para exclusão deve ser positivo")
            Integer id) {
        ItemInventario item = service.buscarPor(id);
        service.excluirPor(id);
        return ResponseEntity.ok(Map.of(
                "mensagem", "Item inativado com sucesso",
                "item", converter.toJsonMap(item)
        ));
    }

    @GetMapping
    public ResponseEntity<?> listarPor(
            @RequestParam(name = "codigoItem", required = false)
            String codigoitem,
            @RequestParam(name = "disponibilidade")
            Disponibilidade disponibilidade,
            @RequestParam(name = "status")
            Status status,
            @RequestParam(name = "pagina")
            Optional<Integer> pagina){
        Pageable paginacao = null;
        paginacao = pagina.map(integer -> PageRequest.of(integer, 5)).orElseGet(() -> PageRequest.of(0, 5));
        Page<ItemInventario> itens = null;
        if(codigoitem == null){
            itens = service.listarPor(disponibilidade, status, paginacao);
        } else {
            itens = service.listarPor(codigoitem, disponibilidade, status, paginacao);
        }
        return ResponseEntity.ok(converter.toJsonList(itens));
    }

    @GetMapping("/proximo-numero-serie")
    public ResponseEntity<?> proximoNumeroSerie() {
        String proximoValor = service.buscaProximoNumeroSerie();
        return ResponseEntity.ok(Map.of("numeroSerie", proximoValor));
    }
}
