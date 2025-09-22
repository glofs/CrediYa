package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.exception.DataNotFoundException;
import co.com.pragma.model.loan.gateways.ConsultInformationRepository;
import co.com.pragma.model.loan.request.InformationUser;
import co.com.pragma.model.loan.response.Data;
import reactor.core.publisher.Mono;

import static co.com.pragma.model.loan.exception.Constants.CONSULT_USER_IS_EMPTY;


public class ConsultUserUseCase {
    private final ConsultInformationRepository consultInformationRepository;

    public ConsultUserUseCase(ConsultInformationRepository consultInformationRepository) {
        this.consultInformationRepository = consultInformationRepository;
    }

    public Mono<Data> consult(String document, String authorization) {
        InformationUser infoUser =
                InformationUser
                        .builder()
                        .document(document)
                        .build();

        return consultInformationRepository.consultInformationUser(infoUser, authorization)
                .switchIfEmpty(Mono.error(new DataNotFoundException(CONSULT_USER_IS_EMPTY)))
                .doOnSuccess(userData -> System.out.println("user information " + userData))
                .doOnError(e -> Mono.error(new DataNotFoundException(e.getMessage())));
    }
}
