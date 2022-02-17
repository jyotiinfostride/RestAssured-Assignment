import io.restassured.response.Response;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class RestAssure {
    String url = "https://api.stripe.com/v1/customers";
    String code = "sk_test_51KTmpaSAZ0t2le0ryLU5XrzLx1kmCitwIjeEoTGdjHjJNhNJycFLLodjcO4OaghKp4ROImradWaSYOgu3qa91e3F00yTPOqhqa";

    @DataProvider(name = "data")
    public Object[][] getdata() throws IOException {
        String path = System.getProperty("user.dir") + "\\Resorces\\data.xlsx";
//            FileReader file = new FileReader(path);
        FileInputStream fis = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int row = sheet.getLastRowNum();
        Object[][] arr = new Object[row][2];
        int a = 0;
        for (int i = 1; i < row+1; i++) {
            arr[a][0] = sheet.getRow(i).getCell(0).getStringCellValue();
            arr[a][1] = sheet.getRow(i).getCell(1).getStringCellValue();
            a++;
        }
        return arr;
    }

    @Test(dataProvider = "data")
    public void enterDataExcel(String name, String email) {
//        System.out.println(name+ "  "+ email);
        Response resp = given().auth().basic(code, "").formParam("email", email).
                formParam("name", name).when().post(url);
        int respcode = resp.getStatusCode();
        System.out.println("Responce code is: " + respcode);
        String responsebody = resp.getBody().asString();
        System.out.println(responsebody);
    }
}
