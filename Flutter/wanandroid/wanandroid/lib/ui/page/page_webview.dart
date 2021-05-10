import 'package:flutter/material.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';

class WebViewPage extends StatefulWidget {
  final data;

  WebViewPage(this.data);

  @override
  _WebViewPageState createState() => _WebViewPageState();
}

class _WebViewPageState extends State<WebViewPage> {
  bool isLoad = true;
  FlutterWebviewPlugin flutterWebViewPlugin;

  @override
  void initState() {
    super.initState();
    flutterWebViewPlugin = new FlutterWebviewPlugin();
    flutterWebViewPlugin.onStateChanged.listen((state) {
      if (state.type == WebViewState.finishLoad) {
        // 加载完成
        setState(() {
          isLoad = false;
        });
      } else if (state.type == WebViewState.startLoad) {
        setState(() {
          isLoad = true;
        });
      }
    });
  }

  @override
  void dispose() {
    flutterWebViewPlugin.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    ///WebView插件
    return WebviewScaffold(
        appBar: AppBar(
          title: Text(widget.data['title']),
          ///appbar下边摆放一个进度条
          bottom: PreferredSize(
              preferredSize: const Size.fromHeight(1.0),
              child: const LinearProgressIndicator()),
          ///透明度
          bottomOpacity: isLoad ? 1.0 : 0.0,
        ),
        withLocalStorage: true, //缓存，数据存储
        url: widget.data['url'],
        withJavascript: true);
  }
}
