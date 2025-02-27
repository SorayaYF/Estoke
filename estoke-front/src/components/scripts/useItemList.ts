import { ref, reactive, onMounted } from 'vue'
import {Item} from "../../modules/itens/item.interface";
import {itemService} from "../../modules/itens/item.service";
import {Disponibilidade} from "../../modules/itens/enums/disponibilidade.enum";
import {Status} from "../../modules/itens/enums/status.enum";

export function useItemList(emit: (e: 'editar', item: Item) => void) {
    const itens = ref<Item[]>([])
    const loading = ref(false)
    const filtros = reactive({
        codigoItem: '',
        disponibilidade: Disponibilidade.DISPONIVEL,
        status: Status.ATIVO
    })
    const currentPage = ref(0)
    const totalPages = ref(1)

    let debounceTimer: number | null = null

    const carregarItens = async () => {
        loading.value = true
        try {
            const params: Record<string, any> = {
                pagina: currentPage.value,
                disponibilidade: filtros.disponibilidade,
                status: filtros.status
            }
            if (filtros.codigoItem.trim() !== '') {
                params.codigoItem = filtros.codigoItem.trim()
            }
            const response = await itemService.listarPor(params)
            itens.value = response.data.listagem
            currentPage.value = response.data.paginaAtual ?? 0
            totalPages.value = response.data.totalDePaginas ?? 1
        } catch (error) {
            console.error('Erro ao carregar itens:', error)
        } finally {
            loading.value = false
        }
    }

    const onFilterInput = () => {
        if (debounceTimer) clearTimeout(debounceTimer)
        debounceTimer = window.setTimeout(() => {
            aplicarFiltros()
        }, 300)
    }

    const aplicarFiltros = () => {
        currentPage.value = 0
        carregarItens()
    }

    const handleExclude = async (id: number) => {
        if (!confirm('Tem certeza que deseja excluir este item?')) return
        try {
            await itemService.excluirPor(id)
            alert('Item excluído (inativado) com sucesso!')
            carregarItens()
        } catch (error) {
            console.error('Erro ao excluir item:', error)
        }
    }

    const handleEdit = (item: Item) => {
        emit('editar', item)
    }

    const paginaAnterior = () => {
        if (currentPage.value > 0) {
            currentPage.value--
            carregarItens()
        }
    }

    const paginaProxima = () => {
        if (currentPage.value + 1 < totalPages.value) {
            currentPage.value++
            carregarItens()
        }
    }

    onMounted(() => {
        carregarItens()
    })

    return {
        itens,
        loading,
        filtros,
        currentPage,
        totalPages,
        carregarItens,
        onFilterInput,
        aplicarFiltros,
        handleExclude,
        handleEdit,
        paginaAnterior,
        paginaProxima
    }
}
