package entity;

/** Produces CommonUser instances. */
public final class CommonUserFactory implements UserFactory {

    @Override
    public User create(String name, String password) {
        return new CommonUser(name, password);
    }

    @Override
    public User create(String name, String password,
                       String securityQuestion,
                       String securityAnswer) {
        throw new UnsupportedOperationException(
                "CommonUser does not support security questions.");
    }
}
