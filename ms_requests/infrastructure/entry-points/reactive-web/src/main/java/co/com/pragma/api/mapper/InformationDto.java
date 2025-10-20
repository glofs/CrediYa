package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.InformationUserDto;
import co.com.pragma.model.loan.request.InformationUser;

public class InformationDto {

    public InformationUser toUser(InformationUserDto informationDto) {

        return InformationUser
                .builder()
                .document(informationDto.getDocument())
                .build();
    }
}
