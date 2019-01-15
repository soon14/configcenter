package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcStrategyStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by oulc on 2018/8/9.
 */
@Repository
public interface StrategyStepRepository extends JpaRepository<CcStrategyStepEntity, Integer> {
    CcStrategyStepEntity findByIdAndStrategyIdAndStatus(int id,int strategyId , byte status);
    List<CcStrategyStepEntity> findByStrategyIdAndStatusOrderByStepNumber(int strategyId,byte status);
}
