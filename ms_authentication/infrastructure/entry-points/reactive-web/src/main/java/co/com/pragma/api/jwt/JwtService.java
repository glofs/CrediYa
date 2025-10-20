package co.com.pragma.api.jwt;

import co.com.pragma.model.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.key}")
    private String SECRET_KEY;



    public Mono<String> generateToken(User user) {
        Map<String, Object> mapRole = new HashMap<>();
        mapRole.put("role", List.of(new SimpleGrantedAuthority(user.getRole())));
        return Mono.just(Jwts
                .builder()
                .setClaims(mapRole)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 120000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact());
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);//algorithm
    }

    private Mono<Date> extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Mono<Boolean> isTokenExpired(String token) {
        return extractExpiration(token)
                .map(date -> date.before(new Date()));
    }

    public Mono<Boolean> isTokenValid(String jwt, String userName) {
        return extractUserName(jwt)
                .map(us -> us.equals(userName) && (Boolean.FALSE.equals(isTokenExpired(jwt).block())));
    }

    public Mono<String> extractUserName(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> Mono<T> extractClaim(String token, Function<Claims, T> claimsTFunction) {//extract once single claim
        return extractAllClaims(token).map(claimsTFunction::apply);
    }

    public Mono<Claims> extractAllClaims(String token) {
        return Mono.just(
                Jwts.parserBuilder()
                        .setSigningKey(getSignKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
        );
    }
}
