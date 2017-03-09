package kema.tank;
import sun.audio.*; //�������һ��Ҫ��

import java.applet.AudioClip;
import java.io.*;
import java.net.URL;

public class PlaySound {
	private String fileName;
	private URL url = null;// �����ļ���URl
	private AudioStream as = null;// ������

	public PlaySound(String fileName) {
		try {
			url = PlaySound.class.getResource(fileName);// ��ȡ�����ļ���url
			InputStream is = url.openStream();// ��������ļ���������
			as = new AudioStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��������
	public void play() {
		AudioPlayer.player.start(as);// ��AudioPlayer��̬��Աplayer.start��������
	}
}