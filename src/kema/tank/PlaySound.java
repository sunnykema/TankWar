package kema.tank;
import sun.audio.*; //引入包，一定要的

import java.applet.AudioClip;
import java.io.*;
import java.net.URL;

public class PlaySound {
	private String fileName;
	private URL url = null;// 音乐文件的URl
	private AudioStream as = null;// 播放器

	public PlaySound(String fileName) {
		try {
			url = PlaySound.class.getResource(fileName);// 获取音乐文件的url
			InputStream is = url.openStream();// 获得音乐文件的输入流
			as = new AudioStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 播放音乐
	public void play() {
		AudioPlayer.player.start(as);// 用AudioPlayer静态成员player.start播放音乐
	}
}