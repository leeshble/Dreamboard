package com.magenta.dreamboard.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.magenta.dreamboard.DBHelper;
import com.magenta.dreamboard.adapter.ButtonAdapter;
import com.magenta.dreamboard.R;
import com.magenta.dreamboard.dialog.AddButtonDialog;
import com.magenta.dreamboard.dialog.DeleteButtonDialog;

import java.io.PrintWriter;
import java.net.Socket;

public class MacroActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Intent intent;
    private Context context;
    private SwipeRefreshLayout listSwipeLayout2;
    private GridView gridView;
    private FloatingActionButton fab2, fab2_sub1, fab2_sub2;
    private boolean isFabOpen = false;
    private Animation fab_open, fab_close;
    private String intentData;

    //넘겨받은 인텐트 값을 Int값으로 변경
    protected int getIntentVal() {
        return Integer.parseInt(intentData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        dbHelper = new DBHelper(MacroActivity.this);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        //인텐트 값 가져오기
        intent = getIntent();
        intentData = intent.getStringExtra("item_id");
        dbHelper = new DBHelper(MacroActivity.this);

        gridView = (GridView)findViewById(R.id.gridView);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab2_sub1 = (FloatingActionButton)findViewById(R.id.fab2_sub1);
        fab2_sub2 = (FloatingActionButton)findViewById(R.id.fab2_sub2);
        listSwipeLayout2 = (SwipeRefreshLayout)findViewById(R.id.listSwipeLayout2);

        context = getApplicationContext();
        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_close);

        //위에서 아래로 스와이프하면 새로고침
        listSwipeLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //버튼 다시 로딩
                displayButton();
                //새로고침 마치기
                listSwipeLayout2.setRefreshing(false);
            }
        });

        //플로팅 버튼 누르면 sub플로팅 버튼이 나오게 함
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });

        //첫번째 플로팅 버튼이 눌리면 DeleteList 다이얼로그 나오게 함
        fab2_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteButtonDialog deleteButtonDialog = new DeleteButtonDialog(MacroActivity.this);
                deleteButtonDialog.callDialog();
            }
        });

        //첫번째 플로팅 버튼이 눌리면 DeleteList 다이얼로그 나오게 함
        fab2_sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddButtonDialog addButtonDialog = new AddButtonDialog(MacroActivity.this, Integer.parseInt(intentData));
                addButtonDialog.callDialog();
            }
        });

        //버튼 표시
        displayButton();
    }

    //플로팅 버튼 애니메이션 실행
    private void toggleFab() {
        if (isFabOpen) {
            fab2_sub1.startAnimation(fab_close);
            fab2_sub2.startAnimation(fab_close);
            fab2_sub1.setClickable(false);
            fab2_sub2.setClickable(false);
            isFabOpen = false;
        } else {
            fab2_sub1.startAnimation(fab_open);
            fab2_sub2.startAnimation(fab_open);
            fab2_sub1.setClickable(true);
            fab2_sub2.setClickable(true);
            isFabOpen = true;
        }
    }

    //버튼을 화면에 생성
    public void displayButton() {
        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM buttonList WHERE list_id = "+ getIntentVal() ,null);

        //그리드뷰에 목록 채워주는 도구인 adapter준비
        ButtonAdapter buttonAdapter = new ButtonAdapter();

        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while(cursor.moveToNext()){
            //num 행은 가장 첫번째에 있으니 0번이 되고, name은 1번
            buttonAdapter.addButtonToList(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        }
        //그리드뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        gridView.setAdapter(buttonAdapter);

        cursor.close();
    }
}