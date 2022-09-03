package br.com.uber.infrastructure.persistence.taxi_shipping_history;

import br.com.uber.domain.taxi_shipping_history.StatusRoute;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EnumStatusRouteAttributeConverter implements AttributeConverter<StatusRoute, String> {

  @Override
  public String convertToDatabaseColumn(StatusRoute attribute) {
    if (attribute == null)
      return null;

    if(attribute == StatusRoute.WAITING_ACCEPT_DRIVER)
      return "WAITING_ACCEPT_DRIVER";

    if(attribute == StatusRoute.DRIVER_ON_WAY)
      return "DRIVER_ON_WAY";

    if(attribute == StatusRoute.TRAVELING)
      return "TRAVELING";

    if(attribute == StatusRoute.FINISHED_ROUTE)
      return "FINISHED_ROUTE";

    throw new IllegalArgumentException(attribute + " not supported.");
  }

  @Override
  public StatusRoute convertToEntityAttribute(String dbData) {
    if (dbData == null)
      return null;

    if(dbData.equals("WAITING_ACCEPT_DRIVER"))
      return StatusRoute.WAITING_ACCEPT_DRIVER;

    if(dbData.equals("DRIVER_ON_WAY"))
      return StatusRoute.DRIVER_ON_WAY;

    if(dbData.equals("TRAVELING"))
      return StatusRoute.TRAVELING;

    if(dbData.equals("FINISHED_ROUTE"))
      return StatusRoute.FINISHED_ROUTE;

    throw new IllegalArgumentException(dbData + " not supported.");
  }
}
