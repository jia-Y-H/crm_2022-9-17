package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet({"/setting/user/login.do"})
public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入到用户模拟器");
        String path = request.getServletPath();

        if("/setting/user/login.do".equals(path)){
            login(request,response);
        }
    }



    private void login(HttpServletRequest request, HttpServletResponse response) {
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);
        String ip = request.getRemoteAddr();        //接收浏览器端IP地址
        System.out.println(ip+"-------------------------------------");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{
            User user = userService.login(loginAct, loginPwd, ip);
            request.getSession().setAttribute("user", user);
            //程序执行到此处，说明业务层没有为Controller抛出任何异常，表示登录成功
            PrintJson.printJsonFlag(response, true);


        }catch (Exception e){
            e.printStackTrace();    //执行此处表示业务层登录失败
            String msg = e.getMessage();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }
    }
}
