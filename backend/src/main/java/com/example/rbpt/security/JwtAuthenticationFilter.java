package com.example.rbpt.security;

import com.example.rbpt.model.User;
import com.example.rbpt.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (auth != null && auth.startsWith("Bearer ")) {
      String token = auth.substring(7);
      try {
        Jws<Claims> jws = jwtUtil.parse(token);
        String email = jws.getBody().getSubject();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
          User u = userOpt.get();
          List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + u.role));
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              u, null, authorities);
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      } catch (Exception ignored) {}
    }
    chain.doFilter(request, response);
  }
}
