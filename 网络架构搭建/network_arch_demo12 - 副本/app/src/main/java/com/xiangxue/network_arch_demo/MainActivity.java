package com.xiangxue.network_arch_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.xiangxue.network.TecentNetworkApi;
import com.xiangxue.network.environment.EnvironmentActivity;
import com.xiangxue.network_arch_demo.api.NewsApiInterface;
import com.xiangxue.network_arch_demo.api.NewsChannelsBean;
import com.xiangxue.network.observer.BaseObserver;
import com.xiangxue.network_arch_demo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    private boolean isShowGenerateSslPinSet = false;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.getNewsChannels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TecentNetworkApi.getService(NewsApiInterface.class)
                        .getNewsChannels()
                        .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsChannelsBean>() {
                            @Override
                            public void onSuccess(NewsChannelsBean newsChannelsBean) {
                                Log.e("MainActivity", new Gson().toJson(newsChannelsBean));
                            }

                            @Override
                            public void onFailure(Throwable e) {

                            }
                        }));
            }
        });
        mainBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i++ == 5) {
                    startActivity(new Intent(MainActivity.this, EnvironmentActivity.class));
                }
            }
        });
        mainBinding.generateSslPinSetId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // From https://github.com/scottyab/ssl-pin-generator
                            SSLPinGenerator calc = new SSLPinGenerator(mainBinding.domainInputId.getText().toString(), 443, "sha-256", false);
                            calc.fetchAndPrintPinHashs();
                        } catch (Exception e) {
                            System.out.println("\nWhoops something went wrong: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();// do your sign-out stuff
        if (i == android.R.id.home) {
            finish();
        } else if (i == R.id.generate_ssl_pin_set) {
            // case blocks for other MenuItems (if any)
            mainBinding.getNewsChannels.setVisibility(View.GONE);
            mainBinding.domainInputId.setVisibility(View.VISIBLE);
            mainBinding.generateSslPinSetId.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
