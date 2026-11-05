import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../servicos/api'

// so agentes veem essa pagina
export default function ClientesLista() {
  const [clientes, setClientes] = useState([])
  const [erro, setErro] = useState('')

  function carregar() {
    api.listarClientes().then(setClientes).catch((e) => setErro(e.message))
  }

  useEffect(carregar, [])

  async function excluir(cliente) {
    if (!window.confirm(`Excluir o cliente ${cliente.nome} e todos os pedidos dele?`)) {
      return
    }
    try {
      await api.excluirCliente(cliente.id)
      carregar()
    } catch (e) {
      setErro(e.message)
    }
  }

  return (
    <main className="pagina">
      <h1>Clientes</h1>
      {erro && <p className="erro">{erro}</p>}
      {clientes.length === 0 ? (
        <p className="aviso">nenhum cliente cadastrado</p>
      ) : (
        <table>
          <thead>
            <tr><th>Nome</th><th>CPF</th><th>Profissão</th><th>Rendimentos</th><th></th></tr>
          </thead>
          <tbody>
            {clientes.map((c) => (
              <tr key={c.id}>
                <td>{c.nome}</td>
                <td>{c.cpf}</td>
                <td>{c.profissao}</td>
                <td>{c.rendimentos.length}</td>
                <td className="acoes">
                  <Link to={`/clientes/${c.id}`}>editar</Link>
                  <button onClick={() => excluir(c)}>excluir</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </main>
  )
}
