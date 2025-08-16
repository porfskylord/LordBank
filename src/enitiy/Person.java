package enitiy;

import enitiy.enums.Gender;

public class Person {
    private String name;
    private int age;
    private Gender gender;
    private String address;

    protected Person(String name, int age, Gender gender, String address){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
    }

    protected Person(){
        this.name = "User";
        this.age = 18;
        this.gender = Gender.Male;
        this.address = "India";
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString(){
        return "Name: "+this.name+" | Age: "+this.age+" | Gender: "+this.gender+" | Address: "+this.address+"\n";
    }

    @Override
    public boolean equals (Object object){
         if(this == object) return true;

         if(!(object instanceof Person)) return false;

         Person p = (Person) object;
         return this.name.equals(p.name);
    }

    @Override
    public int hashCode(){
         return this.name.hashCode();
    }

//    @Override
//    public Person clone() throws CloneNotSupportedException{
//         return (Person) super.clone();
//    }
}
