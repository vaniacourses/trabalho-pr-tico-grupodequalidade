package net.originmobi.pdv;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

public class FuncionalAjusteTest {
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
    void criarAjusteTest() throws InterruptedException {
        login();
        acessarTelaDeAjustes();
        clicarEmNovo();
        preencherFormulario();
        autorizarProcessamento();
        verificarMensagemDeSucesso("Ajuste realizado com sucesso");
        verificarAjusteProcessado();
    }

    private void verificarAjusteProcessado() {
    }

    private void preencherFormulario() throws InterruptedException {
        escreverObservacao("Ajuste de estoque");
        selecionarPrimeiroItemDropdown();
        clicarEmInserir();
        responderPromptCom10();

    }

    private void responderPromptCom10() {
        Alert prompt = wait.until(ExpectedConditions.alertIsPresent());
        prompt.sendKeys("10");
        prompt.accept();
    }

    private void selecionarPrimeiroItemDropdown() throws InterruptedException {
        WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-id='codigoProduto' and contains(@class, 'dropdown-toggle')]")));
        dropdownButton.click();
        Thread.sleep(1000);
        WebElement firstItem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-menu inner selectpicker']//li[2]/a")));
        firstItem.click();
    }

    private void escreverObservacao(String observacao) throws InterruptedException {
        WebElement obsField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("obs")));
        obsField.clear();
        obsField.sendKeys(observacao);
        Thread.sleep(1000); // Optional delay to ensure the text is written
    }

    private void clicarEmInserir() {
        WebElement novoButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/pdv-0.0.1-SNAPSHOT/ajustes/addproduto' and contains(@class, 'btn-azul-padrao')]")));
        novoButton.click();
    }

    private void clicarEmNovo() {
        WebElement novoButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/pdv-0.0.1-SNAPSHOT/ajustes' and contains(@class, 'btn-azul-padrao')]")));
        novoButton.click();
    }

    private void aceitarAlertaDeConfirmacao() {
        Alert confirmationAlert = wait.until(ExpectedConditions.alertIsPresent());
        confirmationAlert.accept();
    }

    private void autorizarProcessamento() {
        WebElement novoButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/pdv-0.0.1-SNAPSHOT/ajustes/processar' and contains(@class, 'btn-azul-padrao')]")));
        novoButton.click();
        aceitarAlertaDeConfirmacao();
    }
    private void login() {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("btn-login"));

        usernameField.sendKeys("gerente");
        passwordField.sendKeys("123");
        loginButton.click();
    }
    private void acessarTelaDeAjustes() {
        driver.get("http://localhost:8080/pdv-0.0.1-SNAPSHOT/ajustes");
    }

    private void verificarMensagemDeSucesso(String mensagemEsperada) {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        assertEquals(mensagemEsperada, alertText);
        alert.accept();
    }
}