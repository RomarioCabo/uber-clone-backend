package br.com.uber.application.exception;

import br.com.uber.domain.user.BusinessRuleException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    fillErrorLog(ex);
    var error = "Malformed JSON request";
    return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, List.of(error)), status);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    fillErrorLog(ex);
    var errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    String.format(
                        "'%s': %s", fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());
    return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, errors), status);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    fillErrorLog(ex);
    var error = ex.getMessage();
    return new ResponseEntity<>(
        new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, List.of(error)), status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGenericException(Exception ex) {
    fillErrorLog(ex);
    return new ResponseEntity<>(
        new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, List.of("Erro interno")),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(JsonMappingException.class)
  public ResponseEntity<Object> handleJsonMappingException(JsonMappingException ex) {
    fillErrorLog(ex);
    return new ResponseEntity<>(
        new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, List.of("Error mapping json")),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(JsonProcessingException.class)
  public ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex) {
    fillErrorLog(ex);
    return new ResponseEntity<>(
        new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, List.of("Error processing json")),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BusinessRuleException.class)
  public ResponseEntity<Object> handleBusinessRuleException(BusinessRuleException ex) {
    fillErrorLog(ex);
    var error = ex.getMessage();
    return new ResponseEntity<>(
        new ApiError(HttpStatus.BAD_REQUEST, List.of(error)), HttpStatus.BAD_REQUEST);
  }

  private void fillErrorLog(Exception ex) {
    log.error("Received exception with message [{}]", ex.getMessage());
  }
}
