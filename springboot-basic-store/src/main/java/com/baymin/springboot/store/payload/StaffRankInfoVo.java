package com.baymin.springboot.store.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRankInfoVo {

    private int myRank;

    private List<StaffRankVo> staffRankVoList;

}
