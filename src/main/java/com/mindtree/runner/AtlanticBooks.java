package com.mindtree.runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mindtree.utility.RetreiveExcelData;
import com.mindtree.pageObjects.AddToCardPage;
import com.mindtree.pageObjects.HomePage;
import com.mindtree.pageObjects.SearchBoxPage;
import com.mindtree.pageObjects.SelectProductPage;
import com.mindtree.pageObjects.ShopingCardPage;
import com.mindtree.pageObjects.SignInPage;
import com.mindtree.reusableComponent.WebDriverHelper;
import com.mindtree.utility.ReadPropertyFile;

public class AtlanticBooks {

	Logger log = LogManager.getLogger(AtlanticBooks.class.getName());

	WebDriver driver = null;
	ReadPropertyFile rp = null;

	/*
	 * this method is responsible for landing at home page and click on sign button.
	 * 
	 * WebDriverHelper class has a initializeDriver() method which will launch and
	 * initialize the driver.
	 * 
	 * ReadPropertyFile class :- contains method which is used to retrieve data from
	 * property file.
	 * 
	 * HomePage class contains method to return webelement of respective web page;
	 */
	@Test(priority = 1)
	public void homePageLanding() throws Exception {

		rp = new ReadPropertyFile();

		driver = WebDriverHelper.initializeDriver();

		driver.get(rp.getUrl());
		System.out.println("url hitted");

		log.info("landing at home page");

		HomePage hp = new HomePage(driver);

		hp.getSignIn().click();
		log.info("clicked on sign button");

		Assert.assertTrue(true);

	}
	
	

	/*
	 * this method is responsible for sending username and password to login form.
	 * 
	 * ReadPropertyFile class :- contains method which is used to retrieve data from
	 * property file.
	 * 
	 * SignInPage class contains method to return webelement of after after sign
	 * page;
	 * 
	 * Data provider attribute is being used to call datasupplier method to return
	 * data combination one by one.
	 */
	@Test(priority = 2, dataProvider = "dataSuplier")
	public void signInTesting(String username, String password) throws Exception {

		SignInPage sp = new SignInPage(driver);

		sp.getUserName().clear();
		sp.getUserName().sendKeys(username);
		log.info(username + " entered");
		sp.getPassword().clear();
		sp.getPassword().sendKeys(password);
		log.info(password + " entered");
		sp.getSignInSmmitButton().click();
		log.info("clicked on summit button");
		
	
		
		

	}
	
	
	

	/*
	 * this method is responsible for sending product name has to be searched.
	 * 
	 * ReadPropertyFile class :- contains method which is used to retrieve data from
	 * property file.
	 * 
	 * SignInPage class contains method to return webElement of after after sign
	 * page;
	 * 
	 * 
	 * RetreiveExcelData class contains a method getData which will return array
	 * list containing search product. and product list is available in excel sheet.
	 * 
	 */
	@Test(priority = 3)
	public void searchProductTesting() throws IOException {

		ArrayList<String> d = RetreiveExcelData.getData("Book_Service");

		SearchBoxPage sb = new SearchBoxPage(driver);

		// System.out.println("items "+d.size() + d.get(d.size()-1));

		for (int i = 1; i < d.size(); i++) {
			sb.getSearchBox().sendKeys(d.get(i));
			sb.getSearchBox().sendKeys(Keys.ENTER);
		}

		log.info("Entered Service Name");
	}
	
	

	/*
	 * this method is responsible for selecting a product after searching it.
	 * 
	 * SelectProductPage contains method to return webElement of select product
	 * page;
	 * 
	 * Here we are retrieving list of products and selecting first one produxt. 
	 * 
	 */
	@Test(priority = 4)
	public void selectProductTesting() throws InterruptedException {

		Thread.sleep(5000L);
		SelectProductPage sp = new SelectProductPage(driver);

		List<WebElement> lb = sp.getSelectProduct();

		if (lb.size() == 0)
			System.out.println("no books found");

		lb.get(0).click();

		log.info("selected serach product");

		Thread.sleep(5000L);
	}
	
	
	
	/*
	 * this method is responsible for adding product into card  after selecting  it.
	 * 
	 * and cross verity it by title that product added or not.
	 * 
	 * AddToCardPage contains method to return webElement of select product page.
	 */
	@Test(priority = 5)
	public void addToCardTesting() throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		AddToCardPage atc = new AddToCardPage(driver);

		atc.getAddToCard().click();

		Thread.sleep(5000L);

		Assert.assertTrue(atc.getTitleAfterAdding().getText().contains("You added"));

		log.info("product added into card successfully");

	}
	
	
	@Test(priority = 6)
	public void shopingCardTesting()
	{
		AddToCardPage atc = new AddToCardPage(driver);
		ShopingCardPage scp = new ShopingCardPage(driver); 
		
		atc.getShopingCard().click();
		
		atc.getViewCart().click();
		
		Assert.assertTrue(scp.getSummuryTitle().getText().equalsIgnoreCase("Summary"));
	}

	@AfterTest
	public void closeDriver() {
		driver.close();
		driver = null;
	}

	@DataProvider
	public Object[][] dataSuplier() {
		Object[][] usernameAndPassword = new Object[2][2];

		usernameAndPassword[0][0] = "Poly123@gmail.com";
		usernameAndPassword[0][1] = "Poly#4321";

		usernameAndPassword[1][0] = "Pulkit@gmail.com";
		usernameAndPassword[1][1] = "PULs@123";

		return usernameAndPassword;
	}

}
