import 'package:flutter/material.dart';

void main() => runApp(Animation1());

class Animation1 extends StatefulWidget {
  @override
  _Animation1State createState() => _Animation1State();
}

class _Animation1State extends State<Animation1> {
  GlobalKey<NavigatorState> key = GlobalKey<NavigatorState>();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      navigatorKey: key,
      home: Scaffold(
        appBar: AppBar(
          title: Text("动画"),
        ),
        body: Row(
          children: <Widget>[
            Expanded(child: Text("11111")),
            InkWell(
              child: Container(
                  width: 120.0,
                  height: 120.0,
                  child: Hero(
                    tag: "aaa",
                    child: Image.network(
                        "https://www.wanandroid.com/blogimgs/ab17e8f9-6b79-450b-8079-0f2287eb6f0f.png"),
                  )),
              onTap: () {
                key.currentState.push(MaterialPageRoute(builder: (_) {
                  return NewPage();
                }));
              },
            ),
            InkWell(
              child: Hero(
                tag: "bbb",
                child: Image.network(
                  "https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png",
                  width: 120,
                  height: 120,
                ),
              ),
              onTap: () {
                key.currentState.push(MaterialPageRoute(builder: (_) {
                  return NewPage();
                }));
              },
            )
          ],
        ),
      ),
    );
  }
}

class NewPage extends StatefulWidget {
  @override
  _NewPageState createState() => _NewPageState();
}

class _NewPageState extends State<NewPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("新界面"),
      ),
      body: Stack(
        children: <Widget>[
          Container(
            color: Colors.yellow,
          ),
          Hero(
              tag: "aaa",
              child: Image.network(
                "https://www.wanandroid.com/blogimgs/ab17e8f9-6b79-450b-8079-0f2287eb6f0f.png",
                width: 80,
                height: 80,
              )),
          Positioned(
            width: 80.0,
            height: 80.0,
            bottom: 10.0,
            right: 10.0,
            child: Container(
                color: Colors.red,
                child: Hero(
                    tag: "bbb",
                    child: Image.network(
                      "https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png",
                    ))),
          )
        ],
      ),
    );
  }
}
