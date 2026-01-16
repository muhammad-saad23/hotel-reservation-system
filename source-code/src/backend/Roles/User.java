package backend.Roles;

public abstract class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;
    private String permissions;

    public User(int id, String name, String email, String phone, String password, String role, String permissions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.permissions = permissions;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public String getPermissions() {
        return (permissions == null || permissions.isEmpty()) ? "None" : permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }


    @Override
    public String toString() {
        return id + "," + name + "," + email + "," + phone + "," + password + "," + role + "," + getPermissions();
    }

    public abstract void showAccess();
}