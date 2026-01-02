package backend.Roles;

public abstract class User {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;

    public User(String name,String email,String phone,String password,String role){
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.password=password;
        this.role=role;

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {return role;}

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

//    @Override
//    public String toString() {
//        return getName() + "," + getEmail() + "," + getPhone() + "," + getPassword() + ",Customer";
//    }


    public abstract void showAccess();
}
