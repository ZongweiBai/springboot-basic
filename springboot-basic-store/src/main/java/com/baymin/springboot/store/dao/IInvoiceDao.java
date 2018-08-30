package com.baymin.springboot.store.dao;

import com.baymin.springboot.store.entity.Invoice;

public interface IInvoiceDao {
    void save(Invoice invoice);
}
