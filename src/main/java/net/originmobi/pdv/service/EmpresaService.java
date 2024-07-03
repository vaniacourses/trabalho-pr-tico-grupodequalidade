package net.originmobi.pdv.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.originmobi.pdv.model.Cidade;
import net.originmobi.pdv.model.Empresa;
import net.originmobi.pdv.model.EmpresaParametro;
import net.originmobi.pdv.model.Endereco;
import net.originmobi.pdv.model.RegimeTributario;
import net.originmobi.pdv.repository.EmpresaParametrosRepository;
import net.originmobi.pdv.repository.EmpresaRepository;

@Service
public class EmpresaService {

	private final EmpresaRepository empresas;
	private final EmpresaParametrosRepository parametros;
	private final RegimeTributarioService regimes;
	private final CidadeService cidades;
	private final EnderecoService enderecos;

	// Injeção via construtor para gerenciar dependências
	public EmpresaService(EmpresaRepository empresas,
						  EmpresaParametrosRepository parametros,
						  RegimeTributarioService regimes,
						  CidadeService cidades,
						  EnderecoService enderecos) {
		this.empresas = empresas;
		this.parametros = parametros;
		this.regimes = regimes;
		this.cidades = cidades;
		this.enderecos = enderecos;
	}
	public void cadastro(Empresa empresa) throws RuntimeException {

		try {
			empresas.save(empresa);
		} catch (Exception e) {
			e.getStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Optional<Empresa> verificaEmpresaCadastrada() {

		return empresas.buscaEmpresaCadastrada();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String merger(Long codigo, String nome, String nomeFantasia, String cnpj, String ie, int serie,
						 int ambiente, Long codRegime, Long codendereco, Long codcidade, String rua, String bairro, String numero,
						 String cep, String referencia, Double aliqCalcCredito) {

		if (codigo != null) {
			try {
				empresas.update(codigo, nome, nomeFantasia, cnpj, ie, codRegime);
			} catch (Exception e) {
				throw new IllegalStateException("Erro ao salvar dados iniciais da empresa. Chame o suporte");
			}

			try {
				parametros.update(serie, ambiente, aliqCalcCredito);
			} catch (Exception e) {
				throw new IllegalStateException ("Erro ao salvar dados da empresa, chame o suporte");
			}

			try {
				enderecos.update(codendereco, codcidade, rua, bairro, numero, cep, referencia);
			} catch (Exception e) {
				throw new IllegalStateException ("Erro ao salvar dados de localização da empresa. Chame o suporte");
			}
		} else {
			EmpresaParametro parametro = new EmpresaParametro();

			try {
				parametro.setAmbiente(ambiente);
				parametro.setSerie_nfe(serie);
				parametro.setpCredSN(aliqCalcCredito);
				parametros.save(parametro);
			} catch (Exception e) {
				throw new DataIntegrityViolationException( "Erro ao salvar dados da empresa, chame o suporte"+parametro);
			}

			Optional<RegimeTributario> tributario = regimes.busca(codRegime);
			Optional<Cidade> cidade = cidades.busca(codcidade);

			LocalDate dataAtual = LocalDate.now();
			Endereco endereco = new Endereco(rua, bairro, numero, cep, referencia, Date.valueOf(dataAtual),
					cidade.get());

			try {
				enderecos.cadastrar(endereco);
			} catch (Exception e) {
				throw new DataIntegrityViolationException ("Erro ao cadastrar endereço da empresa. Chame o suporte");
			}

			try {
				Empresa empresa = new Empresa(nome, nomeFantasia, cnpj, ie, tributario.get(), endereco, parametro);
				empresas.save(empresa);
			} catch (Exception e) {
				throw new DataIntegrityViolationException ("Erro ao salvar novos dados da empresa. Chame o suporte");
			}
		}

		return "Empresa salva com sucesso";
	}

}
