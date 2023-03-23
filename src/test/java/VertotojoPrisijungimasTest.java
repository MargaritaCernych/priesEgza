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

public class LoginTest {

    @BeforeMethod
    public void setUp() {
        Steps.setUp(Steps.URL);
    }

    @Test
    public void sukurtiNaujaPaskyra() {

        Steps.paspaustiLinkaSukurtiNaujaPaskyra();
        Steps.ivestiNaujusDuomenis(Constants.PRISIJUNGIMO_VARDAS, Constants.SLAPTAZODIS, Constants.SLAPTAZODIS);
        Steps.paspaustiSukurtiMygtuka();
        Steps.isitikintiKadVartotojasYraPrisijunges();
    }

    @Test
    public void isitikintiKadPaskyraNebuvoSukurta() {
        Steps.paspaustiLinkaSukurtiNaujaPaskyra();
        Steps.ivestiNaujusDuomenis(Constants.PRISIJUNGIMO_VARDAS2, Constants.SLAPTAZODIS, Constants.NETEISINGAS_SLAPTAZODIS);
        Steps.paspaustiSukurtiMygtuka();
        Steps.isitikintiKadPaskyraNebuvoSukurtaIvedusNeteisingaiSlaptazodi();
    }

    @Test
    public static void prisijugtiPriePaskyros() {
        Steps.ivestiEsamusPrisijungimoDuomenis(Constants.PRISIJUNGIMO_VARDAS, Constants.SLAPTAZODIS);
        Steps.paspaustiPrisijungimoMygtuka();
        Steps.isitikintiKadVartotojasYraPrisijunges();
    }

    @Test
    public void bandytiPrisijugtiIvedusNeteisingaSlaptazodi() {
        Steps.ivestiEsamusPrisijungimoDuomenis(Constants.PRISIJUNGIMO_VARDAS, Constants.NETEISINGAS_SLAPTAZODIS);
        Steps.paspaustiPrisijungimoMygtuka();
        Steps.isitikintiKadVartotojasNeprisijunges();
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