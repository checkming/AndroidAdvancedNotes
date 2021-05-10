package com.zero.rxjavademo02.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.zero.rxjavademo02.ui.op.OpActivity;
import com.zero.rxjavademo02.ui.retrofit.RetrofitLoginActivity;
import com.zero.rxjavademo02.ui.retrofit.RetrofitTestActivity;
import com.zero.rxjavademo02.ui.rxbinding.ButtonClicksActivity;
import com.zero.rxjavademo02.ui.rxbinding.FilterActivity;
import com.zero.rxjavademo02.ui.rxbinding.LoginActivity;
import com.zero.rxjavademo02.ui.rxbinding.RegisterActivity;
import com.zero.rxjavademo02.ui.rxbinding.RxJavaSzActivity;


public class MainFragment extends ListFragment {


    public static Fragment newIntance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    ArrayAdapter<String> arrayAdapter;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] array = new String[]{
                "retrofit网络测试",
                "注册/登录",
                "ButtonClicksActivity",
                "LoginActivity",
                "RegisterActivity",
                "FilterActivity",
                "OpActivity",
                "RxJavaSzActivity"
        };
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        String item = arrayAdapter.getItem(position);
        Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();
        switch (position) {
            case 0:
                startActivity(new Intent(getActivity(), RetrofitTestActivity.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(), RetrofitLoginActivity.class));
                break;
            case 2:
                startActivity(new Intent(getActivity(), ButtonClicksActivity.class));
                break;
            case 3:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case 4:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
            case 5:
                startActivity(new Intent(getActivity(), FilterActivity.class));
                break;
            case 6:
                startActivity(new Intent(getActivity(), OpActivity.class));
                break;
            case 7:
                startActivity(new Intent(getActivity(), RxJavaSzActivity.class));
                break;
            default:
                break;
        }
    }
}
