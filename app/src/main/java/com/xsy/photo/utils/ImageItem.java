package com.xsy.photo.utils;

import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;


public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	private Bitmap bitmap;
	public boolean isSelected = false;

	public long date;
	public long id;
	public boolean isPicked;

	public ImageItem(){}

	public ImageItem(String imagePath,boolean isPicked,long date, long id) {
		this.imagePath = imagePath;
		this.date = date;
		this.id = id;
		this.isPicked = isPicked;
	}


	public ImageItem(String imageId, String thumbnailPath, String imagePath, Bitmap bitmap, boolean isSelected, long date, long id, boolean isPicked) {
		this.imageId = imageId;
		this.thumbnailPath = thumbnailPath;
		this.imagePath = imagePath;
		this.bitmap = bitmap;
		this.isSelected = isSelected;
		this.date = date;
		this.id = id;
		this.isPicked = isPicked;
	}

	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public Bitmap getBitmap() {		
		if(bitmap == null){
			try {
				bitmap = Bimp.revitionImageSize(imagePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isThisImage(String path){
		return this.imagePath.equalsIgnoreCase(path);
	}
}
