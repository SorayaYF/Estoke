<template>
  <div class="container mt-4">
    <h2>{{ item.id ? 'Editar Item' : 'Novo Item' }}</h2>

    <div v-if="serverErros.length" class="alert alert-danger">
      <ul>
        <li v-for="(erro, index) in serverErros" :key="index">
          {{ erro.mensagem }} (Código: {{ erro.codigo }})
        </li>
      </ul>
    </div>

    <form @submit.prevent="salvarItem">
      <div class="form-group">
        <label for="codigoItem">Código do Item</label>
        <input
            id="codigoItem"
            type="text"
            class="form-control"
            :class="{ 'is-invalid': erros.codigoItem }"
            v-model="item.codigoItem"
            @input="validarCodigoItem"
            required
        />
        <small v-if="erros.codigoItem" class="text-danger">{{ erros.codigoItem }}</small>
      </div>

      <div class="form-group">
        <label for="numeroSerie">Número de Série</label>
        <input
            id="numeroSerie"
            type="text"
            class="form-control"
            v-model="item.numeroSerie"
            disabled
        />
      </div>

      <div class="form-group">
        <label for="dataMovimentacao">Data de Movimentação</label>
        <input
            id="dataMovimentacao"
            type="text"
            class="form-control"
            v-model="dataMovimentacao"
            disabled
        />
      </div>

      <div class="form-group">
        <label for="disponibilidade">Disponibilidade</label>
        <select
            id="disponibilidade"
            class="form-control"
            v-model="item.disponibilidade"
            :disabled="!item.id"
            required
        >
          <option value="D">Disponível</option>
          <option value="I">Indisponível</option>
        </select>
      </div>

      <div class="form-group">
        <label for="status">Status</label>
        <select
            id="status"
            class="form-control"
            v-model="item.status"
            :disabled="!item.id"
            required
        >
          <option value="A">Ativo</option>
          <option value="I">Inativo</option>
        </select>
      </div>

      <div class="form-group">
        <label for="descricao">Descrição</label>
        <input
            id="descricao"
            type="text"
            class="form-control"
            :class="{ 'is-invalid': erros.descricao }"
            v-model="item.descricao"
            @input="validarDescricao"
            required
        />
        <small v-if="erros.descricao" class="text-danger">{{ erros.descricao }}</small>
      </div>

      <div class="form-group">
        <label for="localizacao">Localização</label>
        <input
            id="localizacao"
            type="text"
            class="form-control"
            :class="{ 'is-invalid': erros.localizacao }"
            v-model="item.localizacao"
            @input="validarLocalizacao"
            required
        />
        <small v-if="erros.localizacao" class="text-danger">{{ erros.localizacao }}</small>
      </div>

      <button type="submit" class="btn btn-success" :disabled="!formularioValido">
        {{ item.id ? 'Salvar Alterações' : 'Adicionar Item' }}
      </button>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { defineProps, defineEmits } from 'vue'
import {Item} from "@/modules/itens/item.interface";
import {useItemForm} from "@/components/scripts/useItemForm";

const props = defineProps<{
  itemEdicao: Item
}>()

const emit = defineEmits<{ (e: 'salvouItem'): void }>()

const {
  item,
  erros,
  serverErros,
  dataMovimentacao,
  formularioValido,
  validarCodigoItem,
  validarDescricao,
  validarLocalizacao,
  salvarItem
} = useItemForm(props, emit)
</script>

<style src="@/assets/styles/components/ItemForm.css"></style>
