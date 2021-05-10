package com.zero.animationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;


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
                "视图动画",//0
                "帧动画",//1
                "属性动画",//2
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
        Intent gotoAct;
        switch (position) {
            case 0://视图动画
                gotoAct = new Intent(getActivity(),ViewAnimationActivity.class);
                startActivity(gotoAct);
                break;
            case 1://帧动画
                gotoAct = new Intent(getActivity(),DrawableAnimationActivity.class);
                startActivity(gotoAct);
                break;
            case 2://属性动画
                gotoAct = new Intent(getActivity(),PropertyAnimationActivity.class);
                startActivity(gotoAct);
                break;
            default:
                break;
        }
    }


}
