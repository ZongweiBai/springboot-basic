package com.baymin.springboot.store.dao.impl;

import com.baymin.springboot.store.dao.IInvoiceDao;
import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.repository.IInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceDaoImpl implements IInvoiceDao {

    @Autowired
    private IInvoiceRepository invoiceRepository;

    @Override
    public void save(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

}
