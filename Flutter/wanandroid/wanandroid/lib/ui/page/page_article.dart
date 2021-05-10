import 'package:flutter/material.dart';
import 'package:banner_view/banner_view.dart';
import 'package:wanandroid/http/api.dart';
import 'package:wanandroid/ui/page/page_webview.dart';
import 'package:wanandroid/ui/widget/article_item.dart';

class ArticlePage extends StatefulWidget {
  @override
  _ArticlePageState createState() => _ArticlePageState();
}

class _ArticlePageState extends State<ArticlePage> {
  ///滑动控制器
  ScrollController _controller = new ScrollController();

  ///控制正在加载的显示
  bool _isLoading = true;

  ///请求到的文章数据
  List articles = [];

  ///banner图
  List banners = [];

  ///总文章数有多少
  var listTotalSize = 0;

  ///分页加载，当前页码
  var curPage = 0;

  @override
  void initState() {
    super.initState();
    _controller.addListener(() {
      ///获得 SrollController 监听控件可以滚动的最大范围
      var maxScroll = _controller.position.maxScrollExtent;

      ///获得当前位置的像素值
      var pixels = _controller.position.pixels;

      ///当前滑动位置到达底部，同时还有更多数据
      if (maxScroll == pixels && articles.length < listTotalSize) {
        ///加载更多
        _getArticlelist();
      }
    });
    _pullToRefresh();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  _getArticlelist([bool update = true]) async {
    /// 请求成功是map，失败是null
    var data = await Api.getArticleList(curPage);
    if (data != null) {
      var map = data['data'];
      var datas = map['datas'];

      ///文章总数
      listTotalSize = map["total"];

      if (curPage == 0) {
        articles.clear();
      }
      curPage++;
      articles.addAll(datas);

      ///更新ui
      if (update) {
        setState(() {});
      }
    }
  }

  _getBanner([bool update = true]) async {
    var data = await Api.getBanner();
    if (data != null) {
      banners.clear();
      banners.addAll(data['data']);
      if (update) {
        setState(() {});
      }
    }
  }

  ///下拉刷新
  Future<void> _pullToRefresh() async {
    curPage = 0;
    Iterable<Future> futures = [_getArticlelist(), _getBanner()];
    await Future.wait(futures);
    _isLoading = false;
    setState(() {});
    return null;
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        ///正在加载
        Offstage(
          offstage: !_isLoading, //是否隐藏
          child: new Center(child: CircularProgressIndicator()),
        ),

        ///内容
        Offstage(
          offstage: _isLoading,
          child: new RefreshIndicator(
              child: ListView.builder(
                itemCount: articles.length + 1,
                itemBuilder: (context, i) => _buildItem(i),
                controller: _controller,
              ),
              onRefresh: _pullToRefresh),
        ),
      ],
    );
  }

  Widget _buildItem(int i) {
    if (i == 0) {
      return new Container(
        height: 180.0,
        child: _bannerView(),
      );
    }
    var itemData = articles[i - 1];
    return new ArticleItem(itemData);
  }

  Widget _bannerView() {
    var list = banners.map((item) {
      return InkWell(
        child: Image.network(item['imagePath'], fit: BoxFit.cover), //fit 图片充满容器
        ///点击事件
        onTap: () {
          ///跳转页面
          Navigator.of(context).push(new MaterialPageRoute(builder: (context) {
            return WebViewPage(item);
          }));
        },
      );
    }).toList();
    return list.isNotEmpty
        ? BannerView(
            list,
            intervalDuration: const Duration(seconds: 3),
          )
        : null;
  }
}
