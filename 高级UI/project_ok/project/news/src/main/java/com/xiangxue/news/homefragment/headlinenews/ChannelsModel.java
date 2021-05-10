package com.xiangxue.news.homefragment.headlinenews;

import com.arch.demo.core.model.BaseModel;
import com.xiangxue.news.homefragment.api.NewsApi;
import com.arch.demo.network_api.observer.BaseObserver;
import com.arch.demo.network_api.errorhandler.ExceptionHandle;
import com.arch.demo.network_api.beans.NewsChannelsBean;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class ChannelsModel extends BaseModel<ArrayList<ChannelsModel.Channel>> {
    private static final String PREF_KEY_HOME_CHANNEL = "pref_key_home_channel";
    public static final String PREDEFINED_CHANNELS = "[\n" +
            "    {\n" +
            "        \"channelId\": \"5572a108b3cdc86cf39001cd\",\n" +
            "        \"channelName\": \"国内焦点\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"channelId\": \"5572a108b3cdc86cf39001ce\",\n" +
            "        \"channelName\": \"国际焦点\"\n" +
            "    }\n" +
            "]";

    public class Channel {
        public String channelId;
        public String channelName;
    }

    protected String getCachedPreferenceKey() {
        return PREF_KEY_HOME_CHANNEL;
    }

    protected Type getTClass() {
        return new TypeToken<ArrayList<Channel>>() {}.getType();
    }

    protected String getApkString() {
        return PREDEFINED_CHANNELS;
    }

    @Override
    public void refresh() {
    }

    @Override
    protected void load() {
        NewsApi.getInstance().getNewsChannels(new BaseObserver<NewsChannelsBean>(this) {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                e.printStackTrace();
                loadFail(e.message);
            }

            @Override
            public void onNext(NewsChannelsBean newsChannelsBean) {
                ArrayList<Channel> channels = new ArrayList<>();
                for (NewsChannelsBean.ChannelList source : newsChannelsBean.showapiResBody.channelList) {
                    Channel channel = new Channel();
                    channel.channelId = source.channelId;
                    channel.channelName = source.name;
                    channels.add(channel);
                }
                loadSuccess(channels);
            }
        });
    }
}
