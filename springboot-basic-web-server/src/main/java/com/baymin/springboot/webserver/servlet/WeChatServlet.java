package com.baymin.springboot.webserver.servlet;

import com.baymin.springboot.common.util.XmlUtil;
import com.baymin.springboot.pay.wechat.util.SignUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class WeChatServlet extends HttpServlet {

    /**
     * Constructor of the object.
     */
    public WeChatServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
    }

    /**
     * The doGet method of the servlet. <br>
     * <p>
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws IOException if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        log.info("收到微信验证请求");
        // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                log.debug("检查签名成功");
                out.print(echostr);
            } else {
                log.debug("检查签名失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
            out = null;
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String result = "success";
        try {
            Map<String, String> map = XmlUtil.parseXml(request);
            log.debug("收到微信的通知：{}", map);
            String msgType = map.get("MsgType");
            String fromUserName = map.get("FromUserName");// 发送方帐号（一个OpenID）
            String toUserName = map.get("ToUserName"); // 开发者微信号

            if (msgType.equals("event")) {// 判断消息类型(event=点击菜单触发的消息类型)
                String event = map.get("Event");
                if (event.equals("CLICK")) {// 菜单点击事件

                } else if (event.equals("location_select")) {// 点击菜单跳转上报地理位置事件

                } else if (event.equals("VIEW")) {// 点击菜单跳转链接时的事件

                } else if (event.equals("scancode_waitmsg")) {// 点击扫码事件

                }
            } else if (msgType.equals("text")) {
                String content = map.get("Content"); // 用户发送的消息内容
                System.out.println(fromUserName + "给后台发送了消息：" + content);

            } else if (msgType.equals("location")) {

                System.out.println(fromUserName + "给后台发送了位置消息");

            } else if (msgType.equals("image")) {

                System.out.println(fromUserName + "给后台发送了图片");

            }

        } catch (Exception e) {
            log.error("处理微信通知出现异常", e);
        }
        out.print(result);
        out.flush();
        out.close();
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }

}
