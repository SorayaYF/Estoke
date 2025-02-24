package com.estoke.inventario.repository;

import com.estoke.inventario.entity.ItemInventario;
import com.estoke.inventario.entity.enums.Disponibilidade;
import com.estoke.inventario.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItensInventarioRepository extends JpaRepository<ItemInventario, Integer> {

    @Query(value =
            "SELECT i "
                    + "FROM ItemInventario i "
                    + "WHERE i.disponibilidade = :disponibilidade "
                    + "AND i.status = :status "
                    + "ORDER BY i.codigoItem ",
            countQuery =
                    "SELECT Count(i) "
                            + "FROM ItemInventario i "
                            + "WHERE i.disponibilidade = :disponibilidade "
                            + "AND i.status = :status ")
    public Page<ItemInventario> listarPor(Disponibilidade disponibilidade,
                                          Status status, Pageable paginacao);

    @Query(value =
            "SELECT i "
                    + "FROM ItemInventario i "
                    + "WHERE Upper(i.codigoItem) LIKE Upper(:codigoItem) "
                    + "AND i.disponibilidade = :disponibilidade "
                    + "AND i.status = :status "
                    + "ORDER BY i.codigoItem ",
            countQuery =
                    "SELECT Count(i) "
                            + "FROM ItemInventario i "
                            + "WHERE Upper(i.codigoItem) LIKE Upper(:codigoItem) "
                            + "AND i.disponibilidade = :disponibilidade "
                            + "AND i.status = :status ")
    public Page<ItemInventario> listarPor(String codigoItem, Disponibilidade disponibilidade,
                                          Status status, Pageable paginacao);

    @Query(value =
            "SELECT i "
                    + "FROM ItemInventario i "
                    + "WHERE Upper(i.codigoItem) = Upper(:codigoItem) ")
    public ItemInventario buscarPor(String codigoItem);

    @Query(value =
            "SELECT i "
                    + "FROM ItemInventario i "
                    + "WHERE i.id = :id ")
    public ItemInventario buscarPor(Integer id);

    @Modifying
    @Query(value = "UPDATE ItemInventario i SET i.status = 'I' WHERE i.id = :id")
    public void inativar(Integer id);

    @Query("SELECT MAX(CAST(i.numeroSerie AS integer)) FROM ItemInventario i")
    public Integer buscaPorNumeroSerieMaximo();
}
