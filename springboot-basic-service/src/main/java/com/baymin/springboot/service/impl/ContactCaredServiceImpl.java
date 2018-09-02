package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IContactCaredService;
import com.baymin.springboot.store.entity.ContactCared;
import com.baymin.springboot.store.repository.IContactCaredRepository;
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
public class ContactCaredServiceImpl implements IContactCaredService {

    @Autowired
    private IContactCaredRepository contactCaredRepository;

    @Override
    public ContactCared saveContactCared(ContactCared contactCared) {
        if ("T".equalsIgnoreCase(contactCared.getDefaultFlag())) {
            contactCaredRepository.updateDefault(contactCared.getUserId(), "F");
        } else {
            contactCared.setDefaultFlag("F");
        }
        contactCared.setCreateTime(new Date());
        return contactCaredRepository.save(contactCared);
    }

    @Override
    public void deleteContactCared(String userId, String contactCaredId) {
        contactCaredRepository.deleteById(contactCaredId);
    }

    @Override
    public ContactCared updateContactCared(ContactCared contactCared) {
        ContactCared contactInDB = contactCaredRepository.findById(contactCared.getId()).orElse(null);
        if (Objects.isNull(contactInDB)) {
            throw new WebServerException(HttpStatus.NOT_FOUND, new ErrorInfo(not_found.name(), RECORD_NOT_EXIST));
        }

        if ("T".equalsIgnoreCase(contactCared.getDefaultFlag())) {
            contactCaredRepository.updateDefault(contactCared.getUserId(), "F");
        }

        contactInDB.setContactName(contactCared.getContactName());
        contactInDB.setContactNumber(contactCared.getContactNumber());
        contactInDB.setDefaultFlag(contactCared.getDefaultFlag());
        contactInDB.setTall(contactCared.getTall());
        contactInDB.setWeight(contactCared.getWeight());
        contactInDB.setOtherInfo(contactCared.getOtherInfo());
        return contactCaredRepository.save(contactInDB);
    }

    @Override
    public List<ContactCared> queryUserContactCared(String userId) {
        return contactCaredRepository.findByUserId(userId);
    }

    @Override
    public ContactCared queryContactCaredDetail(String contactCaredId) {
        return contactCaredRepository.findById(contactCaredId).orElse(null);
    }
}
