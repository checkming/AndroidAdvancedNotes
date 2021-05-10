import 'package:flutter/material.dart';
import 'package:wanandroid/ui/page/page%20_login.dart';
import 'ui/page/page_article.dart';


void main() => runApp(new ArticleApp());

class ArticleApp extends StatelessWidget {

  final GlobalKey<NavigatorState> navigatorKey = GlobalKey();
  @override
  Widget build(BuildContext context) {

    return new MaterialApp(
      ///相当于注册了一个
      navigatorKey: navigatorKey,
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text(
            '文章',
            style: const TextStyle(color: Colors.white),
          ),
        ),
        drawer: Drawer(
          child: _buildDrawer(context),
        ),
        body: new ArticlePage(),
      ),
    );
  }

  _buildDrawer(context) {
    Widget userHeader = DrawerHeader(
      decoration: BoxDecoration(
        color: Colors.blue,
      ),
      child: InkWell(
        /// 点击进入登录界面
        onTap: (){
          ///todo of时候，它会从context的父级一层层向上开始查找，这里的context代表了ArticleApp
          ///todo 父级就是Flutter的Root了，不属于StateFulElement （看源码）
          Navigator.of(context).push(new MaterialPageRoute(builder: (context) {
            return LoginPage();
          }));
        navigatorKey.currentState.push(new MaterialPageRoute(builder: (context) {
          return LoginPage();
        }));
        },
        child: Column(
          children: <Widget>[
            Padding(
              padding: EdgeInsets.only(bottom: 18.0),
              child: CircleAvatar(
                backgroundImage: AssetImage("assets/images/logo.png"),
                radius: 38.0,
              ),
            ),
            Text(
              "请先登录",
              style: TextStyle(color: Colors.white, fontSize: 18.0),
            )
          ],
        ),
      ),
    );
    return ListView(
      ///不设置会导致状态栏灰色
      padding: EdgeInsets.zero,
      children: <Widget>[
        userHeader,
        ListTile(
          leading: Icon(Icons.favorite),
          title: Text('收藏', style: TextStyle(fontSize: 16.0)),
        )
      ],
    );
  }
}