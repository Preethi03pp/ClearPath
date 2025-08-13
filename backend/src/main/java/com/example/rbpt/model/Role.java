package com.example.rbpt.model;

public enum Role {
  ADMIN, MANAGER, MEMBER;

  public String asAuthority() {
    return "ROLE_" + name();
  }
}
