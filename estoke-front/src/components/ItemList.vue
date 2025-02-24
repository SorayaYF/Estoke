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
            @input="aplicarFiltros"
        />
        <select v-model="filtros.disponibilidade" class="form-control mr-2" @change="aplicarFiltros">
          <option value="D">Disponível</option>
          <option value="I">Indisponível</option>
        </select>
        <select v-model="filtros.status" class="form-control mr-2" @change="aplicarFiltros">
          <option value="A">Ativo</option>
          <option value="I">Inativo</option>
        </select>
      </div>
    </div>

    <table class="table table-bordered mt-3">
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
          <button class="btn btn-sm btn-primary mr-2" @click="editarItem(item)">
            <img src="@/assets/icons/lapis.svg" alt="Editar" style="width:16px; height:16px;" />
          </button>
          <button class="btn btn-sm btn-danger" @click="excluirItem(item.id)">
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

<script lang="ts">
import Vue from 'vue'
import { itemService } from '../services/api'

export default Vue.extend({
  name: 'ItemList',
  data() {
    return {
      itens: [] as any[],
      filtros: {
        codigoItem: '',
        disponibilidade: 'D',
        status: 'A'
      },
      currentPage: 0,
      totalPages: 1
    }
  },
  methods: {
    async carregarItens() {
      try {
        const params: any = {
          pagina: this.currentPage,
          disponibilidade: this.filtros.disponibilidade,
          status: this.filtros.status
        }
        if (this.filtros.codigoItem.trim() !== '') {
          params.codigoItem = this.filtros.codigoItem.trim()
        }
        const response = await itemService.listarPor(params)
        this.itens = response.data.listagem
        this.currentPage = response.data.currentPage || 0
        this.totalPages = response.data.totalPages || 1
      } catch (error) {
        console.error('Erro ao carregar itens:', error)
      }
    },
    aplicarFiltros() {
      this.currentPage = 0
      this.carregarItens()
    },
    editarItem(item: any) {
      this.$emit('editar', item)
    },
    async excluirItem(id: number) {
      if (!confirm('Tem certeza que deseja excluir este item?')) {
        return
      }
      try {
        await itemService.excluirPor(id)
        alert('Item excluído (inativado) com sucesso!')
        this.carregarItens()
      } catch (error) {
        console.error('Erro ao excluir item:', error)
      }
    },
    paginaAnterior() {
      if (this.currentPage > 0) {
        this.currentPage--
        this.carregarItens()
      }
    },
    paginaProxima() {
      if (this.currentPage + 1 < this.totalPages) {
        this.currentPage++
        this.carregarItens()
      }
    }
  },
  mounted() {
    this.carregarItens()
  }
})
</script>

<style src="@/assets/styles/components/ItemList.css"></style>
