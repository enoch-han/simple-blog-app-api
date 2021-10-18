package com.example.BlogAppApi.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.BlogAppApi.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    /*
    * this class filters any api calls and checks of all have the required
    * token for accessing the urls
    * */
    Constants constants = new Constants();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/blogapp/api/login")){
            filterChain.doFilter(request,response);
        } else{
            //takes the request header
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            //checks if the request authorization header exists and have the string owner before it
            if (authorizationHeader != null && authorizationHeader.startsWith("owner ")){
                try {
                    String token = authorizationHeader.substring("owner ".length());
                    Algorithm algorithm = constants.getAlgorithm();
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    //decodes the rest of the token after removing the owner key
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String userName = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    //creates authorities by looping through user roles
                    stream(roles).forEach(role->{
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                }catch (Exception e){
                    System.out.println("error logging in");
                    response.setHeader("error",e.getMessage());
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    Map<String,String> message = new HashMap<>();
                    message.put("error_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),message);
                }
            } else{
                filterChain.doFilter(request,response);
            }
        }
    }
}
