package co.com.pragma.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoanRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/user/loan",
                    beanClass = LoanHandler.class,
                    beanMethod = "generateLoanReq"
            )
    })

    public RouterFunction<ServerResponse> routerFunction(LoanHandler loanHandler) {
        return route(POST("/api/v1/user/loan"), loanHandler::generateLoanReq);
    }
}
