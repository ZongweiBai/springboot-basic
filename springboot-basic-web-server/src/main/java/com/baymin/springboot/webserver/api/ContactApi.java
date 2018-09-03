package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IContactService;
import com.baymin.springboot.store.entity.Contact;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api(value = "常用联系人", tags = "常用联系人")
@RestController
@RequestMapping(path = "/api/contact")
public class ContactApi {

    @Autowired
    private IContactService contactService;

    @ApiOperation(value = "新增常用联系人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contact.myRole", value = "1:监护人 2：被监护人")
    })
    @PostMapping
    @ResponseBody
    public Contact saveContact(@RequestBody Contact contact) {
        if (Objects.isNull(contact)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactService.saveContact(contact);
    }

    @ApiOperation(value = "删除常用联系人")
    @DeleteMapping("/{userId}/{contactId}")
    public void deleteContact(@PathVariable String userId,
                              @PathVariable String contactId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        contactService.deleteContact(userId, contactId);
    }

    @ApiOperation(value = "更新常用联系人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contact.myRole", value = "1:监护人 2：被监护人")
    })
    @PutMapping
    @ResponseBody
    public Contact updateContact(@RequestBody Contact contact) {
        if (Objects.isNull(contact)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactService.updateContact(contact);
    }

    @ApiOperation(value = "根据用户ID查询")
    @GetMapping("/{userId}")
    @ResponseBody
    public List<Contact> queryContact(@PathVariable String userId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactService.queryUserContact(userId);
    }

    @ApiOperation(value = "查询联系人明细")
    @GetMapping("/{userId}/{contactId}")
    @ResponseBody
    public Contact queryContactDetail(@PathVariable String userId,
                                      @PathVariable String contactId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactService.queryContactDetail(contactId);
    }

}
