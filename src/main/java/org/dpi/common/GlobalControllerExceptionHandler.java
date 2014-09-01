package org.dpi.common;

import java.util.Map;

import org.dpi.employment.Employment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/*
 * to apply it globally (all controllers), annotate a class with @ControllerAdvice
 */
@ControllerAdvice
class GlobalControllerExceptionHandler {

	static Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
	
	@Autowired
	private MessageSource message;

	@ExceptionHandler
	public ResponseEntity<String> handleException(Exception ex){
		
	    ex.printStackTrace();
	    
		log.error("Exception ",ex);	

		//TODO inject this as a Spring bean!!!
		ObjectMapper mapper = new CustomObjectMapper();

		String  exceptionString = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(ex);
		
		Map<String, Object> responseMap = new ResponseMap<Employment>().mapError(exceptionString);

		try {
			String serializedResponse = mapper.writeValueAsString(responseMap);

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "application/json;charset=utf-8");

			return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		catch(JsonProcessingException exep) {
			log.error(exep.getMessage());	
			throw new RuntimeException(exep);
		}

	}
}

