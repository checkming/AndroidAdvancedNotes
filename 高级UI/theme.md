# Theme Style Attr
- Theme：主题，它与Style作用一样，不同于Style作用于个一个单独View，而它是作用于Activity上或是整个应用。
- Style：风格，它是一系列Attr的集合用以定义一个View的样式，比如height、width、padding等；
- Attr：属性，风格样式的最小单元；

##  @ 代表引用资源
>  @[package:]type/name
1. 引用自定义资源
```android：text="@string/hello"```
2. 引用系统资源
```android:textColor="@android:color/opaque_red"```

## @*代表引用系统的非public资源
> @*android:type/name
- 系统资源定义分public和非public。public的声明在：
```<sdk_path>\platforms\android-28\data\res\values\public.xml```
- @*android:type/name：可以调用系统定义的所有资源
- @android:type/name：只能够调用publi属性的资源。
- **注意：没在public.xml中声明的资源是google不推荐使用的**

##  ？代表引用主题属性
> ?[namespace:]type/name
> 们使用一个"?"前缀代替了"@"。当你使用这个标记时，你就提供了属性资源的名称，它将会在主题中被查找，所以你不需要显示声明这个类型(如果声明，其形式就是?android:attr/android:textDisabledColor)。除了使用这个资源的标识符来查询主题中的值代替原始的资源，其命名语法和"@"形式一致：?[namespace:]type/name，这里类型可选

```android:textColor="?android:textDisabledColor"```
```android:textColor="?android:attr/android:textDisabledColor"```

##  @+代表在创建或引用资源 
> @+type/name

- ”+”表示在R.java中名为type的内部类中添加一条记录。如"@+id/button"的含义是在R.java 文件中的id 这个静态内部类添加一条常量名为button
-  @id/资源ID名          应用现有已定义的资源ID，包括系统ID
-  @android:id/资源ID名   引用系统ID，其等效于@id/资源ID名

## Android 主题层级
- 鉴于不同的安卓平台定义了不同的主题、样式和属性，最初安卓主题的层级非常繁杂，而且很不直观。直到 v7 支持库带来了全新的主题架构，使得所有安卓平台自 API v7 起能够获得一致的材质外观 (Matertial apperance)。Base.V... 和 Platform.AppCompat 正是在这个时候被加入了进来。
在 AppCompat 中，主题被划分为四个层次，每个层次继承自更低一层：

- Level1 → Level2 → Level3 → Level4

- 除此之外，每个版本的安卓 API 都有一个对应的 values-v{api} 文件夹存放各自需要定义或覆写的样式和属性：

- values, values-v11, values-v14, values-v21, values-v22, values-v23

- Level 4 （最底层）
最底层包含了 Platform.AppCompat 主题。该主题总是继承自当前版本中的默认主题，例如：

- values

Platform.AppCompat -> android:Theme

- values-v11

Platform.AppCompat -> android:Theme.Holo

- values-v21

Platform.AppCompat -> android:Theme.Material

- Level 3
大部分工作在这一层被完成，Base.V7.Theme.AppCompat, Base.V11.Theme.AppCompat, Base.V21.Theme.AppCompat 等也是在这一层被定义。这些主题都继承自 Platform.AppCompat。

- values

Base.V7.Theme.AppCompat* → Platform.AppCompat → android:Theme

- values-v11

Base.V11.Theme.AppCompat → Platform.AppCompat → android:Theme.Holo

- values-v21

Base.V21.ThemeAppCompat → Base.V7.ThemeAppCompat → Platform.AppCompat → android:Theme.Material

**: 还包括 Base.V7.Theme.AppCompat.Light, Base.V7.Theme.AppCompat.Dialog 等变体。**

绝大多数属性和几乎所有工作在 Base.V{api}.Theme.AppCompat 中被定义和完成。ActionBar, DropwDown, ActionMode, Panel, List, Spinner, Toolbar 等控件中的所有属性都在这里被定义。你可以在 这个链接 中查看更多详情。

- Level 2
根据安卓的官方解释，我们在这一层拿到的主题只是第三层主题的别名：

There are the themes which are pointers to the correct third level theme.They can also be used to set attributes for that specific platform (and platforms up until the next declaration).

这些主题指向第三层中相应的主题。它们也可以用来配置那些特定平台的属性。

- values

Base.Theme.AppCompat* → Base.V7.Theme.AppCompat

- values-v21

Base.Theme.AppCompat → Base.V21.Theme.AppCompat

*: 还包括 Base.Theme.AppCompat.Light, Base.Theme.AppCompat.Dialog 等变体。

- Level 1 （最顶层）
Theme.AppCompat, Theme.AppCompat.Light, Theme.AppCompat.NoActionBar 等主题在这里被定义。开发者应该使用这些主题，而非那些更底层的。

- values

Theme.AppCompat → Base.Theme.AppCompat

- 这些主题只在 values 文件夹中被定义，并根据安卓应用运行的 API 环境，继承自下层中定义的相应主题。例如：

- Running in v7 (Android 2.2)
Theme.AppCompat → Base.Theme.AppCompat → Base.V7.Theme.AppCompat → Platform.AppCompat → android:Theme

- Running in v11 (Android 3.0)
Theme.AppCompat → Base.Theme.AppCompat → Base.V7.Theme.AppCompat → Platform.AppCompat → Platform.V11.AppCompat → android:Theme.Holo

- Running in v21 (Android 5.0)
Theme.AppCompat → Base.Theme.AppCompat → Base.V21.Theme.AppCompat → Base.V7.Theme.AppCompat → Platform.AppCompat → android:Theme.Material


## Theme Style 加载时机和源码浅析
- Activity#setContentView
- PhoneWindow#installDecor
- PhoneWindow#generateLayout
- Window#getWindowStyle
- Context#obtainStyledAttributes
- ContextThemeWapprer#getTheme
- ContextThemeWapprer#setTheme
- ContextThemeWapprer#selectDefaultTheme
- ContextThemeWapprer#selectSystemTheme
- ContextThemeWapprer#onApplyThemeResource










































