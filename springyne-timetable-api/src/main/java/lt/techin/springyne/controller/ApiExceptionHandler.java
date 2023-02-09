package lt.techin.springyne.controller;

import org.springframework.http.HttpStatus;
import lt.techin.springyne.dto.ErrorDto;
import lt.techin.springyne.dto.ErrorFieldDto;
import lt.techin.springyne.dto.mapper.ErrorFieldMapper;
import lt.techin.springyne.exception.ScheduleServiceDisabledException;
import lt.techin.springyne.exception.ScheduleValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;


@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Validation error")
    public void handleValidationExceptions(ConstraintViolationException exception) {

        @ExceptionHandler(ConstraintViolationException.class)
        @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Validation error")
        public void handleValidationExceptions(ConstraintViolationException exception) {

    }

        @ExceptionHandler(DataAccessException.class)
        public ResponseEntity<ErrorDto> handleDataAccessException(HttpServletRequest request, DataAccessException dataAccessException) {
                logger.error("DataAccessException: {}. Cause?: {}",
                        dataAccessException.getMessage(), dataAccessException.getMostSpecificCause().getMessage());

                var errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                var errorFields = List.of(
                        new ErrorFieldDto("sql", dataAccessException.getMostSpecificCause().getMessage(), null)
                );
                var errorDto = new ErrorDto(request.getRequestURL().toString(),
                        errorFields,
                        dataAccessException.getMessage(),
                        errorStatus.value(),
                        errorStatus.getReasonPhrase(),
                        request.getRequestURL().toString(),
                        LocalDateTime.now());
                return ResponseEntity.internalServerError().body(errorDto);
        }

        @ExceptionHandler(ScheduleValidationException.class)
        public ResponseEntity<ErrorDto> handleScheduleValidationException(HttpServletRequest request, ScheduleValidationException scheduleValidationException) {
                logger.error("ScheduleValidationException: {}, for field: {}", scheduleValidationException.getMessage(), scheduleValidationException.getField());

                var errorStatus = HttpStatus.BAD_REQUEST;

                var errorFields = List.of(
                        new ErrorFieldDto(scheduleValidationException.getField(), scheduleValidationException.getError(), scheduleValidationException.getRejectedValue())
                );

                var errorDto = new ErrorDto(request.getRequestURL().toString(),
                        errorFields,
                        scheduleValidationException.getMessage(),
                        errorStatus.value(),
                        errorStatus.getReasonPhrase(),
                        request.getRequestURL().toString(),
                        LocalDateTime.now());
                return ResponseEntity.badRequest().body(errorDto);
        }


        @ExceptionHandler(ScheduleServiceDisabledException.class)
        public ResponseEntity<Void> handleScheduleServiceDisabledException(HttpServletRequest request, ScheduleServiceDisabledException serviceDisabledException) {
                logger.error("ScheduleServiceDisabledException: {}", serviceDisabledException.getMessage());

                var errorStatus = HttpStatus.SERVICE_UNAVAILABLE;

                return new ResponseEntity<>(errorStatus);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException notValidException) {
                logger.error("MethodArgumentNotValidException: {}", notValidException.getMessage());

                var errorStatus = HttpStatus.BAD_REQUEST;

                var errorFields = notValidException.getBindingResult()
                        .getAllErrors().stream()
                        .map(ErrorFieldMapper::toErrorFieldDto)
                        .collect(toList());

                var errorDto = new ErrorDto(request.getRequestURL().toString(),
                        errorFields,
                        notValidException.getMessage(),
                        errorStatus.value(),
                        errorStatus.getReasonPhrase(),
                        request.getRequestURL().toString(),
                        LocalDateTime.now());
                return ResponseEntity.badRequest().body(errorDto);
        }

        @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "We do not support this")
        @ExceptionHandler(HttpMediaTypeException.class)
        public void handleHttpMediaTypeException(HttpMediaTypeException mediaTypeException) {
                logger.error("Not supported request resulted in HttpMediaTypeException: {}", mediaTypeException.getMessage());
        }

        @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Something really bad happened")
        @ExceptionHandler(Exception.class)
        public void handleAllExceptions(Exception exception) {
                logger.error("All Exceptions handler: {}", exception.getMessage());
        }
}
