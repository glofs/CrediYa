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
public class UserRouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users/createUser",
                    beanClass = UserHandler.class,
                    beanMethod = "createUsers"
            )
    })

    public RouterFunction<ServerResponse> routerFunction(UserHandler myUserHandler) {
        return route(POST("/api/v1/users/createUser"), myUserHandler::createUsers)
                .andRoute(POST("/api/v1/user/consult"), myUserHandler::consultUserByDocument);
    }
}
