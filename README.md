# Smart

### 1.关于模块的说明

- 非常粗略地划分为了20个模块，可以根据需要裁剪。具体如下：
6个库 + 8个功能模块 + 6个第三方SDK模块
- baselib:  	   基准库（主体窗口/事件/回调/全局配置/网络/拦截器/转化器/适配器/工具/切换动画）
- hintlib:         提示模块   (自定义弹框/加载条/Toast)
- customlib:       自定义布局模块   (左滑删除/编辑全清/倒计时/文字轮播/倒计数/秒表)
- zxinglib:        扫码模块   (条形码/二维码)
- webviewlib:      网页模块   (Jsbridage)
- permissionlib:   权限模块   (权限工具)

- app(依赖base)：  		主模块 (进程，线程，数据库，网络服务，引导，启动页，主窗口，分页，详情，通知，扫码)
- user(依赖base): 		用户模块 (DB/网络/登录/注册/我的/关于/版本更新/修改密码/设置/意见反馈)
- photo(依赖base):      图片模块 (查看，缩放，编辑)
- video(依赖base):      视频模块 (四种播放, 捕获，列表)
- audio(依赖base)：     音频模块 (捕获，播放)
- word(依赖base):       文本模块 (word查看/编辑，pdf查看)
- file(依赖base):       文件模块 (文件列表查看/属性/内容)
- context:              上下文模块 (当前进程/Task)

- push(依赖base): 		推送模块 (极光推送，MQTT, 服务)
- baidmap(依赖base):    地图模块 (百度地图，定位，图层，导航，轨迹)
- txlive(依赖base):     直播模块 (腾讯直播，推流，播放)
- hyphenate(依赖base):  聊天模块 (环信互聊，登录，聊天，群组)
- hikvision(依赖base):  监控模块 (海康监控，播放)
- thirdlogin:           三方登录模块 (友盟，三方登录与分享)

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
- "com.alibaba.android:vlayout:1.2.2@aar"              阿里虚拟布局


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
<img src='http://i1.bvimg.com/645352/41426ca33a2ddeb9.png' width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/a224d91ff8d07552.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/063b415c6c896ce4.png" width = "270" height = "480" align=center />
<img src="http://i2.bvimg.com/645352/7ed595d20a80ed9a.png" width = "270" height = "480" align=center />
<img src="http://i4.bvimg.com/645352/8e49fdf8c5678540.png" width = "270" height = "480" align=center />
<img src="http://i4.bvimg.com/645352/496945712f86291b.png" width = "270" height = "480" align=center />


### 7.使用到的后台
- 极光push平台，包名相关，后台不可修改包名 （manifest key值）
- 百度map平台，包名相关，后台可修改包名 （manifest key值）
- 腾讯Bugly平台, 包名无关（application key值）
- 环信IM平台，包名无关（manifest key值）
- 腾讯直播，包名无关，无key值，自建后台
- 友盟登录分享，包名无关，有key值（manifest key值+代码配置）

### 8.后记
- 1 欢迎沟通
- Email: sjun945@outlook.com 
- WeChat: cheng-junfeng
- 下载地址：https://fir.im/l9vb (使用账号 junfeng 密码 123登录)
- <a href="https://fir.im/l9vb">App</a>

- 2 项目中有很多参考的是别人优秀的开源代码，未标记出处
- 如有版权问题，请与我联系。























