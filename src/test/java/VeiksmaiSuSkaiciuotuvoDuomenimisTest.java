import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class VeiksmaiSuSkaiciuotuvoDuomenimisTest {

    @BeforeMethod
    public void setUp() {
        Steps.setUp(Steps.URL);
        LoginTest.prisijugtiPriePaskyros();
    }

    @Test
    public void ivestiDuomenisSkaiciavimui() {
        Steps.ivestiDuomenisSkaiciavimui(Constants.PIRMAS_SKAICIUS, Constants.ANTRAS_SKAICIUS, Constants.VEIKSMAS);
        Steps.paspaustiSkaiciuotiMygtuka();
        Steps.isitikintiKadDuomenysSuskaiciuotiTeisinga(Constants.PIRMAS_SKAICIUS, Constants.ANTRAS_SKAICIUS, Constants.VEIKSMO_ZENKLAS, Constants.REZULTATAS);
    }

    @Test
    public void ivestiNeteisingusDuomenisSkaiciavimui() {
        Steps.ivestiDuomenisSkaiciavimui(Constants.NETEISINGAS_SKAICIUS, Constants.ANTRAS_SKAICIUS, Constants.VEIKSMAS);
        Steps.paspaustiSkaiciuotiMygtuka();
        Steps.isitikintiKadDuomenysNebuvoSuskaiciuoti(Constants.VALIDACJOS_KLAIDA);
    }

    @Test
    public void patvirtintiKadIeskomasIrasasBuvoRastas() {
        Steps.pasirinktiAtliktosOperacijosPuslapi();
        Steps.surastiIrasaPagalEgzistuojantiId(Constants.EGZISTUOJANTIS_ID);
        Steps.isitikintiKadIrasasSuNurodytuIdBuvoRastas(Constants.EGZISTUOJANTIS_ID);
    }

    @Test
    public void patvirtinimasKadNeegzistuojantiIrasasNebuvoRastas() {
        Steps.pasirinktiAtliktosOperacijosPuslapi();
        Steps.surastiIrasaPagalEgzistuojantiId(Constants.NEEGZISTUOJANTIS_ID);
        Steps.isitikintiKadIrasasSuNurodytuIdNebuvoRastas(Constants.NERASTO_IRASO_PATVIRTINIMO_TEKSTAS);
    }

    @Test
    public void isitikintiKadIrasasBuvoIstrintas() {
        Steps.pasirinktiAtliktosOperacijosPuslapi();
        Steps.istrintiIrasaPagalEgzistuojantiId(Constants.ID_TRINIMUI);
        Steps.surastiIrasaPagalEgzistuojantiId(Constants.ID_TRINIMUI);
        Steps.isitikintiKadIrasasSuNurodytuIdNebuvoRastas(Constants.NERASTO_IRASO_PATVIRTINIMO_TEKSTAS);
    }

    @Test
    public void isitikintiKadIrasasBuvoParedaguotas(){
        Steps.pasirinktiAtliktosOperacijosPuslapi();
        Steps.redaguotiEsamaIrasa(Constants.REDAGUOJAMAS_ID, Constants.REDAGUOJAMAS_PIRMAS_SKAICIUS,
                Constants.REDAGUOJAMAS_ANTRAS_SKAICIUS, Constants.REDAGUOJAMO_VEIKSMO_ZENKLAS, Constants.REDAGUOJAMAS_REZULTATAS);
        Steps.surastiIrasaPagalEgzistuojantiId(Constants.REDAGUOJAMAS_ID);
        Steps.isitikintiKadIrasasBuvoAtnaujintas(Constants.REDAGUOJAMAS_REZULTATAS);
    }

    @Test
    public void isitikintiKadIrasasNebuvoAtnaujintas(){
        Steps.pasirinktiAtliktosOperacijosPuslapi();
        Steps.redaguotiEsamaIrasa(Constants.REDAGUOJAMAS_ID, Constants.NETEISINGAS_SKAICIUS,
                Constants.REDAGUOJAMAS_ANTRAS_SKAICIUS, Constants.REDAGUOJAMO_VEIKSMO_ZENKLAS, Constants.REDAGUOJAMAS_REZULTATAS);
        Steps.isitikintiKadIrasasSuNurodytuIdNebuvoRastas(Constants.NERASTO_IRASO_PATVIRTINIMO_TEKSTAS);
    }

    @AfterMethod
    public void logout(ITestResult result) throws IOException {
        if (result.isSuccess()) {
            Steps.closeBrowser();
        } else {
            SimpleDateFormat time = new SimpleDateFormat("MM_dd_HH_ss");
            time.setTimeZone(TimeZone.getTimeZone("Europe/Vilnius"));
            long failTime = System.currentTimeMillis();
            String failedAt = time.format(failTime);
            System.out.println(failedAt);

            String tcName = result.getName();
            File srcshotFile = ((TakesScreenshot) Steps.getBrowser()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcshotFile, new File("screenshots/failedc/" + tcName + "_" + failedAt + ".png"));
        }
    }
}
