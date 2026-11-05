import { createContext, useContext, useEffect, useState } from 'react'
import { api, guardarCredenciais, limparCredenciais, temCredenciais } from '../servicos/api'

const AutenticacaoContexto = createContext(null)

export function AutenticacaoProvider({ children }) {
  const [usuario, setUsuario] = useState(null)
  const [carregando, setCarregando] = useState(true)

  // se a aba ja tinha login, recupera os dados do usuario
  useEffect(() => {
    if (!temCredenciais()) {
      setCarregando(false)
      return
    }
    api.eu()
      .then(setUsuario)
      .catch(() => limparCredenciais())
      .finally(() => setCarregando(false))
  }, [])

  async function entrar(login, senha) {
    guardarCredenciais(login, senha)
    try {
      const dados = await api.eu()
      setUsuario(dados)
    } catch (erro) {
      limparCredenciais()
      throw erro
    }
  }

  function sair() {
    limparCredenciais()
    setUsuario(null)
  }

  const ehAgente = usuario?.perfil === 'EMPRESA' || usuario?.perfil === 'BANCO'

  return (
    <AutenticacaoContexto.Provider value={{ usuario, carregando, ehAgente, entrar, sair }}>
      {children}
    </AutenticacaoContexto.Provider>
  )
}

export function useAutenticacao() {
  return useContext(AutenticacaoContexto)
}
