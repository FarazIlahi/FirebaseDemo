package aydin.firebasedemo;

public class Person {
    private String name;
    private int age;

    private String phoneNum;

    public Person(String name, int age, String pNum) {
        this.name = name;
        this.age = age;
        this.phoneNum = pNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNum(){
        return phoneNum;
    }

    public void setPhoneNum(String pNum){
        this.phoneNum = pNum;
    }

}
