# wlmusic
基于FFmpeg的音频播放APP，仿网易云音乐播放UI，可移植到自己的音频APP中。

## [我的视频课程：《FFmpeg打造Android万能音频播放器》](https://edu.csdn.net/course/detail/6842)

## Usage

### Maven

	<dependency>
	  <groupId>ywl.ywl5320</groupId>
	  <artifactId>libmusic</artifactId>
	  <version>1.0.0</version>
	  <type>pom</type>
	</dependency>
	

### Gradle

	compile 'ywl.ywl5320:libmusic:1.0.0'


## 一、效果图
![image](https://github.com/wanliyang1990/wlmusic/blob/master/imgs/music.gif)<br/>

## 二、功能特色：
### 1、播放本地音频或网络音频流媒体
### 2、播放状态（准备、开始、暂停、停止、切换）
### 3、FFmpeg解码，兼容几乎所有类型的音频文件
### 4、采用OpenSL ES直接底层播放
### 5、动态权限申请
### 6、迷你播放控制栏
### 7、Activity动画
### 8、Activity、Dialog沉浸状态栏
### 9、网络接口返回数据再封装
### ......

### [实例APP下载，密码：4a2v](https://pan.baidu.com/s/1ADOTD8Fj_WJC07wCfGV0rQ)

## 三、使用方式
### 直接导入库：libmusic 即可

## 四、API（v1.0.0）
	
	public void setSource(String source) //设置音频源

	public void parpared() //准备播放

	public void setOnParparedListener(OnParparedListener onParparedListener) //准备成功回调

	public void start() //开始播放

	public void pause() //暂停播放

	public void resume() //恢复播放（对应于暂停）

	public void stop() //停止播放，回收资源

	public void setPlayNext(boolean playNext) //设置播放下一个状态（true:调用stop后会重新播放音频源）

	public void playNext(String source) //切换播放源

	public void seek(int secds) //seek进度（单位：秒）

	public int getDuration() //获取时长

	public void setOnErrorListener(OnErrorListener onErrorListener) //出错回调

	public void setOnLoadListener(OnLoadListener onLoadListener) //加载回调

	public void setOnInfoListener(OnInfoListener onInfoListener) //进度信息回调

	public void setOnCompleteListener(OnCompleteListener onCompleteListener) //播放完成回调

	public void setOnPauseResumeListener(OnPauseResumeListener onPauseResumeListener) //暂停、恢复回调

## 五、环境
### 1、Android Studio 3.+
### 2、Android 4.0+
### 3、《中国网络广播》api数据

### create by ywl5320
