package com.baymin.springboot.store.payload.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoReport {

    private long userCount;

    private long userHaveOrderCount;

}
