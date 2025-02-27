import {Disponibilidade} from "./enums/disponibilidade.enum";
import {Status} from "./enums/status.enum";

export interface ListarPorParams {
    codigoItem?: string
    disponibilidade?: Disponibilidade
    status?: Status
    pagina?: number
}
