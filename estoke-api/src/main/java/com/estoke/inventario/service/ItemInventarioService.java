package com.estoke.inventario.service;

import com.estoke.inventario.entity.ItemInventario;
import com.estoke.inventario.entity.enums.Disponibilidade;
import com.estoke.inventario.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ItemInventarioService {

    public ItemInventario salvar(
            @NotNull(message = "O item não pode ser nulo")
            ItemInventario itemInventario);

    public ItemInventario buscarPor(
            @NotNull(message = "O código do item é obrigatório")
            String codigoItem);

    public ItemInventario buscarPor(
            @NotNull(message = "O id é obrigatório")
            Integer id);

    public void excluirPor(
            @Positive(message = "O id para exclusão deve ser positivo")
            @NotNull(message = "O id é obrigatório")
            Integer id);

    public Page<ItemInventario> listarPor(
            @NotNull(message = "A disponibilidade para listagem não pode ser nula")
            Disponibilidade disponibilidade,
            @NotNull(message = "O status para listagem não pode ser nulo")
            Status status,
            Pageable paginacao);

    public Page<ItemInventario> listarPor(
            @NotBlank(message = "O código para listagem não pode ser nulo")
            String codigoItem,
            @NotNull(message = "A disponibilidade para listagem não pode ser nula")
            Disponibilidade disponibilidade,
            @NotNull(message = "O status para listagem não pode ser nulo")
            Status status,
            Pageable paginacao);

    public String buscaProximoNumeroSerie();
}
