# wlmusic
基于FFmpeg + OpenSL ES的音频播放SDK。可循环不间断播放短音频；播放raw和assets音频文件；可独立设置音量大小；可实时现在音量分贝大小（用于绘制波形图）；可改变音频播放速度和音调（变速不变调、变调不变速、变速又变调）；可设置播放声道（左声道、右声道和立体声）；可边播边录留住美好音乐；可裁剪指定时间段的音频，制作自己的彩铃等。

## [我的视频课程：《FFmpeg打造Android万能音频播放器》](https://edu.csdn.net/course/detail/6842)

## CPU和内存使用情况：测试设备：红米2A手机
![image](https://github.com/wanliyang1990/wlmusic/blob/master/imgs/cpuuse.gif)
![image](https://github.com/wanliyang1990/wlmusic/blob/master/imgs/memeory.gif)

## Update v1.1.1 增加音频裁剪功能

## Usage:

### Gradle: [ ![Download](https://api.bintray.com/packages/ywl5320/maven/wlmusic/images/download.svg?version=1.1.1) ](https://bintray.com/ywl5320/maven/wlmusic/1.1.1/link)

	compile 'ywl.ywl5320:libmusic:1.1.1'
	or
	implementation 'ywl.ywl5320:libmusic:1.1.1'
	or
	api 'ywl.ywl5320:libmusic:1.1.1'

### Maven:

	<dependency>
	  <groupId>ywl.ywl5320</groupId>
	  <artifactId>libmusic</artifactId>
	  <version>1.1.1</version>
	  <type>pom</type>
	</dependency>

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

	WlMusic wlMusic = WlMusic.getInstance();
	wlMusic.setSource("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3"); //设置音频源
    myMusic.setPlayCircle(true); //设置不间断循环播放音频
    myMusic.setVolume(65); //设置音量 65%
    myMusic.setPlaySpeed(1.0f); //设置播放速度 (1.0正常) 范围：0.25---4.0f
    myMusic.setPlayPitch(1.0f); //设置播放速度 (1.0正常) 范围：0.25---4.0f
    myMusic.setMute(MuteEnum.MUTE_CENTER); //设置立体声（左声道、右声道和立体声）
    wlMusic.parpared();准备开始

	wlMusic.setOnPreparedListener(new OnPreparedListener() {
	    @Override
	    public void onPrepared() {
		wlMusic.start(); //准备完成开始播放
	    }
	});

	//seek时间
	seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            position = myMusic.getDuration() * progress / 100;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            myMusic.seek(position, false, false);// 表示在seeking中，此时不回掉时间
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            myMusic.seek(position, true, true);//表示seek已经完成，然后才回调时间，避免自己控制时间逻辑和时间显示不稳定问题。
        }
    });


#### 2、循环不停顿播放短音频（音频不限时长）

	wlMusic.setPlayCircle(true);

#### 3、播放raw文件和assets文件

    url = RawAssetsUtil.getAssetsFilePath(this, "goready.wav");
    url = RawAssetsUtil.getRawFilePath(this, R.raw.readygo, "readygo.wav");
    wlMusic.setSource(url);

#### 4、设置速度1.5倍 （0.25~4.0f）

    wlMusic.setPlaySpeed(1.5f);

#### 5、设置音调1.5倍 （0.25~4.0f）

    wlMusic.setPlayPitch(1.5f);

#### 6、设置左声道

    wlMusic.setMute(MuteEnum.MUTE_LEFT);

#### 7、开始录制
    myMusic.startRecordPlaying(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ywl5320/record", "myrecord");//生成的录音文件为：myrecord.aac

#### 8、暂停录制
    myMusic.pauseRecordPlaying();

#### 9、恢复录制
    myMusic.resumeRecordPlaying();

#### 10、停止录制
    myMusic.stopRecordPlaying();

#### 11、裁剪音频（对应可获取总长度的音频）
    看CutAudioActivity中演示代码


## 一、效果图（对应设置启动页：MainActivity（实例演示） 或者 SplashActivity（广播列表播放实例）或者 CutAudioActivity（音频裁剪演示））
![image](https://github.com/wanliyang1990/wlmusic/blob/master/imgs/sample.gif)<br/>
![image](https://github.com/wanliyang1990/wlmusic/blob/master/imgs/music.gif)<br/>
![image](https://github.com/wanliyang1990/wlmusic/blob/master/imgs/cutaudio.gif)<br/>
![image](https://github.com/wanliyang1990/wlmusic/blob/master/imgs/cutaudio.png)<br/>

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
### 10、循环不间断播放短音频 ---> add v1.0.1
### 11、可设置音量大小 ---> add v1.0.2
### 12、播放raw和assets ---> add v1.0.2
### 13、声音分贝大小回调用于绘制波形图 ---> add v1.0.3
### 14、添加isPlaying()方法 ---> add v1.0.4
### 15、添加改变播放速率方法 ---> add v1.0.5
### 16、添加设置声道方法 ---> add v1.0.5
### 17、添加设置音频音调方法 ---> add v1.0.6
### 18、更新设置播放速度方法 ---> add v1.0.6
### 19、优化CPU使用率和内存使用率 ---> add v1.0.8
### 20、优化seek时平稳过度 ---> add v1.0.9
### 21、添加边播边录功能 ---> add v1.1.0
### 22、增加音频裁剪预览播放功能 --> add v1.1.1
### 23、增加指定时间段音频裁剪功能 --> add v1.1.1
### ......

### [实例APP下载，密码：0ok7](https://pan.baidu.com/s/1fDCOX-1rY4bT-n7MuTkaaw)


## 三、API（v1.1.1）
	
	public void setSource(String source) //设置音频源

	public void parpared() //准备播放

	public void setOnPreparedListener(OnPreparedListener onPreparedListener) //准备成功回调

	public void start() //开始播放

	public void pause() //暂停播放

	public void resume() //恢复播放（对应于暂停）

	public void stop() //停止播放，回收资源

	public boolean isPlaying(); //是否正在播放中

	public void setPlayCircle(boolean playCircle)//设置是否循环播放短音频

	public void setPlayNext(boolean playNext) //设置播放下一个状态（true:调用stop后会重新播放音频源）

	public void playNext(String source) //切换播放源

	public void seek(final int secds, boolean seekingfinished, boolean showTime) //secds：时间（秒） seekingfinished：true表示在滑动中 false表示滑动停止并执行seek功能 showTime：是否回调时间

	public void setVolume(int percent) //设置音量（0~100）

	public int getDuration() //获取时长

	public void setPlaySpeed(int speed) //设置播放速度（默认正常速度 1.0 范围：0.25x ~ 4.0x）

	public void setPlayPitch(float pitch) //设置音频音调（默认正常音调 1.0 范围：0.25x ~ 4.0x）

	public void setMute(MuteEnum mute) //设置播放声道 （MuteEnum.MUTE_LEFT,MuteEnum.MUTE_RIGHT,MuteEnum.MUTE_CENTER）

	public void startRecordPlaying(String recordSavePath, String recordSaveName) // 边播边录 （recordSavePath：存储目录；recordSaveName：录制文件名称）

	public void stopRecordPlaying() // 停止录制

	public void pauseRecordPlaying() //暂停录制

	public void resumeRecordPlaying() //恢复录制

	public void setOnErrorListener(OnErrorListener onErrorListener) //出错回调

	public void setOnLoadListener(OnLoadListener onLoadListener) //加载回调

	public void setOnInfoListener(OnInfoListener onInfoListener) //进度信息回调

	public void setOnCompleteListener(OnCompleteListener onCompleteListener) //播放完成回调

	public void setOnPauseResumeListener(OnPauseResumeListener onPauseResumeListener) //暂停、恢复回调

	public void setOnVolumeDBListener(OnVolumeDBListener onVolumeDBListener) //声音分贝大小回调

	public void playCutAudio(int start_secs, int end_secs) //裁剪音频预览播放（参数：开始和结束时间 单位：秒）

	public void cutAudio(int start_secs, int end_secs, String savepath, String filename) //开始裁剪 （参数：开始、结束时间（秒）、裁剪音频保存目录、裁剪音频保存名称）


## 四、环境
#### 1、Android Studio 3.+
#### 2、Android 4.1+
#### 3、《中国网络广播》api数据

### create By ywl5320
