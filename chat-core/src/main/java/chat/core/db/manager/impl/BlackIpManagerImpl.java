package chat.core.db.manager.impl;

import chat.core.common.domain.AppConfig;
import chat.core.common.domain.ManagerException;
import chat.core.common.domain.PageResult;
import chat.core.common.redis.RedisManager;
import chat.core.db.manager.BlackIpManager;
import chat.core.db.mapper.BlackIpMapper;
import chat.core.db.model.BlackIp;
import chat.core.db.model.query.BlackIpQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/7 12:37
 */
@Component
public class BlackIpManagerImpl implements BlackIpManager {
    private Logger logger = LoggerFactory.getLogger(BlackIpManagerImpl.class);

    @Autowired
    private BlackIpMapper blackIpMapper;

    @Override
    public boolean insert(BlackIp blackIp) {
        try {
            if (blackIpMapper.insert(blackIp)>0){
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("黑名单insert异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public boolean delete(Long domainId, Long id) {
        try {
            BlackIp blackIp = blackIpMapper.queryById(id);
            if (blackIp!=null){
                if (blackIpMapper.delete(id,domainId)>0){
                    try {
                        String key = AppConfig.IpCache + domainId + blackIp.getIp();
                        RedisManager.removeObject(key);
                    } catch (Exception e) {
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("黑名单delete异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public BlackIp queryById(Long id) {
        try {
            return blackIpMapper.queryById(id);
        } catch (Exception e) {
            logger.error("黑名单queryById异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public BlackIp queryByIdAndDomainId(Long id, Long domainId) {
        try {
            return blackIpMapper.queryByIdAndDomainId(id,domainId);
        } catch (Exception e) {
            logger.error("黑名单queryByIdAndDomainId异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public PageResult<BlackIp> queryPage(BlackIpQuery query) {
        PageResult<BlackIp> pageResult = new PageResult<>();
        try {
            List<BlackIp> list = new ArrayList<>();
            int count = blackIpMapper.queryPageCount(query);
            if (count>0){
                list = blackIpMapper.queryPageList(query);
            }

            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("黑名单queryPage异常",e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    public BlackIp queryByIp(Long domainId, String ip) {
        try {
            return blackIpMapper.queryByIp(domainId,ip);
        } catch (Exception e) {
            logger.error("黑名单queryByIp异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public BlackIp queryByIpCache(Long domainId, String ip) {
        try {
            String key = AppConfig.IpCache + domainId + ip;
            try {
                if (RedisManager.existsObject(key)){
                    BlackIp blackIp =(BlackIp) RedisManager.getObject(key);
                    return blackIp;
                }
            } catch (Exception e) {
                logger.error("redis id查询黑名单异常",e);
            }
            BlackIp blackIp = blackIpMapper.queryByIp(domainId,ip);
            try {
                RedisManager.putObject(key,blackIp);
            } catch (Exception e) {
            }
            return blackIp;
        } catch (Exception e) {
            logger.error("黑名单queryByIpCache异常",e);
            throw new ManagerException(e);
        }
    }
}
