package org.androidpn.server.xmpp.handler;


import org.androidpn.server.service.NotificationService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

/**
 * 处理客户端发回的消息回执
 * @author 张建国
 *
 */
public class IQDeliverConfirmHandler extends IQHandler {
	
	//命名空间
	//注意：必须和客户端回执过来的命名空间相同
	private static final String NAMESPACE = "androidpn:iq:deliverconfirm";
	
	private NotificationService notificationService ;
	
	public IQDeliverConfirmHandler() {
		//获取NotificaitonService的实例
		notificationService = ServiceLocator.getNotificationService() ;
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ reply = null;

        ClientSession session = sessionManager.getSession(packet.getFrom());
        if (session == null) {
            log.error("Session not found for key " + packet.getFrom());
            reply = IQ.createResultIQ(packet);
            reply.setChildElement(packet.getChildElement().createCopy());
            reply.setError(PacketError.Condition.internal_server_error);
            return reply;
        }
        //如果会话（session）的状态为已经注册STATUS_AUTHENTICATED
        if(session.getStatus() == Session.STATUS_AUTHENTICATED){
        	if(IQ.Type.set.equals(packet.getType())){
        		Element element = packet.getChildElement() ;
        		String uuid = element.elementText("uuid") ;
        		/*
        		 * 用户回执一条消息之后就表示用户上线，收取到了推送消息
        		 * 然后就通过UUID将数据库中的这条消息删除掉
        		 */
        		notificationService.deleteNotificationByUUid(uuid);
        	}
        }
		return null;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

}
