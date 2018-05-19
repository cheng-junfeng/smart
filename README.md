# Smart

### 1.关于模块的说明

	粗略划分为了9个模块，可以根据需要裁剪。具体如下：
- base:  			        基准库（主体窗口，事件，回调，全局配置，网络，拦截器，转化器，控件，适配器，工具，切换动画）
- app：  			        主模块 (多进程，数据库，网络服务，引导，启动页，主窗口，分页，详情，通知，扫码)

- Auser(依赖base): 		用户模块 (DB, 网络, 登录/注册/我的/关于/版本更新/修改密码/设置/意见反馈)
- Apush(依赖base): 		推送模块 (服务, 消息接收 极光/MQTT)
- Apermission(依赖base):  权限模块 (权限, 插件)
- Amap(依赖base)：        地图模块 (百度定位, 轨迹)

- videoAudio(独立):       音视频模块 (视频捕获，音频获取)
- zxinglib(独立):         扫码模块   (条形码，二维码 获取)
- webview(独立):          网页模块   (Jsbridage/JavascriptInterface 互调)


### 2.关于开源项目的说明
	常用
- 'com.google.code.gson:gson:2.6.2'                 gson
- 'com.blankj:utilcode:1.7.1'	                   工具库
- 'com.android.support:recyclerview-v7:26.1.0'	     recyclerview
- 'com.squareup.okhttp3:okhttp:3.4.1'               okhttp3
- 'io.reactivex.rxjava2:rxjava:2.0.1'
- 'io.reactivex.rxjava2:rxandroid:2.0.1'           rx2 链式
- 'com.squareup.retrofit2:retrofit:2.1.0'
- 'com.squareup.retrofit2:adapter-rxjava2:2.2.0'
- 'com.squareup.retrofit2:converter-gson:2.1.0'        retrofit2 网络封装
- 'com.trello.rxlifecycle2:rxlifecycle:2.1.0'
- 'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'  rx生命周期
- "com.jakewharton:butterknife:8.5.1"
- "com.jakewharton:butterknife-compiler:8.5.1"               butterKnifeVer
- "org.greenrobot:greendao:3.2.2"                        greendao
- "com.github.bumptech.glide:glide:3.7.0"                Glide 图片加载
- “com.tencent.bugly:crashreport_upgrade:1.3.4”          Bugly 日志和更新
- "com.android.support:cardview-v7:26.1.0"               卡片布局

	扩展
- 'com.wuxiaolong.pullloadmorerecyclerview:library:1.1.2'  上拉加载下拉刷新
- 'cn.bingoogolapple:bga-badgeview-api:1.1.7'
- "cn.bingoogolapple:bga-badgeview-compiler:1.1.7"     角标


### 4.自定义View
	以下优秀的自定义View, 可以参考
- 1 ZxingView           RelativeLayout：条形码+二维码扫描
- 2 BGABadgeTextView    TextView：角标显示
- 3 WatcherBoard        View：模拟数字时钟
- 4 CommEditText        EditText: 密码显示与隐藏，全删
- 5 MultiEditInputView  LinearLayout: 字段输入，字数倒计时
- 6 SwitchButton        View：按钮切换
- 7 WaveSideBarView     View：波浪滑动条
- 8 MarqueeView     	ViewFlipper: 文字轮播

 
### 5.关于Gradle
- 1 项目gradle 建议配置并行，加大内存
- org.gradle.parallel=true
- org.gradle.daemon=true
- org.gradle.jvmargs=-Xmx1536m
- 2 编译SDK 建议使用统一版本
- ext {
-  releaseVer = true
-  compileSdkVersion = 26
-  buildToolsVersion = '25.0.3'
- }
- 3 调试阶段，版本名称带上hash, 方便定位
- versionName = '1.0.2'+getGitHeadRefsSuffix()
- 4 buildConfigField 配置编译参数，代码中可直接通过BuildConfig调用
- android{
-  defaultConfig {
-   if(rootProject.ext.releaseVer){
-    buildConfigField "boolean", "RELEASE",   "true"
-    buildConfigField "String", "APP_NAME",   rootProject.ext.packageNam
-   }else{
-    buildConfigField "boolean", "RELEASE",   "false"
-    buildConfigField "String", "APP_NAME",   rootProject.ext.packageNam
-   }
-    manifestPlaceholders = [APP_KEY: "12345fdafasdfad", APP_ID: rootProject.ext.applicationId]
-  }
-}
- 5 manifestPlaceholders 配置manifest 中的meta值，可以是地图，推送，IM的key值

 
### 6.功能展示
<img src="http://i2.bvimg.com/645352/e1d78dd538c3271c.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/f50f921e5b7fd1b9.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/bbe5ada28baf395b.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/a224d91ff8d07552.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/673eece31a7da525.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/063b415c6c896ce4.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/7ed595d20a80ed9a.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/a7c896ce221d01af.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/71acd6e6b23b3108.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/bce8eaa76591609f.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/de0cfb75caf413f9.png" width = "270" height = "480" align=center />
<img src="http://i4.bvimg.com/645352/03bf03315d8a1d93.png" width = "270" height = "480" align=center />
<img src="http://i4.bvimg.com/645352/8e49fdf8c5678540.png" width = "270" height = "480" align=center />
<img src="http://i4.bvimg.com/645352/496945712f86291b.png" width = "270" height = "480" align=center />


### 7.后记
- 1 欢迎沟通
- Email: sjun945@outlook.com 
- WeChat: cheng-junfeng

- 2 项目中有很多参考的是别人优秀的开源代码，未标记出处
- 如有版权问题，请与我联系。

- 3 项目代码持续优化中


### 8.更新说明
- 1 新增布局：表头+侧滑+底部框+筛选+悬浮按钮+视图分页+倒计时+弹框+文字轮播+照片长廊
- 2 新增布局：照片轮播+拍照选图+相册选图
- 3 新增功能：二维码生成























