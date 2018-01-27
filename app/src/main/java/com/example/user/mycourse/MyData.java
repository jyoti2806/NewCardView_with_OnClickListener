package com.example.user.mycourse;
import android.databinding.BindingAdapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by USER on 24/01/2018.
 */

public class MyData implements Parcelable{
    private int id;
    private String description,image_link,table_of_contents;

    protected MyData(Parcel in) {
        id = in.readInt();
        description = in.readString();
        image_link = in.readString();
       // table_of_contents = in.readString();
    }

    public static final Creator<MyData> CREATOR = new Creator<MyData>() {
        @Override
        public MyData createFromParcel(Parcel in) {
            return new MyData(in);
        }

        @Override
        public MyData[] newArray(int size) {
            return new MyData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public MyData(int id, String description, String image_link/*,String [] table_of_contents*/) {
        this.id = id;
        this.description = description;
        this.image_link = image_link;
       // this.table_of_contents = TextUtils.join(", ",table_of_contents);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(description);
        parcel.writeString(image_link);
      //  parcel.writeString(table_of_contents);
    }
    @BindingAdapter({"android:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {

        Glide.with(view.getContext())
                .load(imageUrl)
                //.placeholder(R.drawable.)
                .into(view);
    }
}
