/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.huizhuang.zxsq.widget.wheel.adapter;

import java.util.List;

import android.content.Context;

import com.huizhuang.zxsq.http.bean.KeyValue;

public class KeyValueWheelAdapter extends AbstractWheelTextAdapter {

	 // items
    private List<KeyValue> mList;

    /**
     * Constructor
     * @param context the current context
     * @param list the items
     */
    public KeyValueWheelAdapter(Context context, List<KeyValue> list) {
        super(context);
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.mList = list;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < mList.size()) {
            KeyValue item = mList.get(index);
            return item.getName();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return mList.size();
    }

}
