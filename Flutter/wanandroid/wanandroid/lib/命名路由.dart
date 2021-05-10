import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Demo',
      home: MainRoute(),
      //注册路由表
      routes: {
        "/" :(context) => MainRoute(),
        "new_page": (context) => SecondRoute(),
      },
    );
  }
}

class MainRoute extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("主页"),
      ),
      body: Column(
        children: <Widget>[
          Text("第一个页面"),
          RaisedButton(
            onPressed: () async {
              //导航到新路由
              var result = await Navigator.pushNamed(context, "new_page");
              debugPrint("返回:$result");
            },
            child: Text("进入第二页"),
          )
        ],
      ),
    );
  }
}

class SecondRoute extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("第二页"),
      ),
      body: Column(
        children: <Widget>[
          Text("第一个页面"),
          RaisedButton(
            onPressed: () {
              //路由pop弹出
              Navigator.pop(context, "结束");
            },
            child: Text("返回"),
          )
        ],
      ),
    );
  }
}