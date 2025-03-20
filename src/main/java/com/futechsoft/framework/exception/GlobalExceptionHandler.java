package com.futechsoft.framework.exception;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.futechsoft.framework.common.constant.ViewInfo;



@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


	@ExceptionHandler(AccessDeniedException.class)
	protected String AccessDeniedException(HttpServletRequest request, Model model, Exception e) {


		String referer =request.getHeader("referer");
		request.setAttribute("message","권한이 없습니다");
		request.setAttribute(ViewInfo.REDIRECT_URL, referer);
		return ViewInfo.ACCESS_DENIED_REDIRECT_PAGE;

		//return ACCESS_ERR_PAGE;
	}

	@ExceptionHandler(Exception.class)
	protected String handleException(Model model, Exception e) {

		e.printStackTrace();
		model.addAttribute("err", e.getMessage());

		return ViewInfo.DEFAULT_ERR_PAGE;
	}
	
	




	@ExceptionHandler(DuplicateKeyException.class)
	protected String dupException(HttpServletRequest request, Model model, SQLException e) {

		e.printStackTrace();

		String referer =request.getHeader("referer");

		request.setAttribute("message","입력중 중복 오류가 발생했습니다.");

		request.setAttribute(ViewInfo.REDIRECT_URL, referer);
		return ViewInfo.ACCESS_DENIED_REDIRECT_PAGE;

	}

	@ExceptionHandler(FileDownloadException.class)
	protected String FileDownloadException(HttpServletRequest request, Model model, Exception e) {

		e.printStackTrace();
		model.addAttribute("err", e.getMessage());

		return ViewInfo.DEFAULT_ERR_PAGE;
	}


	@ExceptionHandler({ JsonMappingException.class, JsonProcessingException.class })
	protected String JsonException(Model model, Exception e) {

		e.printStackTrace();

		model.addAttribute("err", e.getMessage());

		return ViewInfo.DEFAULT_ERR_PAGE;
	}

	/*
	 * @ExceptionHandler(FileUploadException.class) protected String
	 * FileUploadException(Model model,Exception e) {
	 *
	 * e.printStackTrace();
	 *
	 * model.addAttribute("err", e.getMessage());
	 *
	 * return DEFAULT_ERR_PAGE; }
	 * 
	 */
	
	
	  @ModelAttribute("basePath")
	    public String getBasePath(HttpServletRequest request) {

	        return "";
	    }

}
