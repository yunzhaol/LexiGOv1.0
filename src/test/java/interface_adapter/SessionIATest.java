package interface_adapter;


import interface_adapter.session.LoggedInState;
import interface_adapter.session.LoggedInViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

class SessionIATest {

    // ============================================================
    // Tests for LoggedInState (default ctor, copy ctor, getters/setters)
    // ============================================================
    @Test
    void defaultConstructorInitializesEmptyFields() {
        LoggedInState s = new LoggedInState();
        assertAll("defaults",
                () -> assertEquals("", s.getUsername(),   "username should default to empty"),
                () -> assertEquals("", s.getPassword(),   "password should default to empty"),
                () -> assertNull(s.getPasswordError(),    "passwordError should default to null")
        );
    }

    @Test
    void copyConstructorDuplicatesAllFields() {
        LoggedInState orig = new LoggedInState();
        orig.setUsername("u");
        orig.setPassword("p");
        orig.setPasswordError("err");

        LoggedInState copy = new LoggedInState(orig);
        assertAll("copy ctor",
                () -> assertEquals("u",   copy.getUsername()),
                () -> assertEquals("p",   copy.getPassword()),
                () -> assertEquals("err", copy.getPasswordError())
        );
    }

    @Test
    void settersAndGettersWork() {
        LoggedInState s = new LoggedInState();
        s.setUsername("user1");
        s.setPassword("secret");
        s.setPasswordError("oops");

        assertAll("set/get",
                () -> assertEquals("user1", s.getUsername()),
                () -> assertEquals("secret", s.getPassword()),
                () -> assertEquals("oops",   s.getPasswordError())
        );
    }

    // ============================================================
    // Tests for LoggedInViewModel (initial viewName & setState firing)
    // ============================================================
    private LoggedInViewModel vm;
    private TestListener listener;

    @BeforeEach
    void initViewModel() {
        vm = new LoggedInViewModel();
        listener = new TestListener();
        vm.addPropertyChangeListener(listener);
    }

    @Test
    void constructorSetsViewNameAndInitialState() {
        assertAll("constructor wiring",
                () -> assertEquals("logged in", vm.getViewName(), "viewName should be 'logged in'"),
                () -> assertNotNull(vm.getState(), "initial state must not be null"),
                () -> {
                    LoggedInState s = vm.getState();
                    assertAll("state defaults",
                            () -> assertEquals("", s.getUsername()),
                            () -> assertEquals("", s.getPassword()),
                            () -> assertNull(s.getPasswordError())
                    );
                }
        );
    }

    @Test
    void setStateReplacesStateAndFiresEvent() {
        LoggedInState newState = new LoggedInState();
        newState.setUsername("x");

        vm.setState(newState);  // should fire a PropertyChangeEvent internally
        vm.firePropertyChanged("logged in");

        assertSame(newState, vm.getState(), "setState should replace the state");
        assertTrue(listener.fired,            "setState should notify listeners");
    }

    // ------------------------------------------------------------
    // Helper listener to catch PropertyChangeEvent
    // ------------------------------------------------------------
    private static class TestListener implements PropertyChangeListener {
        boolean fired = false;
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fired = true;
        }
    }
}


