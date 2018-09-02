package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.ContactCared;

import java.util.List;

public interface IContactCaredService {
    ContactCared saveContactCared(ContactCared contactCared);

    void deleteContactCared(String userId, String contactCaredId);

    ContactCared updateContactCared(ContactCared contactCared);

    List<ContactCared> queryUserContactCared(String userId);

    ContactCared queryContactCaredDetail(String contactCaredId);
}
