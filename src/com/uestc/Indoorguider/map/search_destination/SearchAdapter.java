package com.uestc.Indoorguider.map.search_destination;

import java.util.List;

import com.uestc.Indoorguider.R;

import android.content.Context;

/**
 * Created by yetwish on 2015-05-11
 */

public class SearchAdapter extends CommonAdapter<DestinationSite>{

    public SearchAdapter(Context context, List<DestinationSite> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {
        holder.setImageResource(R.id.item_search_iv_icon,mData.get(position).getIconId())
                .setText(R.id.item_search_tv_title,mData.get(position).getTitle())
                .setText(R.id.item_search_tv_content,mData.get(position).getContent());
    }
}

