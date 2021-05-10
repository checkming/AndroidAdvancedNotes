import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  final GlobalKey<NavigatorState> navigatorKey = GlobalKey();
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      navigatorKey: navigatorKey,
      title: 'Flutter Demo',
      home: Scaffold(
        appBar: AppBar(
          title: Text("主页"),
        ),



        body: Column(
          children: <Widget>[
            Text("第一个页面"),
            RaisedButton(
              onPressed: ()  {
                ///输出Navigator
                debugPrint(navigatorKey.currentWidget.runtimeType.toString());
                navigatorKey.currentState.push(MaterialPageRoute(builder: (_){
                  return new SecondRoute();
                }));
              },
              child: Text("进入第二页"),
            )
          ],
        ),
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
              Navigator.pop(context);
            },
            child: Text("返回"),
          )
        ],
      ),
    );
  }
}
