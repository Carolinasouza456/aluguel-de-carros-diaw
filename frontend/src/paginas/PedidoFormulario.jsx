import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { api } from '../servicos/api'

// serve para criar (/pedidos/novo) e para modificar (/pedidos/:id/editar)
export default function PedidoFormulario() {
  const { id } = useParams()
  const navegar = useNavigate()
  const [automoveis, setAutomoveis] = useState([])
  const [automovelId, setAutomovelId] = useState('')
  const [dataInicio, setDataInicio] = useState('')
  const [dataFim, setDataFim] = useState('')
  const [erro, setErro] = useState('')

  useEffect(() => {
    api.listarAutomoveis().then(setAutomoveis).catch((e) => setErro(e.message))
    if (id) {
      api.buscarPedido(id)
        .then((p) => {
          setAutomovelId(String(p.automovel.id))
          setDataInicio(p.dataInicio)
          setDataFim(p.dataFim)
        })
        .catch((e) => setErro(e.message))
    }
  }, [id])

  async function enviar(evento) {
    evento.preventDefault()
    setErro('')
    const dados = { automovelId: Number(automovelId), dataInicio, dataFim }
    try {
      if (id) {
        await api.modificarPedido(id, dados)
      } else {
        await api.criarPedido(dados)
      }
      navegar('/pedidos')
    } catch (e) {
      setErro(e.message)
    }
  }

  return (
    <main className="pagina estreita">
      <h1>{id ? `Modificar pedido #${id}` : 'Novo pedido de aluguel'}</h1>
      <form onSubmit={enviar} className="formulario">
        <label>Automóvel
          <select value={automovelId} onChange={(e) => setAutomovelId(e.target.value)} required>
            <option value="">escolha um automóvel</option>
            {automoveis.map((a) => (
              <option key={a.id} value={a.id}>{a.marca} {a.modelo} {a.ano} - {a.placa}</option>
            ))}
          </select>
        </label>
        <label>Data de início
          <input type="date" value={dataInicio} onChange={(e) => setDataInicio(e.target.value)} required />
        </label>
        <label>Data final
          <input type="date" value={dataFim} onChange={(e) => setDataFim(e.target.value)} required />
        </label>
        {erro && <p className="erro">{erro}</p>}
        <button type="submit">{id ? 'Salvar alterações' : 'Enviar pedido'}</button>
      </form>
    </main>
  )
}
