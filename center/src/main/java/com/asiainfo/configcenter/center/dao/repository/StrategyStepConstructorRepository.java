package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcStrategyStepConstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by oulc on 2018/8/9.
 */
@Repository
public interface StrategyStepConstructorRepository extends JpaRepository<CcStrategyStepConstructorEntity, Integer> {
    CcStrategyStepConstructorEntity findByStrategyStepIdAndStatus(int strategyStepId,byte status);
}
