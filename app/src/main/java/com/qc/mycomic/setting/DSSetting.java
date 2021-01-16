package com.qc.mycomic.setting;

import com.qc.mycomic.en.SourceEnum;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.en.Codes;
import com.qc.mycomic.util.SourceUtil;

import java.util.Map;

/**
 * @author LuQiChuang
 * @desc 默认漫画源设置
 * @date 2020/8/15 13:14
 * @ver 1.0
 */
public class DSSetting extends Setting {

    @Override
    public String getSaveStr() {
        return "defaultSource";
    }

    @Override
    public String getDefaultValue() {
        return String.valueOf(SourceEnum.MI_TUI.ID);
    }

    @Override
    public void dealMyMap(Map<String, String> myMap) {
        Map<Integer, Source> sourceMyMap = SourceUtil.getMap();
        for (Map.Entry<Integer, Source> entry : sourceMyMap.entrySet()) {
            myMap.put(String.valueOf(entry.getValue().getSourceId()), entry.getValue().getSourceName());
        }
    }
}
