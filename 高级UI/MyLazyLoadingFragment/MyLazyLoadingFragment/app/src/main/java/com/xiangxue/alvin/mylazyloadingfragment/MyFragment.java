package com.xiangxue.alvin.mylazyloadingfragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * @author 享学课堂 Alvin
 * @package com.xiangxue.alvin.mylazyloadingfragment
 * @fileName MyFragment
 * @date on 2019/6/10
 * @qq 2464061231
 **/
public class MyFragment extends Fragment {
    private static final String TAG = "MyFragment";

    public static final String INTENT_INT_INDEX = "index";
    ImageView imageView;
    TextView textView;
    int tabIndex;
    CountDownTimer con;
    public static MyFragment newInstance(int tabIndex) {
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_INT_INDEX, tabIndex);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, tabIndex + " fragment " + "onAttach: " );
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, tabIndex + " fragment " + "onCreate: " );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lazy_loading,null);
        imageView = view.findViewById(R.id.iv_content);
        textView = view.findViewById(R.id.tv_loading);


        getData();
        Log.d(TAG, tabIndex + " fragment " + "onCreateView: " );
        return view;
    }

    private void getData() {
        con= new CountDownTimer(1000,100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                handler.sendEmptyMessage(0);
            }
        };
        con.start();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, tabIndex + " fragment " + "onResume: " );

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, tabIndex + " fragment " + "onPause: " );

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        StackTraceElement element[] = Thread.currentThread().getStackTrace();
//        for (StackTraceElement stackTraceElement : element) {
//            Log.d(TAG, tabIndex + " fragment "+ "setUserVisibleHint: " + stackTraceElement.toString());
//        }
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        Log.d(TAG, tabIndex + " fragment " + "setUserVisibleHint: " + isVisibleToUser );

    }




    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, tabIndex + " fragment " + "onDetach: " );
    }

    private Handler  handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            textView.setVisibility(View.GONE);
            int id ;
            switch(tabIndex) {
                case 1:
                    id = R.drawable.a;
                    break;
                case 2:
                    id = R.drawable.b;
                    break;
                case 3:
                    id = R.drawable.c;
                    break;
                case 4:
                    id = R.drawable.d;
                    break;
                    default:
                        id = R.drawable.a;
            }
            imageView.setImageResource(id);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setVisibility(View.VISIBLE);
            Log.d(TAG, tabIndex +" handleMessage: " );
            //模拟耗时工作
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        con.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
