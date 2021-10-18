package com.example.BlogAppApi.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.BlogAppApi.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /*
    * this class sets or configures the filters needed for authentication
    * */
    private final AuthenticationManager authenticationManager;
    private final Constants constants = new Constants();
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    //method called on every authentication attempts
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("attempt authentication user : "+ userName + " with password : " + password);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName,password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    //method called on every successful authentication
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User)authentication.getPrincipal();
        //using the secret word to generate a algorithm
        Algorithm algorithm = constants.getAlgorithm();
        //generation of the access token
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+30*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        //generation of the refresh token
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        //setting the headers of the response which will be sent if user us authenticated
        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);
        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
    }
}
