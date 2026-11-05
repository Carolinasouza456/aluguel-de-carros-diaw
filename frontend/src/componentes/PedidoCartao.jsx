import { useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../servicos/api'
import { useAutenticacao } from '../contexto/AutenticacaoContexto'

function formatarData(texto) {
  const [ano, mes, dia] = texto.split('-')
  return `${dia}/${mes}/${ano}`
}

function formatarDinheiro(valor) {
  return Number(valor).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

// mostra um pedido com o status e as acoes que cada perfil pode fazer
export default function PedidoCartao({ pedido, aoMudar }) {
  const { usuario, ehAgente } = useAutenticacao()
  const [painel, setPainel] = useState(null) // 'avaliar' ou 'contratar'
  const [parecer, setParecer] = useState('')
  const [tipoProprietario, setTipoProprietario] = useState('CLIENTE')
  const [comCredito, setComCredito] = useState(false)
  const [valorCredito, setValorCredito] = useState('')
  const [parcelas, setParcelas] = useState('')
  const [erro, setErro] = useState('')

  async function executar(acao) {
    setErro('')
    try {
      await acao()
      setPainel(null)
      aoMudar()
    } catch (e) {
      setErro(e.message)
    }
  }

  function cancelar() {
    if (window.confirm('Cancelar este pedido?')) {
      executar(() => api.cancelarPedido(pedido.id))
    }
  }

  function avaliar(aprovado) {
    executar(() => api.avaliarPedido(pedido.id, { aprovado, parecer }))
  }

  function contratar() {
    const credito = comCredito
      ? { valor: Number(valorCredito), parcelas: Number(parcelas) }
      : null
    executar(() => api.contratarPedido(pedido.id, { tipoProprietario, credito }))
  }

  const emAnalise = pedido.status === 'EM_ANALISE'
  const podeCancelar = !ehAgente && (emAnalise || pedido.status === 'APROVADO')

  return (
    <article className="cartao">
      <div className="cartao-topo">
        <strong>Pedido #{pedido.id}</strong>
        <span className={`status status-${pedido.status}`}>{pedido.status.replace('_', ' ')}</span>
      </div>

      <p>{pedido.automovel.marca} {pedido.automovel.modelo} {pedido.automovel.ano} - placa {pedido.automovel.placa}</p>
      <p>Período: {formatarData(pedido.dataInicio)} a {formatarData(pedido.dataFim)}</p>
      {ehAgente && (
        <p>
          Cliente: {pedido.clienteNome}
          {pedido.rendimentos.length > 0
            ? ' - rendimentos: ' + pedido.rendimentos.map((r) => `${r.entidadeEmpregadora} (${formatarDinheiro(r.valor)})`).join(', ')
            : ' - sem rendimentos informados'}
        </p>
      )}
      {pedido.parecer && <p>Parecer de {pedido.agenteAvaliador}: {pedido.parecer}</p>}
      {pedido.contrato && (
        <p>
          Contrato: automóvel de propriedade de {pedido.contrato.tipoProprietario.toLowerCase()}
          {pedido.contrato.credito &&
            `, crédito de ${formatarDinheiro(pedido.contrato.credito.valor)} em ${pedido.contrato.credito.parcelas}x pelo ${pedido.contrato.credito.banco}`}
        </p>
      )}

      <div className="acoes">
        {emAnalise && <Link to={`/pedidos/${pedido.id}/editar`}>modificar</Link>}
        {podeCancelar && <button onClick={cancelar}>cancelar pedido</button>}
        {ehAgente && emAnalise && <button onClick={() => setPainel('avaliar')}>avaliar</button>}
        {ehAgente && pedido.status === 'APROVADO' && <button onClick={() => setPainel('contratar')}>executar contrato</button>}
      </div>

      {painel === 'avaliar' && (
        <div className="painel">
          <label>Parecer
            <textarea value={parecer} onChange={(e) => setParecer(e.target.value)} maxLength={500} />
          </label>
          <button onClick={() => avaliar(true)}>parecer positivo</button>
          <button onClick={() => avaliar(false)}>parecer negativo</button>
        </div>
      )}

      {painel === 'contratar' && (
        <div className="painel">
          <label>Proprietário do automóvel
            <select value={tipoProprietario} onChange={(e) => setTipoProprietario(e.target.value)}>
              <option value="CLIENTE">Cliente</option>
              <option value="EMPRESA">Empresa</option>
              <option value="BANCO">Banco</option>
            </select>
          </label>
          {usuario.perfil === 'BANCO' && (
            <>
              <label className="marcar">
                <input type="checkbox" checked={comCredito} onChange={(e) => setComCredito(e.target.checked)} />
                associar contrato de crédito
              </label>
              {comCredito && (
                <div className="linha">
                  <input type="number" min="0.01" step="0.01" placeholder="valor do crédito"
                    value={valorCredito} onChange={(e) => setValorCredito(e.target.value)} />
                  <input type="number" min="1" placeholder="parcelas"
                    value={parcelas} onChange={(e) => setParcelas(e.target.value)} />
                </div>
              )}
            </>
          )}
          <button onClick={contratar}>confirmar contrato</button>
        </div>
      )}

      {erro && <p className="erro">{erro}</p>}
    </article>
  )
}
