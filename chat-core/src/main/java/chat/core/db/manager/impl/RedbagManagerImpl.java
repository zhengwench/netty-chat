package chat.core.db.manager.impl;

import chat.core.common.domain.ManagerException;
import chat.core.common.domain.PageResult;
import chat.core.common.utility.RedBagUtility;
import chat.core.db.manager.RedbagManager;
import chat.core.db.mapper.RedbagMapper;
import chat.core.db.model.Redbag;
import chat.core.db.model.query.RedBagQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/7 13:03
 */
@Component
public class RedbagManagerImpl implements RedbagManager {
    private Logger logger = LoggerFactory.getLogger(RedbagManagerImpl.class);

    @Autowired
    private RedbagMapper redbagMapper;

    @Override
    public boolean insert(Redbag redbag) {
        try {
            if (redbagMapper.insert(redbag)>0){
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("红包insert异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public boolean update(Redbag redbag) {
        try {
            if (redbagMapper.update(redbag)>0){
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("红包update异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public PageResult<Redbag> queryPage(RedBagQuery query) {
        PageResult<Redbag> pageResult = new PageResult<>();
        try {
            List<Redbag> list = new ArrayList<>();
            int count = redbagMapper.queryPageCount(query);
            if (count>0){
                list = redbagMapper.queryPageList(query);
            }

            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("红包queryPage异常",e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    @Transactional
    public Redbag createRedbag(String domian, Redbag redbag) {
        try {
            if (redbagMapper.insert(redbag)>0){
                RedBagUtility.createRedBag(domian,redbag.getId(),redbag.getAmount(),redbag.getCount());
                return redbag;
            }
            return null;
        } catch (Exception e) {
            logger.error("红包createRedbag异常",e);
            throw new ManagerException(e);
        }
    }
}
