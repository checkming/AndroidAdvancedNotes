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
    print("build");
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text("动画"),
        ),
//
        body: Column(
          children: <Widget>[
            RaisedButton(onPressed: (){animationController.stop();},child: Text("1111")),
//            MyAnimationWidget(animate),
            AnimatedBuilder(child: Text("1111"),animation: animate, builder: (context,child){
              return Container(
                width: animate.value,
                height: animate.value,
                color: Colors.red,
                child: child,
              );
            })
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
    curvedAnimation = CurvedAnimation(parent: animationController, curve: Curves.bounceInOut);
    animate = Tween(begin: 100.0,end: 300.0).animate(curvedAnimation);
    //启动动画
    animationController.forward();

  }
}



class MyAnimationWidget extends AnimatedWidget{

  @override
  Widget build(BuildContext context) {
    Animation animation = listenable;
    return Container(
      width: animation.value,
      height: animation.value,
      color: Colors.red,
      child: Text("1111"),
    );
  }

  MyAnimationWidget(Animation animation):super(listenable:animation);
}
