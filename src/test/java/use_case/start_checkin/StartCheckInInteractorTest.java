package use_case.start_checkin;

import data_access.InMemoryDeckDataAccessObejct;
import data_access.JsonUserDataAccessObject;
import data_access.JsonUserProfileDAO;
import data_access.JsonUserRecordDataAccessObject;
import data_access.WordBookDataAccessObject;
import data_access.WordDataAccessObject;
import entity.CommonCardFactory;
import entity.CommonWordDeckFactory;
import entity.Language;
import entity.WordDeckFactory;
import infrastructure.DeepLAPIAdapter;
import infrastructure.FreeDictionaryApiAdapter;
import infrastructure.LearnWordsGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StartCheckInInteractor}.
 * <p>
 * Verifies both success and failure flows using mocked external adapters.
 */
class StartCheckInInteractorTest {

    private JsonUserRecordDataAccessObject userRecordDataAccessObject;
    private JsonUserDataAccessObject       userdata;
    private WordBookDataAccessObject       bookgetter;
    private WordDataAccessObject           wordgetter;
    private JsonUserProfileDAO             userprofiledao;
    private InMemoryDeckDataAccessObejct   cardDeck;
    private LearnWordsGenerator            learnWordsGenerator;
    private DeepLAPIAdapter                mockTranslator;
    private FreeDictionaryApiAdapter       mockFreeDictionary;
    private WordDeckFactory                factory;
    private CommonCardFactory              commonCardFactory;

    /**
     * Initialize all DAOs, adapters, and mocks before each test.
     */
    @BeforeEach
    void setUp() {
        try {
            userdata = new JsonUserDataAccessObject(
                    "src/test/resources/data/users.json"
            );
            userRecordDataAccessObject = new JsonUserRecordDataAccessObject(
                    "src/test/resources/data/learn_record.json"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bookgetter         = new WordBookDataAccessObject(
                "src/test/resources/data/wordbook.json"
        );
        wordgetter         = new WordDataAccessObject(
                "src/test/resources/data/words.json"
        );
        userprofiledao     = new JsonUserProfileDAO(
                "src/test/resources/data/"
        );
        cardDeck           = new InMemoryDeckDataAccessObejct();
        learnWordsGenerator = new LearnWordsGenerator();

        // Mock the translation API
        mockTranslator = mock(DeepLAPIAdapter.class);
        Language lan = Language.ZH;
        when(mockTranslator.getTranslation("dream", lan)).thenReturn("梦想");
        when(mockTranslator.getTranslation("zeal", lan)).thenReturn("热情");
        when(mockTranslator.getTranslation("trust", lan)).thenReturn("信任");
        when(mockTranslator.getTranslation("truth", lan)).thenReturn("真理");
        assertEquals("Chinese", lan.displayName());

        // Mock the dictionary API
        mockFreeDictionary = mock(FreeDictionaryApiAdapter.class);
        when(mockFreeDictionary.getWordExample("dream"))
                .thenReturn("No example found");
        when(mockFreeDictionary.getWordExample("zeal"))
                .thenReturn("He spoke with great zeal.");
        when(mockFreeDictionary.getWordExample("trust"))
                .thenReturn("She put her trust in him.");
        when(mockFreeDictionary.getWordExample("truth"))
                .thenReturn("He always tells the truth.");

        factory = new CommonWordDeckFactory();
    }

    /**
     * Scenario: valid page number leads to successful check-in.
     */
    @Test
    void successTest() {
        StartCheckInInputData in3 = new StartCheckInInputData("tester", "2");

        StartCheckInOutputBoundary presenter = new StartCheckInOutputBoundary() {
            @Override
            public void prepareSuccessView(StartCheckInOutputData outputData) {
                assertNotNull(outputData);
                assertEquals("tester", outputData.getUsername());
                assertEquals("2",      outputData.getTotalpage());
                assertFalse(outputData.isUseCaseFailed());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                // Not expected in success scenario
            }

            @Override
            public void switchToDeckView() {
                // No-op for this test
            }
        };

        StartCheckInInteractor interactor = new StartCheckInInteractor(
                userRecordDataAccessObject,
                bookgetter,
                cardDeck,
                wordgetter,
                userprofiledao,
                learnWordsGenerator,
                presenter,
                mockTranslator,
                mockFreeDictionary,
                factory,
                commonCardFactory
        );
        interactor.execute(in3);
    }

    /**
     * Scenario: page number out of range triggers failure callback.
     */
    @Test
    void failureTest() {
        StartCheckInInputData in0  = new StartCheckInInputData("tester", "0");
        StartCheckInInputData in51 = new StartCheckInInputData("tester", "3");

        StartCheckInOutputBoundary presenter = new StartCheckInOutputBoundary() {
            @Override
            public void prepareSuccessView(StartCheckInOutputData outputData) {
                // Not used in failure scenario
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("No more words to learn", errorMessage);
            }

            @Override
            public void switchToDeckView() {
                // Not relevant
            }
        };

        StartCheckInInteractor interactor = new StartCheckInInteractor(
                userRecordDataAccessObject,
                bookgetter,
                cardDeck,
                wordgetter,
                userprofiledao,
                learnWordsGenerator,
                presenter,
                mockTranslator,
                mockFreeDictionary,
                factory,
                commonCardFactory
        );
        interactor.execute(in0);
        interactor.execute(in51);
    }
}
