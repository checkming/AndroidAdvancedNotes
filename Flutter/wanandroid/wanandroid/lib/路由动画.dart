import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Demo',
      home: MainRoute(),
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
              var result = await Navigator.push(
                context,
                PageRouteBuilder(
                  ///动画时间
                    transitionDuration: Duration(milliseconds: 500),
                    pageBuilder: (BuildContext context, Animation animation,
                        Animation secondaryAnimation) {
                      ///透明渐变与旋转
                      return new FadeTransition(
                        opacity: animation,
                        child: new RotationTransition(
                          turns: new Tween<double>(begin: 0.5, end: 1.0)
                              .animate(animation),
                          child: SecondRoute(),
                        ),
                      );
                    },),
              );
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
