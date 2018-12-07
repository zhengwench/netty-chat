package chat.core.db.manager.impl;

import chat.core.common.domain.ManagerException;
import chat.core.common.domain.PageResult;
import chat.core.common.domain.ResultDo;
import chat.core.db.manager.SystemDictManager;
import chat.core.db.mapper.SystemDictMapper;
import chat.core.db.model.SystemDict;
import chat.core.db.model.query.SystemDictQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/12 13:42
 */
@Component
public class SystemDictManagerImpl implements SystemDictManager {
    private Logger logger = LoggerFactory.getLogger(SystemDictManagerImpl.class);

    @Autowired
    private SystemDictMapper systemDictMapper;

    @Override
    public boolean insert(SystemDict systemDict) {
        try {
            if (systemDictMapper.insert(systemDict)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("SystemDictManagerImpl insert异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public boolean update(SystemDict systemDict) {
        try {
            if (systemDictMapper.update(systemDict)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("SystemDictManagerImpl update异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public boolean remove(Long id, Long domainId) {
        try {
            if (systemDictMapper.remove(id,domainId)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("SystemDictManagerImpl remove异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public SystemDict queryById(Long id, Long domainId) {
        try {
            return systemDictMapper.queryById(id,domainId);
        } catch (Exception e) {
            logger.error("SystemDictManagerImpl queryById异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public SystemDict queryByKey(Long domainId, String group, String key) {
        ResultDo<SystemDict> resultDo = new ResultDo<>();
        try {
            return systemDictMapper.queryByKey(domainId,group,key);
        } catch (Exception e) {
            logger.error("SystemDictManagerImpl queryByKey异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public List<SystemDict> queryGroupAllByDomainId(Long domainId, String group) {
        try {
            return systemDictMapper.queryGroupAllByDomainId(domainId,group);
        } catch (Exception e) {
            logger.error("SystemDictManagerImpl queryGroupAllByDomainId异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public PageResult<SystemDict> queryGroupAllPage(SystemDictQuery query) {
        PageResult<SystemDict> pageResult = new PageResult<>();
        try {
            List<SystemDict> list = new ArrayList<>();
            int count = systemDictMapper.queryGroupAllPageCount(query);
            if (count>0){
                list = systemDictMapper.queryGroupAllPageList(query);
            }

            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl queryGroupAllPage异常",e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    public List<SystemDict> queryGroupKey(Long domainId, String sysGroup, String sysKey) {
        try {
            return systemDictMapper.queryGroupKey(domainId,sysGroup,sysKey);
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl queryGroupKey异常",e);
            throw new ManagerException(e);
        }
    }
}
