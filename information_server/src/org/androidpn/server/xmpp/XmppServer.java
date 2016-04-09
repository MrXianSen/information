/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.xmpp;

import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.session.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** 
 * This class starts the server as a standalone application using Spring configuration.
 * 用来启动服务器
 */
public class XmppServer {

    private static final Log log = LogFactory.getLog(XmppServer.class);

    //定义一个自身的实例
    private static XmppServer instance;

    //上下文信息
    private ApplicationContext context;

    //版本号
    private String version = "0.5.0";

    //服务器名称
    private String serverName;

    private String serverHomeDir;

    private boolean shuttingDown;

    /**
     * Returns the singleton instance of XmppServer.
     *
     * @return the server instance.
     */
    public static XmppServer getInstance() {
    	//获取一个唯一的XMPPServer实例对象
        if (instance == null) {
            synchronized (XmppServer.class) {
                instance = new XmppServer();
            }
        }
        return instance;
    }

    /**
     * Constructor. Creates a server and starts it.
     */
    public XmppServer() {
        if (instance != null) {
            throw new IllegalStateException("A server is already running");
        }
        instance = this;
        start();
    }

    /**
     * Starts the server using Spring configuration.
     * 通过Spring文件启动服务器
     */
    public void start() {
        try {
            if (isStandAlone()) {
                Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
            }

          //  locateServer();
            serverName = Config.getString("xmpp.domain", "127.0.0.1").toLowerCase();
            //获取Spring文件的上下文信息
            context = new ClassPathXmlApplicationContext("spring-config.xml");
            log.info("Spring Configuration loaded.");

            log.info("XmppServer started: " + serverName);
            log.info("Androidpn Server v" + version);

        } catch (Exception e) {
            e.printStackTrace();
            shutdownServer();
        }
    }

    /**
     * Stops the server.
     */
    public void stop() {
        shutdownServer();
        Thread shutdownThread = new ShutdownThread();
        shutdownThread.setDaemon(true);
        shutdownThread.start();
    }

    /**
     * Returns a Spring bean that has the given bean name.
     * 获取一个Bean对象就是获取到了一个对应类的实例
     * @param beanName
     * @return a Spring bean 
     */
    public Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * Returns the server name.
     * 
     * @return the server name
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Returns true if the server is being shutdown.
     * 
     * @return true if the server is being down, false otherwise. 
     */
    public boolean isShuttingDown() {
        return shuttingDown;
    }

    /**
     * Returns if the server is running in standalone mode.
     * 
     * @return true if the server is running in standalone mode, false otherwise.
     */
    public boolean isStandAlone() {
        boolean standalone;
        try {
            standalone = Class
                    .forName("org.androidpn.server.starter.ServerStarter") != null;
        } catch (ClassNotFoundException e) {
            standalone = false;
        }
        return standalone;
    }

/*    private void locateServer() throws FileNotFoundException {
    	Context context = new WebAppContext(contexts, homeDir + File.separator
                + );
        String baseDir = "F:/Java���/Ӧ�����/Tomcat 6.0/webapps/ROOT";
        log.debug("base.dir=" + baseDir);

        if (serverHomeDir == null) {
            try {
                File confDir = new File(baseDir, "conf");
                if (confDir.exists()) {
                    serverHomeDir = confDir.getParentFile().getCanonicalPath();
                }
            } catch (FileNotFoundException fe) {
                // Ignore
            } catch (IOException ie) {
                // Ignore
            }
        }

        if (serverHomeDir == null) {
            System.err.println("Could not locate home.");
            throw new FileNotFoundException();
        } else {
            Config.setProperty("server.home.dir", serverHomeDir);
            log.debug("server.home.dir=" + serverHomeDir);
        }
    }*/

    private void shutdownServer() {
        shuttingDown = true;
        // Close all connections
        SessionManager.getInstance().closeAllSessions();
        log.info("XmppServer stopped");
    }

    private class ShutdownHookThread extends Thread {
        @Override
		public void run() {
            shutdownServer();
            log.info("Server halted");
            System.err.println("Server halted");
        }
    }

    private class ShutdownThread extends Thread {
        @Override
		public void run() {
            try {
                Thread.sleep(5000);
                System.exit(0);
            } catch (InterruptedException e) {
            }
        }
    }

}
