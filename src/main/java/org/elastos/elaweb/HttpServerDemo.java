package org.elastos.elaweb;


import org.eclipse.jetty.server.Server;

/**
 * 启动web服务
 * @param  port 服务器端口
 */
public class HttpServerDemo {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8989);
        server.setHandler(new ElaHandle());
        server.start();
        server.join();
    }
}


