package br.pucminas.aluguel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.pucminas.aluguel.model.Cliente;
import br.pucminas.aluguel.model.Perfil;
import br.pucminas.aluguel.model.Rendimento;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

// testes simples, sem subir o spring
class ClienteTest {

    @Test
    void limiteDeRendimentosEhTres() {
        assertEquals(3, Cliente.MAXIMO_RENDIMENTOS);
    }

    @Test
    void rendimentoAdicionadoFicaLigadoAoCliente() {
        Cliente cliente = new Cliente();
        Rendimento rendimento = new Rendimento("Empresa X", new BigDecimal("3500.00"));
        cliente.adicionarRendimento(rendimento);

        assertEquals(1, cliente.getRendimentos().size());
        assertEquals(cliente, rendimento.getCliente());
    }

    @Test
    void empresaEBancoSaoAgentes() {
        assertTrue(Perfil.EMPRESA.ehAgente());
        assertTrue(Perfil.BANCO.ehAgente());
        assertEquals(false, Perfil.CLIENTE.ehAgente());
    }
}
