package com.magenta.dreamboard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingDialog {

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

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection_dialog.dismiss();
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (host_edit.getText().toString().equals("") || port_edit.getText().toString().equals("")) {
                    error_text.setVisibility(View.VISIBLE);
                } else {
                    ClientManager clientManager = new ClientManager();
                    clientManager.setSocket(host_edit.getText().toString(), Integer.parseInt(port_edit.getText().toString()));
                    connection_dialog.dismiss();
                }
            }
        });
    }
}
