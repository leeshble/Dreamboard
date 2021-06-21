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
import com.magenta.dreamboard.activity.MainActivity;
import com.magenta.dreamboard.adapter.ButtonAdapter;
import com.magenta.dreamboard.adapter.ListViewAdapter;

public class DeleteButtonDialog {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    public DeleteButtonDialog(Context context) {
        this.context = context;
    }

    public void callDialog() {
        final Dialog delete_macro_dialog = new Dialog(context);   //다이얼로그 초기화
        delete_macro_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);    //타이틀 제거
        delete_macro_dialog.setContentView(R.layout.dialog_custom);   //xml 레이아웃 파일과 연결
        delete_macro_dialog.show();   //다이얼로그 보이기
        delete_macro_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경

        //dialog_custom.xml 구성 id 연결
        final TextView id_text = (TextView) delete_macro_dialog.findViewById(R.id.title_text);
        final TextView buttonID_text = (TextView) delete_macro_dialog.findViewById(R.id.sub1_text);
        final EditText buttonID_edit = (EditText) delete_macro_dialog.findViewById(R.id.sub1_edit);
        final TextView sub2_text = (TextView) delete_macro_dialog.findViewById(R.id.sub2_text);
        final EditText sub2_edit = (EditText) delete_macro_dialog.findViewById(R.id.sub2_edit);
        final Button cancel_btn = (Button) delete_macro_dialog.findViewById(R.id.cancel_btn);
        final Button confirm_btn = (Button) delete_macro_dialog.findViewById(R.id.confirm_btn);
        final TextView error_text = (TextView) delete_macro_dialog.findViewById(R.id.error_dialog_txt);

        //커스터 다이얼로그 표시 설정
        id_text.setText("Delete Macro");
        buttonID_text.setText("Macro ID");
        buttonID_edit.setHint("ID of macro");
        buttonID_edit.setInputType(2);
        sub2_text.setVisibility(View.GONE);
        sub2_edit.setVisibility(View.GONE);

        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();

        //취소 버튼
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_macro_dialog.dismiss();
            }
        });

        //확인 버튼
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력하지 않은 것이 있을때, 입력하라는 메시지를 다이얼로그에 표시
                if (buttonID_edit.getText().toString().equals("")) {
                    error_text.setText("Please type ID of Pack");
                    error_text.setVisibility(View.VISIBLE);
                } else {
                    //DB에서 버튼 삭제
                    ButtonAdapter buttonAdapter = new ButtonAdapter();
                    int item = (int) buttonAdapter.getItemId(Integer.parseInt(buttonID_edit.getText().toString()));
                    database.execSQL("DELETE FROM buttonList WHERE id = " + item);
                    delete_macro_dialog.dismiss();
                }
            }
        });
    }
}
