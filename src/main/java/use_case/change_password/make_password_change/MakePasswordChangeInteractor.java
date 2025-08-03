package use_case.change_password.make_password_change;

import entity.SecurityUserFactory;
import entity.User;
import entity.UserFactory;
import entity.dto.CommonUserDto;
import entity.dto.SecurityUserDto;

public class MakePasswordChangeInteractor implements MakePasswordChangeInputBoundary {

    private final MakePasswordChangeOutputBoundary presenter;
    private final UserPasswordDataAccessInterface userDAO;
    private final UserFactory<CommonUserDto> commonUserFactory;
    private final UserFactory<SecurityUserDto> securityUserFactory;

    public MakePasswordChangeInteractor(MakePasswordChangeOutputBoundary presenter,
                                        UserPasswordDataAccessInterface userDAO,
                                        UserFactory<CommonUserDto> commonUserFactory,
                                        UserFactory<SecurityUserDto> securityUserFactory) {
        this.presenter = presenter;
        this.userDAO = userDAO;
        this.commonUserFactory = commonUserFactory;
        this.securityUserFactory = securityUserFactory;
    }

    @Override
    public void make_password_change(MakePasswordChangeInputData in) {
        User user = userDAO.get(in.getUsername());
        if (in.getNewPassword().isBlank()) {
            presenter.presentFailure(new MakePasswordChangeOutputData("Password cannot be empty"));
            // new output
            return;
        }
        if (in.getSecurityAnswer() == null ||  in.getSecurityAnswer().isBlank()) {
//            if (in.getNewPassword() == null) {
//                presenter.presentFailure(new MakePasswordChangeOutputData("Password cannot be empty"));
//                        // new output
//                return;
//            }
            CommonUserDto commonDto = CommonUserDto.builder()
                    .name(in.getUsername())
                    .password(in.getNewPassword())
                    .build();
            User newuser = commonUserFactory.create(commonDto);
            userDAO.update(in.getUsername(), newuser);
            presenter.presentSuccess();
            return;
        }

        String answer = userDAO.getAnswer(in.getUsername());

        if (answer.equals(in.getSecurityAnswer())) {
//            if (in.getNewPassword().isBlank()) {
//                presenter.presentFailure(new MakePasswordChangeOutputData("Password cannot be empty"));
//                // new output
//                return;
//            }
            SecurityUserDto securityDto = SecurityUserDto.builder()
                    .name(in.getUsername())
                    .password(in.getNewPassword())
                    .question(userDAO.getQuestion(in.getUsername()))
                    .answer(answer)
                    .build();
            User newuser = securityUserFactory.create(securityDto);
            userDAO.update(in.getUsername(), newuser);
            presenter.presentSuccess();
        } else {
            presenter.presentFailure(new MakePasswordChangeOutputData("Wrong answer"));
        }

    }
}
