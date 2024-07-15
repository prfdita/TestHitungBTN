import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword as WebUIAbstractKeyword
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser('')

WebUI.navigateToUrl('https://www.btnproperti.co.id/tools/hitung-harga-properti')

int rowData

int autoSelect

for (rowData = 1; rowData <= findTestData('HitungProperti/TDHitungProperti').getRowNumbers(); rowData++) {
	
	long pemasukan = Long.parseLong(findTestData('HitungProperti/TDHitungProperti').getValue('Penghasilan', rowData))

    long pengeluaran = Long.parseLong(findTestData('HitungProperti/TDHitungProperti').getValue('Pengeluaran', rowData))
	
	if(pemasukan != '' && pengeluaran !=''){

		autoSelect = (new Random().nextInt(30) + 1)
		
		WebUI.focus(findTestObject('Page Hitung Harga Properti Maksimal BTN/txtPemasukan'))
		
		WebUI.setText(findTestObject('Page Hitung Harga Properti Maksimal BTN/txtPemasukan'), pemasukan.toString())

		WebUI.focus(findTestObject('Page Hitung Harga Properti Maksimal BTN/txtPengeluaran'))
			
		WebUI.setText(findTestObject('Page Hitung Harga Properti Maksimal BTN/txtPengeluaran'), pengeluaran.toString())
		
		WebUI.selectOptionByValue(findTestObject('Page Hitung Harga Properti Maksimal BTN/dropdownTahun'), autoSelect.toString(), 
			false)
		
		WebUI.scrollToElement(findTestObject('Page Hitung Harga Properti Maksimal BTN/dropdownTahun'), 30)
		
		def tpTestCase = findTestData('HitungProperti/TDHitungProperti').getValue('TypeTest', rowData)
		
		String nameCapture = 'TestHitungBTN - Script ' + tpTestCase + ' row ' + rowData
		
		if(tpTestCase == 'Positive') {
			WebUI.focus(findTestObject('Page Hitung Harga Properti Maksimal BTN/Button Hitung'))
			
			WebUI.click(findTestObject('Page Hitung Harga Properti Maksimal BTN/Button Hitung'))
						
			WebUI.focus(findTestObject('Page Hitung Harga Properti Maksimal BTN/txtPemasukan'))
			
			String txtHitungReal = WebUI.getText(findTestObject('Page Hitung Harga Properti Maksimal BTN/TextHasilKalkulasi'))

			long numHitungReal = Long.parseLong(txtHitungReal.replaceAll('[^\\d]', ''))

			long expectedHitung = ((pemasukan - pengeluaran) * (autoSelect * 12)) / 3

			
			try {
				assert numHitungReal == expectedHitung

				WebUI.takeScreenshot('D:\\'+ nameCapture +' - success.png')
				
			}
			catch (AssertionError e) {
				WebUI.takeScreenshot('D:\\'+ nameCapture +' - failed.png')
			} 
			
			
		} else {
			WebUI.focus(findTestObject('Page Hitung Harga Properti Maksimal BTN/txtPemasukan'))
			
			String actualTextValidasi = WebUI.getText(findTestObject('Page Hitung Harga Properti Maksimal BTN/msg Validasi Pengeluaran Lebih Besar'))
			
			try {
				assert actualTextValidasi == 'Isi kurang dari nilai sebelumnya'

				
			}
			catch (AssertionError e) {
				
			}
			
			try {
				WebUI.verifyElementNotClickable(findTestObject('Page Hitung Harga Properti Maksimal BTN/Button Hitung'))

				WebUI.takeScreenshot('D:\\'+ nameCapture +' - success.png')
			}
			catch (Exception e) {
				WebUI.takeScreenshot('D:\\'+ nameCapture +' - failed.png')
			} 
			
		}
		
		WebUI.refresh()
		
	} else {
		try {
			WebUI.verifyElementNotClickable(findTestObject('Page Hitung Harga Properti Maksimal BTN/Button Hitung'))

			WebUI.takeScreenshot('D:\\'+ nameCapture +' - success.png')
			
		}	catch (Exception e) {
		
			WebUI.takeScreenshot('D:\\'+ nameCapture +' - failed.png')
		
		}
	}
}

WebUI.closeBrowser(FailureHandling.CONTINUE_ON_FAILURE)