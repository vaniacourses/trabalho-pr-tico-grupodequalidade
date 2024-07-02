package net.originmobi.pdv;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FuncionalProdutoTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.gecko.driver", "C:\\tools\\geckodriver-v0.34.0-win32\\geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        //options.addArguments("--headless"); // Opcional: roda o browser em modo headless
        driver = new FirefoxDriver(options);
        wait = new WebDriverWait(driver, 10);
        driver.get("http://localhost:8080/pdv-0.0.1-SNAPSHOT");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void criarProdutoTest() {
        login();
        acessarTelaDeProdutos();
        clicarEmNovo();
        String produtoName = gerarNomeDeProdutoTeste();
        preencherFormulario(produtoName);
        clicarEmSalvar();
        verificarProdutoNaLista(produtoName);
        verificarMensagemDeSucesso();
    }

    @Test
    void editarNomeDeProdutoTest() throws InterruptedException {
        login();
        acessarTelaDeProdutos();
        clicarEmEditarProduto(1);
        String produtoName = gerarNomeDeProdutoTeste();
        alterarDescricaoESalvar(produtoName);
        verificarMensagemAtualizacaoDeSucesso();
        clicarEmListar();
        verificarProdutoNaLista(produtoName);

    }

    String gerarNomeDeProdutoTeste(){
        return "Produto Teste " + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    private void alterarDescricaoESalvar(String novaDescricao) throws InterruptedException {
        WebElement descricaoField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descricao")));
        descricaoField.clear();
        descricaoField.sendKeys(novaDescricao);
        clicarEmSalvar();
    }
    private void clicarEmEditarProduto(int produtoId) {
        String xpath = "//a[@href='produto/" + produtoId + "']/span[contains(@class, 'glyphicon-pencil')]";
        WebElement editarButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        editarButton.click();
    }

    private void login() {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("btn-login"));

        usernameField.sendKeys("gerente");
        passwordField.sendKeys("123");
        loginButton.click();
    }

    private void acessarTelaDeProdutos() {
        driver.get("http://localhost:8080/pdv-0.0.1-SNAPSHOT/produto");
    }

    private void clicarEmNovo() {
        WebElement novoButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/pdv-0.0.1-SNAPSHOT/produto/form' and contains(@class, 'btn-azul-padrao')]")));
        novoButton.click();
    }

    private void preencherFormulario(String produtoName) {
        WebElement descricaoField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descricao")));
        WebElement fornecedorSelect = driver.findElement(By.id("fornecedor"));
        WebElement categoriaSelect = driver.findElement(By.id("categoria"));
        WebElement grupoSelect = driver.findElement(By.id("grupo"));
        WebElement balancaSelect = driver.findElement(By.id("balanca"));
        WebElement valorCustoField = driver.findElement(By.id("valorCusto"));
        WebElement validadeField = driver.findElement(By.id("validade"));
        WebElement valorVendaField = driver.findElement(By.id("valorVenda"));
        WebElement ativoSelect = driver.findElement(By.id("ativo"));
        WebElement controlaEstoqueSelect = driver.findElement(By.name("controla_estoque"));
        WebElement unidadeField = driver.findElement(By.id("unidade"));
        WebElement vendavelSelect = driver.findElement(By.id("vendavel"));
        WebElement stSelect = driver.findElement(By.id("st"));
        WebElement ncmField = driver.findElement(By.id("ncm"));
        WebElement cestField = driver.findElement(By.id("cest"));
        WebElement tributacaoSelect = driver.findElement(By.id("tributacao"));
        WebElement modBcIcmsSelect = driver.findElement(By.id("modbc"));

        descricaoField.sendKeys(produtoName);

        Select fornecedorDropdown = new Select(fornecedorSelect);
        fornecedorDropdown.selectByVisibleText("Fornecedor Padrão");
        Select categoriaDropdown = new Select(categoriaSelect);
        categoriaDropdown.selectByVisibleText("Padrão");
        Select grupoDropdown = new Select(grupoSelect);
        grupoDropdown.selectByVisibleText("Padrão");
        Select balancaDropdown = new Select(balancaSelect);
        balancaDropdown.selectByVisibleText("NAO");
        valorCustoField.sendKeys("10.00");
        validadeField.sendKeys("31/12/2024");
        valorVendaField.sendKeys("20.00");
        Select ativoDropdown = new Select(ativoSelect);
        ativoDropdown.selectByVisibleText("ATIVO");
        Select controlaEstoqueDropdown = new Select(controlaEstoqueSelect);
        controlaEstoqueDropdown.selectByVisibleText("SIM");
        unidadeField.sendKeys("UN");
        Select vendavelDropdown = new Select(vendavelSelect);
        vendavelDropdown.selectByVisibleText("SIM");
        Select stDropdown = new Select(stSelect);
        stDropdown.selectByVisibleText("NAO");
        ncmField.sendKeys("12345678");
        cestField.sendKeys("87654321");
        Select tributacaoDropdown = new Select(tributacaoSelect);
        tributacaoDropdown.selectByVisibleText("");
        Select modBcIcmsDropdown = new Select(modBcIcmsSelect);
        modBcIcmsDropdown.selectByVisibleText("Preço Tabelado Máx. (valor)");


    }


    private void clicarEmSalvar() {
        WebElement salvarButton = driver.findElement(By.xpath("//input[@type='submit' and @value='Salvar']"));
        salvarButton.click();
    }

    private void clicarEmListar() {
        WebElement listarButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/pdv-0.0.1-SNAPSHOT/produto' and contains(@class, 'btn-azul-padrao')]")));
        listarButton.click();
    }

    private void verificarProdutoNaLista(String produtoName) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement tabelaProdutos = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[contains(@class, 'table-striped')]")));

        List<WebElement> linhas = tabelaProdutos.findElements(By.xpath(".//tbody/tr"));

        boolean produtoEncontrado = false;

        for (WebElement linha : linhas) {
            WebElement descricao = linha.findElement(By.xpath(".//td[2]"));
            if (descricao.getText().equals(produtoName)) {
                produtoEncontrado = true;
                break;
            }
        }

        assertTrue(produtoEncontrado, "Produto '" + produtoName + "' não encontrado na lista.");
    }

    private void verificarMensagemAtualizacaoDeSucesso() {
        WebElement mensagemSucesso = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Produto atualizado com sucesso']")));
        assertNotNull(mensagemSucesso, "Produto atualizado com sucesso");
    }

    private void verificarMensagemDeSucesso() {
        WebElement mensagemSucesso = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Produto cadastrado com sucesso']")));
        assertNotNull(mensagemSucesso, "Produto cadastrado com sucesso");
    }
}
