package org.elastos.elaweb;



import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/5/20
 */
public class ElaHandle extends AbstractHandler {

    public void handle(String target, Request baseRequest, HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("Content-Type;text/plain");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        //通过输入流的方式获取Request中的json参数
        ServletInputStream inputStream = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer stringBuffer = new StringBuffer();
        java.lang.String tempOneLine ;
        while ((tempOneLine = bufferedReader.readLine())!= null){
            stringBuffer.append(tempOneLine);
        }
        String jsonString = stringBuffer.toString();

        PrintWriter out = response.getWriter();
        if (target.equals("/favicon.ico")) {
            out.println("404");
        } else
        {
            try {
                out.println(ElaController.processMethod(jsonString));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
