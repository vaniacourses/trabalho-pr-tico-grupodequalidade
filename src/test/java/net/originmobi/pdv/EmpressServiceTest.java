package net.originmobi.pdv;

import net.originmobi.pdv.model.Cidade;
import net.originmobi.pdv.model.Empresa;
import net.originmobi.pdv.model.EmpresaParametro;
import net.originmobi.pdv.model.Endereco;
import net.originmobi.pdv.model.RegimeTributario;
import net.originmobi.pdv.repository.EmpresaParametrosRepository;
import net.originmobi.pdv.repository.EmpresaRepository;
import net.originmobi.pdv.service.CidadeService;
import net.originmobi.pdv.service.EmpresaService;
import net.originmobi.pdv.service.EnderecoService;
import net.originmobi.pdv.service.RegimeTributarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpressServiceTest {

    @Mock
    EmpresaRepository empresaRepository;

    @Mock
    EmpresaParametrosRepository parametrosRepository;

    @Mock
    RegimeTributarioService regimeTributarioService;

    @Mock
    CidadeService cidadeService;

    @Mock
    EnderecoService enderecoService;

    EmpresaService empresaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        empresaService = new EmpresaService(empresaRepository, parametrosRepository, regimeTributarioService, cidadeService, enderecoService);
    }

    @Test
    void shouldRegisterEmpresaSuccessfully() {
        Empresa empresa = new Empresa();
        doAnswer(invocation -> {
            return null;
        }).when(empresaRepository).save(empresa);

        assertDoesNotThrow(() -> empresaService.cadastro(empresa));
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    void shouldThrowExceptionWhenCadastroFails() {
        Empresa empresa = new Empresa();
        doThrow(new RuntimeException()).when(empresaRepository).save(empresa);

        assertThrows(RuntimeException.class, () -> empresaService.cadastro(empresa));
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    void shouldReturnEmpresaIfExists() {
        Empresa empresa = new Empresa();
        when(empresaRepository.buscaEmpresaCadastrada()).thenReturn(Optional.of(empresa));

        Optional<Empresa> actualEmpresa = empresaService.verificaEmpresaCadastrada();

        assertTrue(actualEmpresa.isPresent());
        assertEquals(empresa, actualEmpresa.get());
        verify(empresaRepository, times(1)).buscaEmpresaCadastrada();
    }

    @Test
    void shouldReturnEmptyIfEmpresaDoesNotExist() {
        when(empresaRepository.buscaEmpresaCadastrada()).thenReturn(Optional.empty());

        Optional<Empresa> actualEmpresa = empresaService.verificaEmpresaCadastrada();

        assertFalse(actualEmpresa.isPresent());
        verify(empresaRepository, times(1)).buscaEmpresaCadastrada();
    }

    @Test
    void shouldMergeAndUpdateExistingEmpresa() {
        Long codigo = 1L;
        String nome = "Empresa Teste";
        String nomeFantasia = "Fantasia Teste";
        String cnpj = "123456789";
        String ie = "IE123";
        int serie = 1;
        int ambiente = 1;
        Long codRegime = 1L;
        Long codendereco = 1L;
        Long codcidade = 1L;
        String rua = "Rua Teste";
        String bairro = "Bairro Teste";
        String numero = "123";
        String cep = "12345-678";
        String referencia = "Referencia Teste";
        Double aliqCalcCredito = 1.0;

        doNothing().when(empresaRepository).update(codigo, nome, nomeFantasia, cnpj, ie, codRegime);
        doNothing().when(parametrosRepository).update(serie, ambiente, aliqCalcCredito);
        doNothing().when(enderecoService).update(codendereco, codcidade, rua, bairro, numero, cep, referencia);

        String result = empresaService.merger(codigo, nome, nomeFantasia, cnpj, ie, serie, ambiente, codRegime, codendereco, codcidade, rua, bairro, numero, cep, referencia, aliqCalcCredito);

        assertEquals("Empresa salva com sucesso", result);
        verify(empresaRepository, times(1)).update(codigo, nome, nomeFantasia, cnpj, ie, codRegime);
        verify(parametrosRepository, times(1)).update(serie, ambiente, aliqCalcCredito);
        verify(enderecoService, times(1)).update(codendereco, codcidade, rua, bairro, numero, cep, referencia);
    }

    @Test
    void shouldMergeAndSaveNewEmpresa() {
        String nome = "Empresa Teste";
        String nomeFantasia = "Fantasia Teste";
        String cnpj = "123456789";
        String ie = "IE123";
        int serie = 1;
        int ambiente = 1;
        Long codRegime = 1L;
        Long codcidade = 1L;
        String rua = "Rua Teste";
        String bairro = "Bairro Teste";
        String numero = "123";
        String cep = "12345-678";
        String referencia = "Referencia Teste";
        Double aliqCalcCredito = 1.0;

        EmpresaParametro parametro = new EmpresaParametro();
        doAnswer(invocation -> {
            return null;
        }).when(parametrosRepository).save(any(EmpresaParametro.class));

        RegimeTributario regimeTributario = new RegimeTributario();
        when(regimeTributarioService.busca(codRegime)).thenReturn(Optional.of(regimeTributario));

        Cidade cidade = new Cidade();
        when(cidadeService.busca(codcidade)).thenReturn(Optional.of(cidade));

        LocalDate dataAtual = LocalDate.now();
        Endereco endereco = new Endereco(rua, bairro, numero, cep, referencia, Date.valueOf(dataAtual), cidade);
        doAnswer(invocation -> null).when(enderecoService).cadastrar(any(Endereco.class));


        Empresa empresa = new Empresa(nome, nomeFantasia, cnpj, ie, regimeTributario, endereco, parametro);
        doAnswer(invocation -> {
            return null;
        }).when(empresaRepository).save(any(Empresa.class));

        String result = empresaService.merger(null, nome, nomeFantasia, cnpj, ie, serie, ambiente, codRegime, null, codcidade, rua, bairro, numero, cep, referencia, aliqCalcCredito);

        assertEquals("Empresa salva com sucesso", result);
        verify(parametrosRepository, times(1)).save(parametro);
        verify(regimeTributarioService, times(1)).busca(codRegime);
        verify(cidadeService, times(1)).busca(codcidade);
        verify(enderecoService, times(1)).cadastrar(endereco);
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    void shouldThrowExceptionWhenMergeFailsToSaveNewEmpresa() {
        String nome = "Empresa Teste";
        String nomeFantasia = "Fantasia Teste";
        String cnpj = "123456789";
        String ie = "IE123";
        int serie = 1;
        int ambiente = 1;
        Long codRegime = 1L;
        Long codcidade = 1L;
        String rua = "Rua Teste";
        String bairro = "Bairro Teste";
        String numero = "123";
        String cep = "12345-678";
        String referencia = "Referencia Teste";
        Double aliqCalcCredito = 1.0;

        EmpresaParametro parametro = new EmpresaParametro();
        doAnswer(invocation -> null).when(parametrosRepository).save(any(EmpresaParametro.class));

        RegimeTributario regimeTributario = new RegimeTributario();
        when(regimeTributarioService.busca(codRegime)).thenReturn(Optional.of(regimeTributario));

        Cidade cidade = new Cidade();
        when(cidadeService.busca(codcidade)).thenReturn(Optional.of(cidade));

        LocalDate dataAtual = LocalDate.now();
        Endereco endereco = new Endereco(rua, bairro, numero, cep, referencia, Date.valueOf(dataAtual), cidade);
        doAnswer(invocation -> null).when(enderecoService).cadastrar(any(Endereco.class));

        Empresa empresa = new Empresa(nome, nomeFantasia, cnpj, ie, regimeTributario, endereco, parametro);
        doThrow(new DataIntegrityViolationException("Erro ao salvar novos dados da empresa. Chame o suporte")).when(empresaRepository).save(empresa);

        Throwable exception = assertThrows(DataIntegrityViolationException.class, () -> empresaService.merger(null, nome, nomeFantasia, cnpj, ie, serie, ambiente, codRegime, null, codcidade, rua, bairro, numero, cep, referencia, aliqCalcCredito));
        assertEquals("Erro ao salvar novos dados da empresa. Chame o suporte", exception.getMessage());

        verify(parametrosRepository, times(1)).save(parametro);
        verify(regimeTributarioService, times(1)).busca(codRegime);
        verify(cidadeService, times(1)).busca(codcidade);
        verify(enderecoService, times(1)).cadastrar(endereco);
        verify(empresaRepository, times(1)).save(empresa);
    }
}
