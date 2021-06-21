package com.magenta.dreamboard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.magenta.dreamboard.DBHelper;
import com.magenta.dreamboard.adapter.ListViewAdapter;
import com.magenta.dreamboard.R;
import com.magenta.dreamboard.dialog.AddListDialog;
import com.magenta.dreamboard.dialog.DeleteListDialog;
import com.magenta.dreamboard.dialog.SettingDialog;

public class MainActivity extends AppCompatActivity{

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private ListView listView;
    private Context context;
    private SwipeRefreshLayout listSwipeLayout;
    private FloatingActionButton fab, fab_sub1, fab_sub2;
    private boolean isFabOpen = false;
    private Animation fab_open, fab_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DBHelper 생성
        dbHelper = new DBHelper(MainActivity.this);
        
        //activity_main.xml 연결
        listView = (ListView)findViewById(R.id.listView);
        listSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.listSwipeLayout);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab_sub1 = (FloatingActionButton)findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton)findViewById(R.id.fab_sub2);

        //anim폴더의 fab_close, fab_open.xml 등록
        context = getApplicationContext();
        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_close);

        //위에서 아래로 스와이프하면 새로고침
        listSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //리스트 다시 로딩
                displayList();
                //새로고침 마치기
                listSwipeLayout.setRefreshing(false);
            }
        });

        //플로팅 버튼 누르면 sub플로팅 버튼이 나오게 함
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });

        //첫번째 플로팅 버튼이 눌리면 DeleteList 다이얼로그 나오게 함
        fab_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteListDialog deleteListDialog = new DeleteListDialog(MainActivity.this);
                deleteListDialog.callDialog();
            }
        });

        //두번쨰 플로팅 버튼이 눌리면 AddList 다이얼로그 나오게 함
        fab_sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddListDialog addListDialog = new AddListDialog(MainActivity.this);
                addListDialog.callDialog();
            }
        });

        //리스트 표시
        displayList();
    }

    //플로팅 버튼 애니메이션 실행
    private void toggleFab() {
        if (isFabOpen) {
            fab_sub1.startAnimation(fab_close);
            fab_sub2.startAnimation(fab_close);
            fab_sub1.setClickable(false);
            fab_sub2.setClickable(false);
            isFabOpen = false;
        } else {
            fab_sub1.startAnimation(fab_open);
            fab_sub2.startAnimation(fab_open);
            fab_sub1.setClickable(true);
            fab_sub2.setClickable(true);
            isFabOpen = true;
        }
    }

    //리스트를 화면에 생성
    void displayList(){
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        dbHelper = new DBHelper(MainActivity.this);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM list",null);

        //리스트뷰에 목록 채워주는 도구인 adapter준비
        ListViewAdapter listAdapter = new ListViewAdapter();

        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while(cursor.moveToNext()){
            //num 행은 가장 첫번째에 있으니 0번이 되고, name은 1번
            listAdapter.addItemToList(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        }
        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        listView.setAdapter(listAdapter);

        cursor.close();
    }
    
    //menu폴더의 menu.xml의 모양대로 액션바 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //액션바의 설정 버튼 아이콘 설정
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.setting_btn).setIcon(R.drawable.ic_baseline_settings_black_24);
        return super.onPrepareOptionsMenu(menu);
    }

    //액션바의 설정버튼이 눌리면 Setting 다이얼로그 열리도록 설정
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
