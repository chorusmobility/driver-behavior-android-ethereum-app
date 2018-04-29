package demo.technology.chorus.chorusdemo.model;

public class PrivateInfo {
    private String name;
    private String surname;
    private int age;
    private boolean gender;

    public PrivateInfo(String name) {
        this.name = name;
    }

    public PrivateInfo(String name, String surname, int age, boolean gender) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public boolean isGender() {
        return gender;
    }
}
