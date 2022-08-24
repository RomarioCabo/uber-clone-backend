package br.com.uber.infrastructure.persistence.user;

import br.com.uber.domain.user.TypeUser;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EnumTypeUserAttributeConverter implements AttributeConverter<TypeUser, String> {

  @Override
  public String convertToDatabaseColumn(TypeUser attribute) {
    if (attribute == null)
      return null;

    if(attribute == TypeUser.DRIVER)
      return "DRIVER";


    if(attribute == TypeUser.PASSENGER)
      return "PASSENGER";


    throw new IllegalArgumentException(attribute + " not supported.");
  }

  @Override
  public TypeUser convertToEntityAttribute(String dbData) {
    if (dbData == null)
      return null;

    if(dbData.equals("DRIVER"))
      return TypeUser.DRIVER;

    if(dbData.equals("PASSENGER"))
      return TypeUser.PASSENGER;

    throw new IllegalArgumentException(dbData + " not supported.");
  }
}
