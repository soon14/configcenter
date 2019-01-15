package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcStaticDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 静态数据表数据库访问接口
 * Created by bawy on 2018/7/23 14:18.
 */
@Repository
public interface StaticDataRepository extends JpaRepository<CcStaticDataEntity, Integer>{

    List<CcStaticDataEntity> findByCodeTypeAndStatusOrderByExt1(String codeType, byte status);

}
