package com.audio.ui.bean;

import java.io.Serializable;

public class AudioInfoBean implements Serializable
{

    private String id;
    private String audioPath;
	private String audioTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getAduioPath()
	{
		return audioPath;
	}

	public void setAdudioPath(String audioPath)
	{
		this.audioPath = audioPath;
	}

	public String getAudioTime()
	{
		return audioTime;
	}

	public void setAudioTime(String audioTime)
	{
		this.audioTime = audioTime;
	}

}
