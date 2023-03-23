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
        Zingsniai.setUp(Zingsniai.URL);
        Zingsniai.ivestiEsamusPrisijungimoDuomenis(Konstantos.ESAMAS_VARTOTOJAS, Konstantos.SLAPTAZODIS);
        Zingsniai.paspaustiPrisijungimoMygtuka();
    }

    @Test
    public void isitikintiKadNaujasIrasasBuvoSukurtas() {
        Zingsniai.ivestiDuomenisSkaiciavimui(Konstantos.PIRMAS_SKAICIUS, Konstantos.ANTRAS_SKAICIUS, Konstantos.VEIKSMAS);
        Zingsniai.paspaustiSkaiciuotiMygtuka();
        Zingsniai.isitikintiKadDuomenysSuskaiciuotiTeisinga(Konstantos.PIRMAS_SKAICIUS, Konstantos.ANTRAS_SKAICIUS, Konstantos.VEIKSMO_ZENKLAS, Konstantos.REZULTATAS);
    }

    @Test
    public void isitikintiKadIrasasNebuvoSukurtasIvedusNeteisingusDuomenis() {
        Zingsniai.ivestiDuomenisSkaiciavimui(Konstantos.NETEISINGAS_SKAICIUS, Konstantos.ANTRAS_SKAICIUS, Konstantos.VEIKSMAS);
        Zingsniai.paspaustiSkaiciuotiMygtuka();
        Zingsniai.isitikintiKadDuomenysNebuvoSuskaiciuoti(Konstantos.VALIDACJOS_KLAIDA);
    }

    @Test
    public void isitikintiKadIeskomasIrasasBuvoRastas() {
        Zingsniai.pasirinktiAtliktosOperacijosPuslapi();
        Zingsniai.surastiIrasaPagalEgzistuojantiId(Konstantos.EGZISTUOJANTIS_ID);
        Zingsniai.isitikintiKadIrasasSuNurodytuIdBuvoRastas(Konstantos.EGZISTUOJANTIS_ID);
    }

    @Test
    public void isitikintiKadNeegzistuojantisIrasasNebuvoRastas() {
        Zingsniai.pasirinktiAtliktosOperacijosPuslapi();
        Zingsniai.surastiIrasaPagalEgzistuojantiId(Konstantos.NEEGZISTUOJANTIS_ID);
        Zingsniai.isitikintiKadIeskomasIrasasNebuvoRastas(Konstantos.NERASTO_IRASO_PATVIRTINIMO_TEKSTAS);
    }

    @AfterMethod
    public void logout(ITestResult result) throws IOException {
        if (result.isSuccess()) {
            Zingsniai.closeBrowser();
        } else {
            SimpleDateFormat time = new SimpleDateFormat("MM_dd_HH_ss");
            time.setTimeZone(TimeZone.getTimeZone("Europe/Vilnius"));
            long failTime = System.currentTimeMillis();
            String failedAt = time.format(failTime);
            System.out.println(failedAt);

            String tcName = result.getName();
            File srcshotFile = ((TakesScreenshot) Zingsniai.getBrowser()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcshotFile, new File("screenshots/failedc/" + tcName + "_" + failedAt + ".png"));
        }
    }
}