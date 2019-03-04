package fintech_hw;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.annotation.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.io.*;
import java.lang.String;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.jna.platform.win32.Netapi32Util;
import com.sun.org.apache.xml.internal.utils.StringToIntTable;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.HttpEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.http.Header;
import org.json.simple.JSONValue;
import java.io.IOException;


import static org.apache.http.HttpHeaders.USER_AGENT;
public class example {
    public static void main(String[] args) throws Exception {
        Random rand = new Random();
        int count = 1 + rand.nextInt(30);
        ArrayList<fintech_hw.User> UserMas = new ArrayList<fintech_hw.User>();
        HttpResponse response = getResponse();
        String[] Header = new String[13];
        if (response == null){
            final int constCount = 30;
            int countMen = rand.nextInt(count + 1);
            int countWomen = count - countMen;
            String dir = System.getProperty("user.dir") + File.separator+"src"+File.separator +"main"+File.separator+"resources"+File.separator;
            ArrayList<Integer> NumbersFIOMen = new ArrayList(getNumbers(countMen,constCount));
            ArrayList<Integer> NumbersFIOWomen = new ArrayList(getNumbers(countWomen,constCount));
            ArrayList<Integer> NumbersAddress = new ArrayList(getNumbers(count, constCount));
            ArrayList<String> SurnameMen = new ArrayList<String>(getLineByLine(NumbersFIOMen,dir + "SurnameMen.txt"));
            ArrayList<String> NameMen = new ArrayList<String>(getLineByLine(NumbersFIOMen,dir + "NameMen.txt"));
            ArrayList<String> PatronymicMen = new ArrayList<String>(getLineByLine(NumbersFIOMen,dir + "PatronymicMen.txt"));
            ArrayList<String> SurnameWomen = new ArrayList<String>(getLineByLine(NumbersFIOWomen,dir + "SurnameWomen.txt"));
            ArrayList<String> NameWomen = new ArrayList<String>(getLineByLine(NumbersFIOWomen,dir + "NameWomen.txt"));
            ArrayList<String> PatronymicWomen = new ArrayList<String>(getLineByLine(NumbersFIOWomen,dir + "PatronymicWomen.txt"));
            for (int i = 0; i < count; i ++){
                UserMas.add(new fintech_hw.User());
                UserMas.get(i).setState(fintech_hw.User.getAddress(dir + "Country.txt", constCount));
                UserMas.get(i).setCity(fintech_hw.User.getAddress(dir +"City.txt", constCount));
                UserMas.get(i).setStreet(fintech_hw.User.getAddress(dir + "Street.txt",constCount));
                UserMas.get(i).setPostcode();
                UserMas.get(i).setHouse();
                UserMas.get(i).setFlat();
                UserMas.get(i).setINN();
                UserMas.get(i).setDayOfBirth();
                UserMas.get(i).setAge(UserMas.get(i).getDob());
            }
            for(int i = 0; i < countMen; i ++){
                UserMas.get(i).setLastName(SurnameMen.get(i));
                UserMas.get(i).setFirstName(NameMen.get(i));
                try {
                    UserMas.get(i).setTitle(PatronymicMen.get(i));
                }catch (IndexOutOfBoundsException e){}
                UserMas.get(i).setGender("M");
            }
            for(int i = 0;i < countWomen; i ++){
                UserMas.get(i+ countMen).setLastName(SurnameWomen.get(i));
                UserMas.get(i + countMen).setFirstName(NameWomen.get(i));
                try{
                UserMas.get(i + countMen).setTitle(PatronymicWomen.get(i));}
                catch (IndexOutOfBoundsException e){}
                UserMas.get(i + countMen).setGender("Ж");
            }
            Collections.shuffle(UserMas);
            Header = new String[]{"Фамилия", "Имя", "Отчество", "Возраст", "Пол", "Дата рождения", "ИНН", "Индекс", "Страна", "Город", "Улица", "Дом", "Квартира"};
        }
        else {
            for (int i = 0; i < count; i++) {
                String json = new String(streamToString(getResponse()));
                JSONObject results = new JSONObject((StringToJson(json)));
                fintech_hw.User user = new fintech_hw.User();
                String gender = String.valueOf(results.get("gender"));
                user.SetGender(gender);
                JSONObject name = (JSONObject) results.get("name");
                String title = String.valueOf(name.get("title"));
                user.setTitle(title);
                String first = String.valueOf(name.get("first"));
                user.setFirstName(first);
                String last = String.valueOf(name.get("last"));
                user.setLastName(last);
                JSONObject location = (JSONObject) results.get("location");
                String state = String.valueOf(location.get("state"));
                user.setState(state);
                String city = String.valueOf(location.get("city"));
                user.setCity(city);
                String postcode = String.valueOf(location.get("postcode"));
                user.setPostcode(postcode);
                String streetHouse = String.valueOf(location.get("street"));
                char[] House = new char[100];
                streetHouse.getChars(0, streetHouse.indexOf(' '), House, 0);
                user.setHouse(String.copyValueOf(House));
                char[] Street = new char[100];
                streetHouse.getChars(streetHouse.indexOf(" "),streetHouse.length(),Street,0);
                user.setStreet(String.copyValueOf(Street));
                JSONObject dob = (JSONObject) results.get("dob");
                String date = String.valueOf(dob.get("date"));
                char [] Year = new char[4];
                date.getChars(0,4,Year,0);
                int year = Integer.valueOf(String.copyValueOf(Year));
                char [] Month = new char[2];
                date.getChars(5,7,Month,0);
                int month = Integer.valueOf(String.copyValueOf(Month));
                char [] Day = new char[2];
                date.getChars(8,10,Day,0);
                int day = Integer.valueOf(String.copyValueOf(Day));
                GregorianCalendar gc = new GregorianCalendar(year,month,day);
                user.setDob(gc);
                int age = Integer.valueOf(String.valueOf(dob.get("age")));
                user.setAGE(age);
                user.setINN();
                user.setFlat();
                UserMas.add(user);
            }
            Header = new String[]{"Фамилия", "Имя", "Title", "Возраст", "Пол", "Дата рождения", "ИНН", "Индекс", "Страна", "Город", "Улица", "Дом", "Квартира"};
        }
        writeToExcel(UserMas, count, Header);
        writeToPdf(UserMas, count, Header);
    }
    private static ArrayList getNumbers(int countPart, int count) {
        ArrayList<Integer> ArrayRandom = new ArrayList();
        int i = 0;

        while(i < countPart) {
            Random rand = new Random();
            int x = 1 + rand.nextInt(count);
            if (!ArrayRandom.contains(x)) {
                ArrayRandom.add(x);
                ++i;
            }
        }

        return ArrayRandom;
    }
    private static ArrayList getLineByLine(ArrayList Numbers, String filename) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(beginReading(filename));
        ArrayList<String> Lines = new ArrayList();

        for(Integer pointLine = 1; bufferedReader.ready(); pointLine = pointLine + 1) {
            String line = bufferedReader.readLine();
            boolean f = false;

            for(int i = 0; i < Numbers.size(); ++i) {
                if (pointLine == Numbers.get(i)) {
                    f = true;
                }
            }

            if (f) {
                Lines.add(line);
            }
        }

        Collections.shuffle(Lines);
        return Lines;
    }
    private static BufferedReader beginReading(String filename) throws Exception {
        File file = new File(filename);
        String encoding = System.getProperty("console.encoding", "Cp1251");
        Reader fileReader = new InputStreamReader(new FileInputStream(file), encoding);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }
    private static void getUser(fintech_hw.User user, int constCount)throws Exception {
        String dir = System.getProperty("user.dir");
        user.setState(fintech_hw.User.getAddress(dir +"\\resources\\"+ "Country.txt", constCount));
        user.setCity(fintech_hw.User.getAddress(dir + "\\resources\\"+"City.txt", constCount));
        user.setStreet(fintech_hw.User.getAddress(dir + "\\resources\\" + "Street.txt",constCount));
        user.setPostcode();
        user.setHouse();
        user.setFlat();
        user.setINN();
        user.setDayOfBirth();
        user.setAge(user.getDob());
    }
    private static HttpResponse getResponse() throws Exception {
        try {
            String url = "https://randomuser.me/api/";
            URIBuilder builder = new URIBuilder(url);
            HttpClient client = HttpClients.createDefault();
            builder.setParameter("first", "0").setParameter("last", "20");
            HttpGet request = new HttpGet(builder.build());
            request.addHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request);
            return response;
        } catch (IOException e) {
        }
        return null;
    }

    private static String streamToString(HttpResponse res) throws Exception {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            reader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject StringToJson(String json) throws Exception {
        try {
            JSONObject obj = (JSONObject) JSONValue.parse(json);
            JSONArray JsonResults = (JSONArray) obj.get("results");
            JSONObject JResults = (JSONObject) JsonResults.get(0);
            return JResults;
        } catch (Exception e) {

        }
        return null;
    }
    private static void writeToPdf(ArrayList<fintech_hw.User> UserMas, int count, String[] Header){
        Document document = new Document();
        try{
            PdfWriter.getInstance(document,new FileOutputStream("my.pdf"));
            document.open();
            BaseFont bf =
                    BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf",BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
            Font font = new Font(bf);

            PdfPTable table = new PdfPTable(13);
            table.setWidthPercentage(100);
            table.setSpacingBefore(0f);
            table.setSpacingAfter(0f);

            for(int i = 0; i < 13; i ++){
                table.addCell(new Paragraph(Header[i],font));
            }
            for(int i = 0; i < count; i ++){
                table.addCell(new Paragraph(UserMas.get(i).getLastName(),font));
                table.addCell(new Paragraph(UserMas.get(i).getFirstName(),font));
                table.addCell(new Paragraph(UserMas.get(i).getTitle(),font));
                table.addCell(new Paragraph(String.valueOf(UserMas.get(i).getAge()),font));
                table.addCell(new Paragraph(UserMas.get(i).getGender(),font));
                GregorianCalendar gc = UserMas.get(i).getDob();
                table.addCell(new Paragraph(gc.get(Calendar.DAY_OF_MONTH) + "." + gc.get(Calendar.MONTH) + "." + gc.get(Calendar.YEAR)));
                table.addCell(new Paragraph(UserMas.get(i).getInn(),font));
                table.addCell(new Paragraph(UserMas.get(i).getPostcode()));
                table.addCell(new Paragraph(UserMas.get(i).getState(),font));
                table.addCell(new Paragraph(UserMas.get(i).getCity(),font));
                table.addCell(new Paragraph(UserMas.get(i).getStreet(),font));
                table.addCell(new Paragraph(UserMas.get(i).getHouse(),font));
                table.addCell(new Paragraph(String.valueOf(UserMas.get(i).getFlat()),font));

            }

            document.add(table);
            document.close();
            System.out.println("Файл создан. Путь:" + System.getProperty("user.dir") + File.separator + "my.pdf");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void writeToExcel(ArrayList<fintech_hw.User>UserMas, int count, String[] Header)throws Exception{
        Workbook Excel = new HSSFWorkbook();
        Sheet Sheet = Excel.createSheet("MyWork");
        try {
            FileOutputStream fos = new FileOutputStream("my.xls");
            for (int i = 0; i <= count; ++i) {
                Row row = Sheet.createRow(i);
                if (i == 0) {
                    for (int j = 0; j < 13; j++) {
                        row.createCell(j).setCellValue(Header[j]);
                    }
                } else {
                    row.createCell(0).setCellValue(UserMas.get(i - 1).getLastName());
                    row.createCell(1).setCellValue(UserMas.get(i - 1).getFirstName());
                    row.createCell(2).setCellValue(UserMas.get(i-1).getTitle());
                    row.createCell(3).setCellValue(UserMas.get(i - 1).getAge());
                    row.createCell(4).setCellValue(UserMas.get(i - 1).getGender());
                    GregorianCalendar gc = UserMas.get(i-1).getDob();
                    row.createCell(5).setCellValue(gc.get(Calendar.DAY_OF_MONTH) + "." + gc.get(Calendar.MONTH) + "." + gc.get(Calendar.YEAR));
                    row.createCell(6).setCellValue(UserMas.get(i - 1).getInn());
                    row.createCell(7).setCellValue(UserMas.get(i - 1).getPostcode());
                    row.createCell(8).setCellValue(UserMas.get(i - 1).getState());
                    row.createCell(9).setCellValue(UserMas.get(i - 1).getCity());
                    row.createCell(10).setCellValue(UserMas.get(i - 1).getStreet());
                    row.createCell(11).setCellValue(UserMas.get(i - 1).getHouse());
                    row.createCell(12).setCellValue(UserMas.get(i - 1).getFlat());
                }

            }
            Excel.write(fos);
            fos.close();
            System.out.println("Файл создан. Путь:" + System.getProperty("user.dir") + File.separator + "my.xls");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}