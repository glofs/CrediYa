package co.com.pragma.api.jwt;

import co.com.pragma.model.users.exception.DynamicBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtService jwtService;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .flatMap(auth -> jwtService.extractAllClaims(auth.getCredentials().toString()))
                .log()
                .onErrorResume(e -> Mono.error(new DynamicBusinessException("Invalid token has been sent", 401)))//401 status require authentication(unauthorized), 403 is invalid authorization (forbidden) or role
                .map(claims ->
                        new UsernamePasswordAuthenticationToken(
                                claims,
                                null,
                                Stream.of(claims.get("role"))
                                        .map(roleObj -> (List<Map<String, String>>) roleObj)
                                        .flatMap(role -> role.stream()
                                                .map(r -> r.get("authority"))
                                                .map(SimpleGrantedAuthority::new))
                                        .toList()
                        )
                );

    }
}