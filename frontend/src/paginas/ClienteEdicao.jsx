import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import FormularioCliente from '../componentes/FormularioCliente'
import { useAutenticacao } from '../contexto/AutenticacaoContexto'
import { api } from '../servicos/api'

// o cliente edita o proprio cadastro, o agente edita qualquer um
export default function ClienteEdicao() {
  const { id } = useParams()
  const { ehAgente, sair } = useAutenticacao()
  const navegar = useNavigate()
  const [cliente, setCliente] = useState(null)
  const [erro, setErro] = useState('')
  const [mensagem, setMensagem] = useState('')

  useEffect(() => {
    api.buscarCliente(id).then(setCliente).catch((e) => setErro(e.message))
  }, [id])

  async function salvar(dados) {
    setMensagem('')
    const atualizado = await api.atualizarCliente(id, dados)
    setCliente(atualizado)
    setMensagem('dados salvos')
  }

  async function excluir() {
    if (!window.confirm('Excluir este cadastro e todos os pedidos? Não dá para desfazer.')) {
      return
    }
    try {
      await api.excluirCliente(id)
      if (ehAgente) {
        navegar('/clientes')
      } else {
        sair()
        navegar('/login')
      }
    } catch (e) {
      setErro(e.message)
    }
  }

  if (erro && !cliente) {
    return <main className="pagina"><p className="erro">{erro}</p></main>
  }
  if (!cliente) {
    return <main className="pagina"><p className="aviso">carregando...</p></main>
  }

  return (
    <main className="pagina estreita">
      <h1>{ehAgente ? `Cliente: ${cliente.nome}` : 'Meu cadastro'}</h1>
      <p>Usuário: {cliente.login}</p>
      {/* a key faz o formulario recarregar quando o cliente muda */}
      <FormularioCliente key={cliente.id} modo="edicao" inicial={cliente} aoEnviar={salvar} textoBotao="Salvar" />
      {mensagem && <p className="sucesso">{mensagem}</p>}
      {erro && <p className="erro">{erro}</p>}
      <button className="perigo" onClick={excluir}>Excluir cadastro</button>
    </main>
  )
}
