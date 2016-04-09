package org.androidpn.server.xmpp.handler;


import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.androidpn.server.xmpp.session.SessionManager;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

/**
 * 处理客户端发回的消息回执
 * @author Administrator
 *
 */
public class IQSetAliasHandler extends IQHandler {
	
	private SessionManager sessionManager ;
	
	
	//命名空间
	//注意：必须和客户端回执过来的命名空间相同
	private static final String NAMESPACE = "androidpn:iq:setalias";
	
	public IQSetAliasHandler() {
		//获取NotificaitonService的实例
		sessionManager = SessionManager.getInstance() ;
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
        		String username = element.elementText("username") ;
        		System.out.println("用户姓名："+username);
        		String alias = element.elementText("alias") ;
        		System.out.println("别名："+alias);
        		if(username != null && !username.equals("") 
        				&& alias != null && !alias.equals(""))
        		sessionManager.setUserAlias(username, alias);
        	}
        }
		return null;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

}
