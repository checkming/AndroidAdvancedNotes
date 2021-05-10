var dj = {};
dj.os = {};
dj.os.isIOS = /iOS|iPhone|iPad|iPod/i.test(navigator.userAgent);
dj.os.isAndroid = !dj.os.isIOS;
dj.callbackname = function(){
    return "djapi_callback_" + (new Date()).getTime() + "_" + Math.floor(Math.random() * 10000);
};
dj.callbacks = {};
dj.addCallback = function(name,func,userdata){
    delete dj.callbacks[name];
    dj.callbacks[name] = {callback:func,userdata:userdata};
};

dj.callback = function(para){
    var callbackobject = dj.callbacks[para.callbackname];
    if (callbackobject !== undefined){
        if (callbackobject.userdata !== undefined){
            callbackobject.userdata.callbackData = para;
        }
        if(callbackobject.callback != undefined){
            var ret = callbackobject.callback(para,callbackobject.userdata);
            if(ret === false){
                return
            }
            delete dj.callbacks[para.callbackname];
        }
    }
};

dj.post = function(cmd,para){
    if(dj.os.isIOS){
        var message = {};
        message.meta = {
            cmd:cmd
        };
        message.para = para || {};
        window.webview.post(message);
    }else if(window.dj.os.isAndroid){
        window.webview.post(cmd,JSON.stringify(para));
    }
};
dj.postWithCallback = function(cmd,para,callback,ud){
    var callbackname = dj.callbackname();
    dj.addCallback(callbackname,callback,ud);
    if(dj.os.isIOS){
        var message = {};
        message.meta  = {
            cmd:cmd,
            callback:callbackname
        };
        message.para = para;
        window.webview.post(message);
    }else if(window.dj.os.isAndroid){
        para.callback = callbackname;
        window.webview.post(cmd,JSON.stringify(para));
    }
};
dj.dispatchEvent = function(para){
    if (!para) {
        para = {"name":"webviewLoadComplete"};
    }
    var evt = {};
    try {
        evt = new Event(para.name);
        evt.para = para.para;
    } catch(e) {
        evt = document.createEvent("HTMLEvents");
        evt.initEvent(para.name, false, false);
    }
    window.dispatchEvent(evt);
};
dj.nativecallback = function(obj){
    if(dj.os.isIOS){
        return dj.stringify(obj.data);
    }else if(window.dj.os.isAndroid){
        window.webview.post(obj.callback,dj.stringify(obj));
    }
};


window.dj = dj;
