/*
Класс => builder.
В процессе написания класса возникла необходимость этого шаблона.
Самый стандартный способ реализации.
https://habr.com/ru/post/244521/
*/
package sample.source;

public class User {
    private int id;
    private final String firstName;
    private final String lastName;
    private final String userName;
    private final String password;
    private final String location;
    private final String gender;

    public void setId(int id) {
        this.id = id;
    }

    private User(UserBuilder builder){
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.userName = builder.userName;
        this.password = builder.password;
        this.location = builder.location;
        this.gender = builder.gender;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getLocation() {
        return location;
    }

    public String getGender() {
        return gender;
    }

    public static class UserBuilder {
        private int id;
        private  String firstName;
        private  String lastName;
        private final String userName;
        private final String password;
        private String location;
        private String gender;

        public UserBuilder(String userName, String password){
            this.userName = userName;
            this.password = password;
        }

        public UserBuilder id (int id){
            this.id = id;
            return this;
        }

        public  UserBuilder firstName (String firstName){
            this.firstName = firstName;
            return this;
        }

        public  UserBuilder lastName (String lastName){
            this.lastName = lastName;
            return this;
        }

        public  UserBuilder location (String location){
            this.location = location;
            return this;
        }

        public  UserBuilder gender (String gender){
            this.gender = gender;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
