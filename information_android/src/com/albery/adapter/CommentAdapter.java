package com.albery.adapter;

import java.util.ArrayList;

import com.albery.entity.Comment;
import com.albery.entity.InformationEntity;
import com.albery.information.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*****************************************************
 * �����������Զ���Adapter
 *
 * @author �Ž���
 * @version 1.0
 *****************************************************/
public class CommentAdapter extends BaseAdapter {
    /*************************************************
     * ���ݶ�����
     *************************************************/
    ArrayList<Comment> list;
    LayoutInflater inflater;

    /*************************************************
     * ���캯��
     *************************************************/
    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    /*************************************************
     * ��������ݷ����仯��ʱ��֪ͨ�������
     *************************************************/
    public void onDataChange(ArrayList<Comment> list) {
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
        Comment comment = list.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.comment_layout, null);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.name);
            viewHolder.content = (TextView) convertView
                    .findViewById(R.id.content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(comment.getName());
        viewHolder.content.setText(comment.getContent());
        return convertView;
    }

    /*************************************************
     * ��������ListView�е�Item
     *************************************************/
    class ViewHolder {
        TextView name;
        TextView content;
    }
}
