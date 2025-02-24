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

      <button
          type="submit"
          class="btn btn-success"
          :disabled="!formularioValido"
      >
        {{ item.id ? 'Salvar Alterações' : 'Adicionar Item' }}
      </button>
    </form>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { itemService } from '../services/api'

export default Vue.extend({
  name: 'ItemForm',
  props: {
    itemEdicao: {
      type: Object,
      default: () => ({
        id: null,
        codigoItem: '',
        numeroSerie: '',
        disponibilidade: 'D',
        status: 'A',
        descricao: '',
        localizacao: ''
      })
    }
  },
  data() {
    return {
      item: {
        ...this.itemEdicao,
        disponibilidade: this.itemEdicao.id ? this.itemEdicao.disponibilidade : 'D',
        status: this.itemEdicao.id ? this.itemEdicao.status : 'A'
      },
      erros: {
        codigoItem: '',
        descricao: '',
        localizacao: ''
      },
      serverErros: [] as Array<{ mensagem: string; codigo: string }>,
      dataMovimentacao: '',
      timer: 0
    }
  },
  computed: {
    formularioValido(): boolean {
      return Object.values(this.erros).every(msg => msg === '') &&
          this.item.codigoItem !== '' &&
          this.item.descricao !== '' &&
          this.item.localizacao !== ''
    }
  },
  watch: {
    itemEdicao(newVal) {
      this.item = {
        ...newVal,
        disponibilidade: newVal.id ? newVal.disponibilidade : 'D',
        status: newVal.id ? newVal.status : 'A'
      }
    }
  },
  mounted() {
    if (!this.item.id) {
      this.carregarProximoNumeroSerie()
      this.iniciarAtualizacaoData()
    } else if (this.item.dataMovimentacao) {
      this.dataMovimentacao = this.item.dataMovimentacao
    }
  },
  beforeDestroy() {
    clearInterval(this.timer)
  },
  methods: {
    async carregarProximoNumeroSerie() {
      try {
        const { data } = await itemService.proximoNumeroSerie()
        this.item.numeroSerie = data.numeroSerie
        this.$forceUpdate()
      } catch (error) {
        console.error('Erro ao obter próximo número de série:', error)
      }
    },
    iniciarAtualizacaoData() {
      this.atualizarDataMovimentacao()
      this.timer = setInterval(() => {
        this.atualizarDataMovimentacao()
      }, 1000)
    },
    atualizarDataMovimentacao() {
      const now = new Date()
      this.dataMovimentacao = this.formatDate(now)
    },
    formatDate(date: Date): string {
      const pad = (n: number) => n < 10 ? '0' + n : n
      const day = pad(date.getDate())
      const month = pad(date.getMonth() + 1)
      const year = date.getFullYear()
      const hours = pad(date.getHours())
      const minutes = pad(date.getMinutes())
      const seconds = pad(date.getSeconds())
      return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`
    },
    validarCodigoItem() {
      if (!this.item.codigoItem.trim()) {
        this.erros.codigoItem = 'O código do item é obrigatório.'
      } else if (this.item.codigoItem.length > 7) {
        this.erros.codigoItem = 'Máximo de 7 caracteres.'
      } else if (!/^[A-Za-z0-9]+$/.test(this.item.codigoItem)) {
        this.erros.codigoItem = 'Apenas letras e números são permitidos.'
      } else {
        this.erros.codigoItem = ''
      }
    },
    validarDescricao() {
      if (!this.item.descricao.trim()) {
        this.erros.descricao = 'A descrição é obrigatória.'
      } else if (this.item.descricao.length > 100) {
        this.erros.descricao = 'Máximo de 100 caracteres.'
      } else {
        this.erros.descricao = ''
      }
    },
    validarLocalizacao() {
      if (!this.item.localizacao.trim()) {
        this.erros.localizacao = 'A localização é obrigatória.'
      } else if (this.item.localizacao.length < 3 || this.item.localizacao.length > 250) {
        this.erros.localizacao = 'Deve ter entre 3 e 250 caracteres.'
      } else {
        this.erros.localizacao = ''
      }
    },
    async salvarItem() {
      this.validarCodigoItem()
      this.validarDescricao()
      this.validarLocalizacao()

      if (!this.formularioValido) return

      try {
        const payload = {
          ...this.item,
          ...(!this.item.id && {
            disponibilidade: 'D',
            status: 'A'
          }),
          dataMovimentacao: this.dataMovimentacao
        }

        if (!this.item.id) {
          await itemService.inserir(payload)
          alert('Item criado com sucesso!')
        } else {
          await itemService.alterar(payload)
          alert('Item atualizado com sucesso!')
        }

        this.$emit('salvouItem')
      } catch (error) {
        console.error('Erro ao salvar item:', error)
        this.serverErros = []
        const data = error.response?.data
        if (data?.erros && Array.isArray(data.erros)) {
          this.serverErros = data.erros
        } else {
          alert(data?.message || 'Erro ao salvar item.')
        }
      }
    }
  },
})
</script>

<style src="@/assets/styles/components/ItemForm.css"></style>
