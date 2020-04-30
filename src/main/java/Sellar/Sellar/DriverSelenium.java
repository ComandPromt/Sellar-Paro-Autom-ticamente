package Sellar.Sellar;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class DriverSelenium {

	protected static WebDriver driver;

	public WebDriver getDriver() {
		return DriverSelenium.driver;
	}

	public void esperarElementoEsteVisibleById(String id) {
		final WebDriverWait wait = new WebDriverWait(DriverSelenium.driver, 110);
		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(id))).click();
	}

}
