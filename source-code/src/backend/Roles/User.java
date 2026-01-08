package backend.Roles;

public abstract class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;

    public User(int id,String name,String email,String phone,String password,String role){
        this.id=id;
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.password=password;
        this.role=role;

    }

    public int getId() {return id;}

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
