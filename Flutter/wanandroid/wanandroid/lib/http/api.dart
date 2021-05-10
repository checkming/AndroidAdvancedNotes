import 'package:wanandroid/http/http_manager.dart';

typedef void OnResult(Map<String, dynamic> data);

class Api {
  static const String baseUrl = "http://www.wanandroid.com/";

  //首页文章列表 http://www.wanandroid.com/article/list/0/json
  static const String ARTICLE_LIST = "article/list/";

  static const String BANNER = "banner/json";

  //登录
  static const String LOGIN = "user/login";

  //注册
  static const String REGISTER = "user/register";

  //退出
  static const String LOGOUT = "user/logout/json";

  static getArticleList(int page) async {
    return await HttpManager.getInstance().request('$ARTICLE_LIST$page/json');
  }

  static getBanner() async {
    return await HttpManager.getInstance().request(BANNER);
  }

  static login(String username, String password) async {
    return await HttpManager.getInstance().request(LOGIN,
        data: "username=$username&password=$password", method: "post");
  }
}
