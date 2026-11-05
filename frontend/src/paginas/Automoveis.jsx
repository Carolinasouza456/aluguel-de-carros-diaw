import { useEffect, useState } from 'react'
import { useAutenticacao } from '../contexto/AutenticacaoContexto'
import { api } from '../servicos/api'

const VAZIO = { matricula: '', ano: '', marca: '', modelo: '', placa: '' }

export default function Automoveis() {
  const { ehAgente } = useAutenticacao()
  const [automoveis, setAutomoveis] = useState([])
  const [novo, setNovo] = useState(VAZIO)
  const [erro, setErro] = useState('')

  function carregar() {
    api.listarAutomoveis().then(setAutomoveis).catch((e) => setErro(e.message))
  }

  useEffect(carregar, [])

  function mudar(campo, valor) {
    setNovo({ ...novo, [campo]: valor })
  }

  async function cadastrar(evento) {
    evento.preventDefault()
    setErro('')
    try {
      await api.criarAutomovel({ ...novo, ano: Number(novo.ano) })
      setNovo(VAZIO)
      carregar()
    } catch (e) {
      setErro(e.message)
    }
  }

  return (
    <main className="pagina">
      <h1>Automóveis</h1>
      <table>
        <thead>
          <tr><th>Marca</th><th>Modelo</th><th>Ano</th><th>Placa</th><th>Matrícula</th></tr>
        </thead>
        <tbody>
          {automoveis.map((a) => (
            <tr key={a.id}>
              <td>{a.marca}</td><td>{a.modelo}</td><td>{a.ano}</td><td>{a.placa}</td><td>{a.matricula}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {ehAgente && (
        <>
          <h2>Cadastrar automóvel</h2>
          <form onSubmit={cadastrar} className="formulario">
            <label>Marca
              <input value={novo.marca} onChange={(e) => mudar('marca', e.target.value)} required />
            </label>
            <label>Modelo
              <input value={novo.modelo} onChange={(e) => mudar('modelo', e.target.value)} required />
            </label>
            <label>Ano
              <input type="number" value={novo.ano} onChange={(e) => mudar('ano', e.target.value)} required />
            </label>
            <label>Placa
              <input value={novo.placa} onChange={(e) => mudar('placa', e.target.value)} required />
            </label>
            <label>Matrícula
              <input value={novo.matricula} onChange={(e) => mudar('matricula', e.target.value)} required />
            </label>
            <button type="submit">Cadastrar</button>
          </form>
        </>
      )}
      {erro && <p className="erro">{erro}</p>}
    </main>
  )
}
