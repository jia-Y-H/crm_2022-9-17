package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.sun.org.apache.xml.internal.utils.res.XResourceBundle;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.*;

@WebListener
public class SysInitListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    public SysInitListener() {
    }

    //该方法用来监听上下文域对象的创建，当服务器启动，上下文域对象创建，对象创建完毕后，马上执行该方法
    @Override
    public void contextInitialized(ServletContextEvent sce) {   //该参数能够取得坚挺的对象，监听的是什么对象，就可以通过改参数取得什么对象
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        System.out.println("上下文域对象创建了");
        System.out.println("服务器缓存处理数据字典开始");
        ServletContext application = sce.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        //取数据字典
        Map<String, List<DicValue>> map = ds.getAll();
        Set<String> set = map.keySet();
        for(String key : set){
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");

        //数据字典处理完毕后，处理Stage2Possibility.properties文件
        Map<String, String> pMap = new HashMap<String, String>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
            String key = e.nextElement();       //阶段
            String value = rb.getString(key);       //可能性
            pMap.put(key, value);
        }
        //将pMap保存到服务器缓存中
        application.setAttribute("pMap", pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}
