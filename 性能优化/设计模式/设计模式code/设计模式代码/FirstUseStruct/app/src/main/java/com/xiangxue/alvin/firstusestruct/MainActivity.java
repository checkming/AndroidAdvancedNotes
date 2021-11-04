package com.xiangxue.alvin.firstusestruct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xiangxue.alvin.firstusestruct.bean.PhotoSetInfo;
import com.yuyh.library.BubblePopupWindow;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    Button button;
    TextView mounth;
    private String url = "http://c.3g.163.com/photo/api/set/0001%2F2250173.json";
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn);
        mounth = findViewById(R.id.point);
        button.setOnClickListener(this);
        mQueue = Volley.newRequestQueue(this);
//        initBubble(mounth);
    }

    private void initBubble(View view, String info){
        BubblePopupWindow leftTopWindow = new BubblePopupWindow(MainActivity.this);
        View bubbleView = getLayoutInflater().inflate(R.layout.layout_popup_view, null);
        TextView tvContent = (TextView) bubbleView.findViewById(R.id.tvContent);
        tvContent.setText(info);
        leftTopWindow.setBubbleView(bubbleView); // 设置气泡内容
        leftTopWindow.show(view, Gravity.BOTTOM, 0); // 显示弹窗
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        HttpHelper<PhotoSetInfo> helper = new HttpHelper<PhotoSetInfo>().ge;
                        PhotoSetInfo inof = new BaseType<PhotoSetInfo>(){

                        }.getResult(response);
                        initBubble(mounth, inof.getDesc());
                        Log.d(TAG, "Network result：" + response );
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Network onErrorResponse：" + error.toString() );
                    }
                });
                mQueue.add(stringRequest);
                break;
        }
    }
}
