package use_case.start_checkin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entity.Card;
import entity.CardFactory;
import entity.Language;
import entity.LearnRecord;
import entity.WordBook;
import entity.WordDeck;
import entity.WordDeckFactory;
import use_case.gateway.UserRecordDataAccessInterface;

/**
 * Use‑case: start a daily (check‑in) learning session.
 *
 * <p>
 * All dependencies are injected as interfaces; this class contains
 * only business rules and is completely framework‑agnostic.
 */
public class StartCheckInInteractor implements StartCheckInInputBoundary {

    /* ====== GATEWAYS / SERVICES (names kept as you defined) ====== */
    private final DaiDto daoDto;

    // outer algorithms
    private final LearnDeckGenerator generator;
    private final FormatDetector formatter;

    // presenter
    private final StartCheckInOutputBoundary presenter;

    // APIs
    private final WordTranslationAPI translator;
    private final WordDetailAPI detailGenerator;

    // factories
    private final FactoryDto factoryDto;

    /* ====== CONSTRUCTOR ====== */
    public StartCheckInInteractor(
            DaiDto daoDto,
            FactoryDto factoryDto,
            LearnDeckGenerator generator,
            StartCheckInOutputBoundary presenter,
            WordTranslationAPI translator,
            WordDetailAPI detailGenerator,
            FormatDetector formatProcessor) {

        this.daoDto = daoDto;
        this.generator = generator;
        this.presenter = presenter;
        this.translator = translator;
        this.detailGenerator = detailGenerator;
        this.factoryDto = factoryDto;
        this.formatter = formatProcessor;
    }

    /* ====== MAIN BUSINESS METHOD ====== */
    @Override
    public void execute(StartCheckInInputData input) {
        // Format detector, as it might change, put it out of interactor
        if (formatter.execute(input.getLength())) {
            /* 1. Load domain data */

            // get wordbook dao from dto
            final WordBookAccessInterface wordbookdao;
            final UserRecordDataAccessInterface userrecorddao;

            wordbookdao = daoDto.getWordBookAccessObject();
            userrecorddao = daoDto.getUserDataAccessObject();

            // get data from DAI(injected DAO)
            final WordBook wordBook = wordbookdao.get();
            final List<LearnRecord> history = userrecorddao.get(input.getUsername());

            /* 2. Check remaining words in the book */
            // Note: as logic might not be extended, there's no strategic relocation
            int learnedCount = 0;
            for (LearnRecord eachRecord : history) {
                learnedCount += eachRecord.getLearnedWordIds().size();
            }

            final int remaining = wordBook.getWordIds().size() - learnedCount;
            if (remaining < Integer.parseInt(input.getLength())) {
                // business logic: u cannot learn words u learnt before :)
                presenter.prepareFailView("No more words to learn");
            }
            else {
                /* 3. Use strategy to pick word IDs */
                // strategy might change, so put it outside
                final List<UUID> wordIds = generator.generate(
                        wordBook,
                        history,
                        input.getLength()
                );

                /* 4. Build Card objects one by one (no stream API) */
                final List<Card> cards = new ArrayList<>();
                for (UUID wordId : wordIds) {
                    cards.add(buildCard(wordId, input.getUsername()));
                }

                /* 5. Create deck via factory and persist */
                // get factory from dto
                final WordDeckFactory wordDeckFactory = factoryDto.getWordDeckFactory();

                // get dao(dai)
                final UserCheckInDeckAccessInterface deckAccess;
                deckAccess = daoDto.getUserDeckAccessObject();

                // factory logic && DAO logic
                final WordDeck deck = wordDeckFactory.create(cards);
                deckAccess.save(deck);

                /* 6. Inform presenter of success */
                presenter.prepareSuccessView(
                        new StartCheckInOutputData(input.getLength(), false, input.getUsername())
                );
            }
        }
        else {
            presenter.prepareFailView("You should input valid positive interger");
        }
    }

    /* Helper: build a Card from a Word ID and user context */
    private Card buildCard(UUID wordId, String username) {

        // get DAOs/Factories
        final WordDataAccessInterface wordDataAccess = daoDto.getWordDataAccessObject();
        final UserProfileDataAccessInterface userProfileDataAccess = daoDto.getUserProfileDataAccessObject();

        final CardFactory cardFactory = factoryDto.getCommonCardFactory();

        // DAO & Factory logic
        final String text = wordDataAccess.get(wordId);

        final Language targetLang =
                userProfileDataAccess.getLanguage(username);

        final String translation = translator.getTranslation(text, targetLang);

        final String example = detailGenerator.getWordExample(text);

        // get card entity and return
        return cardFactory.create(wordId, text, translation, example);
    }
}

