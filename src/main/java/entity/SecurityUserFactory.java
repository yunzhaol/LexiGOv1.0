package entity;

public class SecurityUserFactory implements UserFactory {
    @Override
    public User create(String name, String password) {
        return null;
    }

    @Override
    public User create(String name, String password, String securityQuestion, String securityAnswer) {
        return new SecurityUser(name, password, securityQuestion, securityAnswer);
    }
}
