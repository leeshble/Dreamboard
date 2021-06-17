package com.magenta.dreamboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    DBHelper dbHelper;
    ListViewAdapter listAdapter;
    ListView listView;
    TextView itemTitle, itemPurpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientManager clientManager = new ClientManager();
                clientManager.setOutput(text_edit.getText().toString());
                Thread tcpThread = new Thread(new ClientManager());
                tcpThread.start();
            }
        });

        invalidateOptionsMenu();
         */

        dbHelper = new DBHelper(MainActivity.this);

        listView = (ListView)findViewById(R.id.listView);
        itemTitle = (TextView)findViewById(R.id.itemTitle);
        itemPurpose = (TextView)findViewById(R.id.itemPurpose);

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddListDialog addListDialog = new AddListDialog(MainActivity.this);
                addListDialog.callDialog();
            }
        });

        displayList();
    }

    @Override
    protected void onResume() {
        //리스트 다시 로딩
        //listAdapter.notifyDataSetChanged();
        super.onResume();
    }



    void displayList(){
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        dbHelper = new DBHelper(MainActivity.this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = database.rawQuery("SELECT * FROM list",null);

        //리스트뷰에 목록 채워주는 도구인 adapter준비
        ListViewAdapter listAdapter = new ListViewAdapter();

        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while(cursor.moveToNext()){
            //num 행은 가장 첫번째에 있으니 0번이 되고, name은 1번
            listAdapter.addItemToList(cursor.getString(0),cursor.getString(1));
        }
        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        listView.setAdapter(listAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.setting_btn).setIcon(R.drawable.ic_baseline_settings_black_24);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_btn:
                SettingDialog dialog = new SettingDialog(this);
                dialog.callDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}
