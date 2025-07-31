package entity;

/**
 * Registry of built‑in user factories.
 * Each enum constant is a thread‑safe singleton that delegates to
 * its concrete factory implementation.
 */
public enum BuiltInUserFactory implements UserFactory {

    /** Factory for plain users without security questions. */
    COMMON(new DefaultUserFactory()),

    /** Factory for users that include a security question / answer. */
    SECURITY(new SecurityUserFactory());

    private final UserFactory delegate;

    BuiltInUserFactory(UserFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public User create(String name, String password) {
        return delegate.create(name, password);
    }

    @Override
    public User create(String name, String password,
                       String securityQuestion,
                       String securityAnswer) {
        return delegate.create(name, password,
                securityQuestion, securityAnswer);
    }
}
