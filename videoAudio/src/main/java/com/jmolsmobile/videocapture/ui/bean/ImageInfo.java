package com.jmolsmobile.videocapture.ui.bean;

import java.io.Serializable;

public class ImageInfo implements Serializable
{

	private String imagePath;
	private int ImageType;

	public String getImagePath()
	{
		return imagePath;
	}

	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}

	public int getImageType()
	{
		return ImageType;
	}

	public void setImageType(int imageType)
	{
		ImageType = imageType;
	}

}
