import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import PedidoCartao from '../componentes/PedidoCartao'
import { useAutenticacao } from '../contexto/AutenticacaoContexto'
import { api } from '../servicos/api'

export default function Pedidos() {
  const { ehAgente } = useAutenticacao()
  const [pedidos, setPedidos] = useState([])
  const [erro, setErro] = useState('')

  function carregar() {
    api.listarPedidos().then(setPedidos).catch((e) => setErro(e.message))
  }

  useEffect(carregar, [])

  return (
    <main className="pagina">
      <h1>{ehAgente ? 'Todos os pedidos' : 'Meus pedidos'}</h1>
      {!ehAgente && <p><Link to="/pedidos/novo">Fazer um novo pedido</Link></p>}
      {erro && <p className="erro">{erro}</p>}
      {pedidos.length === 0 && !erro && <p className="aviso">nenhum pedido por enquanto</p>}
      {pedidos.map((p) => (
        <PedidoCartao key={p.id} pedido={p} aoMudar={carregar} />
      ))}
    </main>
  )
}
