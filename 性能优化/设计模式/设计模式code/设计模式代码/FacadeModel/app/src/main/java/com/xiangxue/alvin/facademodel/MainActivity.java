package com.xiangxue.alvin.facademodel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiangxue.alvin.facademodel.bean.PhotoSetInfo;
import com.yuyh.library.BubblePopupWindow;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    Button button;
    TextView mounth;
    private String url = "http://c.3g.163.com/photo/api/set/0001%2F2250173.json";
    private Map<String,Object> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn);
        mounth = findViewById(R.id.point);
        button.setOnClickListener(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                button.setText("hhh");
//            }
//        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                FacadeNetwork<String> facadeNetwork = FacadeNetwork.getInstance(this);
                facadeNetwork.get(url, params, new FacadeNetwork.Callback<PhotoSetInfo>() {
                    @Override
                    public void onSuccess(PhotoSetInfo response) {
                        initBubble(mounth, response.getDesc());
                        Log.d(TAG, "Network result：" + response );
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }).start();
                    }


                    @Override
                    public void onFailed(String failed) {

                    }
                });
                break;
        }
    }

    private void initBubble(View view, String info){
        BubblePopupWindow leftTopWindow = new BubblePopupWindow(MainActivity.this);
        View bubbleView = getLayoutInflater().inflate(R.layout.layout_popup_view, null);
        TextView tvContent = (TextView) bubbleView.findViewById(R.id.tvContent);
        tvContent.setText(info);
        leftTopWindow.setBubbleView(bubbleView); // 设置气泡内容
        leftTopWindow.show(view, Gravity.BOTTOM, 0); // 显示弹窗
    }
}
