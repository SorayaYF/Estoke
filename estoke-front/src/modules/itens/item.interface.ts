import {Disponibilidade} from "./enums/disponibilidade.enum";
import {Status} from "./enums/status.enum";

export interface Item {
    id: number
    codigoItem: string
    numeroSerie: string
    disponibilidade: Disponibilidade
    status: Status
    descricao: string
    localizacao: string
    dataMovimentacao: string
}

