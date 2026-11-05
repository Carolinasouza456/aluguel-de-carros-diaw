import { Link, useNavigate } from 'react-router-dom'
import FormularioCliente from '../componentes/FormularioCliente'
import { useAutenticacao } from '../contexto/AutenticacaoContexto'
import { api } from '../servicos/api'

export default function Cadastro() {
  const { entrar } = useAutenticacao()
  const navegar = useNavigate()

  // cadastra e ja entra no sistema
  async function cadastrar(dados) {
    await api.cadastrarCliente(dados)
    await entrar(dados.login, dados.senha)
    navegar('/pedidos')
  }

  return (
    <main className="pagina estreita">
      <h1>Cadastro de cliente</h1>
      <FormularioCliente modo="cadastro" aoEnviar={cadastrar} textoBotao="Cadastrar" />
      <p>Já tem cadastro? <Link to="/login">Entrar</Link></p>
    </main>
  )
}
