package use_case.change_password.make_password_change;

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
        final User user = userDao.get(inputData.getUsername());
        if (inputData.getNewPassword().isBlank()) {
            presenter.presentFailure(new MakePasswordChangeOutputData("Password cannot be empty"));

        }
        else if (inputData.getNewPassword().equals(user.getPassword())) {
            presenter.presentFailure(new MakePasswordChangeOutputData("Password cannot be same as the old password"));
        }
        else {
            if (inputData.getSecurityAnswer() == null || inputData.getSecurityAnswer().isBlank()) {
                final CommonUserDto commonDto = CommonUserDto.builder()
                        .name(inputData.getUsername())
                        .password(inputData.getNewPassword())
                        .build();
                final User newuser = commonUserFactory.create(commonDto);
                userDao.update(inputData.getUsername(), newuser);
                presenter.presentSuccess();
            }
            else {
                final String answer = userDao.getAnswer(inputData.getUsername());

                if (answer.equals(inputData.getSecurityAnswer())) {
                    final SecurityUserDto securityDto = SecurityUserDto.builder()
                            .name(inputData.getUsername())
                            .password(inputData.getNewPassword())
                            .question(userDao.getQuestion(inputData.getUsername()))
                            .answer(answer)
                            .build();
                    final User newuser = securityUserFactory.create(securityDto);
                    userDao.update(inputData.getUsername(), newuser);
                    presenter.presentSuccess();
                }
                else {
                    presenter.presentFailure(new MakePasswordChangeOutputData("Wrong answer"));
                }
            }
        }
    }
}
