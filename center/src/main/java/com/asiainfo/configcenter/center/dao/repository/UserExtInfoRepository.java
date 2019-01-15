package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcUserExtInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExtInfoRepository extends JpaRepository<CcUserExtInfoEntity, Integer> {
    CcUserExtInfoEntity findByUserIdAndExtInfoKeyAndStatus(int id ,String extInfoKey, byte status);

    CcUserExtInfoEntity findByUserIdAndExtInfoKeyAndExtInfoValueAndStatus(int id, String extInfoKey, String extInfoValue, byte status);
}
