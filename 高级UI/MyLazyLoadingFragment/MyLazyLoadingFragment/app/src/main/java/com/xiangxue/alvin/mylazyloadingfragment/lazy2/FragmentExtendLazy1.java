package com.xiangxue.alvin.mylazyloadingfragment.lazy2;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiangxue.alvin.mylazyloadingfragment.R;


/**
 * @author 享学课堂 Alvin
 * @package com.xiangxue.alvin.mylazyloadingfragment.lazyloading
 * @fileName FragmentExtendLazy1
 * @date on 2019/6/24
 * @qq 2464061231
 **/
public class FragmentExtendLazy1 extends LazyFragment {

    private static final String TAG = "FragmentExtendLazy1";

    public static final String INTENT_INT_INDEX = "index";
    ImageView imageView;
    TextView textView;
    int tabIndex;
    private CountDownTimer con;

    public static FragmentExtendLazy1 newInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_INT_INDEX, position);
        FragmentExtendLazy1 fragment = new FragmentExtendLazy1();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, tabIndex + " fragment " + "onCreate: " );
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_lazy_loading;
    }

    @Override
    protected void initView(View view) {
        imageView = view.findViewById(R.id.iv_content);
        textView = view.findViewById(R.id.tv_loading);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);


        Log.d(TAG, tabIndex + " fragment " + "initView: " );
    }

    @Override
    protected void onFragmentFirstVisible() {
        Log.d(TAG, tabIndex + " fragment " + "real onFragmentFirstVisible" );

    }

    @Override
    protected void onFragmentResume() {
        super.onFragmentResume();
        getData();
        Log.d(TAG, tabIndex + " fragment " + "更新界面" );
    }

    @Override
    protected void onFragmentPause() {
        super.onFragmentPause();
        handler.removeMessages(10);
        con.cancel();
        Log.d(TAG, tabIndex + " fragment " + "暂停一切操作 pause" );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

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
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        Log.d(TAG, tabIndex + " fragment " + "setUserVisibleHint: " + isVisibleToUser );

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, tabIndex + " fragment " + "onAttach: " );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, tabIndex + " fragment " + "onDetach: " );
    }

    private Handler handler = new Handler() {
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
        if (con != null) {
            con.cancel();
        }
    }
}
