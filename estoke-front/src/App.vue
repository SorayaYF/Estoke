<template>
  <div class="estoke-layout">
    <header class="app-header">
      <img src="@/assets/icons/logo.svg" alt="Estoke Logo" class="app-logo" />
    </header>

    <main class="app-main">
      <section class="app-form">
        <h2>{{ mostrarForm ? 'Editar ou Adicionar Item' : 'Adicionar Novo Item' }}</h2>
        <button class="btn btn-primary" @click="criarNovo">
          Novo Item
        </button>
        <ItemForm v-if="mostrarForm" :itemEdicao="itemParaEdicao" @salvouItem="quandoSalvar" />
      </section>

      <section class="app-list">
        <ItemList @editar="abrirEdicao" ref="listagemRef" />
      </section>
    </main>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import type { Item } from '@/modules/itens/item.interface'
import ItemList from '@/components/ItemList.vue'
import ItemForm from '@/components/ItemForm.vue'
import {Disponibilidade} from "@/modules/itens/enums/disponibilidade.enum";
import {Status} from "@/modules/itens/enums/status.enum";

const listagemRef = ref<InstanceType<typeof ItemList> | null>(null)

const mostrarForm = ref(false)
const itemParaEdicao = ref<Item>({} as Item)

const criarNovo = () => {
  itemParaEdicao.value = {
    id: null,
    codigoItem: '',
    numeroSerie: '',
    disponibilidade: Disponibilidade.DISPONIVEL,
    status: Status.ATIVO,
    descricao: '',
    localizacao: '',
    dataMovimentacao: ''
  }
  mostrarForm.value = false
  setTimeout(() => {
    mostrarForm.value = true
  }, 0)
}

const abrirEdicao = (item: Item) => {
  itemParaEdicao.value = { ...item }
  mostrarForm.value = true
}

const quandoSalvar = () => {
  if (listagemRef.value) {
    listagemRef.value.carregarItens()
  }
  mostrarForm.value = false
}
</script>

<style src="@/assets/styles/components/App.css"></style>
