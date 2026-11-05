import { Navigate } from 'react-router-dom'
import { useAutenticacao } from '../contexto/AutenticacaoContexto'

// so deixa entrar quem fez login. se apenasAgente for true, so agentes entram
export default function RotaProtegida({ children, apenasAgente = false }) {
  const { usuario, carregando, ehAgente } = useAutenticacao()

  if (carregando) {
    return <p className="aviso">carregando...</p>
  }
  if (!usuario) {
    return <Navigate to="/login" replace />
  }
  if (apenasAgente && !ehAgente) {
    return <Navigate to="/pedidos" replace />
  }
  return children
}
