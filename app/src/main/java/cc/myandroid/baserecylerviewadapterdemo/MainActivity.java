package cc.myandroid.baserecylerviewadapterdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_list);
        mDatas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mDatas.add("数据"+i);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(this);
        myAdapter.setDatas(mDatas);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        myAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, int position, String data) {
                Toast.makeText(MainActivity.this,"clicked!  data=" +data+",   position="+position, Toast.LENGTH_SHORT).show();
            }
        });

        myAdapter.setOnItemSelectedListener(new BaseAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                Toast.makeText(MainActivity.this, "Selected!  position="+position, Toast.LENGTH_SHORT).show();
            }
        });
        myAdapter.setHeaderViewId(R.layout.header_layout);
        myAdapter.setFooterViewId(R.layout.footer_layout);
        myAdapter.setOnHeaderClickListener(new BaseAdapter.OnHeaderClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "onClick  Header!", Toast.LENGTH_SHORT).show();
            }
        });
        myAdapter.setOnFooterClickListener(new BaseAdapter.OnFooterClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "onClick  Footer!", Toast.LENGTH_SHORT).show();
            }
        });
        System.out.println(myAdapter.getItemCount());
    }
}
