package com.hm.enums;


import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

public enum NoticeTypeEnum {
	SYSTS(101, "broadcast_1", "系统消息特殊事件") {
		@Override
		public String getSysStr(Player player, ObservableEnum observableEnum, Object... argc) {
			//return String.format(getLanguage(getLanguageKey()), argc[0]);
			return (String) argc[0];
		}
	},
	SYSTSBETWEENTIME(230, "broadcast_2", "系统消息特殊事件") {//时间段广播
        @Override
        public String getSysStr(Player player, ObservableEnum observableEnum, Object... argc) {
        	return (String) argc[0];
        }
    },
	CityOccupy(71, "player_win_city", "攻城成功") {
		@Override
		public String getSysStr(Player player, ObservableEnum observableEnum, Object... argc) {
			return String.format(getLanguage(getLanguageKey()), argc[0], argc[1]);
		}
	},
	
	;
	
	//languageConfig 对应的LanguageCnTemplate 的配置项 @FileConfig("language_cn")
	private NoticeTypeEnum(int type, String languageKey, String desc){
		this.type = type;
		this.desc = desc;
		this.languageKey = languageKey;
	}
	
	private int type;
	private String languageKey;
	private String desc;

	public int getType() {
		return type;
	}
	public String getLanguageKey() {
		return languageKey;
	}
	public static NoticeTypeEnum getAddType(int type) {
		for (NoticeTypeEnum temp : NoticeTypeEnum.values()) {
			if(type == temp.getType()) return temp; 
		}
		return null;
	}
	
	public String getLanguage(String key) {
		LanguageCnTemplateConfig langeConfig = SpringUtil.getBean(LanguageCnTemplateConfig.class);
		return langeConfig.getValue(getLanguageKey());
	}
	
	public abstract String getSysStr(Player player, ObservableEnum observableEnum, Object... argc);
	public boolean isCon() {return true;}
}






