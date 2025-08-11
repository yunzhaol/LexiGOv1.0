package use_case.change_password.make_password_change;

import java.util.Optional;
import java.util.function.Supplier;

import entity.User;
import entity.UserFactory;
import entity.dto.CommonUserDto;
import entity.dto.SecurityUserDto;

public class MakePasswordChangeInteractor implements MakePasswordChangeInputBoundary {

    private final MakePasswordChangeOutputBoundary presenter;
    private final UserPasswordDataAccessInterface userDao;
    private final UserFactory<CommonUserDto> commonUserFactory;
    private final UserFactory<SecurityUserDto> securityUserFactory;

    public MakePasswordChangeInteractor(MakePasswordChangeOutputBoundary presenter,
                                        UserPasswordDataAccessInterface userDao,
                                        UserFactory<CommonUserDto> commonUserFactory,
                                        UserFactory<SecurityUserDto> securityUserFactory) {
        this.presenter = presenter;
        this.userDao = userDao;
        this.commonUserFactory = commonUserFactory;
        this.securityUserFactory = securityUserFactory;
    }

    @Override
    public void makePasswordChange(MakePasswordChangeInputData inputData) {
        final String username = inputData.getUsername();
        final User user = userDao.get(username);
        final String newPwd = inputData.getNewPassword();
        final String oldPwd = user.getPassword();

        // use processor to check if new pwd is empty or the same as old one
        final Processor.Output out = processor().process(oldPwd, newPwd);
        if (!out.isSuccess()) {
            presenter.presentFailure(new MakePasswordChangeOutputData(out.getErrorMessage()));
        }
        else {
            // non-security user case
            final String question = userDao.getQuestion(username);
            if (question.isEmpty()) {
                final CommonUserDto commonDto = CommonUserDto.builder()
                        .name(username)
                        .password(newPwd)
                        .build();
                final User newUser = commonUserFactory.create(commonDto);
                userDao.update(username, newUser);
                presenter.presentSuccess();
            }
            else {
                // security user case: need match up answer
                final String storedAnswer = userDao.getAnswer(username);
                if (!storedAnswer.equals(inputData.getSecurityAnswer())) {
                    presenter.presentFailure(new MakePasswordChangeOutputData("Wrong answer"));
                }
                else {
                    final SecurityUserDto securityDto = SecurityUserDto.builder()
                            .name(username)
                            .password(newPwd)
                            .question(question)
                            .answer(storedAnswer)
                            .build();
                    final User newUser = securityUserFactory.create(securityDto);
                    userDao.update(username, newUser);
                    presenter.presentSuccess();
                }
            }
        }
    }

    /**
     * Factory method for Processor.
     * @return a new Processor instance
     */
    public static Processor processor() {
        return new Processor();
    }

    private static final class Processor {

        private static final Output SUCCESS = new Output(null, true);

        Output process(String oldPassword, String newPassword) {
            return firstFailure(
                    () -> processPwBlank(newPassword),
                    () -> processDuplicate(oldPassword, newPassword)
            ).orElse(SUCCESS);
        }

        @SafeVarargs
        private final Optional<Output> firstFailure(Supplier<Output>... steps) {
            Optional<Output> result = Optional.empty();
            for (Supplier<Output> s : steps) {
                final Output o = s.get();
                if (!o.isSuccess()) {
                    result = Optional.of(o);
                    break;
                }
            }
            return result;
        }

        private Output processPwBlank(String password) {
            Output result = SUCCESS;
            if (password == null || password.isBlank()) {
                result = new Output("Password cannot be empty", false);
            }
            return result;
        }

        private Output processDuplicate(String pwOld, String pwNew) {
            Output result = SUCCESS;
            if (pwOld != null && pwOld.equals(pwNew)) {
                result = new Output("Password cannot be same as the old password", false);
            }
            return result;
        }

        // output model
        static final class Output {
            private final String errorMessage;
            private final boolean success;

            Output(String errorMessage, boolean success) {
                this.errorMessage = errorMessage;
                this.success = success;
            }

            public String getErrorMessage() {
                return errorMessage;
            }

            public boolean isSuccess() {
                return success;
            }
        }
    }
}
