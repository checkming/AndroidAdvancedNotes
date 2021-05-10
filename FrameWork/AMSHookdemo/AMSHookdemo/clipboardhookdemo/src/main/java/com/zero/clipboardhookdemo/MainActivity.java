package com.zero.clipboardhookdemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Zero";

    @BindView(R.id.tv_show)
    TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_clip)
    public void onBtnClipClicked() {
        clipboardManagerTest();
    }

    private void clipboardManagerTest(){
        //获取剪切板服务
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        //设置剪切板内容
        cm.setPrimaryClip(ClipData.newPlainText("data","Zero"));
        //获取剪切板数据对象
        ClipData cd = cm.getPrimaryClip();
        String msg = cd.getItemAt(0).getText().toString();
        Log.i(TAG, "clipboardManagerTest: " + msg);


    }

    static class HookBinderInvocationHandler implements  InvocationHandler{

        Object base;

        public HookBinderInvocationHandler(IBinder binder,Class<?> stub){
            try {
                Method asInterfaceMethod = stub.getDeclaredMethod("asInterface",IBinder.class);
                this.base =  asInterfaceMethod.invoke(null,binder);
            } catch (Exception e) {
                Log.e(TAG, "HookBinderInvocationHandler: "+ e.getMessage() );
                e.printStackTrace();
            }
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.i(TAG, "invoke: HookBinderInvocationHandler method " + method.getName());
            if("getPrimaryClip".equals(method.getName())){
                Log.d(TAG, "invoke: hook getPrimaryClip: ");
                return ClipData.newPlainText(null,"you are hooked");
            }
            if("hasPrimaryClip".equals(method.getName())){
                return true;
            }
            return method.invoke(base,args);
        }
    }

    static class IClipboardHookBinderHandler implements InvocationHandler{

        IBinder base;
        Class<?> stub;
        Class<?> iinterface;

        public IClipboardHookBinderHandler(IBinder binder){
            base = binder;
            try {
                this.stub = Class.forName("android.content.IClipboard$Stub");
                this.iinterface = Class.forName("android.content.IClipboard");
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "IClipboardHookBinderHandler: "+ e.getMessage() );
                e.printStackTrace();
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.i(TAG, "invoke: IClipboardHookBinderHandler method " + method.getName());
            if("queryLocalInterface".equals(method.getName())){
                return  Proxy.newProxyInstance(base.getClass().getClassLoader()
                ,new Class[]{Class.forName("android.content.IClipboard")}
                ,new HookBinderInvocationHandler(base,stub));
            }
            return method.invoke(base,args);
        }
    }

    public  void hookclip(){
      //TODO:
        try {
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            Method getService = serviceManager.getDeclaredMethod("getService",String.class);
            //获取ServiceManager里面的原始的Clipboard Binder对象
            IBinder rawBinder = (IBinder)getService.invoke(null,Context.CLIPBOARD_SERVICE);
            //Hook 掉这个Binder代理对象的queryLocalInterface方法

            //远程Binder的动态代理
            IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader()
                    , new Class<?>[]{IBinder.class}
                    , new IClipboardHookBinderHandler(rawBinder));


            Field cacheField = serviceManager.getDeclaredField("sCache");
            cacheField.setAccessible(true);
            Map<String,IBinder> cache = (Map<String,IBinder>)cacheField.get(null);
            //替换掉原始的剪切板服务的binder
            cache.put(Context.CLIPBOARD_SERVICE,hookedBinder);

        } catch (Exception e) {
            Log.e(TAG, "hookclip: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_hook)
    public void onBtnHookClicked() {
        hookclip();
    }
}
