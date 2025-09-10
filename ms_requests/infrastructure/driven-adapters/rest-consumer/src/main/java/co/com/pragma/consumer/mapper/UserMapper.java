package co.com.pragma.consumer.mapper;

import co.com.pragma.consumer.UserInformation;
import co.com.pragma.model.loan.request.InformationUser;

public class UserMapper {

    public static UserInformation modelToDto(InformationUser informationUser) {
        return UserInformation
                .builder()
                .document(informationUser.getDocument())
                .build();
    }
}
