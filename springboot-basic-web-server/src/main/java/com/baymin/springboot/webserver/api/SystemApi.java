package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IFeedbackService;
import com.baymin.springboot.store.entity.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@RestController
@RequestMapping(path = "/sys")
public class SystemApi {

    @Autowired
    private IFeedbackService feedBackService;

    @PostMapping("/feedback")
    @ResponseBody
    public void feedback(@RequestBody Feedback feedback) {
        if (Objects.isNull(feedback)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        feedBackService.saveFeedback(feedback);
    }

}
