package com.example.administrator.review;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewActivity extends AppCompatActivity {
    private Button clickTouchBtn;
    private ImageView touchIv;
    private MyLayout myLayout;
    private TextView myTv;
    private MyListView myLv;
    private MyAdapter adapter;
    private List<HashMap<String,String>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        clickTouchBtn = (Button)findViewById(R.id.clickTouchBtn);
        clickTouchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("wanlijun","button:onClick");
            }
        });
        //onTouch执行完了才执行onClick，点击一次onTouch执行多次（包括ACTION_DOWN一次，ACTION_MOVE多次，ACTION_UP一次）
        //onTouch返回值决定是否执行onClick，默认返回false，执行onClick，如果返回true则onClick不执行
        clickTouchBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("wanlijun","button:onTouch:"+event.getAction());
                return false;
            }
        });
        touchIv = (ImageView)findViewById(R.id.touchIv);
        //当没有给ImageView注册点击事件的时候，onTouch只会执行一次（ACTION_DOWN），因为ImageView默认不可点击
//        touchIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("wanlijun","onClick");
//            }
//        });
        touchIv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("wanlijun","imageview:onTouch:"+event.getAction());
                return false;
            }
        });
        myLayout = (MyLayout)findViewById(R.id.myLayout);
        myLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("wanlijun","layout:onTouch:"+event.getAction());
                return false;
            }
        });
       myLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               LayoutInflater layoutInflater = LayoutInflater.from(ViewActivity.this);
               View view = layoutInflater.inflate(R.layout.add_btn_layout,null);
               myLayout.addView(view);
           }
       });

      ViewParent viewParent = myLayout.getParent();
      Log.i("wanlijun",viewParent+"");
        myTv = (TextView)findViewById(R.id.myTv);
        Log.i("wanlijun",myTv.requestFocus()+"");
        Log.i("wanlijun",myTv.isFocusableInTouchMode()+"");
        Log.i("wanlijun",myTv.isFocusable()+"");
        Log.i("wanlijun",myTv.isFocused()+"");
        myLv = (MyListView)findViewById(R.id.myLv);
        HashMap<String,String> map = new HashMap<>();
        map.put("浪人琵琶","小生的花伞还落在你家");
        datas.add(map);
        map = new HashMap<>();
        map.put("浪人琵琶","你美娟如花我浪迹在天涯");
        datas.add(map);
        map = new HashMap<>();
        map.put("that girl","I keep saying no");
        datas.add(map);
        map = new HashMap<>();
        map.put("shape of you","grab on my waist");
        datas.add(map);
        map = new HashMap<>();
        map.put("浪人琵琶","我情不自禁会为你牵挂");
        datas.add(map);
        map = new HashMap<>();
        map.put("讲真的","有个号码一直被存放");
        datas.add(map);
        map = new HashMap<>();
        map.put("讲真的","会不会是我被鬼迷心窍了");
        datas.add(map);
        map = new HashMap<>();
        map.put("我的将军","狼烟风沙口，还请将军少饮酒");
        datas.add(map);
        map = new HashMap<>();
        map.put("浪人琵琶","为我泡杯花茶和你有些不搭");
        datas.add(map);
        map = new HashMap<>();
        map.put("浪人琵琶","我化成风不舍一脸美梦");
        datas.add(map);
        map = new HashMap<>();
        map.put("浪人琵琶","浪人回头心动则心痛");
        datas.add(map);
        map = new HashMap<>();
        map.put("学猫叫","我们一起学猫叫，一起喵喵喵喵喵");
        datas.add(map);
        map = new HashMap<>();
        map.put("全是爱","痴情不是罪过，忘情不是洒脱");
        datas.add(map);
        map = new HashMap<>();
        map.put("落花成泥","大雨淅淅沥沥");
        datas.add(map);
        map = new HashMap<>();
        map.put("123我爱你","轻轻贴近你得耳朵，桑浪嘿哟");
        datas.add(map);
        map = new HashMap<>();
        map.put("9277","手牵手一起走在幸福的大街");
        datas.add(map);
        map = new HashMap<>();
        map.put("醉赤壁","确认过眼神，我遇上对的人");
        datas.add(map);
        adapter = new MyAdapter(ViewActivity.this,datas);
        myLv.setAdapter(adapter);
        myLv.setOnDeleteListener(new MyListView.OnDeleteListener() {
            @Override
            public void onDelete(int index) {
                Log.i("wanlijun","index="+index);
                datas.remove(index);
                adapter.notifyDataSetChanged();
            }
        });
    }
    public class MyAdapter extends BaseAdapter{
        private Context mContext;
        private List<HashMap<String,String>> mDatas;
        public MyAdapter(Context context, List<HashMap<String,String>> list){
            this.mContext = context;
            this.mDatas = list;
        }
        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layout,null);
                viewHolder = new ViewHolder(convertView);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if(viewHolder != null){
                HashMap<String,String> map = mDatas.get(position);
                Iterator<Map.Entry<String,String>> iterator = map.entrySet().iterator();
                if(iterator.hasNext()){
                    Map.Entry<String,String> entry = iterator.next();
                    viewHolder.titleTv.setText(entry.getKey());
                    viewHolder.lyricTv.setText(entry.getValue());
                }
            }
            return convertView;
        }
        class ViewHolder{
            TextView titleTv;
            TextView lyricTv;
            public ViewHolder(View view){
                titleTv = (TextView)view.findViewById(R.id.titleTv);
                lyricTv = (TextView)view.findViewById(R.id.lyricTv);
                view.setTag(this);
            }
        }
    }
}
