package fintech_hw;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.xml.internal.ws.api.ResourceLoader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.URL;
import java.util.*;

import static org.apache.http.HttpHeaders.USER_AGENT;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Helper {



    public void writeToExcel(ArrayList<User> UserMas, int count, String[] Header)throws Exception{
        Workbook Excel = new HSSFWorkbook();
        Sheet Sheet = Excel.createSheet("MyWork");
        FileOutputStream fos = new FileOutputStream("my.xls");
        try {

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
                    row.createCell(5).setCellValue(String.format("%s%s%s%s%s",gc.get(Calendar.DAY_OF_MONTH), ".",(gc.get(Calendar.MONTH)+1),".",gc.get(Calendar.YEAR)));
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
            System.out.println(String.format("%s%s%s%s","Файл создан. Путь:",System.getProperty("user.dir"), File.separator,"my.xls"));
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            fos.close();
        }
    }
    public void writeToPdf(ArrayList<fintech_hw.User> UserMas, int count, String[] Header) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("my.pdf"));
            document.open();
            BaseFont bf =
                    BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf);

            PdfPTable table = new PdfPTable(13);
            table.setWidthPercentage(100);
            table.setSpacingBefore(0f);
            table.setSpacingAfter(0f);

            for (int i = 0; i < 13; i++) {
                table.addCell(new Paragraph(Header[i], font));
            }
            for (int i = 0; i < count; i++) {
                table.addCell(new Paragraph(UserMas.get(i).getLastName(), font));
                table.addCell(new Paragraph(UserMas.get(i).getFirstName(), font));
                table.addCell(new Paragraph(UserMas.get(i).getTitle(), font));
                table.addCell(new Paragraph(String.valueOf(UserMas.get(i).getAge()), font));
                table.addCell(new Paragraph(UserMas.get(i).getGender(), font));
                GregorianCalendar gc = UserMas.get(i).getDob();
                table.addCell(new Paragraph(String.format("%s%s%s%s%s",gc.get(Calendar.DAY_OF_MONTH),"." ,(gc.get(Calendar.MONTH)+1),".",gc.get(Calendar.YEAR))));
                table.addCell(new Paragraph(UserMas.get(i).getInn(), font));
                table.addCell(new Paragraph(UserMas.get(i).getPostcode()));
                table.addCell(new Paragraph(UserMas.get(i).getState(), font));
                table.addCell(new Paragraph(UserMas.get(i).getCity(), font));
                table.addCell(new Paragraph(UserMas.get(i).getStreet(), font));
                table.addCell(new Paragraph(UserMas.get(i).getHouse(), font));
                table.addCell(new Paragraph(String.valueOf(UserMas.get(i).getFlat()), font));

            }

            document.add(table);
            document.close();
            System.out.println(String.format("%s%s%s%s", "Файл создан. Путь:", System.getProperty("user.dir"), File.separator, "my.pdf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList getNumbers(int countPart, int count) {
        ArrayList<Integer> ArrayRandom = new ArrayList();
        int i = 0;

        while (i < countPart) {
            Random rand = new Random();
            int x = 1 + rand.nextInt(count);
            if (!ArrayRandom.contains(x)) {
                ArrayRandom.add(x);
                ++i;
            }
        }

        return ArrayRandom;
    }


    public ArrayList getLineByLine(ArrayList Numbers, String filename) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(beginReading(filename));
        ArrayList<String> Lines = new ArrayList();

        for (Integer pointLine = 1; bufferedReader.ready(); pointLine = pointLine + 1) {
            String line = bufferedReader.readLine();
            boolean f = false;

            for (int i = 0; i < Numbers.size(); ++i) {
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

    private BufferedReader beginReading(String filename) throws Exception {
        File file = new File(filename);
        String encoding = System.getProperty("console.encoding", "Cp1251");
        Reader fileReader = new InputStreamReader(new FileInputStream(file), encoding);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }

    public void getUser(fintech_hw.User user, int constCount) throws Exception {
        String dir = System.getProperty("user.dir");
        user.setState(fintech_hw.User.getAddress(String.format("%s%s%s",dir, "\\resources\\", "Country.txt"), constCount));
        user.setCity(fintech_hw.User.getAddress(String.format("%s%s%s",dir,"\\resources\\","City.txt"), constCount));
        user.setStreet(fintech_hw.User.getAddress(String.format("%s%s%s",dir, "\\resources\\", "Street.txt"), constCount));
        user.setPostcode();
        user.setHouse();
        user.setFlat();
        user.setINN();
        user.setDayOfBirth();
        user.setAge(user.getDob());
    }

    public HttpResponse getResponse() throws Exception {
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

    public String streamToString(HttpResponse res) throws Exception {
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

    public JSONObject StringToJson(String json) throws Exception {
        try {
            JSONObject obj = (JSONObject) JSONValue.parse(json);
            JSONArray JsonResults = (JSONArray) obj.get("results");
            JSONObject JResults = (JSONObject) JsonResults.get(0);
            return JResults;
        } catch (Exception e) {

        }
        return null;
    }
    public UserApi mapUser(String json)throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json).get("results");
        UserApi userApi = new UserApi();
        try{
            UserApi userApi1 = mapper.readValue(jsonNode.get(0).toString(),UserApi.class);
            userApi = userApi1;
            return userApi;
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return userApi;
        }
    }

}
