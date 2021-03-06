package com.magenta.dreamboard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.magenta.dreamboard.DBHelper;
import com.magenta.dreamboard.adapter.ListViewAdapter;
import com.magenta.dreamboard.R;

public class AddListDialog{

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    //커스텀 다이얼로그 플로팅 버튼을 통해 띄우기
    public AddListDialog(Context context) {
        this.context = context;
    }

    public void callDialog() {
        final Dialog add_list_dialog = new Dialog(context);   //다이얼로그 초기화
        add_list_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);    //타이틀 제거
        add_list_dialog.setContentView(R.layout.dialog_custom);   //xml 레이아웃 파일과 연결
        add_list_dialog.show();   //다이얼로그 보이기
        add_list_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경

        //xml id 등록
        final TextView title_text = (TextView) add_list_dialog.findViewById(R.id.title_text);
        final TextView listTitle_text = (TextView) add_list_dialog.findViewById(R.id.sub1_text);
        final EditText listTitle_edit = (EditText) add_list_dialog.findViewById(R.id.sub1_edit);
        final TextView listPurpose_text = (TextView) add_list_dialog.findViewById(R.id.sub2_text);
        final EditText listPurpose_edit = (EditText) add_list_dialog.findViewById(R.id.sub2_edit);
        final Button cancel_btn = (Button) add_list_dialog.findViewById(R.id.cancel_btn);
        final Button confirm_btn = (Button) add_list_dialog.findViewById(R.id.confirm_btn);
        final TextView error_text = (TextView) add_list_dialog.findViewById(R.id.error_dialog_txt);

        //커스텀 다이얼로그 표시 설정
        title_text.setText("Add Pack");
        listTitle_text.setText("Title");
        listTitle_edit.setHint("Title of pack");
        listPurpose_text.setText("Purpose");
        listPurpose_edit.setHint("Purpose of pack");
        
        //DBhelper의 쓰기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();

        //취소 버튼
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_list_dialog.dismiss();
            }
        });

        //확인 버튼
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력하지 않은 것이 있을때, 입력하라는 메시지를 다이얼로그에 표시
                if (listTitle_edit.getText().toString().equals("") || listPurpose_edit.getText().toString().equals("")) {
                    error_text.setVisibility(View.VISIBLE);
                } else {
                    //DB에 삽입
                    database.execSQL("INSERT INTO list(title, purpose) VALUES ('"+ listTitle_edit.getText() + "','" + listPurpose_edit.getText() +"') ");
                    add_list_dialog.dismiss();
                }
            }
        });
    }
}
