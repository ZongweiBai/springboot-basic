package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IContactService;
import com.baymin.springboot.store.entity.Contact;
import com.baymin.springboot.store.repository.IContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorCode.not_found;
import static com.baymin.springboot.common.exception.ErrorDescription.RECORD_NOT_EXIST;

@Service
@Transactional
public class ContactServiceImpl implements IContactService {

    @Autowired
    private IContactRepository contactRepository;

    @Override
    public Contact saveContact(Contact contact) {
        if ("T".equalsIgnoreCase(contact.getDefaultFlag())) {
            contactRepository.updateDefault(contact.getUserId(), contact.getMyRole(), "F");
        }

        contact.setCreateTime(new Date());
        return contactRepository.save(contact);
    }

    @Override
    public void deleteContact(String userId, String contactId) {
        contactRepository.deleteById(contactId);
    }

    @Override
    public Contact updateContact(Contact contact) {
        Contact contactInDB = contactRepository.findById(contact.getId()).orElse(null);
        if (Objects.isNull(contactInDB)) {
            throw new WebServerException(HttpStatus.NOT_FOUND, new ErrorInfo(not_found.name(), RECORD_NOT_EXIST));
        }

        if ("T".equalsIgnoreCase(contact.getDefaultFlag())) {
            contactRepository.updateDefault(contact.getUserId(), contact.getMyRole(), "F");
        }

        contactInDB.setContactName(contact.getContactName());
        contactInDB.setContactNumber(contact.getContactNumber());
        contactInDB.setDefaultFlag(contact.getDefaultFlag());
        contactInDB.setMyRole(contact.getMyRole());
        contactInDB.setSex(contact.getSex());
        return contactRepository.save(contactInDB);
    }

    @Override
    public List<Contact> queryUserContact(String userId) {
        return contactRepository.findByUserId(userId);
    }

    @Override
    public Contact queryContactDetail(String contactId) {
        return contactRepository.findById(contactId).orElse(null);
    }
}
