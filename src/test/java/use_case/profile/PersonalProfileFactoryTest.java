package use_case.profile;
import entity.PersonalProfile;
import entity.Language;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonalProfileFactoryTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String expectedUsername = "testUser";
        Language expectedLanguage = Language.EN;

        // Act
        PersonalProfile profile = new PersonalProfile(expectedUsername, expectedLanguage);

        // Assert
        assertEquals(expectedUsername, profile.getUsername());
        assertEquals(expectedLanguage, profile.getLanguage());
    }

    @Test
    void testDifferentLanguage() {
        // Arrange
        String expectedUsername = "anotherUser";
        Language expectedLanguage = Language.ZH;

        // Act
        PersonalProfile profile = new PersonalProfile(expectedUsername, expectedLanguage);

        // Assert
        assertNotEquals(Language.EN, profile.getLanguage());
        assertEquals(expectedLanguage, profile.getLanguage());
        assertEquals(expectedUsername, profile.getUsername());
    }
}
