package com.magenta.dreamboard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
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

public class SettingDialog {

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public SettingDialog(Context context) {
        this.context = context;
    }

    public void callDialog() {
        final Dialog connection_dialog = new Dialog(context);   //다이얼로그 초기화
        connection_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);    //타이틀 제거
        connection_dialog.setContentView(R.layout.dialog_custom);   //xml 레이아웃 파일과 연결
        connection_dialog.show();   //다이얼로그 보이기
        connection_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경

        //dialog_custom.xml 구성 id 연결
        final TextView title_text = (TextView) connection_dialog.findViewById(R.id.title_text);
        final TextView host_text = (TextView) connection_dialog.findViewById(R.id.sub1_text);
        final EditText host_edit = (EditText) connection_dialog.findViewById(R.id.sub1_edit);
        final TextView port_text = (TextView) connection_dialog.findViewById(R.id.sub2_text);
        final EditText port_edit = (EditText) connection_dialog.findViewById(R.id.sub2_edit);
        final Button cancel_btn = (Button) connection_dialog.findViewById(R.id.cancel_btn);
        final Button confirm_btn = (Button) connection_dialog.findViewById(R.id.confirm_btn);
        final TextView error_text = (TextView) connection_dialog.findViewById(R.id.error_dialog_txt);

        //커스텀 다이얼로그 내부 표시 설정
        title_text.setText("Setting");
        host_text.setText("Host IP");
        host_edit.setHint("xxx.xxx.xxx.xxx");
        port_text.setText("Port");
        port_edit.setHint("xxxx");
        port_edit.setInputType(2);

        //DBhelper의 쓰기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        //이전에 입력한 값이 있으면 칸에 미리 입력해놓음
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT host, port FROM setting WHERE id = 1",null);
        String host_info = "0";
        int port_info = 0;
        if( cursor != null && cursor.moveToFirst() ){
            host_info = cursor.getString(0);
            port_info= cursor.getInt(1);
        }
        cursor.close();
        if (!(host_info.equals(0) || port_info == 0)) {
            host_edit.setText(host_info);
            port_edit.setText(Integer.toString(port_info));
        }

        //취소버튼 누를때
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection_dialog.dismiss();
            }
        });

        //확인버튼 누를때
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //둘중 하나라도 입력하지 않은 것이 있다면 입력하라고 하는 문구 보이게 함
                if (host_edit.getText().toString().equals("") || port_edit.getText().toString().equals("")) {
                    error_text.setVisibility(View.VISIBLE);
                } else {
                    //세팅 테이블의 첫번째 행에 host값과 port값을 저장함
                    String host = host_edit.getText().toString();
                    int port = Integer.parseInt(port_edit.getText().toString());
                    sqLiteDatabase.execSQL("UPDATE setting SET host = '" + host + "', port = '" + port + "' WHERE id = 1");
                    connection_dialog.dismiss();
                }
            }
        });
    }
}
