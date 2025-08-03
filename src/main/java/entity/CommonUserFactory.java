package entity;

import entity.dto.CommonUserDto;

public class CommonUserFactory
        implements UserFactory<CommonUserDto> {

    @Override
    public CommonUser create(CommonUserDto dto) {
        return new CommonUser(
                dto.getName(),
                dto.getPassword()
        );
    }
}
