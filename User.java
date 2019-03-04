
import org.json.simple.JSONObject;

public class User {
    private String gender;

    private String title;
    private String firstName;
    private String lastName;

    private String city;
    private String street;
    private String state;
    private String house;
    private long postcode;

    private String dob;
    private long age;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public long getPostcode() {
        return postcode;
    }

    public void setPostcode(long postcode) {
        this.postcode = postcode;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }
    public User getUser(JSONObject obj)throws Exception{
        User Ans = new User();
        String gender = (obj.get("gender")).toString();
        Ans.setGender(gender);
        JSONObject name = (JSONObject) obj.get("name");
        String title = (name.get("title")).toString();
        Ans.setTitle(title);
        String first = (name.get("first")).toString();
        Ans.setFirstName(first);
        String last = (name.get("last")).toString();
        Ans.setLastName(last);
        JSONObject location = (JSONObject)obj.get("location");
        String city = (location.get("city")).toString();
        Ans.setCity(city);
        String state = (location.get("state")).toString();
        Ans.setCity(state);
        long postcode = Long.parseLong((location.get("postcode")).toString());
        Ans.setPostcode(postcode);
        String streetHouse = (location.get("street")).toString();
        char [] house = new char[20];
        streetHouse.getChars(0,streetHouse.indexOf(' '),house,0);
        Ans.setHouse(String.copyValueOf(house));
        char [] street = new char[100];
        streetHouse.getChars(streetHouse.indexOf(' ')+1,streetHouse.length(),street,0);
        Ans.setStreet(String.copyValueOf(street));
        JSONObject dob = (JSONObject)obj.get("dob");
        String date = (dob.get("date")).toString();
        char [] dateOfBirth = new char[10];
        date.getChars(0,date.indexOf('T'),dateOfBirth,0);
        Ans.setDob(String.copyValueOf(dateOfBirth));
        long age = (long)(dob.get("age"));
        Ans.setAge(age);
        return Ans;
    }
}
