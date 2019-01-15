package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcAuthCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;

/**
 * 验证码数据库访问接口
 * Created by bawy on 2018/7/19 23:37.
 */
public interface AuthCodeRepository extends JpaRepository<CcAuthCodeEntity, Integer> {

    CcAuthCodeEntity findByCodeAndStatusAndExpireTimeAfter(String code, byte status, Timestamp currentTime);

}
