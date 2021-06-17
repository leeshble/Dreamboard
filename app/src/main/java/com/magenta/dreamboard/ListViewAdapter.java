package com.magenta.dreamboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    ArrayList<ListViewAdapterData> list = new ArrayList<ListViewAdapterData>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater. inflate(R.layout.listview_custom, viewGroup, false);
        }


        TextView itemTitle = (TextView)view.findViewById(R.id.itemTitle);
        TextView itemPurpose = (TextView)view.findViewById(R.id.itemPurpose);

        ListViewAdapterData listData = list.get(i);

        itemTitle.setText(listData.getTitle());
        itemPurpose.setText(listData.getPurpose());

        return view;
    }

    //ArrayList로 선언된 list 변수에 목록을 채워주기 위함
    public void addItemToList(String title, String purpose){
        ListViewAdapterData listdata = new ListViewAdapterData();

        listdata.setTitle(title);
        listdata.setTitle(purpose);

        //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
        list.add(listdata);

    }
}
