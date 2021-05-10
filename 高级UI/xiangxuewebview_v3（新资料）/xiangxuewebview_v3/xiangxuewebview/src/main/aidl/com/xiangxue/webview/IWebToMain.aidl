package com.xiangxue.webview;

// Declare any non-default types here with import statements
import com.xiangxue.webview.ICallbackFromMainToWeb;

interface IWebToMain {
     /**
      * actionName: 不同的action， jsonParams: 需要根据不同的action从map中读取并依次转成其他
      */
      void handleWebAction(String actionName, String jsonParams, in ICallbackFromMainToWeb callback);
}
