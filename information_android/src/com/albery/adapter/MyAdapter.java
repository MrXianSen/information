package com.albery.adapter;

import java.util.ArrayList;

import com.albery.entity.InformationEntity;
import com.albery.information.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/*****************************************************
 * �����������Զ���Adapter
 *
 * @author �Ž���
 * @version 1.0
 *****************************************************/
public class MyAdapter extends BaseAdapter {
    /*****************************************************
     * ���ݶ�����
     *****************************************************/
    ArrayList<InformationEntity> list;
    LayoutInflater inflater;

    /*****************************************************
     * ���캯��
     *****************************************************/
    public MyAdapter(Context context, ArrayList<InformationEntity> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    /*****************************************************
     * ���ݱ仯ʱ���½���
     *****************************************************/
    public void onDataChange(ArrayList<InformationEntity> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        InformationEntity information = list.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_layout, null);
            viewHolder.title_tv = (TextView) convertView
                    .findViewById(R.id.item_title);
            viewHolder.date_tv = (TextView) convertView
                    .findViewById(R.id.item_date);
            viewHolder.type_tv = (TextView) convertView
                    .findViewById(R.id.item_type);
            viewHolder.content_tv = (TextView) convertView
                    .findViewById(R.id.item_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title_tv.setText(information.getTitle());
        viewHolder.date_tv.setText(information.getDate());
        viewHolder.type_tv.setText(information.getType());
        viewHolder.content_tv.setText(information.getContent());
        return convertView;
    }

    /*************************************************
     * ��������ListView�е�Item
     *************************************************/
    class ViewHolder {
        TextView title_tv;
        TextView date_tv;
        TextView type_tv;
        TextView content_tv;
    }

}
