package br.com.uber.domain.user;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public enum TypeUser {
  DRIVER("DRIVER"),
  PASSENGER("PASSENGER");

  private final String type;
  private static final Map<String, TypeUser> values = new HashMap<>();

  TypeUser(String type) {
    this.type = type;
  }

  static {
    for (TypeUser typeUser : TypeUser.values()) {
      values.put(typeUser.type, typeUser);
    }
  }

  public static TypeUser toEnum(String type) {
    for (TypeUser typeUser : TypeUser.values()) {
      if (typeUser.getTypeUserString().equals(type)) {
        return typeUser;
      }
    }

    throw new BusinessRuleException("Score entered is invalid");
  }

  @JsonValue
  public String getTypeUserString() {
    return this.type;
  }

  public static Boolean valueOf(TypeUser type) {
    TypeUser enumTypeUser = values.get(type.type);
    return enumTypeUser != null;
  }
}
