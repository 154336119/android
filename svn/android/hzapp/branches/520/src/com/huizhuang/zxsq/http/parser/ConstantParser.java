package com.huizhuang.zxsq.http.parser;

import com.huizhuang.zxsq.http.bean.Constant;
import com.huizhuang.zxsq.http.bean.Constant.JourneyType;
import com.huizhuang.zxsq.http.bean.Constant.RankType;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.http.parser.base.BaseParser;
import com.huizhuang.zxsq.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConstantParser implements BaseParser<Constant> {

    @Override
    public Constant parse(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        return constantParser(obj);
    }

    private Constant constantParser(JSONObject obj) {
        // LogUtil.i("jiengyh constantParser  obj:"+obj);
        Constant constant = new Constant();
        //解析装修记账分类
        journeyTypePaeser(obj, constant);

        keyValueParser(obj, constant);

        constant.setCostRanges(costRangesParser("cost_ranges", obj, constant));

        JSONObject objRank = obj.optJSONObject("rank_types");

        constant.setAppStore(rankTypeParser("app_store", objRank, constant));
        constant.setAppDiaryv2(rankTypeParser("app_diary_v2", objRank, constant));
        constant.setJlStaff(rankTypeParser("jl_staff", objRank, constant));
        constant.setJlStore(rankTypeParser("jl_store", objRank, constant));
        constant.setJlGongzhang(rankTypeParser("jl_gongzhang", objRank, constant));
        
        LogUtil.i("jiengyh constantParser  constant:" + constant);
        return constant;
    }

    private void keyValueParser(JSONObject obj, Constant constant) {
        List<KeyValue> list = keyValueParser("room_types", obj, constant);
        constant.setRoomTypes(list);

        List<KeyValue> roomSpacesList = keyValueParser("room_spaces", obj, constant);
        constant.setRoomSpaces(roomSpacesList);

        List<KeyValue> roomPartList = keyValueParser("room_parts", obj, constant);
        constant.setRoomParts(roomPartList);

        List<KeyValue> roomStyleList = keyValueParser("room_styles", obj, constant);
        constant.setRoomStyles(roomStyleList);

        List<KeyValue> zxNodesList = keyValueParser("gongzhang_node", obj, constant);
        constant.setZxNodes(zxNodesList);
        
        List<KeyValue> jlNodesList = keyValueParser("jl_nodes", obj, constant);
        constant.setJlNodes(jlNodesList);

        List<KeyValue> decorationTypesList = keyValueParser("decoration_types", obj, constant);
        constant.setDecorationTypes(decorationTypesList);

        List<KeyValue> renovationWaysList = keyValueParser("renovation_ways", obj, constant);
        constant.setRenovationWays(renovationWaysList);

        List<KeyValue> storeOrderOptionsList = keyValueParser("store_order_options", obj, constant);
        constant.setStoreOrderOptions(storeOrderOptionsList);

        List<KeyValue> diaryOrderOptionsList = keyValueParser("diary_order_options", obj, constant);
        constant.setDiaryOrderOptions(diaryOrderOptionsList);
    }

    private List<KeyValue> keyValueParser(String key, JSONObject obj, Constant constant) {
        List<KeyValue> list = new ArrayList<KeyValue>();
        JSONArray roomTypes = obj.optJSONArray(key);
        if(roomTypes == null){
            return list;
        }
        for (int i = 0; i < roomTypes.length(); i++) {
            JSONObject roomObj = roomTypes.optJSONObject(i);
            KeyValue keyValue = new KeyValue();
            keyValue.setId(roomObj.optString("id"));
            keyValue.setName(roomObj.optString("name"));
            list.add(keyValue);
            LogUtil.i("keyValueParser list:" + list);
        }
        return list;
    }

    private List<RankType> rankTypeParser(String key, JSONObject obj, Constant constant) {
        List<RankType> list = new ArrayList<Constant.RankType>();
        JSONArray roomTypes = obj.optJSONArray(key);
        for (int i = 0; i < roomTypes.length(); i++) {
            JSONObject roomObj = roomTypes.optJSONObject(i);
            RankType rankType = constant.new RankType();
            rankType.setId(roomObj.optInt("id"));
            rankType.setName(roomObj.optString("name"));
            rankType.setMaxScore(roomObj.optInt("max_score"));
            list.add(rankType);
        }
        return list;
    }


    private List<KeyValue> costRangesParser(String key, JSONObject obj, Constant constant) {
        List<KeyValue> list = new ArrayList<KeyValue>();
        JSONArray roomTypes = obj.optJSONArray(key);
        for (int i = 0; i < roomTypes.length(); i++) {
            JSONObject roomObj = roomTypes.optJSONObject(i);
            KeyValue costRange = new KeyValue();
            costRange.setId(roomObj.optString("id"));
            costRange.setName(roomObj.optString("name"));
            //costRange.setCost(roomObj.optString("cost"));
            list.add(costRange);
        }
        return list;
    }

    private void journeyTypePaeser(JSONObject obj, Constant constant) {
        JSONArray journeyTypes = obj.optJSONArray("journey_types");
        List<JourneyType> jourList = new ArrayList<Constant.JourneyType>();

        for (int i = 0; i < journeyTypes.length(); i++) {
            JSONObject journeyObj = journeyTypes.optJSONObject(i);
            JourneyType journeyType = parserBaseJourneyType(constant,
                    journeyObj);
            JSONArray subArray = journeyObj.optJSONArray("sub");
            List<JourneyType> jourListSub = new ArrayList<Constant.JourneyType>();
            for (int j = 0; j < subArray.length(); j++) {
                JSONObject journeyObjSub = subArray.optJSONObject(j);
                JourneyType journeySub = parserBaseJourneyType(constant,
                        journeyObjSub);
                jourListSub.add(journeySub);
            }
            journeyType.setStub(jourListSub);
            jourList.add(journeyType);
        }
        constant.setJourneyTypeList(jourList);
    }

    private JourneyType parserBaseJourneyType(Constant constant,
                                              JSONObject journeyObj) {
        JourneyType journeyType = constant.new JourneyType();
        journeyType.setCid(journeyObj.optInt("cid"));
        journeyType.setId(journeyObj.optInt("id"));
        journeyType.setPid(journeyObj.optInt("pid"));
        journeyType.setName(journeyObj.optString("name"));
        journeyType.setImg(journeyObj.optString("img"));
        return journeyType;
    }

}
