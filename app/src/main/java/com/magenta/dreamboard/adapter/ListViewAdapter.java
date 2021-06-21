package com.magenta.dreamboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.magenta.dreamboard.R;
import com.magenta.dreamboard.activity.MacroActivity;
import com.magenta.dreamboard.activity.MainActivity;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    ArrayList<ListViewAdapterData> list = new ArrayList<ListViewAdapterData>();

    private LinearLayout listItemBtn;

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

        //리스트뷰에 아이템이 인플레이트 되어있는지 확인한후
        //아이템이 없다면 아래처럼 아이템 레이아웃을 인플레이트 하고 view객체에 담는다.
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater. inflate(R.layout.listview_custom, viewGroup, false);
        }

        //이제 아이템에 존재하는 텍스트뷰 객체들을 view객체에서 찾아 가져온다
        listItemBtn = (LinearLayout)view.findViewById(R.id.list_item_btn);
        TextView itemId = (TextView) view.findViewById(R.id.listId);
        TextView itemTitle = (TextView)view.findViewById(R.id.itemTitle);
        TextView itemPurpose = (TextView)view.findViewById(R.id.itemPurpose);

        //현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list배열에서 객체를 가져온다.
        ListViewAdapterData listData = list.get(i);

        itemId.setText(Integer.toString(listData.getId()));
        itemTitle.setText(listData.getTitle());
        itemPurpose.setText(listData.getPurpose());

        listItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MacroActivity.class);
                intent.putExtra("item_id", Integer.toString(list.get(i).getId()));
                context.startActivity(intent);
                Toast.makeText(view.getContext(), Integer.toString(list.get(i).getId()), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //ArrayList로 선언된 list 변수에 목록을 채워주기 위함
    public void addItemToList(int id, String title, String purpose){
        ListViewAdapterData listdata = new ListViewAdapterData();

        listdata.setId(id);
        listdata.setTitle(title);
        listdata.setPurpose(purpose);

        //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
        list.add(listdata);
    }
}
