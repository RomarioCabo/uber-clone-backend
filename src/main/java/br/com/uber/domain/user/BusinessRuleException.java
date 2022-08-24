package br.com.uber.domain.user;

public class BusinessRuleException extends RuntimeException {
  public BusinessRuleException(String mensagem) {
    super(mensagem);
  }
}