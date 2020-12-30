package com.codmain.orderapi.services;

import java.util.Date;

import com.codmain.orderapi.Exceptions.GeneralServiceException;
import com.codmain.orderapi.Exceptions.NoDataFoundException;
import com.codmain.orderapi.Exceptions.ValidateServiceException;
import com.codmain.orderapi.converters.UserConverter;
import com.codmain.orderapi.dtos.LoginRequestDTO;
import com.codmain.orderapi.dtos.LoginResponseDTO;
import com.codmain.orderapi.entity.User;
import com.codmain.orderapi.repository.UserRepository;
import com.codmain.orderapi.validators.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Value("${jwt.password}")
    private String jwtSecret;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        try {

            UserValidator.signup(user);

            User existUser = userRepo.findByUsername(user.getUsername()).orElse(null);

            if (existUser != null)
                throw new ValidateServiceException("el nombre de usuario ya existe");

            String enconder = passwordEncoder.encode(user.getPassword());
            user.setPassword(enconder);

            return userRepo.save(user);
        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        try {

            User user = userRepo.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ValidateServiceException("Usuario o password Incorrectos"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
                throw new ValidateServiceException("Usuario o password Incorrectos");

            String token = createToken(user);

            return new LoginResponseDTO(userConverter.fromEntity(user), token);

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    public String createToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (1000 * 60 * 60));

        return Jwts.builder().setSubject(user.getUsername()).setIssuedAt(now).setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (UnsupportedJwtException e) {
            log.error("JWT in a particular format/configuration that does not match the format expected");
        } catch (MalformedJwtException e) {
            log.error("JWT was not correctly constructed and should be rejected.");
        } catch (SignatureException e) {
            log.error("Signature or veryfing an existing signature of a JWT failed");
        } catch (ExpiredJwtException e) {
            log.error("JWT was accepted after it expired and must be rejected");
        }
        return false;
    }

    public String getUsernameFromToken(String jwt) {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ValidateServiceException("Invalid Token");
        }
    }
}
