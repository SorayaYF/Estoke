<template>
  <div class="container mt-4">
    <h2>Listagem de Itens</h2>

    <div class="filters mb-3">
      <div class="form-inline">
        <input
            v-model="filtros.codigoItem"
            type="text"
            placeholder="Pesquisar por Código"
            class="form-control mr-2"
            @input="onFilterInput"
        />
        <select
            v-model="filtros.disponibilidade"
            class="form-control mr-2"
            @change="aplicarFiltros"
        >
          <option :value="Disponibilidade.DISPONIVEL">Disponível</option>
          <option :value="Disponibilidade.INDISPONIVEL">Indisponível</option>
        </select>

        <select
            v-model="filtros.status"
            class="form-control mr-2"
            @change="aplicarFiltros"
        >
          <option :value="Status.ATIVO">Ativo</option>
          <option :value="Status.INATIVO">Inativo</option>
        </select>
      </div>
    </div>

    <div v-if="loading" class="text-center my-3">
      <span>Carregando itens...</span>
    </div>

    <table v-else class="table table-bordered mt-3">
      <thead>
      <tr>
        <th>Código</th>
        <th>Número de Série</th>
        <th>Disponibilidade</th>
        <th>Status</th>
        <th>Descrição</th>
        <th>Localização</th>
        <th>Data de Movimentação</th>
        <th>Ações</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in itens" :key="item.id">
        <td>{{ item.codigoItem }}</td>
        <td>{{ item.numeroSerie }}</td>
        <td>{{ item.disponibilidade }}</td>
        <td>{{ item.status }}</td>
        <td>{{ item.descricao }}</td>
        <td>{{ item.localizacao }}</td>
        <td>{{ item.dataMovimentacao }}</td>
        <td>
          <button class="btn btn-sm btn-primary mr-2" @click="handleEdit(item)">
            <img src="@/assets/icons/lapis.svg" alt="Editar" style="width:16px; height:16px;" />
          </button>
          <button class="btn btn-sm btn-danger" @click="handleExclude(item.id)">
            <img src="@/assets/icons/lixeira.svg" alt="Excluir" style="width:16px; height:16px;" />
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <div class="pagination mt-3 d-flex align-items-center">
      <button class="btn btn-secondary mr-2" @click="paginaAnterior" :disabled="currentPage === 0">
        Anterior
      </button>
      <span>Página {{ currentPage + 1 }} de {{ totalPages }}</span>
      <button class="btn btn-secondary ml-2" @click="paginaProxima" :disabled="currentPage + 1 >= totalPages">
        Próxima
      </button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {defineEmits, onMounted} from 'vue'
import {Item} from "@/modules/itens/item.interface";
import {useItemList} from "@/components/scripts/useItemList";
import {Disponibilidade} from "@/modules/itens/enums/disponibilidade.enum";
import {Status} from "@/modules/itens/enums/status.enum";

const emit = defineEmits<{
  (e: 'editar', item: Item): void
}>()

const {
  itens,
  loading,
  filtros,
  currentPage,
  totalPages,
  carregarItens,
  onFilterInput,
  aplicarFiltros,
  handleExclude,
  handleEdit,
  paginaAnterior,
  paginaProxima
} = useItemList(emit)

defineExpose({ carregarItens })

onMounted(() => carregarItens())
</script>

<style src="@/assets/styles/components/ItemList.css"></style>
