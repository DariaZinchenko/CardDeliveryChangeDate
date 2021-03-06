public class Person {
    private final String name;
    private final String phone;
    private final String city;

    public Person(String name, String phone, String city) {
        this.city = city;
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }
}