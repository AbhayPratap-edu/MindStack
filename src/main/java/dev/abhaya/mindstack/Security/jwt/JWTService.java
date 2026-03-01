package dev.abhaya.mindstack.Security.jwt;

import dev.abhaya.mindstack.Security.authorities.Permission;
import dev.abhaya.mindstack.Security.authorities.RolePermissionMapping;
import dev.abhaya.mindstack.model.StackUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class JWTService {

    //initialize a secreteKey
    // You can use any random key, but it must be a 256-bit key (32 bytes) for HMAC-SHA256
    @Value("${jwt.secretKey}")
    private String jwtSecreteKey ;
    private final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 minutes

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecreteKey.getBytes(StandardCharsets.UTF_8));
    }


    public String createAccessToken(StackUser stackUser){

        Set<Permission> permissions = RolePermissionMapping.getPermissions(stackUser.getRole());

        List<String>  permissionNames = permissions.stream().map(Enum::name).toList();

        return Jwts.builder()
                .subject(stackUser.getUserID().toString())
                .claim("email",stackUser.getEmail())
                .claim("role",stackUser.getRole().name())
                .claim("permissions",permissionNames)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_VALIDITY)) // Valid for 1 Minute
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateAccessToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }
        catch (JwtException e){
            return false;
        }
    }


//to verify token, use this method, will give the id that we are using to create this token
    public Claims verifyTokenAndGeClaims(String token){

        //Receive JWT → Parser → Attach Key → Build → Parse → Verify Signature → Check Exp → Extract Claims
        //  Read Subject → Convert to long → userId

        return Jwts.parser()// to parse it ( Creates a JwtParserBuilder )
                .verifyWith(getSigningKey())// verify the key that we have
                .build()// build the parser
                .parseSignedClaims(token)//parse the token and get jws<claims>
                .getPayload();
    }

}
