package com.baymin.springboot.store.payload.report;

import lombok.Data;

@Data
public class PlatformOrderReport {

    private double orderTotalFee;

    private long validOrderCount;

    private double validOrderTotalFee;

    private long invalidOrderCount;

    private double invalidOrderTotalFee;

    private long refundOrderCount;

    private double refundOrderTotalFee;

}
