import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

public class Zingsniai {
    private static WebDriver browser;
    public static final String URL = "http://localhost:8080/skaiciuotuvas";
    public static final String TIRKINTI_URL = "http://localhost:8080/rodyti?id=";
    public static final String TIKRINTI_AUTORIZACIJA_URL = "http://localhost:8080/skaiciai";

    public static void setUp(String url) {
        System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        browser = new ChromeDriver(options);
        browser.get(url);
    }

    public static void ivestiNaujusDuomenis(String prisijungimoVardas, String slaptazodis, String slaptazodzioPatvirtinimas) {
        WebElement naujoVartotojoPrisijungimoVarda = browser.findElement(By.id("username"));
        naujoVartotojoPrisijungimoVarda.sendKeys(prisijungimoVardas);

        WebElement naujoVartotojoSlaptazodi = browser.findElement(By.id("password"));
        naujoVartotojoSlaptazodi.sendKeys(slaptazodis);

        WebElement naujoSlaptazodzioPatvirtinimas = browser.findElement(By.id("passwordConfirm"));
        naujoSlaptazodzioPatvirtinimas.sendKeys(slaptazodzioPatvirtinimas);
    }

    public static void ivestiEsamusPrisijungimoDuomenis(String prisijungimoVardas, String slaptazodis) {
        WebElement esamasPrisijungimoVarda = browser.findElement(By.name("username"));
        esamasPrisijungimoVarda.sendKeys(prisijungimoVardas);

        WebElement tesiingasSlaptazodis = browser.findElement(By.name("password"));
        tesiingasSlaptazodis.sendKeys(slaptazodis);
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

    public static void ivestiURLPasiekiamaTikAutorizuotiemsVartuotojams() {
        browser.get(TIKRINTI_AUTORIZACIJA_URL);
    }

    public static void paspaustiNuorodaSukurtiNaujaPaskyra() {
        WebElement sukurtuNaujaPaskyra = browser.findElement(By.linkText("Sukurti naują paskyrą"));
        sukurtuNaujaPaskyra.click();
    }

    public static void paspaustiSukurtiMygtuka() {
        WebElement spaustiSukurtiMygtuka = browser.findElement(By.cssSelector("button[class='btn btn-lg btn-primary btn-block']"));
        spaustiSukurtiMygtuka.click();
    }

    public static void paspaustiPrisijungimoMygtuka() {
        WebElement spaustiPrisijungtiMygtuka = browser.findElement(By.cssSelector("button[class='btn btn-lg btn-primary btn-block']"));
        spaustiPrisijungtiMygtuka.click();
    }

    public static void paspaustiSkaiciuotiMygtuka() {
        WebElement spaustiSkaiciuotiMygtuka = browser.findElement(By.cssSelector("[value='skaičiuoti']"));
        spaustiSkaiciuotiMygtuka.click();
    }

    public static void surastiIrasaPagalEgzistuojantiId(String id) {
        browser.get(TIRKINTI_URL + id);
    }

    public static void isitikintiKadVartotojasYraPrisijunges(String autorizacijosPatvirtinimas) {
        String irasasSuNurodytuIdBuvoRastas = browser.findElement(By.className("container")).getText();
        assertTrue(irasasSuNurodytuIdBuvoRastas.contains(autorizacijosPatvirtinimas));
    }

    public static void isitikintiKadVartotojasNeprisijunges() {
        boolean patvirtinimasKadBuvoIvestiNeteisingiPrisijungimoDuomenys =
                browser.findElement(By.xpath("//span[text()='Įvestas prisijungimo vardas ir/ arba slaptažodis yra neteisingi']")).isDisplayed();
        Assert.assertEquals(patvirtinimasKadBuvoIvestiNeteisingiPrisijungimoDuomenys, true);
    }

    public static void isitikintiKadVartotojasYraPrisijungimoPuslapyje(String prisijungimoIdentifikatorius) {
        String matomasPrisijungimoPuslapis = browser.getPageSource();
        assertTrue(matomasPrisijungimoPuslapis.contains(prisijungimoIdentifikatorius));
    }

    public static void isitikintiKadPaskyraNebuvoSukurtaIvedusNeteisingaiSlaptazodi() {
        boolean ivestasNeteisingaiSlaptazodis = browser.findElement(By.id("passwordConfirm.errors")).isDisplayed();
        Assert.assertEquals(ivestasNeteisingaiSlaptazodis, true);
    }

    public static void isitikintiKadDuomenysSuskaiciuotiTeisinga(String pirmasSkaicius, String antrasSkaicius, String veiksmoZenklas, String rezultatas) {
        String patvirtinimasKadDuomenysBuvoSuskaiciuotiTeisingai = browser.findElement(By.className("container")).getText();
        String tikrinamasTekstas = pirmasSkaicius + " " + veiksmoZenklas + " " + antrasSkaicius + " = " + rezultatas;
        patvirtinimasKadDuomenysBuvoSuskaiciuotiTeisingai.contains(tikrinamasTekstas);
    }

    public static void isitikintiKadDuomenysNebuvoSuskaiciuoti(String klaidosTekstas) {
        String patvirtinimasKadDuomenysBuvoSuskaiciuotiTeisingai = browser.findElement(By.className("container")).getText();
        assertTrue(patvirtinimasKadDuomenysBuvoSuskaiciuotiTeisingai.contains(klaidosTekstas));
    }

    public static void isitikintiKadIrasasSuNurodytuIdBuvoRastas(String ieskomasId) {
        String irasasSuNurodytuIdBuvoRastas = browser.findElement(By.className("container")).getText();
        assertTrue(irasasSuNurodytuIdBuvoRastas.contains(ieskomasId));
    }

    public static void isitikintiKadIeskomasIrasasNebuvoRastas(String paieskosRezultatoTekstas) {
        String irasasSuNurodytuIdNebuvoRastas = browser.getPageSource();
        assertTrue(irasasSuNurodytuIdNebuvoRastas.contains(paieskosRezultatoTekstas));
    }

    public static void pasirinktiAtliktosOperacijosPuslapi() {
        WebElement atliktosOperacijosNuoroda = browser.findElement(By.linkText("Atliktos operacijos"));
        atliktosOperacijosNuoroda.click();
    }

    public static void closeBrowser() {
        browser.close();
    }

    public static WebDriver getBrowser() {
        return browser;
    }
}