package use_case.start_checkin;

import entity.*;
import use_case.gateway.UserRecordDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Use‑case: start a daily (check‑in) learning session.
 * <p>
 * All dependencies are injected as interfaces; this class contains
 * only business rules and is completely framework‑agnostic.
 */
public class StartCheckInInteractor implements StartCheckInInputBoundary {

    /* ====== GATEWAYS / SERVICES (names kept as you defined) ====== */
    private final UserRecordDataAccessInterface userDataAccessObject;
    private final WordBookAccessInterface        wordBookAccessObject;
    private final UserCheckInDeckAccessInterface userDeckAccessObject;
    private final WordDataAccessInterface        wordDataAccessObject;
    private final UserProfileDataAccessInterface userProfileDataAccessObject;

    // outer algorithms
    private final LearnDeckGenerator             generator;

    // presenter
    private final StartCheckInOutputBoundary     presenter;

    // APIs
    private final WordTranslationAPI             translator;
    private final WordDetailAPI                  detailGenerator;

    // factories
    private final WordDeckFactory                wordDeckFactory;
    private final CommonCardFactory                commonCardFactory;

    /* ====== CONSTRUCTOR ====== */
    public StartCheckInInteractor(
            UserRecordDataAccessInterface  userDataAccessObject,
            WordBookAccessInterface        wordBookAccessObject,
            UserCheckInDeckAccessInterface userDeckAccessObject,
            WordDataAccessInterface        wordDataAccessObject,
            UserProfileDataAccessInterface userProfileDataAccessObject,
            LearnDeckGenerator             generator,
            StartCheckInOutputBoundary     presenter,
            WordTranslationAPI             translator,
            WordDetailAPI                  detailGenerator,
            WordDeckFactory wordDeckFactory, CommonCardFactory commonCardFactory) {

        this.userDataAccessObject        = userDataAccessObject;
        this.wordBookAccessObject        = wordBookAccessObject;
        this.userDeckAccessObject        = userDeckAccessObject;
        this.wordDataAccessObject        = wordDataAccessObject;
        this.userProfileDataAccessObject = userProfileDataAccessObject;
        this.generator                   = generator;
        this.presenter                   = presenter;
        this.translator                  = translator;
        this.detailGenerator             = detailGenerator;
        this.wordDeckFactory             = wordDeckFactory;
        this.commonCardFactory = commonCardFactory;
    }

    /* ====== MAIN BUSINESS METHOD ====== */
    @Override
    public void execute(StartCheckInInputData input) {

        /* 1. Load domain data */
        WordBook            wordBook = wordBookAccessObject.get();
        List<LearnRecord>   history  = userDataAccessObject.get(input.getUsername());

        /* 2. Check remaining words in the book */
        int learnedCount = 0;
        for (LearnRecord record : history) {
            learnedCount += record.getLearnedWordIds().size();
        }

        int remaining = wordBook.getWordIds().size() - learnedCount;
        if (remaining < Integer.parseInt(input.getLength())) {
            presenter.prepareFailView("No more words to learn");
            return;           // stop the use‑case on business failure
        }

        /* 3. Use strategy to pick word IDs */
        List<UUID> wordIds = generator.generate(
                wordBook,
                history,
                input.getLength()
        );

        /* 4. Build Card objects one by one (no stream API) */
        List<CommonCard> cards = new ArrayList<>();
        for (UUID wordId : wordIds) {
            cards.add(buildCard(wordId, input.getUsername()));
        }

        /* 5. Create deck via factory and persist */
        WordDeck deck = wordDeckFactory.create(cards);
        userDeckAccessObject.save(deck);

        /* 6. Inform presenter of success */
        presenter.prepareSuccessView(
                new StartCheckInOutputData(input.getLength(), false, input.getUsername())
        );
    }

    /* Helper: build a Card from a Word ID and user context */
    private CommonCard buildCard(UUID wordId, String username) {

        String text = wordDataAccessObject.get(wordId);

        Language targetLang =
                userProfileDataAccessObject.getLanguage(username);

        String translation = translator.getTranslation(text, targetLang);

        String example     = detailGenerator.getWordExample(text);

        CommonCard card =  commonCardFactory.create(wordId, text, translation, example);
        return card;
    }
}

