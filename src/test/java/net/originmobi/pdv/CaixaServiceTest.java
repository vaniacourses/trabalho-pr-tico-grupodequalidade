package net.originmobi.pdv;

import net.originmobi.pdv.enumerado.caixa.CaixaTipo;
import net.originmobi.pdv.filter.BancoFilter;
import net.originmobi.pdv.filter.CaixaFilter;
import net.originmobi.pdv.model.Caixa;
import net.originmobi.pdv.model.CaixaLancamento;
import net.originmobi.pdv.model.Usuario;
import net.originmobi.pdv.repository.CaixaRepository;
import net.originmobi.pdv.service.CaixaLancamentoService;
import net.originmobi.pdv.service.CaixaService;
import net.originmobi.pdv.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class CaixaServiceTest {
    @Mock
    private CaixaRepository caixas;
    @Mock
    private UsuarioService usuarios;
    @Mock
    private CaixaLancamentoService lancamentos;
    @InjectMocks
    private CaixaService caixaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("usuario");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCadastroValorAberturaNulo() {
        Caixa caixa = new Caixa();
        caixa.setTipo(CaixaTipo.CAIXA);
        caixa.setValor_abertura(0.00);
        caixa.setCodigo(1L);
        caixa.setDescricao("");
        Usuario usuario = new Usuario();
        usuario.setCodigo(1L);

        when(caixas.caixaAberto()).thenReturn(Optional.empty());
        when(usuarios.buscaUsuario(anyString())).thenReturn(usuario);

        Long result = caixaService.cadastro(caixa);

        assertNotNull(result);
        verify(caixas, times(1)).save(any(Caixa.class));
        verify(lancamentos, times(0)).lancamento(any(CaixaLancamento.class));
    }

    @Test
    void testCadastroValorAberturaNegativo() {
        Caixa caixa = new Caixa();
        caixa.setTipo(CaixaTipo.CAIXA);
        caixa.setValor_abertura(-10.0);

        when(caixas.caixaAberto()).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            caixaService.cadastro(caixa);
        });

        assertEquals("Valor informado é inválido", exception.getMessage());
    }

    @Test
    void testCadastroTipoCofre() {
        Caixa caixa = new Caixa();
        caixa.setCodigo(1L);
        caixa.setTipo(CaixaTipo.COFRE);
        caixa.setValor_abertura(100.0);
        caixa.setDescricao("");
        Usuario usuario = new Usuario();
        usuario.setCodigo(1L);

        when(caixas.caixaAberto()).thenReturn(Optional.empty());
        when(usuarios.buscaUsuario(anyString())).thenReturn(usuario);

        Long result = caixaService.cadastro(caixa);

        assertNotNull(result);
        verify(caixas, times(1)).save(any(Caixa.class));
        verify(lancamentos, times(1)).lancamento(any(CaixaLancamento.class));
    }

    @Test
    void testCadastroBanco() {
        Caixa caixa = new Caixa();
        caixa.setTipo(CaixaTipo.BANCO);
        caixa.setCodigo(1L);
        caixa.setValor_abertura(0.0);
        caixa.setDescricao("");
        caixa.setAgencia("123-4");
        caixa.setConta("56789-0");
        Usuario usuario = new Usuario();
        usuario.setCodigo(1L);

        when(caixas.caixaAberto()).thenReturn(Optional.empty());
        when(usuarios.buscaUsuario(anyString())).thenReturn(usuario);

        Long result = caixaService.cadastro(caixa);

        assertNotNull(result);
        verify(caixas, times(1)).save(any(Caixa.class));
        assertEquals("1234", caixa.getAgencia());
        assertEquals("567890", caixa.getConta());
    }

    @Test
    void testFechaCaixaSenhaIncorreta() {
        Long caixaId = 1L;
        String senha = "senhaIncorreta";
        Usuario usuario = new Usuario();
        usuario.setSenha(new BCryptPasswordEncoder().encode("senhaCorreta"));

        when(usuarios.buscaUsuario(anyString())).thenReturn(usuario);

        String result = caixaService.fechaCaixa(caixaId, senha);

        assertEquals("Senha incorreta, favor verifique", result);
        verify(caixas, times(0)).save(any(Caixa.class));
    }

    @Test
    void testFechaCaixaJaFechado() {
        Long caixaId = 1L;
        String senha = "senhaCorreta";
        Usuario usuario = new Usuario();
        usuario.setSenha(new BCryptPasswordEncoder().encode(senha));

        Caixa caixa = new Caixa();
        caixa.setCodigo(caixaId);
        caixa.setData_fechamento(new Timestamp(System.currentTimeMillis()));

        when(usuarios.buscaUsuario(anyString())).thenReturn(usuario);
        when(caixas.findById(caixaId)).thenReturn(Optional.of(caixa));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            caixaService.fechaCaixa(caixaId, senha);
        });

        assertEquals("Caixa já esta fechado", exception.getMessage());
    }

    @Test
    void testCadastroErroNaGravacao() {
        Caixa caixa = new Caixa();
        caixa.setTipo(CaixaTipo.CAIXA);
        caixa.setValor_abertura(100.0);
        caixa.setDescricao("");
        Usuario usuario = new Usuario();
        usuario.setCodigo(1L);

        when(caixas.caixaAberto()).thenReturn(Optional.empty());
        when(usuarios.buscaUsuario(anyString())).thenReturn(usuario);
        doThrow(new RuntimeException()).when(caixas).save(any(Caixa.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            caixaService.cadastro(caixa);
        });

        assertEquals("Erro no processo de abertura, chame o suporte técnico", exception.getMessage());
    }

    @Test
    void testFechaCaixaErroNaGravacao() {
        Long caixaId = 1L;
        String senha = "senhaCorreta";
        Usuario usuario = new Usuario();
        usuario.setSenha(new BCryptPasswordEncoder().encode(senha));

        Caixa caixa = new Caixa();
        caixa.setCodigo(caixaId);

        when(usuarios.buscaUsuario(anyString())).thenReturn(usuario);
        when(caixas.findById(caixaId)).thenReturn(Optional.of(caixa));
        doThrow(new RuntimeException()).when(caixas).save(any(Caixa.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            caixaService.fechaCaixa(caixaId, senha);
        });

        assertEquals("Ocorreu um erro ao fechar o caixa, chame o suporte", exception.getMessage());
    }

    @Test
    void testCaixaIsAberto() {
        when(caixas.caixaAberto()).thenReturn(Optional.of(new Caixa()));

        boolean result = caixaService.caixaIsAberto();

        assertTrue(result);
        verify(caixas, times(1)).caixaAberto();
    }

    @Test
    void testListaTodos() {
        List<Caixa> mockList = new ArrayList<>();
        mockList.add(new Caixa());
        when(caixas.findByCodigoOrdenado()).thenReturn(mockList);

        List<Caixa> result = caixaService.listaTodos();

        assertNotNull(result);
        assertEquals(1, mockList.size());
    }

    @Test
    void testCaixaAberto() {
        Caixa caixa = new Caixa();
        when(caixas.caixaAberto()).thenReturn(Optional.of(caixa));

        Optional<Caixa> result = caixaService.caixaAberto();

        assertNotNull(result);
    }

    @Test
    void testCaixasAbertos() {
        List<Caixa> mockList = new ArrayList<>();
        mockList.add(new Caixa());
        when(caixas.caixasAbertos()).thenReturn(mockList);

        List<Caixa> result = caixaService.caixasAbertos();

        assertNotNull(result);
        assertEquals(1, mockList.size());
    }

    @Test
    void testListaBancosAbertosTipoFilterBanco() {
        List<Caixa> mockList = new ArrayList<>();
        mockList.add(new Caixa());
        BancoFilter filter = new BancoFilter();
        filter.setData_cadastro("2000/01/01");
        when(caixas.buscaCaixaTipoData(CaixaTipo.BANCO, new Date())).thenReturn(mockList);

        List<Caixa> result = caixaService.listaBancosAbertosTipoFilterBanco(CaixaTipo.CAIXA, filter);

        assertNotNull(result);
        assertEquals(1, mockList.size());
    }

    @Test
    void testListaBancosAbertosTipoFilterBancoEmptyDate() {
        List<Caixa> mockList = new ArrayList<>();
        mockList.add(new Caixa());
        mockList.add(new Caixa());
        BancoFilter filter = new BancoFilter();
        filter.setData_cadastro("");
        when(caixas.buscaCaixaTipo(CaixaTipo.BANCO)).thenReturn(mockList);

        List<Caixa> result = caixaService.listaBancosAbertosTipoFilterBanco(CaixaTipo.CAIXA, filter);

        assertNotNull(result);
        assertEquals(2, mockList.size());
    }

    @Test
    void testListarCaixas() {
        List<Caixa> mockList = new ArrayList<>();
        mockList.add(new Caixa());
        CaixaFilter filter = new CaixaFilter();
        filter.setData_cadastro("2000/01/01");
        when(caixas.buscaCaixaTipoData(CaixaTipo.BANCO, new Date())).thenReturn(mockList);

        List<Caixa> result = caixaService.listarCaixas(filter);

        assertNotNull(result);
        assertEquals(1, mockList.size());
    }

    @Test
    void testListarCaixasEmptyDate() {
        List<Caixa> mockList = new ArrayList<>();
        mockList.add(new Caixa());
        mockList.add(new Caixa());
        CaixaFilter filter = new CaixaFilter();
        filter.setData_cadastro("");
        when(caixas.buscaCaixaTipo(CaixaTipo.BANCO)).thenReturn(mockList);

        List<Caixa> result = caixaService.listarCaixas(filter);

        assertNotNull(result);
        assertEquals(2, mockList.size());
    }
}