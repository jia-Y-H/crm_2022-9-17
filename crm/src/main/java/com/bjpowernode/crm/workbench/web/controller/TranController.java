package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/workbench/transaction/add.do","/workbench/transaction/getCustomerName.do",
             "/workbench/transaction/save.do","/workbench/transaction/detail.do",
             "/workbench/transaction/getHistoryListByTranId.do","/workbench/transaction/changeStage.do",
             "/workbench/transaction/getCharts.do"})
public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入交易控制器");
        String path = request.getServletPath();

        if("/workbench/transaction/add.do".equals(path)){
            add(request,response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request,response);
        } else if ("/workbench/transaction/save.do".equals(path)) {
            save(request,response);
        } else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request,response);
        } else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)) {
            getHistoryListByTranId(request,response);
        } else if ("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request,response);
        } else if ("/workbench/transaction/getCharts.do".equals(path)) {
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得交易阶段数量统计图表的数据");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map = ts.getCharts();
        PrintJson.printJsonObj(response, map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行改变阶段的操作");
        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editTime = DateTimeUtil.getSysTime();
        String editeBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditTime(editTime);
        t.setEditBy(editeBy);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.changeStage(t);
        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("t", t);
        map.put("success", flag);
        PrintJson.printJsonObj(response, map);
    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据交易id取得相应的历史列表");
        String tranId = request.getParameter("tranId");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> thList = ts.getHistoryListByTranId(tranId);
        //阶段和可能性之间的关系
        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        //将交易历史列表遍历
        for(TranHistory th : thList){
            //根据每一条交易历史，取出每一个阶段
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);

        }
        PrintJson.printJsonObj(response, thList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页");
        String id = request.getParameter("id");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t = ts.detail(id);
        //处理可能性
        String stage = t.getStage();
        //ServletContext application = this.getServletContext();
        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        request.setAttribute("t", t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("执行添加交易的操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();      //创建时间，系统当前时间
        String createBy = ((User)request.getSession().getAttribute("user")).getName();     //创建人，当前登录的用户
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Boolean flag = ts.save(t, customerName);

        if (flag) {
            //如果添加交易成功，跳转到列表页
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得客户名称列表（按照客户名称模糊查询）");
        String name = request.getParameter("name");
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> sList = cs.getCustomerName(name);
        PrintJson.printJsonObj(response, sList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转交易添加页的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList =  us.getUserList();
        request.setAttribute("uList", uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }
}
