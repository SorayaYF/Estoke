package com.estoke.inventario.entity;

import com.estoke.inventario.entity.enums.Disponibilidade;
import com.estoke.inventario.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;


@Table(name = "item_inventario")
@Entity(name = "ItemInventario")
public class ItemInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O código do item é obrigatório.")
    @Size(max = 7, message = "O código do item deve ter até {max} caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "O código do item deve conter apenas letras e números.")
    @Column(name = "codigoItem", nullable = false, unique = true, length = 7)
    private String codigoItem;

    @Column(name = "numeroSerie", nullable = false, length = 5)
    private String numeroSerie;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "A disponibilidade do item não deve ser nula")
    @Column(name = "disponibilidade", nullable = false)
    private Disponibilidade disponibilidade;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status do item não deve ser nulo")
    @Column(name = "status", nullable = false)
    private Status status;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 100, message = "A descrição deve ter até {max} caracteres.")
    @Column(name = "descricao", nullable = false, length = 100)
    private String descricao;

    @NotBlank(message = "A localização é obrigatória.")
    @Size(min = 3, max = 250, message = "A localização deve ter entre {min} e {max} caracteres.")
    @Column(name = "localizacao", nullable = false, length = 250)
    private String localizacao;

    @Column(name = "dataMovimentacao", updatable = false, nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataMovimentacao;

    public ItemInventario() {
        this.disponibilidade = Disponibilidade.D;
        this.status = Status.A;
        this.dataMovimentacao = LocalDateTime.now();
    }

    @JsonIgnore
    @Transient
    public boolean isPersistido() {
        return getId() != null && getId() > 0;
    }

    @JsonIgnore
    @Transient
    public boolean isDisponivel() {
        return getDisponibilidade() == Disponibilidade.D;
    }

    @JsonIgnore
    @Transient
    public boolean isAtivo() {
        return getStatus() == Status.A;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public Disponibilidade getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Disponibilidade disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemInventario that = (ItemInventario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
