package com.kingz.ipcdemo

import android.os.Parcel
import android.os.Parcelable

/**
 * author: King.Z <br></br>
 * date:  2016/10/17 16:21 <br></br>
 * description: 实现Parcelable接口 执行序列化操作 <br></br>
 * 这里有一个坑： 默认生成的模板类的对象只支持为 in 的定向 tag 。
 * 为什么呢？因为默认生成的类里面只有 writeToParcel() 方法，
 * 而如果要支持为 out 或者 inout 的定向 tag 的话，
 * 还需要实现 readFromParcel() 方法——而这个方法其实并没有在 Parcelable 接口里面，
 * 所以需要从头写。具体为什么可以去看：
 * http://www.open-open.com/lib/view/open1469494342021.html
 */
class Book : Parcelable {
    constructor()
    constructor(income: Parcel) {
        name = income.readString()
        price = income.readInt()
    }


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Book?> = object : Parcelable.Creator<Book?> {
            override fun createFromParcel(income: Parcel): Book? {
                return Book(income)
            }

            override fun newArray(size: Int): Array<Book?> {
                return arrayOfNulls(size)
            }
        }
    }

    var name: String? = null
    var price = 0

    override fun describeContents(): Int { return 0}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeInt(price)
    }

    /**
     * @param dest  用来存储和传输数据
     * 添加了 readFromParcel() 方法之后，
     * 我们的 Book 类的对象在AIDL文件里就可以用 out 或者 inout 来作为它的定向 tag 了。
     */
    fun readFromParcel(dest: Parcel) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        name = dest.readString()
        price = dest.readInt()
        //至此，关于AIDL中非默认支持数据类型的序列化操作就完成了。
    }

    override fun toString(): String {
        return "name : $name , price : $price"
    }
}