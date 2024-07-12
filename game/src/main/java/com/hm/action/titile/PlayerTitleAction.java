package com.hm.action.titile;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;

/**
 * ClassName: PlayerTitleAction. <br/>  
 * Function: 玩家称号的处理. <br/>  
 * date: 2019年4月28日 下午2:32:11 <br/>  
 * @author zxj  
 * @version
 */
@Action
public class PlayerTitleAction extends AbstractPlayerAction{

	@Resource
    private TitleBiz titleBiz;
	
	/**
	 * changeTitle:(更好称号). <br/>  
	 * @author zxj  
	 * @param player
	 * @param msg  使用说明
	 */
    @MsgMethod(MessageComm.C2S_Change_Title)
    public void changeTitle(Player player, JsonMsg msg) {
    	int id = msg.getInt("id");
    	if(!player.playerTitle().haveTitle(id)){
    		player.sendErrorMsg(SysConstant.Player_Title_Null);
    		return;
    	}
    	player.playerTitle().useTitle(id);
    	player.notifyObservers(ObservableEnum.ChangeTitle);
    	player.sendUserUpdateMsg();
    	player.sendMsg(MessageComm.S2C_Change_Title, SysConstant.YES);
    }
    
  	/**
  	 * 获取称号的列表
  	 * @param player
  	 * @param msg
  	 */
    @MsgMethod(MessageComm.C2S_Get_Title)
    public void getTitle(Player player, JsonMsg msg) {
    	titleBiz.sendTitleList(player);
    }
    /**
     * 用于测试称号的，使用完后，注释
     * TODO 注释掉
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Get_TitleTest)
    public void getTitleTest(Player player, JsonMsg msg) {
    	/*int type = msg.getInt("type");
    	titleBiz.addTitle(player, PlayerTitleType.getType(type));*/
    }
}




