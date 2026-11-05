import { useState } from 'react'

const MAXIMO_RENDIMENTOS = 3

// formulario usado no cadastro (modo "cadastro") e na edicao (modo "edicao")
export default function FormularioCliente({ modo, inicial, aoEnviar, textoBotao }) {
  const cadastro = modo === 'cadastro'
  const [dados, setDados] = useState({
    login: inicial?.login || '',
    senha: '',
    rg: inicial?.rg || '',
    cpf: inicial?.cpf || '',
    nome: inicial?.nome || '',
    endereco: inicial?.endereco || '',
    profissao: inicial?.profissao || '',
  })
  const [rendimentos, setRendimentos] = useState(inicial?.rendimentos || [])
  const [erro, setErro] = useState('')
  const [enviando, setEnviando] = useState(false)

  function mudar(campo, valor) {
    setDados({ ...dados, [campo]: valor })
  }

  function adicionarRendimento() {
    if (rendimentos.length < MAXIMO_RENDIMENTOS) {
      setRendimentos([...rendimentos, { entidadeEmpregadora: '', valor: '' }])
    }
  }

  function mudarRendimento(posicao, campo, valor) {
    setRendimentos(rendimentos.map((r, i) => (i === posicao ? { ...r, [campo]: valor } : r)))
  }

  function removerRendimento(posicao) {
    setRendimentos(rendimentos.filter((_, i) => i !== posicao))
  }

  async function enviar(evento) {
    evento.preventDefault()
    setErro('')
    setEnviando(true)
    try {
      await aoEnviar({
        ...dados,
        rendimentos: rendimentos.map((r) => ({
          entidadeEmpregadora: r.entidadeEmpregadora,
          valor: Number(r.valor),
        })),
      })
    } catch (e) {
      setErro(e.message)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <form onSubmit={enviar} className="formulario">
      {cadastro && (
        <>
          <label>Usuário
            <input value={dados.login} onChange={(e) => mudar('login', e.target.value)} required />
          </label>
          <label>Senha (mínimo 6 caracteres)
            <input type="password" value={dados.senha} onChange={(e) => mudar('senha', e.target.value)} required />
          </label>
        </>
      )}
      {!cadastro && (
        <label>Nova senha (deixe em branco para manter a atual)
          <input type="password" value={dados.senha} onChange={(e) => mudar('senha', e.target.value)} />
        </label>
      )}

      <label>Nome
        <input value={dados.nome} onChange={(e) => mudar('nome', e.target.value)} required />
      </label>
      <label>RG
        <input value={dados.rg} onChange={(e) => mudar('rg', e.target.value)} required />
      </label>
      <label>CPF
        <input value={dados.cpf} onChange={(e) => mudar('cpf', e.target.value)} required />
      </label>
      <label>Endereço
        <input value={dados.endereco} onChange={(e) => mudar('endereco', e.target.value)} required />
      </label>
      <label>Profissão
        <input value={dados.profissao} onChange={(e) => mudar('profissao', e.target.value)} />
      </label>

      <fieldset>
        <legend>Rendimentos (máximo {MAXIMO_RENDIMENTOS})</legend>
        {rendimentos.map((r, i) => (
          <div className="linha" key={i}>
            <input
              placeholder="entidade empregadora"
              value={r.entidadeEmpregadora}
              onChange={(e) => mudarRendimento(i, 'entidadeEmpregadora', e.target.value)}
              required
            />
            <input
              type="number"
              min="0"
              step="0.01"
              placeholder="valor mensal"
              value={r.valor}
              onChange={(e) => mudarRendimento(i, 'valor', e.target.value)}
              required
            />
            <button type="button" onClick={() => removerRendimento(i)}>remover</button>
          </div>
        ))}
        <button type="button" onClick={adicionarRendimento} disabled={rendimentos.length >= MAXIMO_RENDIMENTOS}>
          adicionar rendimento
        </button>
      </fieldset>

      {erro && <p className="erro">{erro}</p>}
      <button type="submit" disabled={enviando}>{textoBotao}</button>
    </form>
  )
}
