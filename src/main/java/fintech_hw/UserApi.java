package fintech_hw;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.codehaus.jackson.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserApi implements AutoCloseable{
    private String gender;
    private Map<String, String> name;
    private Map<Object, Object> location;
    private Map<String, String> dob;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public Map<Object, Object> getLocation() {
        return location;
    }

    public void setLocation(Map<Object, Object> location) {
        this.location = location;
    }

    public Map<String, String> getDob() {
        return dob;
    }

    public void setDob(Map<String, String> dob) {
        this.dob = dob;
    }

    public User getUserApi() throws Exception {
        SimpleDateFormat dataFormat =new SimpleDateFormat("YYYY-MM-dd");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dataFormat.parse(dob.get("date")));
        User user = new User();
        user.setFirstName(name.get("first"));
        user.setLastName(name.get("last"));
        user.setTitle(name.get("title"));
        user.setGender(gender);
        user.setCity((String)location.get("city"));
        user.setState(String.valueOf(location.get("state")));
        user.setStreet(String.valueOf(location.get("street")));
        user.setPostcode(String.valueOf(location.get("postcode")));
        user.setAGE(Integer.parseInt(dob.get("age")));
        user.setHouse();
        user.setFlat();
        user.setINN();
        user.setDob(calendar);
        return user;
    }
    public void close(){
        this.setDob(null);
        this.setGender(null);
        this.setName(null);
        this.setLocation(null);
    }

}
