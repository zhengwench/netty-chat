package chat.core.db.manager.impl;

import chat.core.common.domain.ManagerException;
import chat.core.common.domain.PageResult;
import chat.core.db.manager.UserRobotManager;
import chat.core.db.mapper.UserRobotMapper;
import chat.core.db.model.UserRobot;
import chat.core.db.model.query.UserRobotQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @auther a-de
 * @date 2018/11/19 13:35
 */
@Component
public class UserRobotManagerImpl implements UserRobotManager {
    private Logger logger = LoggerFactory.getLogger(UserRobotManagerImpl.class);
    @Autowired
    private UserRobotMapper userRobotMapper;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public boolean insert(UserRobot userRobot) {
        try {
            if (userRobotMapper.insert(userRobot)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("机器人 insert异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public void insertBatch(Integer count,Long domainId,Long roomId) {
        if (count>500){
            count = 500;
        }
        int times = count % 50 > 0 ? count / 50 + 1 : count / 50;
        boolean remain = count % 50 > 0;
        for (int i = 1; i <= times; i++) {
            if (remain && i == times){
                int executeCount = count % 50;
                taskExecutor.execute(new InsertBatchThread(executeCount,domainId,roomId));
            }else {
                taskExecutor.execute(new InsertBatchThread(50,domainId,roomId));
            }
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            if (userRobotMapper.delete(id)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("机器人 delete异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public void deleteBatch(List<Long> list, Long domainId) {
        try {
            if (list!=null && list.size()>0){
                userRobotMapper.deleteBatch(list,domainId);
            }
        } catch (Exception e) {
            logger.error("机器人 deleteBatch异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public PageResult<UserRobot> queryPage(UserRobotQuery query) {
        PageResult<UserRobot> pageResult = new PageResult<>();
        try {
            List<UserRobot> list = new ArrayList<>();
            int count = userRobotMapper.queryPageCount(query);
            if (count>0){
                list = userRobotMapper.queryPageList(query);
            }
            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("机器人 queryPage异常",e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    public List<UserRobot> queryList(Integer count) {
        try {
            return userRobotMapper.queryList(count);
        } catch (Exception e) {
            logger.error("机器人 queryList异常",e);
            throw new ManagerException(e);
        }
    }

    class InsertBatchThread implements Runnable{
        private Integer executeCount;
        private Long domainId;
        private Long roomId;

        public InsertBatchThread(Integer _executeCount,Long _domainId,Long _roomId){
            executeCount = _executeCount;
            domainId = _domainId;
            roomId = _roomId;
        }

        @Override
        public void run() {
            List<UserRobot> list = new ArrayList<>();
            UserRobot userRobot = null;
            for (int i = 0;i<executeCount;i++){
                userRobot = new UserRobot();
                userRobot.setDomainId(domainId);
                userRobot.setRoomId(roomId);
                userRobot.setUserName(randomName());
                userRobot.setIcon("");
                list.add(userRobot);
            }
            userRobotMapper.insertBatch(list);
        }
    }

    private String randomName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("游客");
        try {
            String[] charArray = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
            Random random = new Random();
            for (int i = 0; i < 7; i++) {
                int index = random.nextInt(26);
                String indexvalue = charArray[index];
                stringBuilder.append(indexvalue);
            }
        } catch (Exception e) {
        }
        return stringBuilder.toString();
    }
}
