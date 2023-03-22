import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import static org.testng.Assert.assertTrue;

public class Steps {
    private static WebDriver browser;
    public static final String URL = "http://localhost:8080/skaiciuotuvas";
    public static final String TIRKINTI_URL = "http://localhost:8080/rodyti?id=";
    public static final String TRINTI_URL = "http://localhost:8080/trinti?id=";
    public static final String REDAGUOTI_URL = "http://localhost:8080/atnaujinti?id=";

    public static void setUp(String url) {
        System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        browser = new ChromeDriver(options);
        browser.get(url);
    }

    public static void paspaustiLinkaSukurtiNaujaPaskyra() {
        WebElement sukurtuNaujaPaskyra = browser.findElement(By.linkText("Sukurti naują paskyrą"));
        sukurtuNaujaPaskyra.click();
    }

    public static void ivestiNaujusDuomenis(String prisijungimoVardas, String slaptazodis, String slaptazodzioPatvirtinimas) {
        WebElement naujoVartotojoPrisijungimoVarda = browser.findElement(By.id("username"));
        naujoVartotojoPrisijungimoVarda.sendKeys(prisijungimoVardas);

        WebElement naujoVartotojoSlaptazodi = browser.findElement(By.id("password"));
        naujoVartotojoSlaptazodi.sendKeys(slaptazodis);

        WebElement naujoSlaptazodzioPatvirtinimas = browser.findElement(By.id("passwordConfirm"));
        naujoSlaptazodzioPatvirtinimas.sendKeys(slaptazodzioPatvirtinimas);
    }

    public static void paspaustiSukurtiMygtuka() {
        WebElement spaustiSukurtiMygtuka = browser.findElement(By.cssSelector("button[class='btn btn-lg btn-primary btn-block']"));
        spaustiSukurtiMygtuka.click();
    }

    public static void isitikintiKadVartotojasYraPrisijunges() {
        WebElement skaiciuotuvas = browser.findElement(By.cssSelector("[class='navbar-brand']"));
        Assert.assertEquals("Skaičiuotuvas", skaiciuotuvas.getText());
    }

    public static void isitikintiKadPaskyraNebuvoSukurtaIvedusNeteisingaiSlaptazodi() {
        boolean ivestasNeteisingaiSlaptazodis = browser.findElement(By.id("passwordConfirm.errors")).isDisplayed();
        Assert.assertEquals(ivestasNeteisingaiSlaptazodis, true);
    }

    public static void ivestiEsamusPrisijungimoDuomenis(String prisijungimoVardas, String slaptazodis) {
        WebElement esamasPrisijungimoVarda = browser.findElement(By.name("username"));
        esamasPrisijungimoVarda.sendKeys(prisijungimoVardas);

        WebElement tesiingasSlaptazodis = browser.findElement(By.name("password"));
        tesiingasSlaptazodis.sendKeys(slaptazodis);
    }

    public static void paspaustiPrisijungimoMygtuka() {
        WebElement spaustiPrisijungtiMygtuka = browser.findElement(By.cssSelector("button[class='btn btn-lg btn-primary btn-block']"));
        spaustiPrisijungtiMygtuka.click();
    }

    public static void isitikintiKadVartotojasNeprisijunges() {
        boolean patvirtinimasKadBuvoIvestiNeteisingiPrisijungimoDuomenys =
                browser.findElement(By.xpath("//span[text()='Įvestas prisijungimo vardas ir/ arba slaptažodis yra neteisingi']")).isDisplayed();
        Assert.assertEquals(patvirtinimasKadBuvoIvestiNeteisingiPrisijungimoDuomenys, true);
    }

    public static void ivestiDuomenisSkaiciavimui(String pirmasSkaicius, String antrasSkaicius, String veiksmas) {
        WebElement ivestasPirmasSkaiicus = browser.findElement(By.id("sk1"));
        ivestasPirmasSkaiicus.clear();
        ivestasPirmasSkaiicus.sendKeys(pirmasSkaicius);

        WebElement ivestasAntrasSkaiicus = browser.findElement(By.id("sk2"));
        ivestasAntrasSkaiicus.clear();
        ivestasAntrasSkaiicus.sendKeys(antrasSkaicius);

        WebElement opeacijosZentklas = browser.findElement(By.name("zenklas"));
        opeacijosZentklas.click();

        Select norimasVeiksmas = new Select((browser.findElement(By.name("zenklas"))));
        norimasVeiksmas.selectByVisibleText(veiksmas);
    }

    public static void paspaustiSkaiciuotiMygtuka() {
        WebElement spaustiSkaiciuotiMygtuka = browser.findElement(By.cssSelector("[value='skaičiuoti']"));
        spaustiSkaiciuotiMygtuka.click();
    }

    public static void isitikintiKadDuomenysSuskaiciuotiTeisinga(String pirmasSkaicius, String antrasSkaicius, String veiksmoZenklas, String rezultatas) {
        String patvirtinimasKadDuomenysBuvoSuskaiciuotiTeisingai = browser.findElement(By.className("container")).getText();
        String tikrinamasTekstas = pirmasSkaicius + " " + veiksmoZenklas + " " + antrasSkaicius + " = " + rezultatas;
        patvirtinimasKadDuomenysBuvoSuskaiciuotiTeisingai.contains(tikrinamasTekstas);
    }

    public static void isitikintiKadDuomenysNebuvoSuskaiciuoti(String klaidosTekstas) {
        String patvirtinimasKadDuomenysBuvoSuskaiciuotiTeisingai = browser.findElement(By.className("container")).getText();
        patvirtinimasKadDuomenysBuvoSuskaiciuotiTeisingai.contains(klaidosTekstas);
    }

    public static void pasirinktiAtliktosOperacijosPuslapi() {
        WebElement atliktosOperacijosNuoroda = browser.findElement(By.linkText("Atliktos operacijos"));
        atliktosOperacijosNuoroda.click();
    }

    public static void surastiIrasaPagalEgzistuojantiId(String id) {
        browser.get(TIRKINTI_URL + id);
    }

    public static void istrintiIrasaPagalEgzistuojantiId(String id) {
        browser.get(TRINTI_URL + id);
    }

    public static void isitikintiKadIrasasSuNurodytuIdBuvoRastas(String ieskomasId) {
        String irasasSuNurodytuIdBuvoRastas = browser.findElement(By.className("container")).getText();
        irasasSuNurodytuIdBuvoRastas.contains(ieskomasId);
    }

    public static void isitikintiKadIrasasSuNurodytuIdNebuvoRastas(String paieskosRezultatoTekstas) {
        String irasasSuNurodytuIdNebuvoRastas = browser.getPageSource();
        assertTrue(irasasSuNurodytuIdNebuvoRastas.contains(paieskosRezultatoTekstas));
    }

    public static void redaguotiEsamaIrasa(String id, String redaguojamasPirmasSkaicius, String redaguojamasAntrasSkaicius, String veiksmoZenklas, String redaguojamasRezultatas) {
        browser.get(REDAGUOTI_URL + id);
        WebElement ivestasPirmasSkaiicus = browser.findElement(By.name("sk1"));
        ivestasPirmasSkaiicus.clear();
        ivestasPirmasSkaiicus.sendKeys(redaguojamasPirmasSkaicius);

        WebElement zenklas = browser.findElement(By.name("zenklas"));
        zenklas.clear();
        zenklas.sendKeys(veiksmoZenklas);

        WebElement ivestasAntrasSkaiicus = browser.findElement(By.name("sk2"));
        ivestasAntrasSkaiicus.clear();
        ivestasAntrasSkaiicus.sendKeys(redaguojamasAntrasSkaicius);

        WebElement rezultatas = browser.findElement(By.name("rezult"));
        rezultatas.clear();
        rezultatas.sendKeys(redaguojamasRezultatas);

        WebElement spaustiAtnaujintiMygtuka = browser.findElement(By.cssSelector("[value='Atnaujinti']"));
        spaustiAtnaujintiMygtuka.click();
    }

    public static void isitikintiKadIrasasBuvoAtnaujintas(String naujasReultats){
        String irasasSuNurodytuIdBuvoRastas = browser.findElement(By.className("container")).getText();
        assertTrue(irasasSuNurodytuIdBuvoRastas.contains(naujasReultats));
    }

    public static void closeBrowser() {

        browser.close();
    }

    public static WebDriver getBrowser() {
        return browser;
    }
}