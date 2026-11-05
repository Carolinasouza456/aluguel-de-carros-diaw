import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import { AutenticacaoProvider } from './contexto/AutenticacaoContexto'
import './estilos.css'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <AutenticacaoProvider>
        <App />
      </AutenticacaoProvider>
    </BrowserRouter>
  </StrictMode>,
)
