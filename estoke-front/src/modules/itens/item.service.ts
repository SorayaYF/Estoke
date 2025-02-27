import {ListarPorParams} from "./listar-por-params.interface";
import {ListagemResponse} from "./listagem-response.interface";
import {Item} from "./item.interface";
import {ProximoNumeroSerieResponse} from "./proximo-numero-serie-response.interface";
import apiConnection from "../../api/apiConnection";

export const itemService = {
    listarPor(params: ListarPorParams) {
        return apiConnection.get<ListagemResponse>('/itens-inventario', { params })
    },

    inserir(itemData: Partial<Item>) {
        return apiConnection.post('/itens-inventario', itemData)
    },

    alterar(itemData: Partial<Item>) {
        return apiConnection.put('/itens-inventario', itemData)
    },

    excluirPor(id: number) {
        return apiConnection.delete(`/itens-inventario/id/${id}`)
    },

    proximoNumeroSerie() {
        return apiConnection.get<ProximoNumeroSerieResponse>('/itens-inventario/proximo-numero-serie')
    }
}
