package com.bjpowernode.workbench.test;

import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import org.junit.Assert;
import org.junit.Test;

public class ActivityTest {
    @Test
    public void testSave(){
        Activity a = new Activity();
        a.setId(UUIDUtil.getUUID());
        a.setName("宣传推广费");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = as.save(a);
        Assert.assertEquals(flag, true);
        //System.out.println(flag);
    }

    @Test
    public void testUpdate(){

    }
}
