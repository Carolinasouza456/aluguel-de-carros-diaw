import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAutenticacao } from '../contexto/AutenticacaoContexto'

export default function Login() {
  const { entrar } = useAutenticacao()
  const navegar = useNavigate()
  const [login, setLogin] = useState('')
  const [senha, setSenha] = useState('')
  const [erro, setErro] = useState('')

  async function enviar(evento) {
    evento.preventDefault()
    setErro('')
    try {
      await entrar(login, senha)
      navegar('/pedidos')
    } catch (e) {
      setErro(e.message)
    }
  }

  return (
    <main className="pagina estreita">
      <h1>Entrar</h1>
      <form onSubmit={enviar} className="formulario">
        <label>Usuário
          <input value={login} onChange={(e) => setLogin(e.target.value)} required />
        </label>
        <label>Senha
          <input type="password" value={senha} onChange={(e) => setSenha(e.target.value)} required />
        </label>
        {erro && <p className="erro">{erro}</p>}
        <button type="submit">Entrar</button>
      </form>
      <p>Ainda não tem cadastro? <Link to="/cadastro">Cadastre-se</Link></p>
    </main>
  )
}
