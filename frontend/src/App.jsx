import { Navigate, Route, Routes } from 'react-router-dom'
import Menu from './componentes/Menu'
import RotaProtegida from './componentes/RotaProtegida'
import Automoveis from './paginas/Automoveis'
import Cadastro from './paginas/Cadastro'
import ClienteEdicao from './paginas/ClienteEdicao'
import ClientesLista from './paginas/ClientesLista'
import Login from './paginas/Login'
import PedidoFormulario from './paginas/PedidoFormulario'
import Pedidos from './paginas/Pedidos'

export default function App() {
  return (
    <>
      <Menu />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/cadastro" element={<Cadastro />} />

        <Route path="/pedidos" element={<RotaProtegida><Pedidos /></RotaProtegida>} />
        <Route path="/pedidos/novo" element={<RotaProtegida><PedidoFormulario /></RotaProtegida>} />
        <Route path="/pedidos/:id/editar" element={<RotaProtegida><PedidoFormulario /></RotaProtegida>} />
        <Route path="/automoveis" element={<RotaProtegida><Automoveis /></RotaProtegida>} />
        <Route path="/clientes" element={<RotaProtegida apenasAgente><ClientesLista /></RotaProtegida>} />
        <Route path="/clientes/:id" element={<RotaProtegida><ClienteEdicao /></RotaProtegida>} />

        <Route path="*" element={<Navigate to="/pedidos" replace />} />
      </Routes>
    </>
  )
}
