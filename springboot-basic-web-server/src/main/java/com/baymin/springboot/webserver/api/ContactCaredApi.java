package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IContactCaredService;
import com.baymin.springboot.store.entity.ContactCared;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@RestController
@RequestMapping(path = "/api/contactcared")
public class ContactCaredApi {

    @Autowired
    private IContactCaredService contactCaredService;

    @PostMapping
    @ResponseBody
    public ContactCared saveAddress(@RequestBody ContactCared contactCared) {
        if (Objects.isNull(contactCared)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactCaredService.saveContactCared(contactCared);
    }

    @DeleteMapping("/{userId}/{contactCaredId}")
    public void deleteAddress(@PathVariable String userId,
                              @PathVariable String contactCaredId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        contactCaredService.deleteContactCared(userId, contactCaredId);
    }

    @PutMapping
    @ResponseBody
    public ContactCared updateAddress(@RequestBody ContactCared contactCared) {
        if (Objects.isNull(contactCared)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactCaredService.updateContactCared(contactCared);
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public List<ContactCared> queryAddress(@PathVariable String userId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactCaredService.queryUserContactCared(userId);
    }

    @GetMapping("/{userId}/{contactCaredId}")
    @ResponseBody
    public ContactCared queryAddressDetail(@PathVariable String userId,
                                      @PathVariable String contactCaredId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactCaredService.queryContactCaredDetail(contactCaredId);
    }

}
