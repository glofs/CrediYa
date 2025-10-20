package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.request.InformationUser;
import co.com.pragma.model.loan.response.Data;
import reactor.core.publisher.Mono;

public interface ConsultInformationRepository {
    Mono<Data> consultInformationUser(InformationUser infoUser,String authorization, String spectedRole);
}
