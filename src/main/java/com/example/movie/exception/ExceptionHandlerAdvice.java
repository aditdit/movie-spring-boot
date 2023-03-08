package com.example.movie.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.movie.dto.error.ErrorResponseDTO;
import com.example.movie.dto.error.InvalidDataErrorResponseDTO;
import com.example.movie.dto.error.NotValidResponseDTO;
import com.example.movie.enums.ErrorCode;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<NotValidResponseDTO> details = new ArrayList<NotValidResponseDTO>();

		ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
			String rejectedValue = "";
			if (fieldError.getRejectedValue() == null) {
				rejectedValue = "null";
			} else {
				rejectedValue = fieldError.getRejectedValue().toString();
			}

			NotValidResponseDTO dto = new NotValidResponseDTO();
			dto.setCode(fieldError.getCode());
			dto.setProperty(fieldError.getField());
			dto.setRejectedValue(rejectedValue);
			dto.setMessage(fieldError.getDefaultMessage());

			details.add(dto);
		});

		InvalidDataErrorResponseDTO errorResponse = InvalidDataErrorResponseDTO.of("invalid data", details,
				ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("internal server error", details, ErrorCode.INTERNAL_ERROR,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex,
			WebRequest request) {
		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("data not found", details, ErrorCode.DATA_NOT_FOUND,
				HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponseDTO> handleMaxSizeException(MaxUploadSizeExceededException ex) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("file too large", details, ErrorCode.INVALID_DATA,
				HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(errorResponse);
	}

}
