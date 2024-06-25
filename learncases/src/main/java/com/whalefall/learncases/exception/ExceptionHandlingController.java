package com.whalefall.learncases.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

/**
 * codes copy from => <a href="https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc">...</a>
 *
 * @author WhaleFall
 * @date 2022-05-28 10:41
 */
@Slf4j
@ControllerAdvice// 指定为全局异常处理
public class ExceptionHandlingController {

    // Specify name of a specific view that will be used to display the error:
    @ExceptionHandler(SQLException.class)
    public String databaseError() {
        // Nothing to do.  Returns the logical view name of an error page, passed
        // to the view-resolver(s) in usual way.
        // Note that the exception is NOT available to this view (it is not added
        // to the model) but see "Extending ExceptionHandlerExceptionResolver"
        // below.
        return "databaseError";
    }

    // Total control - set up a model and return the view name yourself. Or
    // consider subclassing ExceptionHandlerExceptionResolver (see below).
    @ExceptionHandler(Exception.class)
    @SuppressWarnings("all")
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        log.error("Request: {} raised", req.getRequestURL(), ex);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

}
