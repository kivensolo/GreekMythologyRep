package com.kingz.ipcdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: King.Z <br>
 * date:  2016/10/17 16:21 <br>
 * description: 实现Parcelable接口 执行序列化操作 <br>
 *     这里有一个坑： 默认生成的模板类的对象只支持为 in 的定向 tag 。
 *     为什么呢？因为默认生成的类里面只有 writeToParcel() 方法，
 *     而如果要支持为 out 或者 inout 的定向 tag 的话，
 *     还需要实现 readFromParcel() 方法——而这个方法其实并没有在 Parcelable 接口里面，
 *     所以需要我们从头写。具体为什么可以去看：
 *     http://www.open-open.com/lib/view/open1469494342021.html
 */

public class Book implements Parcelable{
    protected Book(Parcel in) {
        name = in.readString();
        price = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private String name;
    private int price;

    public Book() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
    }

    /**
     * @param dest  用来存储和传输数据
     *  添加了 readFromParcel() 方法之后，
     *  我们的 Book 类的对象在AIDL文件里就可以用 out 或者 inout 来作为它的定向 tag 了。
     */
    public void readFromParcel(Parcel dest){
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        name = dest.readString();
        price = dest.readInt();
        //至此，关于AIDL中非默认支持数据类型的序列化操作就完成了。
    }

    @Override
    public String toString() {
        return "name : " + name + " , price : " + price;
    }
}
