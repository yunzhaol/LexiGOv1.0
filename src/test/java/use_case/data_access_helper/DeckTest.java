package use_case.data_access_helper;

import data_access.InMemoryDeckDataAccessObejct;
import entity.CommonCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
public class DeckTest {


    private InMemoryDeckDataAccessObejct dao;

    @BeforeEach
    void setUp() {
        dao = new InMemoryDeckDataAccessObejct();
    }

    @Test
    void getText_returnsCorrectText() {
        CommonCard card = new CommonCard(UUID.randomUUID(),
                "apple",
                "苹果",
                "I eat an apple.");
        dao.save(() -> List.of(card));
        String result = dao.getText(0);
        assertEquals("apple", result);
    }

    @Test
    void getText_throwsException_whenIndexOutOfBounds() {
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> dao.getText(0));
        assertNotNull(exception.getMessage());
    }
}


