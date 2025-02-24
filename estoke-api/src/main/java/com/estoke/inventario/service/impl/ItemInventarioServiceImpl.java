package com.estoke.inventario.service.impl;

import com.estoke.inventario.entity.ItemInventario;
import com.estoke.inventario.entity.enums.Disponibilidade;
import com.estoke.inventario.entity.enums.Status;
import com.estoke.inventario.repository.ItensInventarioRepository;
import com.estoke.inventario.service.ItemInventarioService;
import com.google.common.base.Preconditions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemInventarioServiceImpl implements ItemInventarioService {

    private final ItensInventarioRepository repository;

    public ItemInventarioServiceImpl(ItensInventarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public ItemInventario salvar(ItemInventario itemInventario) {
        boolean ehNovo = (itemInventario.getId() == null);
        ItemInventario outroItemInventario = repository.buscarPor(itemInventario.getCodigoItem());
        if (outroItemInventario != null && outroItemInventario.isPersistido()) {
            Preconditions.checkArgument(outroItemInventario.equals(itemInventario),
                    "O código do item já está em uso");
        }
        if (ehNovo) {
            Integer maxAtual = repository.buscaPorNumeroSerieMaximo();
            int proximo = (maxAtual == null ? 1 : maxAtual + 1);
            String numeroSerieGerado = String.format("%05d", proximo);
            itemInventario.setNumeroSerie(numeroSerieGerado);
        } else {
            repository.findById(itemInventario.getId()).ifPresent(existente -> Preconditions.checkArgument(!existente.getNumeroSerie().equals(itemInventario.getNumeroSerie()),
                    "Não é permitido alterar o número de série de um item existente"));
        }
        return repository.save(itemInventario);
    }

    @Override
    public ItemInventario buscarPor(String codigoItem) {
        ItemInventario itemInventarioEncontrado = repository.buscarPor(codigoItem);
        Preconditions.checkNotNull(itemInventarioEncontrado,
                "Não existe item no inventário para o código informado");
        Preconditions.checkArgument(itemInventarioEncontrado.isAtivo(),
                "O item está inativo");
        return itemInventarioEncontrado;
    }

    @Override
    public ItemInventario buscarPor(Integer id) {
        ItemInventario itemInventarioEncontrado = repository.buscarPor(id);
        Preconditions.checkNotNull(itemInventarioEncontrado,
                "Não existe item no inventário para o id informado");
        Preconditions.checkArgument(itemInventarioEncontrado.isAtivo(),
                "O item está inativo");
        return itemInventarioEncontrado;
    }

    @Override
    public void excluirPor(Integer id) {
        repository.inativar(id);
    }

    @Override
    public Page<ItemInventario> listarPor(Disponibilidade disponibilidade, Status status, Pageable paginacao) {
        return repository.listarPor(disponibilidade, status, paginacao);
    }

    @Override
    public Page<ItemInventario> listarPor(String codigoItem, Disponibilidade disponibilidade, Status status, Pageable paginacao) {
        return repository.listarPor(codigoItem + "%", disponibilidade, status, paginacao);
    }

    @Override
    public String buscaProximoNumeroSerie() {
        Integer maxAtual = repository.buscaPorNumeroSerieMaximo();
        int proximo = (maxAtual == null ? 1 : maxAtual + 1);
        return String.format("%05d", proximo);
    }

}
