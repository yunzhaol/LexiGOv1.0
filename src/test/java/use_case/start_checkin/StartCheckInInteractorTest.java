package use_case.start_checkin;

import data_access.*;
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

class StartCheckInInteractorTest {
    //create a test data )inputData
    // initialize all data access objects
    // give a fake presenter that does nothing but only receives output data and inside it assert output data should be xxxx
    // and then call the interacter method
    private JsonUserRecordDataAccessObject userRecordDataAccessObject;
    private JsonUserDataAccessObject userdata;
    private WordBookDataAccessObject bookgetter;
    private WordDataAccessObject wordgetter;
    private JsonUserProfileDAO userprofiledao;
    private InMemoryDeckDataAccessObejct cardDeck;
    private LearnWordsGenerator learnWordsGenerator;
    private DeepLAPIAdapter mockTranslator;
    private FreeDictionaryApiAdapter mockFreeDictionary;
    private WordDeckFactory factory;

    @BeforeEach
    void setUp() {
        try {
            userdata = new JsonUserDataAccessObject("src/test/resources/users.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bookgetter = new WordBookDataAccessObject("src/test/resources/wordbook.json");
        wordgetter = new WordDataAccessObject("src/test/resources/words.json");
        userprofiledao = new JsonUserProfileDAO("src/test/resources/");
        cardDeck = new InMemoryDeckDataAccessObejct();
        learnWordsGenerator = new LearnWordsGenerator();
        userRecordDataAccessObject = new JsonUserRecordDataAccessObject("src/test/resources/learn_record.json");

        mockTranslator = mock(DeepLAPIAdapter.class);
        Language lan = Language.ZH;
        when(mockTranslator.getTranslation("dream", lan)).thenReturn("梦想");
        when(mockTranslator.getTranslation("zeal", lan)).thenReturn("热情");
        when(mockTranslator.getTranslation("trust", lan)).thenReturn("信任");
        when(mockTranslator.getTranslation("truth", lan)).thenReturn("真理");
        assertEquals(lan.displayName(), "Chinese");

        mockFreeDictionary = mock(FreeDictionaryApiAdapter.class);
        when(mockFreeDictionary.getWordExample("dream")).thenReturn("No example found");
        when(mockFreeDictionary.getWordExample("zeal")).thenReturn("He spoke with great zeal.");
        when(mockFreeDictionary.getWordExample("trust")).thenReturn("She put her trust in him.");
        when(mockFreeDictionary.getWordExample("truth")).thenReturn("He always tells the truth.");


        factory = new CommonWordDeckFactory();
    }

    @Test
    void successTest() {
        StartCheckInInputData in3 = new StartCheckInInputData("tester", "2");

        StartCheckInOutputBoundary presenter = new StartCheckInOutputBoundary() {
            @Override
            public void prepareSuccessView(StartCheckInOutputData outputData) {
                assertNotNull(outputData);
                assertEquals(outputData.getUsername(), "tester");
                assertEquals(outputData.getTotalpage(), "2");
                assertFalse(outputData.isUseCaseFailed());
            }

            @Override
            public void prepareFailView(String errorMessage) {

            }

            @Override
            public void switchToDeckView() {
            }
        };

        StartCheckInInteractor interactor = new StartCheckInInteractor(userRecordDataAccessObject, bookgetter,
                cardDeck, wordgetter, userprofiledao,learnWordsGenerator,
                presenter, mockTranslator, mockFreeDictionary, factory);
        interactor.execute(in3);
    }

    @Test
    void failureTest() {
        StartCheckInInputData in0 = new StartCheckInInputData("tester", "0");
        // StartCheckInInputData inNegative = new StartCheckInInputData("tester", "-1");
        StartCheckInInputData in51 = new StartCheckInInputData("tester", "3");

        StartCheckInOutputBoundary presenter = new StartCheckInOutputBoundary() {

            @Override
            public void prepareSuccessView(StartCheckInOutputData outputData) {

            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(errorMessage, "No more words to learn");
            }

            @Override
            public void switchToDeckView() {

            }
        };
        StartCheckInInteractor interactor = new StartCheckInInteractor(userRecordDataAccessObject, bookgetter,
                cardDeck, wordgetter, userprofiledao,learnWordsGenerator,
                presenter, mockTranslator, mockFreeDictionary, factory);
        interactor.execute(in0);
        //interactor.execute(inNegative);
        interactor.execute(in51);
    }
}
