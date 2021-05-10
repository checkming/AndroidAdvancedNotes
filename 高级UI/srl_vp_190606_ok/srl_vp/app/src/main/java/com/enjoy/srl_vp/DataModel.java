package com.enjoy.srl_vp;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class DataModel {

    public static List<Fragment> getFragmentList1() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(new AFragment());
        list.add(new BFragment());
        return list;
    }

}
