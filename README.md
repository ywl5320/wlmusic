# wlmusic
基于FFmpeg的音频播放SDK，可循环不间断播放短音频，播放raw和assets音频文件，可独立设置音量大小，可实时现在音量分贝大小（用于绘制波形图），可改变播放速率，可设置播放声道等。

## [我的视频课程：《FFmpeg打造Android万能音频播放器》](https://edu.csdn.net/course/detail/6842)

## Usage:

### Maven:

	<dependency>
	  <groupId>ywl.ywl5320</groupId>
	  <artifactId>libmusic</artifactId>
	  <version>1.0.5</version>
	  <type>pom</type>
	</dependency>
	

### Gradle:

	compile 'ywl.ywl5320:libmusic:1.0.5'

### 配置NDK编译平台:

	defaultConfig {
		...
		ndk {
		    abiFilter("armeabi")
		    abiFilter("x86")
		}

	    }

### 需要权限:

	<uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

### 接入代码:
#### 1、播放一般音乐

	WlMusic wlMusic = new WlMusic();
	wlMusic.setSource("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
	wlMusic.parpared();

	//wlMusic.setPlayNext(false);//控制是否播放下一个
	//wlMusic.setVolume(50);//音量控制（0~100）
	//wlMusic.setPlayCircle(true); //循环播放短音频
	//wlMusic.setPlaySpeed(1500); //设置速度1.5倍 （500~2000）
	//wlMusic.setMute(MuteEnum.MUTE_LEFT); //设置左声道

	wlMusic.setOnParparedListener(new OnParparedListener() {
	    @Override
	    public void onParpared() {
		wlMusic.start();
	    }
	});

#### 2、循环不停顿播放短音频（时长最多90s)

	wlMusic.setPlayCircle(true);

#### 3、播放raw文件和assets文件

    url = RawAssetsUtil.getAssetsFilePath(this, "goready.wav");
    url = RawAssetsUtil.getRawFilePath(this, R.raw.readygo, "readygo.wav");
    wlMusic.setSource(url);

#### 4、设置速度1.5倍 （500~2000）

    wlMusic.setPlaySpeed(1500);

#### 5、设置左声道

    wlMusic.setMute(MuteEnum.MUTE_LEFT);
	

## 一、效果图（对应设置启动页：MainActivity 或者 SplashActivity）
1、
![image](https://github.com/wanliyang1990/wlmusic/blob/master/imgs/sample.gif)
2、
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
### 10、循环不间断播放短音频（时长小于90s) ---> add v1.0.1
### 11、可设置音量大小 ---> add v1.0.2
### 12、播放raw和assets ---> add v1.0.2
### 13、声音分贝大小回调用于绘制波形图 ---> add v1.0.3
### 14、添加isPlaying()方法 ---> add v1.0.4
### 15、添加改变播放速率方法 ---> add v1.0.5
### 16、添加设置声道方法 ---> add v1.0.5
### ......

### [实例APP下载，密码：4a2v](https://pan.baidu.com/s/1ADOTD8Fj_WJC07wCfGV0rQ)


## 三、API（v1.0.5）
	
	public void setSource(String source) //设置音频源

	public void parpared() //准备播放

	public void setOnParparedListener(OnParparedListener onParparedListener) //准备成功回调

	public void start() //开始播放

	public void pause() //暂停播放

	public void resume() //恢复播放（对应于暂停）

	public void stop() //停止播放，回收资源

	public boolean isPlaying(); //是否正在播放中

	public void setPlayCircle(boolean playCircle)//设置是否循环播放短音频

	public void setPlayNext(boolean playNext) //设置播放下一个状态（true:调用stop后会重新播放音频源）

	public void playNext(String source) //切换播放源

	public void seek(int secds) //seek进度（单位：秒）

	public void setVolume(int percent) //设置音量（0~100）

	public int getDuration() //获取时长

	public void setPlaySpeed(int speed) //设置播放速率（默认正常速率 1000 范围：500 ~ 2000 0.5x~2.0x）

	public void setMute(MuteEnum mute) //设置播放声道 （MuteEnum.MUTE_LEFT,MuteEnum.MUTE_RIGHT,MuteEnum.MUTE_CENTER）

	public void setOnErrorListener(OnErrorListener onErrorListener) //出错回调

	public void setOnLoadListener(OnLoadListener onLoadListener) //加载回调

	public void setOnInfoListener(OnInfoListener onInfoListener) //进度信息回调

	public void setOnCompleteListener(OnCompleteListener onCompleteListener) //播放完成回调

	public void setOnPauseResumeListener(OnPauseResumeListener onPauseResumeListener) //暂停、恢复回调

	public void setOnVolumeDBListener(OnVolumeDBListener onVolumeDBListener) //声音分贝大小回调

## 四、环境
#### 1、Android Studio 3.+
#### 2、Android 4.0+
#### 3、《中国网络广播》api数据

### create By ywl5320
