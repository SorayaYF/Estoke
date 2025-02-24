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
        <ItemForm
            v-if="mostrarForm"
            :itemEdicao="itemParaEdicao"
            @salvouItem="quandoSalvar"
        />
      </section>

      <section class="app-list">
        <ItemList
            @editar="abrirEdicao"
            ref="listagem"
        />
      </section>
    </main>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import ItemList from './components/ItemList.vue'
import ItemForm from './components/ItemForm.vue'

export default Vue.extend({
  name: 'App',
  components: {
    ItemList,
    ItemForm
  },
  data() {
    return {
      mostrarForm: false,
      itemParaEdicao: {}
    }
  },
  methods: {
    criarNovo() {
      this.itemParaEdicao = {}
      this.mostrarForm = true
    },
    abrirEdicao(item: any) {
      this.itemParaEdicao = { ...item }
      this.mostrarForm = true
    },
    quandoSalvar() {
      (this.$refs.listagem as any).carregarItens()
      this.mostrarForm = false
    }
  }
})
</script>

<style src="@/assets/styles/components/App.css"></style>