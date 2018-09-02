package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IContactService;
import com.baymin.springboot.store.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@RestController
@RequestMapping(path = "/contact")
public class ContactApi {

    @Autowired
    private IContactService contactService;

    @PostMapping
    @ResponseBody
    public Contact saveAddress(@RequestBody Contact contact) {
        if (Objects.isNull(contact)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactService.saveContact(contact);
    }

    @DeleteMapping("/{userId}/{contactId}")
    public void deleteAddress(@PathVariable String userId,
                              @PathVariable String contactId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        contactService.deleteContact(userId, contactId);
    }

    @PutMapping
    @ResponseBody
    public Contact updateAddress(@RequestBody Contact contact) {
        if (Objects.isNull(contact)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactService.updateContact(contact);
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public List<Contact> queryAddress(@PathVariable String userId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactService.queryUserContact(userId);
    }

    @GetMapping("/{userId}/{contactId}")
    @ResponseBody
    public Contact queryAddressDetail(@PathVariable String userId,
                                      @PathVariable String contactId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactService.queryContactDetail(contactId);
    }

}
