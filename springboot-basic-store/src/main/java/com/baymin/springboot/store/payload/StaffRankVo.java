package com.baymin.springboot.store.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRankVo {

    private String staffId;

    private Double totalIncome;

    private String mobile;

    private String userName;

}
