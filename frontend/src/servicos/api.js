// todas as chamadas ao back-end passam por aqui
const URL_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const CHAVE = 'credenciais'

// o login usa http basic: guardamos "login:senha" em base64 so enquanto a aba esta aberta
export function guardarCredenciais(login, senha) {
  const bytes = new TextEncoder().encode(`${login}:${senha}`)
  const texto = btoa(String.fromCharCode(...bytes))
  sessionStorage.setItem(CHAVE, texto)
}

export function limparCredenciais() {
  sessionStorage.removeItem(CHAVE)
}

export function temCredenciais() {
  return sessionStorage.getItem(CHAVE) !== null
}

async function requisicao(caminho, metodo = 'GET', corpo) {
  const cabecalhos = { 'Content-Type': 'application/json' }
  const credenciais = sessionStorage.getItem(CHAVE)
  if (credenciais) {
    cabecalhos.Authorization = `Basic ${credenciais}`
  }

  const resposta = await fetch(`${URL_BASE}${caminho}`, {
    method: metodo,
    headers: cabecalhos,
    body: corpo ? JSON.stringify(corpo) : undefined,
  })

  if (resposta.status === 401) {
    throw new Error('usuário ou senha incorretos')
  }
  if (resposta.status === 204) {
    return null
  }

  const dados = await resposta.json().catch(() => null)
  if (!resposta.ok) {
    throw new Error(dados?.mensagem || 'erro ao falar com o servidor')
  }
  return dados
}

export const api = {
  eu: () => requisicao('/api/auth/eu'),

  cadastrarCliente: (dados) => requisicao('/api/clientes', 'POST', dados),
  listarClientes: () => requisicao('/api/clientes'),
  buscarCliente: (id) => requisicao(`/api/clientes/${id}`),
  atualizarCliente: (id, dados) => requisicao(`/api/clientes/${id}`, 'PUT', dados),
  excluirCliente: (id) => requisicao(`/api/clientes/${id}`, 'DELETE'),

  listarAutomoveis: () => requisicao('/api/automoveis'),
  criarAutomovel: (dados) => requisicao('/api/automoveis', 'POST', dados),

  listarPedidos: () => requisicao('/api/pedidos'),
  buscarPedido: (id) => requisicao(`/api/pedidos/${id}`),
  criarPedido: (dados) => requisicao('/api/pedidos', 'POST', dados),
  modificarPedido: (id, dados) => requisicao(`/api/pedidos/${id}`, 'PUT', dados),
  cancelarPedido: (id) => requisicao(`/api/pedidos/${id}/cancelamento`, 'PATCH'),
  avaliarPedido: (id, dados) => requisicao(`/api/pedidos/${id}/avaliacao`, 'PATCH', dados),
  contratarPedido: (id, dados) => requisicao(`/api/pedidos/${id}/contrato`, 'POST', dados),
}
