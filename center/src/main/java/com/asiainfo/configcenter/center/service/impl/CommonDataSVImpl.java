package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.dao.repository.StaticDataRepository;
import com.asiainfo.configcenter.center.entity.CcStaticDataEntity;
import com.asiainfo.configcenter.center.service.interfaces.ICommonDataSV;
import com.asiainfo.configcenter.center.service.interfaces.IRoleSV;
import com.asiainfo.configcenter.center.service.interfaces.IUserSV;
import com.asiainfo.configcenter.center.vo.system.SelectDataVO;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 公共数据服务实现类
 * Created by bawy on 2018/7/23 14:17.
 */
@Service
public class CommonDataSVImpl implements ICommonDataSV {

    //private Map<String, List<SelectDataVO>> selectDataCache = new HashMap<>();
    @Resource
    private HashOperations<String, String, List<SelectDataVO>> hashOperations;

    @Resource
    private StaticDataRepository staticDataRepository;
    @Resource
    private IUserSV userSV;

    @Resource
    private IRoleSV roleSV;

    @Override
    public List<SelectDataVO> getSelectData(String key) {
        Assert4CC.hasLength(key, "下拉列表数据对应的key不可为空");
        //List<SelectDataVO> selectDatas = selectDataCache.get(key);
        //HashOperations<String, String, List<SelectDataVO>> hashOperations = redisTemplate.opsForHash();
        List<SelectDataVO> selectDatas = hashOperations.get(ProjectConstants.REDIS_STATIC_DATA_KEY, key);
        if (selectDatas  == null) {
            List<CcStaticDataEntity> staticDataEntities = staticDataRepository.findByCodeTypeAndStatusOrderByExt1(key, ProjectConstants.STATUS_VALID);
            if (staticDataEntities != null) {
                selectDatas = new ArrayList<>();
                for (CcStaticDataEntity entity : staticDataEntities) {
                    SelectDataVO selectData = new SelectDataVO();
                    selectData.setValue(entity.getCodeValue());
                    selectData.setText(entity.getCodeText());
                    selectData.setExt2(entity.getExt2());
                    selectData.setEx3(entity.getExt3());
                    selectDatas.add(selectData);
                }
            }
            if (selectDatas == null || selectDatas.size()==0){
                throw new ErrorCodeException(ResultCodeEnum.SELECT_KEY_ERROR);
            }else{
                //selectDataCache.put(key, selectDatas);
                hashOperations.put(ProjectConstants.REDIS_STATIC_DATA_KEY, key, selectDatas);
            }
        }
        return selectDatas;
    }

    @Override
    public void cleanCache(int userId) {
        Assert4CC.isTrue(userSV.isAdministrator(userId), ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "当前登录用户不是管理员");
        //selectDataCache.clear();
        hashOperations.getOperations().delete(ProjectConstants.REDIS_STATIC_DATA_KEY);
        userSV.cleanCache();
        roleSV.cleanCache();
        PermissionCommon.cleanCache();
    }

}
