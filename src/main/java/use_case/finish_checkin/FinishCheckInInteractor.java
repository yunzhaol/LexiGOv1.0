package use_case.finish_checkin;

import entity.CommonCard;
import entity.LearnRecord;
import entity.LearnRecordFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FinishCheckInInteractor implements FinishCheckInInputBoundary {

    private final FinishCheckInOutputBoundary presenter;
    private final UserSaveRecordDataAccessInterface recordSaver;
    private final UserDeckGetTextDataAccessInterface textsdataGetter;
    private final LearnRecordFactory learnRecordFactory;
    private final TimeGetter timegenerator;

    public FinishCheckInInteractor(FinishCheckInOutputBoundary presenter,
                                   UserSaveRecordDataAccessInterface recordSaver,
                                   UserDeckGetTextDataAccessInterface textsdataGetter,
                                   LearnRecordFactory learnRecordFactory,
                                   TimeGetter timegenerator) {
        this.presenter = presenter;
        this.recordSaver = recordSaver;
        this.textsdataGetter = textsdataGetter;
        this.learnRecordFactory = learnRecordFactory;
        this.timegenerator = timegenerator;
    }

    @Override
    public void execute(FinishCheckInInputData in) {
        List<UUID> uuidList = new ArrayList<>();
        for (CommonCard eachCard : textsdataGetter.getWordDeck()) {
            uuidList.add(eachCard.getWordId());
        }
        LearnRecord learnRecord = learnRecordFactory.create(in.getUsername(), timegenerator.generate(), uuidList);
        recordSaver.save(learnRecord);
        presenter.prepareSuccessView();

    }
}
