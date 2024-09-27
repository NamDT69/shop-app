package com.project.shopapp.filter;

import com.project.shopapp.component.JwtProvider;
import com.project.shopapp.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Filter;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       try{
           if (isByPassToken(request)){
               doFilter(request,response, filterChain);
               return;
           }
           final String authHeader = request.getHeader("Authorization");
           if (authHeader != null && authHeader.startsWith("Bearer ")){
               String token = authHeader.substring(7);
               String userName = jwtProvider.getEmail(token);
               if (userName != null && SecurityContextHolder.getContext().getAuthentication()==null){
                   UserDetails userDetails = (User)userDetailsService.loadUserByUsername(userName);
                   if (jwtProvider.isTokenValid(token, userDetails)){
                       UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                               userDetails.getUsername(),userDetails.getPassword(),
                               userDetails.getAuthorities()
                       );
                       authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                       SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                   }
               }
           }else{
               response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
           }
           doFilter(request,response,filterChain);
       }catch (Exception e){
           response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
       }

    }
    private Boolean isByPassToken(@NonNull HttpServletRequest request){
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of("api/v1/products", "GET"),
                Pair.of("api/v1/categories", "GET"),
                Pair.of("api/v1/users/login", "POST"),
                Pair.of("api/v1/users/register", "POST"),
                Pair.of("api/v1/products/uploads", "POST")
        );
        for (Pair<String, String> byPassToken: byPassTokens){
            if (request.getServletPath().contains(byPassToken.getFirst())
                    && request.getMethod().contains(byPassToken.getSecond()))
            {
                return true;
            }
        }
        return false;

    }

}
