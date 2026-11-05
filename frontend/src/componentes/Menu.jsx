import { Link, useNavigate } from 'react-router-dom'
import { useAutenticacao } from '../contexto/AutenticacaoContexto'

export default function Menu() {
  const { usuario, ehAgente, sair } = useAutenticacao()
  const navegar = useNavigate()

  function aoSair() {
    sair()
    navegar('/login')
  }

  return (
    <header className="menu">
      <strong>Aluguel de Carros</strong>
      {usuario && (
        <nav>
          <Link to="/pedidos">Pedidos</Link>
          {!ehAgente && <Link to="/pedidos/novo">Novo pedido</Link>}
          <Link to="/automoveis">Automóveis</Link>
          {ehAgente ? (
            <Link to="/clientes">Clientes</Link>
          ) : (
            <Link to={`/clientes/${usuario.clienteId}`}>Meu cadastro</Link>
          )}
          <span className="usuario">{usuario.nome} ({usuario.perfil.toLowerCase()})</span>
          <button onClick={aoSair}>Sair</button>
        </nav>
      )}
    </header>
  )
}
