import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080'
})

export const itemService = {
    listarPor(params: {
        codigoItem?: string,
        disponibilidade?: string,
        status?: string,
        pagina?: number
    }) {
        return api.get('/itens-inventario', { params })
    },

    inserir(itemData: any) {
        return api.post('/itens-inventario', itemData)
    },

    alterar(itemData: any) {
        return api.put('/itens-inventario', itemData)
    },

    excluirPor(id: number) {
        return api.delete(`/itens-inventario/id/${id}`)
    },

    proximoNumeroSerie() {
        return api.get('/itens-inventario/proximo-numero-serie')
    }
}

export default api
