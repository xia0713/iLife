package com.szgs.bbs.find;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;


public class FindCategoryResponse implements Parcelable {

	public long id;
	public String description;
	public String icon;
	public String name;
	
	public FindCategoryResponse(){}
	
	private FindCategoryResponse(Parcel in){
		id = in.readLong();
		name = in.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeLong(id);
		dest.writeString(name);
	}
	
	public static final Parcelable.Creator<FindCategoryResponse> CREATOR = new Creator<FindCategoryResponse>() {
		
		@Override
		public FindCategoryResponse[] newArray(int size) {
			// TODO Auto-generated method stub
			return new FindCategoryResponse[size];
		}
		
		@Override
		public FindCategoryResponse createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new FindCategoryResponse(source);
		}
	};

	// public Embedded _embedded;
	//
	// public class Embedded {
	// public List<Categorie> categories;
	//
	// public class Categorie {
	// public String name;
	// public int id;
	// public String description;
	// }
	// }
}
