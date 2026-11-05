package br.pucminas.aluguel.config;

import br.pucminas.aluguel.model.Agente;
import br.pucminas.aluguel.model.Automovel;
import br.pucminas.aluguel.model.Perfil;
import br.pucminas.aluguel.model.Usuario;
import br.pucminas.aluguel.repository.AgenteRepository;
import br.pucminas.aluguel.repository.AutomovelRepository;
import br.pucminas.aluguel.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// so para desenvolvimento: cria agentes e automoveis de exemplo se o banco estiver vazio.
// agentes nao se cadastram pela tela (o cadastro previo deles e feito aqui).
// em producao, desligue com DADOS_INICIAIS=false e troque as senhas
@Component
@ConditionalOnProperty(name = "app.dados-iniciais", havingValue = "true")
public class DadosIniciais implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AgenteRepository agenteRepository;
    private final AutomovelRepository automovelRepository;
    private final PasswordEncoder passwordEncoder;

    public DadosIniciais(UsuarioRepository usuarioRepository,
                         AgenteRepository agenteRepository,
                         AutomovelRepository automovelRepository,
                         PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.agenteRepository = agenteRepository;
        this.automovelRepository = automovelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByLogin("empresa")) {
            Usuario usuario = new Usuario("empresa", passwordEncoder.encode("empresa123"), Perfil.EMPRESA);
            agenteRepository.save(new Agente("Locadora Central", usuario));
        }
        if (!usuarioRepository.existsByLogin("banco")) {
            Usuario usuario = new Usuario("banco", passwordEncoder.encode("banco123"), Perfil.BANCO);
            agenteRepository.save(new Agente("Banco Minas", usuario));
        }
        if (automovelRepository.count() == 0) {
            automovelRepository.save(new Automovel("MAT-0001", 2023, "Fiat", "Argo", "ABC1D23"));
            automovelRepository.save(new Automovel("MAT-0002", 2022, "Volkswagen", "Polo", "BRA2E19"));
            automovelRepository.save(new Automovel("MAT-0003", 2024, "Chevrolet", "Onix", "QWE3F45"));
            automovelRepository.save(new Automovel("MAT-0004", 2021, "Toyota", "Corolla", "RTY4G67"));
            automovelRepository.save(new Automovel("MAT-0005", 2023, "Hyundai", "HB20", "UIO5H89"));
        }
    }
}
