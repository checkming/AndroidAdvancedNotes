package com.xiangxue.news.homefragment.headlinenews;

import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.xiangxue.news.homefragment.newslist.NewsListFragment;

import java.util.ArrayList;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class HeadlineNewsFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<ChannelsModel.Channel> mChannels;

    public HeadlineNewsFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setChannels(ArrayList<ChannelsModel.Channel> channels){
        this.mChannels = channels;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int pos) {
        return NewsListFragment.newInstance(mChannels.get(pos).channelId, mChannels.get(pos).channelName);
    }

    @Override
    public int getItemPosition(Object object) {
        return FragmentPagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        if(mChannels != null && mChannels.size() > 0) {
            return mChannels.size();
        }
        return 0;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader){
    }
}