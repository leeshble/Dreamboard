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
import com.magenta.dreamboard.R;
import com.magenta.dreamboard.activity.MacroActivity;

public class AddButtonDialog {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;
    private int listId;

    //커스텀 다이얼로그 플로팅 버튼을 통해 띄우기
    public AddButtonDialog(Context context, int listId) {
        this.context = context;
        this.listId = listId;
    }

    public void callDialog() {
        final Dialog add_macro_dialog = new Dialog(context);   //다이얼로그 초기화
        add_macro_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);    //타이틀 제거
        add_macro_dialog.setContentView(R.layout.dialog_custom);   //xml 레이아웃 파일과 연결
        add_macro_dialog.show();   //다이얼로그 보이기
        add_macro_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경

        //xml id 등록
        final TextView title_text = (TextView) add_macro_dialog.findViewById(R.id.title_text);
        final TextView buttonTitle_text = (TextView) add_macro_dialog.findViewById(R.id.sub1_text);
        final EditText buttonTitle_edit = (EditText) add_macro_dialog.findViewById(R.id.sub1_edit);
        final TextView buttonAction_text = (TextView) add_macro_dialog.findViewById(R.id.sub2_text);
        final EditText buttonAction_edit = (EditText) add_macro_dialog.findViewById(R.id.sub2_edit);
        final Button cancel_btn = (Button) add_macro_dialog.findViewById(R.id.cancel_btn);
        final Button confirm_btn = (Button) add_macro_dialog.findViewById(R.id.confirm_btn);
        final TextView error_text = (TextView) add_macro_dialog.findViewById(R.id.error_dialog_txt);

        //커스텀 다이얼로그 표시 설정
        title_text.setText("Add Macro");
        buttonTitle_text.setText("Title");
        buttonTitle_edit.setHint("Title of macro");
        buttonAction_text.setText("Action");
        buttonAction_edit.setHint("Action of macro");

        //DBhelper의 쓰기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();

        //취소 버튼
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_macro_dialog.dismiss();
            }
        });

        //확인 버튼
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력하지 않은 것이 있을때, 입력하라는 메시지를 다이얼로그에 표시
                if (buttonTitle_edit.getText().toString().equals("") || buttonAction_edit.getText().toString().equals("")) {
                    error_text.setVisibility(View.VISIBLE);
                } else {
                    //DB에 삽입
                    database.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES ('"+ buttonTitle_edit.getText() + "','" + buttonAction_edit.getText() + "','" + listId + "') ");
                    add_macro_dialog.dismiss();
                }
            }
        });
    }
}
