package com.hm.action.vip;

import com.hm.action.AbstractPlayerAction;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;

/**
 * @description: vip认证
 * @author: chenwei
 * @create: 2019-12-18 10:31
 **/
@Action
public class VipAction extends AbstractPlayerAction {

    @Resource
    private VipBiz vipBiz;


}
