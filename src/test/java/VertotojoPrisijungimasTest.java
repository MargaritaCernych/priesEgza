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

public class VertotojoPrisijungimasTest {

    @BeforeMethod
    public void setUp() {
        Zingsniai.setUp(Zingsniai.URL);
    }

    @Test
    public void isitikintiKadNaujaPaskyraBuvoSukurta() {

        Zingsniai.paspaustiNuorodaSukurtiNaujaPaskyra();
        Zingsniai.ivestiNaujusDuomenis(Konstantos.PRISIJUNGIMO_VARDAS, Konstantos.SLAPTAZODIS, Konstantos.SLAPTAZODIS);
        Zingsniai.paspaustiSukurtiMygtuka();
        Zingsniai.isitikintiKadVartotojasYraPrisijunges(Konstantos.AUTORIZACIJOS_PATVIRITNIMO_TEKSTAS + Konstantos.PRISIJUNGIMO_VARDAS);
    }

    @Test
    public void isitikintiKadPaskyraNebuvoSukurta() {
        Zingsniai.paspaustiNuorodaSukurtiNaujaPaskyra();
        Zingsniai.ivestiNaujusDuomenis(Konstantos.PRISIJUNGIMO_VARDAS2, Konstantos.SLAPTAZODIS, Konstantos.NETEISINGAS_SLAPTAZODIS);
        Zingsniai.paspaustiSukurtiMygtuka();
        Zingsniai.isitikintiKadPaskyraNebuvoSukurtaIvedusNeteisingaiSlaptazodi();
    }

    @Test
    public static void isitikintiKadVartotojasPrisijungePriePaskyros() {
        Zingsniai.ivestiEsamusPrisijungimoDuomenis(Konstantos.ESAMAS_VARTOTOJAS, Konstantos.SLAPTAZODIS);
        Zingsniai.paspaustiPrisijungimoMygtuka();
        Zingsniai.isitikintiKadVartotojasYraPrisijunges(Konstantos.AUTORIZACIJOS_PATVIRITNIMO_TEKSTAS + Konstantos.ESAMAS_VARTOTOJAS);
    }

    @Test
    public void isitikintiKadVartotojuiNepavykoPrisijungtiIvedusNetesingaSlaptazodi() {
        Zingsniai.ivestiEsamusPrisijungimoDuomenis(Konstantos.ESAMAS_VARTOTOJAS, Konstantos.NETEISINGAS_SLAPTAZODIS);
        Zingsniai.paspaustiPrisijungimoMygtuka();
        Zingsniai.isitikintiKadVartotojasNeprisijunges();
    }

    @Test
    public void isitikintiKadAutorizuotasVartotojasGaliNaudotiSistema() {
        Zingsniai.ivestiEsamusPrisijungimoDuomenis(Konstantos.ESAMAS_VARTOTOJAS, Konstantos.SLAPTAZODIS);
        Zingsniai.paspaustiPrisijungimoMygtuka();
        Zingsniai.ivestiURLPasiekiamaTikAutorizuotiemsVartuotojams();
        Zingsniai.isitikintiKadVartotojasYraPrisijunges(Konstantos.AUTORIZACIJOS_PATVIRITNIMO_TEKSTAS + Konstantos.ESAMAS_VARTOTOJAS);
    }

    @Test
    public void isitikintiKadNeutorizuotasVartotojasNegaliNaudotisSistema() {
        Zingsniai.ivestiURLPasiekiamaTikAutorizuotiemsVartuotojams();
        Zingsniai.isitikintiKadVartotojasYraPrisijungimoPuslapyje(Konstantos.PRISIJUNGIMO_IDENTIFEKAVIMO_TEKSTAS);
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