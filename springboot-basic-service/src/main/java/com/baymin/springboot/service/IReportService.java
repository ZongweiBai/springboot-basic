package com.baymin.springboot.service;

import com.baymin.springboot.store.payload.report.PlatformOrderReport;

import java.util.Date;

public interface IReportService {

    PlatformOrderReport queryPlatformOrderReport(Date minDate, Date maxDate);

}
