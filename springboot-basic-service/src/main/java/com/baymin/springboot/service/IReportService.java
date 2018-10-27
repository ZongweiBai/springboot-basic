package com.baymin.springboot.service;

import com.baymin.springboot.store.payload.report.PlatformOrderReport;
import com.baymin.springboot.store.payload.report.ServiceStaffReport;
import com.baymin.springboot.store.payload.report.UserInfoReport;

import java.util.Date;

public interface IReportService {

    PlatformOrderReport queryPlatformOrderReport(Date minDate, Date maxDate);

    UserInfoReport queryUserInfoReport(Date minDate, Date maxDate);

    ServiceStaffReport queryServiceStaffReport(Date minDate, Date maxDate);
}
