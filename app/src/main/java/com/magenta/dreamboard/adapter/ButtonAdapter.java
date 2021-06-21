package com.magenta.dreamboard.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magenta.dreamboard.DBHelper;
import com.magenta.dreamboard.R;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ButtonAdapter extends BaseAdapter {

    ArrayList<ButtonAdapterData> list = new ArrayList<ButtonAdapterData>();

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    public String SERVER_IP = "192.168.0.2";
    public int PORT = 10000;
    public String action;

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
            view = layoutInflater.inflate(R.layout.button_custom, viewGroup, false);
        }

        //DBhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        //button_custom.xml 구성 id 연결
        LinearLayout button = (LinearLayout)view.findViewById(R.id.button);
        TextView buttonId = (TextView)view.findViewById(R.id.buttonId);
        TextView buttonTitle = (TextView)view.findViewById(R.id.buttonTitle);
        TextView buttonAction = (TextView) view.findViewById(R.id.buttonAction);

        ButtonAdapterData buttonData = list.get(i);
        //커스텀 버튼 구성 설정
        buttonId.setText(Integer.toString(buttonData.getId()));
        buttonTitle.setText(buttonData.getTitle());
        buttonAction.setText(buttonData.getAction());

        //서버, 포트 값 설정
        setConnection();

        //버튼 클릭시
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //지정된 action PC로 전송
                action = buttonData.getAction();
                ClientManager clientManager = new ClientManager();
                clientManager.start();
            }
        });

        return view;
    }

    //버튼의 id, title, action을 리스트에 저장
    public void addButtonToList(int id, String title, String action){
        ButtonAdapterData buttonData = new ButtonAdapterData();

        buttonData.setId(id);
        buttonData.setTitle(title);
        buttonData.setAction(action);

        //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
        list.add(buttonData);
    }

    //소켓 클라이언트
    class ClientManager extends Thread {
        @Override
        public void run() {
            try {
                //IP주소 변환
                InetAddress hostAddress = InetAddress.getByName(SERVER_IP);
                //소켓 생성
                Socket clientSocket = new Socket(hostAddress, PORT);
                //데이터 전송
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
                out.println(action);
                //소켓 종료
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //IP, PORT 주소 DB의 Setting테이블에서 가져와서 SERVER_IP, PORT에 입력
    private void setConnection() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM setting WHERE id = 1",null);
        if( cursor != null && cursor.moveToFirst() ){
            SERVER_IP = cursor.getString(1);
            PORT = cursor.getInt(2);
        }
        cursor.close();
    }
}
