package br.pucminas.aluguel.service;

import br.pucminas.aluguel.dto.AutomovelDto;
import br.pucminas.aluguel.excecao.RegraDeNegocioException;
import br.pucminas.aluguel.model.Automovel;
import br.pucminas.aluguel.repository.AutomovelRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutomovelService {

    private final AutomovelRepository automovelRepository;

    public AutomovelService(AutomovelRepository automovelRepository) {
        this.automovelRepository = automovelRepository;
    }

    @Transactional(readOnly = true)
    public List<AutomovelDto> listar() {
        return automovelRepository.findAll().stream().map(AutomovelService::paraDto).toList();
    }

    @Transactional
    public AutomovelDto criar(AutomovelDto dto) {
        String placa = dto.placa().trim().toUpperCase();
        if (automovelRepository.existsByPlaca(placa)) {
            throw new RegraDeNegocioException("ja existe um automovel com essa placa");
        }
        if (automovelRepository.existsByMatricula(dto.matricula().trim())) {
            throw new RegraDeNegocioException("ja existe um automovel com essa matricula");
        }
        Automovel automovel = new Automovel(dto.matricula().trim(), dto.ano(), dto.marca().trim(),
                dto.modelo().trim(), placa);
        return paraDto(automovelRepository.save(automovel));
    }

    public static AutomovelDto paraDto(Automovel a) {
        return new AutomovelDto(a.getId(), a.getMatricula(), a.getAno(), a.getMarca(), a.getModelo(), a.getPlaca());
    }
}
