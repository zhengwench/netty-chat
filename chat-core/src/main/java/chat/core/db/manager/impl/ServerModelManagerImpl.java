package chat.core.db.manager.impl;

import chat.core.common.domain.ManagerException;
import chat.core.common.domain.PageResult;
import chat.core.db.manager.ServerModelManager;
import chat.core.db.mapper.ServerModelMapper;
import chat.core.db.model.ServerModel;
import chat.core.db.model.query.ServerModelQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/24 20:17
 */
@Component
public class ServerModelManagerImpl implements ServerModelManager {
    private Logger logger = LoggerFactory.getLogger(ServerModelManagerImpl.class);

    @Autowired
    private ServerModelMapper serverModelMapper;

    @Override
    public boolean insert(ServerModel serverModel) {
        try {
            if (serverModelMapper.insert(serverModel)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("服务insert异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public boolean update(ServerModel serverModel) {
        try {
            if (serverModelMapper.update(serverModel)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("服务update异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public PageResult<ServerModel> queryPage(ServerModelQuery query) {
        PageResult<ServerModel> pageResult = new PageResult<>();
        try {
            List<ServerModel> list = new ArrayList<>();
            int count = serverModelMapper.queryPageCount(query);
            if (count > 0) {
                list = serverModelMapper.queryPageList(query);
                if (list!=null&&list.size()>0){
                    for (ServerModel s : list){

                    }
                }
            }
            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("服务queryPage异常", e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    public boolean delete(Long id) {
        try {
            if (serverModelMapper.delete(id)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("服务delete异常", e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public ServerModel queryById(Long id) {
        try {
            return serverModelMapper.queryById(id);
        } catch (Exception e) {
            logger.error("服务queryById异常", e);
            throw new ManagerException(e);
        }
    }
}
