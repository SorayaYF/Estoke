import {Item} from "./item.interface";

export interface ListagemResponse {
    listagem: Item[]
    paginaAtual: number
    totalDePaginas: number
}
