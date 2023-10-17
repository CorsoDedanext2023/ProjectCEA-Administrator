package it.dedagroup.project_cea.exception.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import it.dedagroup.project_cea.dto.response.MessageDto;

@RestControllerAdvice
public class ExceptionHandlerCustom {
	@ExceptionHandler(NotValidDataException.class)
	public ResponseEntity<MessageDto> handleInvalidData(NotValidDataException e){
		MessageDto m = new MessageDto(e.getMessage(), HttpStatus.BAD_REQUEST.value(), e.getOggetto(),LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MessageDto> validationError(MethodArgumentNotValidException e){
		Map<String,String> map = e.getBindingResult()
				.getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
		List<String> errori = new ArrayList<>();
		for (String s : map.keySet()) {
			errori.add(s+": "+map.get(s));
		}
		MessageDto m = new MessageDto(errori, HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
	}
}
