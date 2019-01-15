package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcStrategyStepMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by oulc on 2018/8/10.
 */
@Repository
public interface StrategyStepMethodRepository extends JpaRepository<CcStrategyStepMethodEntity, Integer> {
    CcStrategyStepMethodEntity findByStrategyStepIdAndStatus(int strategyStepId,byte status);
}
