package com.hm.config.excel;

import com.google.common.collect.*;
import com.hm.config.GameConstants;
import com.hm.config.excel.temlate.CommnValueTemlpateImpl;
import com.hm.enums.CommValueV2Type;
import com.hm.enums.CommonValueType;
import com.hm.enums.TankAttrType;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.item.ItemGroup;
import com.hm.model.item.Items;
import com.hm.util.*;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Config
public class CommValueConfig extends ExcleConfig{
	private Map<Integer,CommnValueTemlpateImpl> map = Maps.newHashMap();
	private Map<Integer,Object> convertMap = Maps.newHashMap();

	private Map<Integer,Integer> tankJingzhuMap = Maps.newConcurrentMap();
	private List<ItemGroup> seasonTopItemList = Lists.newArrayList();
	private ArrayListMultimap<Integer, Integer> CarrierStrikeMultimap = ArrayListMultimap.create();
	private Map<Integer, List<Items>> backFlowMap = Maps.newConcurrentMap();
    private Table<Integer,Integer,Integer> colorWeightTable = HashBasedTable.create();
	@Getter
	private int firstRechargeVipCard;
	public static long MissionBoxMaxTime = 8*GameConstants.HOUR;

	@Override
	public void loadConfig() {
		Map<Integer,CommnValueTemlpateImpl> map = Maps.newHashMap();
		Map<Integer,Object> convertMap = Maps.newHashMap();
		for(CommnValueTemlpateImpl template : json2ImmutableList(CommnValueTemlpateImpl.class)) {
			template.init();
			map.put(template.getId(),template);

			CommonValueType commonValueType = CommonValueType.getCommonType(template.getId());
			if(commonValueType != null && commonValueType.getV2Type() != CommValueV2Type.None) {
				convertMap.put(template.getId(),commonValueType.getV2Type().parseValue(template));
			}
		}
		this.map = ImmutableMap.copyOf(map);
		this.convertMap = ImmutableMap.copyOf(convertMap);
		loadCarrierStrike();
		loadBackFlowMap();

		this.initTankJingzhu();
		this.seasonTopItemList = ItemGroup.buildItemGroupList(getStrValue(CommonValueType.KFSeasonPlayerScoreReward));
		this.colorWeightTable = ImmutableTable.copyOf(initColorWeight());
		this.firstRechargeVipCard = getCommValue(CommonValueType.FIRST_RECHARGE_GIFT_Card);
		this.MissionBoxMaxTime = getCommValue(CommonValueType.MissionBoxMaxMinute)*GameConstants.MINUTE;
	}


	public <T> T getConvertObj(CommonValueType commonValueType) {
		return (T)this.convertMap.get(commonValueType.getType());
	}
	
	public int getCommValue(int type) {
		if(!map.containsKey(type)) {
			return 0;
		}
		return map.get(type).getValue().intValue();
	}
	public double getDoubleCommValue(int type) {
		if(!map.containsKey(type)) {
			return 0;
		}
		return map.get(type).getValue();
	}
	public String getStrValue(int type) {
		if(!map.containsKey(type)) {
			return null;
		}
		return map.get(type).getPara();
	}
	public List<Items> getListItem(int type) {
		if(!map.containsKey(type)) {
			return null;
		}
		return map.get(type).getListItems();
	}
	
	public int getCommValue(CommonValueType type) {
		return getCommValue(type.getType());
	}
	public double getDoubleCommValue(CommonValueType type) {
		return getDoubleCommValue(type.getType());
	}
	public String getStrValue(CommonValueType type) {
		return getStrValue(type.getType());
	}
	public List<Items> getListItem(CommonValueType type) {
		return getListItem(type.getType());
	}
	
	public double[] getCommonValueByDoubles(CommonValueType valueType){
		return StringUtil.strToDoubleArray(getStrValue(valueType), ",");
	}
	
	public int[] getCommonValueByInts(CommonValueType valueType){
		return StringUtil.strToIntArray(getStrValue(valueType), ",");
	}
	
	
	public int getFlopPrice(int count){
		int [] costArray =StringUtil.strToIntArray(getStrValue(CommonValueType.FlopPrice), ",") ;
		return costArray[count-1];
	}
	
	//根据翻牌类型获取翻牌价格
	public int getFlopCostByType(int type){
		switch(type){
		case 1://只翻一张
			return getFlopPrice(3);
		case 2://全翻
			return getCommValue(CommonValueType.FlopAllPrice);
		}
		return 0;
	}
	
	
	public List<Items> getFirstRechargeItems(int id) {
		if(getFirstRecharge().contains(id)) {
			GiftPackageConfig giftPackageConfig = SpringUtil.getBean(GiftPackageConfig.class);
			return giftPackageConfig.rewardGiftList(id);
		}
		return null;
	}
	public List<Integer> getFirstRecharge() {
		return getConvertObj(CommonValueType.FristRechargeGiftId);
	}



	//是否产出城池特殊奖励
	public boolean isCanSpecialReward(int hour) {
		List<Integer> citySpecialHours = getConvertObj(CommonValueType.CitySpicalHour);
		return citySpecialHours.contains(hour);
	}

	public double getLvModeValue(CommonValueType type,int lv) {
		LevelChoiceMode levelChoiceMode = getConvertObj(type);
		return levelChoiceMode != null?levelChoiceMode.getLvValue(lv):0;
	}

	public String getFarWinOpen(int type){
		if(type==2)return getStrValue(CommonValueType.FarWinOpen);
		if(type==3)return getStrValue(CommonValueType.FarWinOpen2);
		return "";
	}
	//获取单兵奇袭阶段限制箱子数量
	public int getRaidBattleBoxLimit(int bigStage){
		List<Integer> limits = StringUtil.splitStr2IntegerList(getStrValue(CommonValueType.RaidBattleBoxLimit), ",");
		return limits.get(bigStage-1);
	}
	
	//获取魔改属性
	public Map<TankAttrType,Double> getMagicReformAttr(int lv){
		Map<TankAttrType,Double> map = getAttrMap(CommonValueType.MagicRefromAttr);
		for(Map.Entry<TankAttrType, Double> entry: map.entrySet()){
			map.put(entry.getKey(), entry.getValue()*lv);
		}
		return map;
	}

	private void loadCarrierStrike() {
		String infos = getStrValue(CommonValueType.CarrierStrike_map_relation);
		for (String info : infos.split(";")) {
			String[] split = info.split(":");
			for(String id : split[1].split(",")){
				CarrierStrikeMultimap.put(PubFunc.parseInt(split[0]),PubFunc.parseInt(id));
			}
		}
	}

	private void loadBackFlowMap() {
		Map<Integer, List<Items>> tempMap = Maps.newConcurrentMap();
		String strValue = getStrValue(CommonValueType.BackFlowReward);
		String[] split = strValue.split(";");
		for (int i = 0; i < split.length; i++) {
			String str = split[i];
			List<Items> items = ItemUtils.str2DefaultItemImmutableList(str);
			tempMap.put(i, items);
		}
		this.backFlowMap = ImmutableMap.copyOf(tempMap);
	}

	public List<Integer>  checkCanMove(int id){
		List<Integer> list = CarrierStrikeMultimap.get(id);
		return list;
	}

	public List<Items> getBackFlowReward(int vipLv) {
		List<Items> itemsList = backFlowMap.getOrDefault(vipLv, Lists.newArrayList());
		return itemsList;
	}

	public Map<TankAttrType, Double> getAttrMap(CommonValueType type){
		return TankAttrUtils.str2TankAttrMap(getStrValue(type), ",", ":");
	}

	private void initTankJingzhu() {
		tankJingzhuMap = StringUtil.strToMap(this.getStrValue(CommonValueType.TroopAdvanceWeight), ";", "_");
	}

	/**
	 * 坦克精铸，随机品阶
     * @Title: getTankJingzhuRandomQuality
     * @Description:
	 * @param type 0普通精铸；1高级精铸
	 * @param size 随机的数量
	 * @param oldQuality 旧的品质id
	 * @return
     * @return: List<Integer>
	 * @throws
	 */
	public int getTankJingzhuRandomQuality(int type, int position, int oldQuality) {
		WeightMeta<Integer> tempWeight = null;
		Map<Integer, Integer> tempMap = Maps.newHashMap();
		if(type==0) {
			tempMap.put(oldQuality, tankJingzhuMap.get(oldQuality));
			tempMap.put(oldQuality+1, tankJingzhuMap.getOrDefault(oldQuality+1, 0));
		}else {
			tempMap.put(oldQuality, tankJingzhuMap.get(oldQuality));
			tempMap.put(oldQuality+1, tankJingzhuMap.getOrDefault(oldQuality+1, 0));
		}
		tempWeight = RandomUtils.buildWeightMeta(tempMap);
		Integer result = tempWeight.random();
		return null==result?0:result;
	}

	public List<ItemGroup> getSeasonTopItemList() {
		return seasonTopItemList;
	}

    public int getPayEveryPoint(int rechargeId) {
		Map<Integer,Integer> payEveryDayPoints = getConvertObj(CommonValueType.BUY_EVERY_DAY_POINTS);
        return payEveryDayPoints.getOrDefault(rechargeId, 0);
    }

    public List<Integer> getPayEveryIds() {
		Map<Integer,Integer> payEveryDayPoints = getConvertObj(CommonValueType.BUY_EVERY_DAY_POINTS);
		return Lists.newArrayList(payEveryDayPoints.keySet());
    }

    public  Table<Integer,Integer,Integer> initColorWeight(){
		Table<Integer,Integer,Integer> colorWeightTable = HashBasedTable.create();
		String[] strs = getStrValue(CommonValueType.ColorWeight).split(";");
		for(int i=0;i<strs.length;i++){
			for(String ss:strs[i].split(",")){
				String[] s = ss.split(":");
				colorWeightTable.put(i+1,Integer.parseInt(s[0]),Integer.parseInt(s[1]));
			}
		}
		return colorWeightTable;
	}

    public int getColorWeight(boolean flag, int colorId) {
        int libId = flag ? 2 : 1;
        if (!colorWeightTable.contains(libId, colorId)) {
            return 0;
        }
        return colorWeightTable.get(libId, colorId);
    }

	public int getTroopNum(int missionId) {
		int[] troops = getConvertObj(CommonValueType.TroopUnlockMissionIds);
		for (int i = troops.length-1; i >= 0; i--) {
			if(missionId >= troops[i]) {
				return i+1;
			}
		}
		return 1;
	}

	public int getRareCostItemId(int rare){
		Map<Integer,Integer> rareCostItems = getConvertObj(CommonValueType.TankResearchItemIds);
		return rareCostItems.getOrDefault(rare, 0);
	}

}






