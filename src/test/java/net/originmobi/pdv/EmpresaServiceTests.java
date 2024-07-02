package net.originmobi.pdv;

import net.originmobi.pdv.model.*;
import net.originmobi.pdv.repository.EmpresaParametrosRepository;
import net.originmobi.pdv.service.EmpresaService;
import net.originmobi.pdv.service.RegimeTributarioService;
import net.originmobi.pdv.service.CidadeService;
import net.originmobi.pdv.service.EnderecoService;
//import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

//import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class EmpresaServiceTests {

    @Autowired
    private EmpresaService empresaService;

    @MockBean
    private EmpresaParametrosRepository parametros;

    @MockBean
    private RegimeTributarioService regimes;

    @MockBean
    private CidadeService cidades;

    @MockBean
    private EnderecoService enderecos;

//    @Test
//    @Transactional
//    public void testCadastrarEmpresaSucesso() {
//
//        // Dados Empresa
//        Empresa empresa = new Empresa("Empresa Domenicos Ltda",
//                "Empresa Teste Fantasia",
//                "12.345.678.901234-56",
//                "123.456.789-1",
//                RegimeTributario.SIMPLES_NACIONAL,
//                criarEndereco(),
//                criarParametro());
//
//        // Cenário: Cadastrar empresa
//        empresaService.cadastro(empresa);
//
//        // Verifica se o método passou (sucesso ou falha)
//        verify(empresaService, times(1)).cadastro(empresa);
//    }

    @Test
    @Transactional
    public void testCadastrarEmpresaDadosIncompletos() {

        // Dados incompletos da Empresa
        Empresa empresa = new Empresa(null,
                null,
                null,
                null,
                null,
                null,
                null);

        // Cenário: Tentativa de cadastrar empresa
        try {
            empresaService.cadastro(empresa); // Tentar cadastro
        } catch (IllegalArgumentException e) {
            // Capturar exceção se lançada
        }

        // Verifica se o método foi chamado ou nao
        verify(empresaService, times(0)).cadastro(empresa);
    }

    private void assertThrows(Class<IllegalArgumentException> illegalArgumentExceptionClass, Object o) {
    }

    private Endereco criarEndereco() {
        LocalDate dataAtual = LocalDate.now();
        return new Endereco("Rua S",
                "Bairro Algum",
                "50",
                "22333456",
                "Ponto de referência", Date.valueOf(dataAtual), criarCidade());
    }

    private Cidade criarCidade() {
        return new Cidade();
    }

    private EmpresaParametro criarParametro() {
        return new EmpresaParametro();
    }
}

