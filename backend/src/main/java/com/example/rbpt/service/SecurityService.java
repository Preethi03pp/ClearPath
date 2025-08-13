package com.example.rbpt.service;

import com.example.rbpt.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
  public User currentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof User)) return null;
    return (User) auth.getPrincipal();
  }
  public boolean isAdmin() { User u = currentUser(); return u != null && "ADMIN".equals(u.role); }
  public boolean isManager() { User u = currentUser(); return u != null && "MANAGER".equals(u.role); }
  public boolean isMember() { User u = currentUser(); return u != null && "MEMBER".equals(u.role); }
}
