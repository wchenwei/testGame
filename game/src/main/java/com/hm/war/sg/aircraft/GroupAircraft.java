package com.hm.war.sg.aircraft;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.war.sg.Frame;
import com.hm.war.sg.skillnew.Skill;
import lombok.Data;

import java.util.List;

/**
 * @Description: 战斗系统-航母
 * @author siyunlong  
 * @date 2020年12月11日 下午2:04:52 
 * @version V1.0
 */
@Data
public class GroupAircraft {
	private int mp = 5;//初始油量
	private int curMp = 5;//当前油量
	//飞机队列
	private List<PlaneSkill> planeList = Lists.newArrayList();
	//释放技能的
	private int index;
	
	public boolean canAddMp(Frame frame) {
		return frame.getId() == 1 || frame.getId() > 0 && frame.getId()%10 == 0 
				&&this.index < this.planeList.size();
	}

	public void addCurMp(int val) {
		this.curMp += val;
	}
	
	public void addPlane(PlaneSkill planeSkill) {
		this.planeList.add(planeSkill);
	}
	
	public void checkMp(Frame frame) {
		if(frame.getId() > 1) {
			this.curMp += 1;
		}
	}
	
	public Skill isCanReleaseSkill(long curFrame) {
		PlaneSkill curPlaneSkill = getCurPlaneSkill();
		if(curPlaneSkill == null) {
			return null;
		}
		if(curMp >= curPlaneSkill.getMp()) {
			return curPlaneSkill.getSkill();
		}
		return null;
	}
	
	public void doSkillAfter() {
		this.index ++;
		this.curMp = 0;
	}
	
	public PlaneSkill getCurPlaneSkill() {
		if(index >= planeList.size()) {
			return null;
		}
		return planeList.get(index);
	}

    public boolean havePlaneSkill() {
        return CollUtil.isNotEmpty(planeList);
    }
	
}
