import 'package:flutter/material.dart';

void main() => runApp(Animation1());

class Animation1 extends StatefulWidget {
  @override
  _Animation1State createState() => _Animation1State();
}

class _Animation1State extends State<Animation1>
    with SingleTickerProviderStateMixin {
  AnimationController animationController;

  CurvedAnimation curvedAnimation;

  Animation<double> animate;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text("动画"),
        ),
//
        body: Column(
          children: <Widget>[
            RaisedButton(onPressed: (){animationController.stop();},child: Text("1111")),
            Container(
              width: animate.value,
              height: animate.value,
              color: Colors.red,
              child: Text("1111"),
            ),
          ],
        ),
      ),
    );
  }

  @override
  void dispose() {
    animationController.dispose();
    super.dispose();
  }

  @override
  void initState() {
    animationController = AnimationController(
        duration: Duration(seconds: 3),
        vsync: this);

    animationController.addListener(() {
      setState(() {

      });
    });

    animationController.addStatusListener((AnimationStatus status){
      print(status);
      /// 在正向动画完成之后是 completed
        if(status == AnimationStatus.completed){
//          animationController.reverse();
          ///在反向结束了之后 是 dismissed
        }else if(status == AnimationStatus.dismissed){
          animationController.forward();
        }
    });
     curvedAnimation = CurvedAnimation(parent: animationController, curve: Curves.bounceInOut);
       animate = Tween(begin: 100.0,end: 300.0).animate(curvedAnimation);

    //启动动画
    animationController.forward();

  }
}
