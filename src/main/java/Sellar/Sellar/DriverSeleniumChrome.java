package Sellar.Sellar;


public class DriverSeleniumChrome extends DriverSelenium {

	public DriverSeleniumChrome() {

		if (Sellar.getOs().equals("Linux")) {
			System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
		}

		else {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		}

	}
}
