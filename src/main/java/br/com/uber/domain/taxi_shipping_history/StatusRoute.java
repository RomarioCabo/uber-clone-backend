package br.com.uber.domain.taxi_shipping_history;

import br.com.uber.domain.user.BusinessRuleException;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public enum StatusRoute {
  WAITING_ACCEPT_DRIVER("WAITING_ACCEPT_DRIVER"),
  DRIVER_ON_WAY("DRIVER_ON_WAY"),
  TRAVELING("TRAVELING"),
  FINISHED_ROUTE("FINISHED_ROUTE"),
  CANCELED_BY_PASSENGER("CANCELED_BY_PASSENGER"),
  CANCELED_BY_DRIVER("CANCELED_BY_DRIVER");

  private final String status;

  private static final Map<String, StatusRoute> values = new HashMap<>();

  StatusRoute(String status) {
    this.status = status;
  }

  static {
    for (StatusRoute statusRoute : StatusRoute.values()) {
      values.put(statusRoute.status, statusRoute);
    }
  }

  public static StatusRoute toEnum(String type) {
    for (StatusRoute statusRoute : StatusRoute.values()) {
      if (statusRoute.getTypeUserString().equals(type)) {
        return statusRoute;
      }
    }

    throw new BusinessRuleException("Status route entered is invalid");
  }

  @JsonValue
  public String getTypeUserString() {
    return this.status;
  }

  public static Boolean valueOf(StatusRoute statusRoute) {
    StatusRoute enumTypeUser = values.get(statusRoute.status);
    return enumTypeUser != null;
  }
}
