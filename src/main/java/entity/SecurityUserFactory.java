package entity;

import entity.dto.SecurityUserDto;

public class SecurityUserFactory
        implements UserFactory<SecurityUserDto> {

    @Override
    public SecurityUser create(SecurityUserDto dto) {
        return new SecurityUser(
                dto.getName(),
                dto.getPassword(),
                dto.getSecurityQuestion(),
                dto.getSecurityAnswer()
        );
    }
}
