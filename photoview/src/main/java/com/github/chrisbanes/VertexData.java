package com.github.chrisbanes;

import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

public class VertexData implements Parcelable {


    /**
     * ObjectID : 1000_01
     * X : 1399
     * Y : 2447
     * Width : 155
     * Height : 11
     * ArticleID : 1000
     * Reference : GDS4VU4U1.11
     */

    private String ObjectID;
    private int X;
    private int Y;
    private int Width;
    private int Height;
    private String ArticleID;
    private String Reference;
    private RectF mRect;

    public RectF getRect() {
        return mRect;
    }

    public void setRect(RectF rect) {
        this.mRect = rect;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public String getArticleID() {
        return ArticleID;
    }

    public void setArticleID(String articleID) {
        ArticleID = articleID;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ObjectID);
        dest.writeInt(this.X);
        dest.writeInt(this.Y);
        dest.writeInt(this.Width);
        dest.writeInt(this.Height);
        dest.writeString(this.ArticleID);
        dest.writeString(this.Reference);
        dest.writeParcelable(this.mRect, flags);
    }

    public VertexData() {
    }

    protected VertexData(Parcel in) {
        this.ObjectID = in.readString();
        this.X = in.readInt();
        this.Y = in.readInt();
        this.Width = in.readInt();
        this.Height = in.readInt();
        this.ArticleID = in.readString();
        this.Reference = in.readString();
        this.mRect = in.readParcelable(RectF.class.getClassLoader());
    }

    public static final Parcelable.Creator<VertexData> CREATOR = new Parcelable.Creator<VertexData>() {
        @Override
        public VertexData createFromParcel(Parcel source) {
            return new VertexData(source);
        }

        @Override
        public VertexData[] newArray(int size) {
            return new VertexData[size];
        }
    };
}
