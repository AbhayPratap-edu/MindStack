package dev.abhaya.mindstack.Security;

import dev.abhaya.mindstack.model.StackUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
public class JWTService {

    //initialize a secreteKey
    // You can use any random key, but it must be a 256-bit key (32 bytes) for HMAC-SHA256
    @Value("${jwt.secretKey}")
    private String jwtSecreteKey ;

    private SecretKey generateSecreteKey(){
        return Keys.hmacShaKeyFor(jwtSecreteKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(StackUser stackUser){
        return Jwts.builder()
                .subject(stackUser.getUserID().toString())
                .claim("email",stackUser.getEmail())
                .claim("roles", Set.of("ADMIN","USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60))
                .signWith(generateSecreteKey())
                .compact();
    }

    //to verify token, use this method, will give the id that we are using to create this token
    public long verifyTokenAndGetUserId(String token){
        Claims claims = Jwts.parser()// to parse it ( Creates a JwtParserBuilder )
                .verifyWith(generateSecreteKey())// verify the key that we have
                .build()// build the parser
                .parseSignedClaims(token)//parse the token and get jws<claims>
                .getPayload();

        //Receive JWT → Parser → Attach Key → Build → Parse → Verify Signature → Check Exp → Extract Claims
        //  Read Subject → Convert to long → userId

        return Long.parseLong(claims.getSubject());
    }
}
