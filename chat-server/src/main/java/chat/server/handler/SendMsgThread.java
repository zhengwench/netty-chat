package chat.server.handler;

import chat.server.channel.ChannelContextMap;

/**
 * @auther a-de
 * @date 2018/11/13 10:50
 */
public class SendMsgThread implements Runnable {
    ChannelContextMap channelContextMap;
    String msg;

    SendMsgThread(ChannelContextMap _channelContextMap,String _msg){
        channelContextMap = _channelContextMap;
        msg = _msg;
    }

    @Override
    public void run() {
        if (channelContextMap!=null){
            channelContextMap.sendAll(msg);
        }
    }
}
