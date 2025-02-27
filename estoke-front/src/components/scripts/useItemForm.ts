import {computed, onBeforeUnmount, onMounted, reactive, ref, watch} from 'vue'
import {Item} from "../../modules/itens/item.interface";
import {itemService} from "../../modules/itens/item.service";
import {Disponibilidade} from "../../modules/itens/enums/disponibilidade.enum";
import {Status} from "../../modules/itens/enums/status.enum";

export function useItemForm(
    props: { itemEdicao: Item },
    emit: (event: 'salvouItem') => void
) {
    const item = reactive<Item>({
        ...props.itemEdicao,
        disponibilidade: props.itemEdicao.id ? props.itemEdicao.disponibilidade : Disponibilidade.DISPONIVEL,
        status: props.itemEdicao.id ? props.itemEdicao.status : Status.ATIVO
    })

    const erros = reactive({
        codigoItem: '',
        descricao: '',
        localizacao: ''
    })

    const serverErros = ref<Array<{ mensagem: string; codigo: string }>>([])
    const dataMovimentacao = ref('')

    let timer: number | undefined

    const formularioValido = computed(() => {
        return (
            Object.values(erros).every(msg => msg === '') &&
            item.codigoItem.trim() !== '' &&
            item.descricao.trim() !== '' &&
            item.localizacao.trim() !== ''
        )
    })

    watch(
        () => props.itemEdicao,
        (newVal) => {
            Object.assign(item, {
                ...newVal,
                disponibilidade: newVal.id ? newVal.disponibilidade : Disponibilidade.DISPONIVEL,
                status: newVal.id ? newVal.status : Status.ATIVO
            })
        },
        { flush: 'post' }
    )

    onMounted(() => {
        if (!item.id) {
            carregarProximoNumeroSerie()
            iniciarAtualizacaoData()
        } else if (item.dataMovimentacao) {
            dataMovimentacao.value = item.dataMovimentacao
        }
    })

    onBeforeUnmount(() => {
        if (timer) clearInterval(timer)
    })

    const carregarProximoNumeroSerie = async () => {
        try {
            const { data } = await itemService.proximoNumeroSerie()
            item.numeroSerie = data.numeroSerie
        } catch (error) {
            console.error('Erro ao obter próximo número de série:', error)
        }
    }

    const iniciarAtualizacaoData = () => {
        atualizarDataMovimentacao()
        timer = window.setInterval(() => {
            atualizarDataMovimentacao()
        }, 1000)
    }

    const atualizarDataMovimentacao = () => {
        dataMovimentacao.value = formatDate(new Date())
    }

    const formatDate = (date: Date): string => {
        const pad = (n: number) => (n < 10 ? '0' + n : n.toString())
        const day = pad(date.getDate())
        const month = pad(date.getMonth() + 1)
        const year = date.getFullYear()
        const hours = pad(date.getHours())
        const minutes = pad(date.getMinutes())
        const seconds = pad(date.getSeconds())
        return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`
    }

    const validarCodigoItem = () => {
        if (!item.codigoItem.trim()) {
            erros.codigoItem = 'O código do item é obrigatório.'
        } else if (item.codigoItem.length > 7) {
            erros.codigoItem = 'Máximo de 7 caracteres.'
        } else if (!/^[A-Za-z0-9]+$/.test(item.codigoItem)) {
            erros.codigoItem = 'Apenas letras e números são permitidos.'
        } else {
            erros.codigoItem = ''
        }
    }

    const validarDescricao = () => {
        if (!item.descricao.trim()) {
            erros.descricao = 'A descrição é obrigatória.'
        } else if (item.descricao.length > 100) {
            erros.descricao = 'Máximo de 100 caracteres.'
        } else {
            erros.descricao = ''
        }
    }

    const validarLocalizacao = () => {
        if (!item.localizacao.trim()) {
            erros.localizacao = 'A localização é obrigatória.'
        } else if (item.localizacao.length < 3 || item.localizacao.length > 250) {
            erros.localizacao = 'Deve ter entre 3 e 250 caracteres.'
        } else {
            erros.localizacao = ''
        }
    }

    const salvarItem = async () => {
        // Executa as validações
        validarCodigoItem()
        validarDescricao()
        validarLocalizacao()

        if (!formularioValido.value) return

        try {
            const payload = {
                ...item,
                ...(!item.id && { disponibilidade: Disponibilidade.DISPONIVEL, status: Status.ATIVO }),
                dataMovimentacao: dataMovimentacao.value
            }

            if (!item.id) {
                await itemService.inserir(payload)
                alert('Item criado com sucesso!')
            } else {
                await itemService.alterar(payload)
                alert('Item atualizado com sucesso!')
            }
            emit('salvouItem')
        } catch (error: any) {
            console.error('Erro ao salvar item:', error)
            serverErros.value = []
            const data = error.response?.data
            if (data?.erros && Array.isArray(data.erros)) {
                serverErros.value = data.erros
            } else {
                alert(data?.message || 'Erro ao salvar item.')
            }
        }
    }

    return {
        item,
        erros,
        serverErros,
        dataMovimentacao,
        formularioValido,
        validarCodigoItem,
        validarDescricao,
        validarLocalizacao,
        salvarItem
    }
}
