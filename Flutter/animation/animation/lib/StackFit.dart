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
              Expanded(child: Stack(
                fit: StackFit.passthrough,
                children: <Widget>[
                  Positioned(
                    child: Container(
                      width: 120,
                      height: 120,
                      color: Colors.red,
                      child: Text("2"),
                    ),
                  )
                ],
              )),
              Container(
                width: 120,
                height: 120,
                color: Colors.yellow,
                child: Text("1111111"),
              )
            ],
          )),
    );
  }
}
