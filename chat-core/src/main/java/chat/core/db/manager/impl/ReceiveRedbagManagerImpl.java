package chat.core.db.manager.impl;

import chat.core.common.domain.ManagerException;
import chat.core.common.domain.PageResult;
import chat.core.common.utility.RedBagUtility;
import chat.core.db.manager.ReceiveRedbagManager;
import chat.core.db.mapper.DomainConfigMapper;
import chat.core.db.mapper.ReceiveRedbagMapper;
import chat.core.db.mapper.RedbagMapper;
import chat.core.db.mapper.UserMapper;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.ReceiveRedbag;
import chat.core.db.model.Redbag;
import chat.core.db.model.User;
import chat.core.db.model.query.ReceiveRedBagQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/7 12:59
 */
@Component
public class ReceiveRedbagManagerImpl implements ReceiveRedbagManager {
    private Logger logger = LoggerFactory.getLogger(ReceiveRedbagManagerImpl.class);

    @Autowired
    private ReceiveRedbagMapper receiveRedbagMapper;

    @Autowired
    private DomainConfigMapper domainConfigMapper;

    @Autowired
    private RedbagMapper redbagMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean insert(ReceiveRedbag receiveRedbag) {
        try {
            if (receiveRedbagMapper.insert(receiveRedbag)>0){
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("领取红包insert异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public boolean update(ReceiveRedbag receiveRedbag) {
        try {
            if (receiveRedbagMapper.update(receiveRedbag)>0){
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("领取红包update异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public PageResult<ReceiveRedbag> queryPage(ReceiveRedBagQuery query) {
        PageResult<ReceiveRedbag> pageResult = new PageResult<>();
        try {
            List<ReceiveRedbag> list = new ArrayList<>();
            int count = receiveRedbagMapper.queryPageCount(query);
            if (count>0){
                list = receiveRedbagMapper.queryPageList(query);
            }

            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("领取红包queryPage异常",e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    @Transactional
    public ReceiveRedbag receiveRedbag(String domain, Long redBagId, Long userId) {
        try {
            DomainConfig domainConfig = domainConfigMapper.queryByDomainName(domain);
            if (domainConfig == null){
                return null;
            }
            Redbag redbag = redbagMapper.queryByIdForLock(redBagId);
            if (redbag == null || !domainConfig.getId().equals(redbag.getDomainId())){
                return null;
            }
            BigDecimal value = RedBagUtility.receiveRedBag(domain,redBagId);
            if (value==null){
                return null;
            }
            User user = userMapper.queryById(userId);
            if (user == null){
                return null;
            }
            ReceiveRedbag insert = new ReceiveRedbag();
            insert.setDomainId(domainConfig.getId());
            insert.setRedbagId(redBagId);
            insert.setReceiveUserId(user.getId());
            insert.setReceiveUserName(user.getUserName());
            insert.setSendUserId(redbag.getSendUserId());
            insert.setSendUserName(redbag.getSendUserName());
            insert.setSendUserIcon(redbag.getSendUserIcon());
            insert.setAmount(value);
            insert.setStatus(1);
            receiveRedbagMapper.insert(insert);
            return insert;
        } catch (Exception e) {
            logger.error("领取红包receiveRedbag异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public ReceiveRedbag queryById(Long id) {
        try {
            return receiveRedbagMapper.queryById(id);
        } catch (Exception e) {
            logger.error("领取红包queryById异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public ReceiveRedbag queryByIdAndDomainId(Long id, Long domainId) {
        try {
            return receiveRedbagMapper.queryByIdAndDomainId(id,domainId);
        } catch (Exception e) {
            logger.error("领取红包queryByIdAndDomainId异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public List<ReceiveRedbag> queryUserReceived(Long domainId,Long userId) {
        try {
            return receiveRedbagMapper.queryUserReceived(domainId,userId);
        } catch (Exception e) {
            logger.error("领取红包查询用户红包异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public ReceiveRedbag userReceivedRedbag(String domain, Long redBagId, Long userId) {
        try {
            DomainConfig domainConfig = domainConfigMapper.queryByDomainName(domain);
            if (domainConfig == null){
                return null;
            }
            return receiveRedbagMapper.userReceivedRedbag(domainConfig.getId(),userId,redBagId);
        } catch (Exception e) {
            logger.error("领取红包查询用户领取红包数据异常",e);
            throw new ManagerException(e);
        }
    }
}
