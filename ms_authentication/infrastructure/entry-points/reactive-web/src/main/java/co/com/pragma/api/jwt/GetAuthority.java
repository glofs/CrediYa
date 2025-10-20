package co.com.pragma.api.jwt;

import co.com.pragma.model.users.exception.DynamicBusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetAuthority {

    public Mono<String> roles(String role) {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Authentication auth = securityContext.getAuthentication();
                    return auth.getAuthorities()
                            .stream()
                            .findFirst()
                            .map(GrantedAuthority::getAuthority)
                            .orElse("NOT_AUTHORITY");
                })
                .filter(rol -> rol.equals(role))
                .switchIfEmpty(Mono.error(new DynamicBusinessException(role + " AUTHORITY IS REQUIRED", 403)));
    }
}
