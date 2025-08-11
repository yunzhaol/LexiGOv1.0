package use_case.change_password.make_password_change;

import entity.User;
import entity.UserFactory;
import entity.dto.CommonUserDto;
import entity.dto.SecurityUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure JUnit 5 tests (NO Mockito).
 * - We avoid mocking final methods by NOT mocking at all.
 * - DAO/Factories/Presenter are minimal hand-written stubs.
 * - User is created via JDK Proxy if it's an interface (works without inline).
 * - MakePasswordChangeInputData is instantiated via reflection to support:
 *      a) public/protected/package ctor(String, String, String), OR
 *      b) builder()...build(), OR
 *      c) no-arg + setters/fields.
 */
class MakePasswordChangeInteractor2Test {

    private StubPresenter presenter;
    private StubUserDao userDao;
    private StubCommonFactory commonFactory;
    private StubSecurityFactory securityFactory;

    private MakePasswordChangeInteractor interactor;

    @BeforeEach
    void setUp() {
        presenter = new StubPresenter();
        userDao = new StubUserDao();
        commonFactory = new StubCommonFactory();
        securityFactory = new StubSecurityFactory();
        interactor = new MakePasswordChangeInteractor(presenter, userDao, commonFactory, securityFactory);
    }

    /* ======================= helpers ======================= */

    private static MakePasswordChangeInputData input(String username, String newPwd, String securityAnswer) {
        try {
            // 1) Try any (String,String,String) constructor
            for (Constructor<?> c : MakePasswordChangeInputData.class.getDeclaredConstructors()) {
                if (c.getParameterCount() == 3 &&
                        Arrays.stream(c.getParameterTypes()).allMatch(t -> t == String.class)) {
                    c.setAccessible(true);
                    return (MakePasswordChangeInputData) c.newInstance(username, newPwd, securityAnswer);
                }
            }

            // 2) Try builder(): username(...).newPassword(...).securityAnswer(...).build()
            try {
                Method builder = MakePasswordChangeInputData.class.getMethod("builder");
                Object b = builder.invoke(null);
                tryInvoke(b, "username", username);
                tryInvoke(b, "name", username); // in case it's named "name"
                tryInvoke(b, "newPassword", newPwd);
                tryInvoke(b, "password", newPwd); // fallback
                tryInvoke(b, "securityAnswer", securityAnswer);
                Method build = b.getClass().getMethod("build");
                return (MakePasswordChangeInputData) build.invoke(b);
            } catch (NoSuchMethodException ignore) {
                // fall through
            }

            // 3) Try no-arg + setters, or set fields directly
            Constructor<?> c0 = MakePasswordChangeInputData.class.getDeclaredConstructor();
            c0.setAccessible(true);
            Object obj = c0.newInstance();
            if (!tryInvoke(obj, "setUsername", username)) {
                setField(obj, "username", username);
                setField(obj, "name", username);
            }
            if (!tryInvoke(obj, "setNewPassword", newPwd)) {
                setField(obj, "newPassword", newPwd);
                setField(obj, "password", newPwd);
            }
            if (!tryInvoke(obj, "setSecurityAnswer", securityAnswer)) {
                setField(obj, "securityAnswer", securityAnswer);
            }
            return (MakePasswordChangeInputData) obj;

        } catch (Exception e) {
            throw new RuntimeException("Cannot construct MakePasswordChangeInputData for tests", e);
        }
    }

    private static boolean tryInvoke(Object target, String method, Object arg) {
        try {
            Method m = target.getClass().getMethod(method, arg == null ? String.class : arg.getClass());
            m.setAccessible(true);
            m.invoke(target, arg);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static void setField(Object target, String field, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception ignored) {
            // best effort; OK if field doesn't exist
        }
    }

    /** Create a User without mocking: if User is an interface, use a Proxy; otherwise, try a minimal subclass/ctor. */
    private static User userWithPassword(String pwd) {
        if (User.class.isInterface()) {
            InvocationHandler h = (proxy, method, args) -> {
                String name = method.getName();
                if ("getPassword".equals(name)) return pwd;
                if ("getName".equals(name)) return null; // common accessor some User interfaces have
                Class<?> rt = method.getReturnType();
                if (rt == void.class) return null;
                if (rt.isPrimitive()) {
                    if (rt == boolean.class) return false;
                    if (rt == byte.class) return (byte) 0;
                    if (rt == short.class) return (short) 0;
                    if (rt == int.class) return 0;
                    if (rt == long.class) return 0L;
                    if (rt == float.class) return 0f;
                    if (rt == double.class) return 0d;
                    if (rt == char.class) return '\0';
                }
                return null;
            };
            return (User) Proxy.newProxyInstance(User.class.getClassLoader(), new Class<?>[]{User.class}, h);
        } else {
            // If User is a class: try to find a no-arg ctor and override getPassword via anonymous subclass
            try {
                Constructor<?> ctor = User.class.getDeclaredConstructor();
                ctor.setAccessible(true);
                Object base = ctor.newInstance();
                return new User() {
                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override public String getPassword() { return pwd; }
                    // add no-op overrides here if User has abstract methods
                };
            } catch (Exception e) {
                throw new RuntimeException("Provide a concrete way to instantiate entity.User for tests.", e);
            }
        }
    }

    /* ======================= tests (12) ======================= */

    // 1) New password is empty -> failure; no update; no question lookup
    @Test
    void blankPassword_fails() {
        userDao.userToReturn = userWithPassword("old");
        interactor.makePasswordChange(input("u", "", null));

        assertEquals(1, presenter.failCount);
        assertEquals(0, userDao.updateCount);
        assertEquals(0, userDao.getQuestionCount);
        assertEquals(0, commonFactory.createCount);
        assertEquals(0, securityFactory.createCount);
    }

    // 2) New password equals old -> failure
    @Test
    void samePassword_fails() {
        userDao.userToReturn = userWithPassword("old");
        interactor.makePasswordChange(input("u", "old", null));

        assertEquals(1, presenter.failCount);
        assertEquals(0, userDao.updateCount);
    }

    // 3) No security question -> success using Common factory
    @Test
    void nonSecurityUser_success_updatesWithCommonFactory() {
        userDao.userToReturn = userWithPassword("old");
        userDao.questionToReturn = "";
        commonFactory.userToReturn = userWithPassword("newCommon");

        interactor.makePasswordChange(input("u", "new", null));

        assertEquals(1, presenter.successCount);
        assertEquals(1, commonFactory.createCount);
        assertEquals(0, securityFactory.createCount);
        assertEquals(1, userDao.updateCount);
        assertSame(commonFactory.userToReturn, userDao.updatedUser);
        assertEquals("u", userDao.updatedUsername);
        assertEquals(0, userDao.getAnswerCount);
    }

    // 4) Has security question but wrong answer -> failure; no update
    @Test
    void securityUser_wrongAnswer_fails() {
        userDao.userToReturn = userWithPassword("old");
        userDao.questionToReturn = "Q1";
        userDao.answerToReturn = "A1";

        interactor.makePasswordChange(input("u", "new", "A2"));

        assertEquals(1, presenter.failCount);
        assertEquals(0, userDao.updateCount);
        assertEquals(0, commonFactory.createCount);
        assertEquals(0, securityFactory.createCount);
        assertEquals(1, userDao.getAnswerCount);
    }

    // 5) Has security question and correct answer -> success using Security factory
    @Test
    void securityUser_correctAnswer_success_updatesWithSecurityFactory() {
        userDao.userToReturn = userWithPassword("old");
        userDao.questionToReturn = "Q1";
        userDao.answerToReturn = "A1";
        securityFactory.userToReturn = userWithPassword("newSec");

        interactor.makePasswordChange(input("u", "new", "A1"));

        assertEquals(1, presenter.successCount);
        assertEquals(0, commonFactory.createCount);
        assertEquals(1, securityFactory.createCount);
        assertEquals(1, userDao.updateCount);
        assertSame(securityFactory.userToReturn, userDao.updatedUser);
    }

    // 6) New password is whitespace-only -> failure (String#isBlank)
    @Test
    void whitespacePassword_isBlank_fails() {
        userDao.userToReturn = userWithPassword("old");
        interactor.makePasswordChange(input("u", "   ", null));

        assertEquals(1, presenter.failCount);
        assertEquals(0, userDao.updateCount);
    }

    // 7) Old password is null + no security question -> success
    @Test
    void oldPasswordNull_nonSecurity_success() {
        userDao.userToReturn = userWithPassword(null);
        userDao.questionToReturn = "";
        commonFactory.userToReturn = userWithPassword("newCommon");

        interactor.makePasswordChange(input("u", "new", null));

        assertEquals(1, presenter.successCount);
        assertEquals(1, userDao.updateCount);
        assertEquals(0, userDao.getAnswerCount);
    }

    // 8) When validation fails, getQuestion must NOT be called
    @Test
    void whenValidationFails_getQuestionNotCalled() {
        userDao.userToReturn = userWithPassword("old");
        interactor.makePasswordChange(input("u", "", null));

        assertEquals(0, userDao.getQuestionCount);
    }

    // 9) No-security path must NOT call getAnswer
    @Test
    void nonSecurityUser_doesNotCallGetAnswer() {
        userDao.userToReturn = userWithPassword("old");
        userDao.questionToReturn = "";
        commonFactory.userToReturn = userWithPassword("x");

        interactor.makePasswordChange(input("u", "new", null));

        assertEquals(0, userDao.getAnswerCount);
    }

    // 10) Security path must call getAnswer exactly once
    @Test
    void securityUser_callsGetAnswerOnce() {
        userDao.userToReturn = userWithPassword("old");
        userDao.questionToReturn = "Q1";
        userDao.answerToReturn = "A1";
        securityFactory.userToReturn = userWithPassword("y");

        interactor.makePasswordChange(input("u", "new", "A1"));

        assertEquals(1, userDao.getAnswerCount);
    }

    // 11) No-security path must NOT use Security factory
    @Test
    void nonSecurityUser_doesNotUseSecurityFactory() {
        userDao.userToReturn = userWithPassword("old");
        userDao.questionToReturn = "";
        commonFactory.userToReturn = userWithPassword("z");

        interactor.makePasswordChange(input("u", "new", null));

        assertEquals(0, securityFactory.createCount);
        assertEquals(1, commonFactory.createCount);
    }

    // 12) Security path must NOT use Common factory
    @Test
    void securityUser_doesNotUseCommonFactory() {
        userDao.userToReturn = userWithPassword("old");
        userDao.questionToReturn = "Q1";
        userDao.answerToReturn = "A1";
        securityFactory.userToReturn = userWithPassword("s");

        interactor.makePasswordChange(input("u", "new", "A1"));

        assertEquals(0, commonFactory.createCount);
        assertEquals(1, securityFactory.createCount);
    }

    /* ======================= stubs ======================= */

    private static final class StubPresenter implements MakePasswordChangeOutputBoundary {
        int successCount;
        int failCount;

        @Override public void presentSuccess() { successCount++; }

        @Override public void presentFailure(MakePasswordChangeOutputData data) { failCount++; }
    }

    private static final class StubUserDao implements UserPasswordDataAccessInterface {
        User userToReturn;
        String questionToReturn;
        String answerToReturn;

        int updateCount;
        String updatedUsername;
        User updatedUser;

        int getQuestionCount;
        int getAnswerCount;

        @Override public User get(String username) { return userToReturn; }

        @Override public void update(String username, User newUser) {
            updateCount++; updatedUsername = username; updatedUser = newUser;
        }

        @Override public String getQuestion(String username) {
            getQuestionCount++; return questionToReturn;
        }

        @Override public String getAnswer(String username) {
            getAnswerCount++; return answerToReturn;
        }
    }

    private static final class StubCommonFactory implements UserFactory<CommonUserDto> {
        int createCount;
        User userToReturn;

        @Override public User create(CommonUserDto dto) {
            createCount++; return userToReturn;
        }
    }

    private static final class StubSecurityFactory implements UserFactory<SecurityUserDto> {
        int createCount;
        User userToReturn;

        @Override public User create(SecurityUserDto dto) {
            createCount++; return userToReturn;
        }
    }
}
