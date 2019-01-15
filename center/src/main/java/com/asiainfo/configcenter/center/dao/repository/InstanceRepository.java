package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by oulc on 2018/7/26.
 */
@Repository
public interface InstanceRepository extends JpaRepository<CcInstanceEntity, Integer>, JpaSpecificationExecutor<CcInstanceEntity> {

    @Query(value = "select count(a) from CcInstanceEntity a where a.envId = :envId and a.isAlive = :isAlive and a.status = 1")
    long findCountByEnvIdAndIsAlive(@Param(value = "envId") int envId,@Param(value = "isAlive") byte isAlive);

    List<CcInstanceEntity> findByEnvIdAndIsAliveAndStatus(int envId,byte isAlive,byte status);

    List<CcInstanceEntity> findByEnvIdAndInsNameAndStatus(int envId,String insName,byte status);

    CcInstanceEntity findByIdAndStatus(int id,byte status);

    List<CcInstanceEntity> findAllByIdInAndStatus(int []ids,byte status);

    List<CcInstanceEntity> findAllByEnvIdAndIdNotInAndStatusAndIsAliveAndInsNameLikeAndInsIpLike(int envId,int []ids,byte status,byte isAlive,String insName,String insIp);

    CcInstanceEntity findByIdAndInsNameLikeAndInsIpLikeAndStatusAndIsAlive(int id,String insName,String insIp,byte status,byte isAlive);

}
