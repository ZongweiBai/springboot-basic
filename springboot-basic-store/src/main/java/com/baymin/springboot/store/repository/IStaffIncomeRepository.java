package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.StaffIncome;
import com.baymin.springboot.store.payload.StaffRankVo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IStaffIncomeRepository extends PagingAndSortingRepository<StaffIncome, String>,
        JpaSpecificationExecutor<StaffIncome>,
        QuerydslPredicateExecutor<StaffIncome> {
    List<StaffIncome> findByStaffId(String staffId);

    @Query("select COALESCE(sum(t.income),0) from StaffIncome t where t.staffId = :staffId and t.createTime between :minDate and :maxDate")
    double sumIncomeByDate(@Param("staffId") String staffId, @Param("minDate") Date monthFirst, @Param("maxDate") Date monthLast);

    @Query("select new com.baymin.springboot.store.payload.StaffRankVo(income.staffId, COALESCE(sum(income.income),0), staff.mobile, staff.userName) " +
            "from StaffIncome income left join ServiceStaff staff on income.staffId=staff.id " +
            "group by income.staffId order by COALESCE(sum(income.income),0) desc")
    List<StaffRankVo> queryStaffRank();
}
