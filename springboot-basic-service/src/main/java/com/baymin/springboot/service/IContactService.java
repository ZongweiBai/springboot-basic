package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Contact;

import java.util.List;

public interface IContactService {
    Contact saveContact(Contact contact);

    void deleteContact(String userId, String contactId);

    Contact updateContact(Contact contact);

    List<Contact> queryUserContact(String userId);

    Contact queryContactDetail(String contactId);
}
