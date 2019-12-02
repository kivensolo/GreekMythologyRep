// BookManager.aidl
package com.kingz.ipcdemo;
import com.kingz.ipcdemo.Book;

// Declare any non-default types here with import statements
interface IBookManager {
    /**
     * 演示一些可用作参数的基本类型,并在AIDL可用作返回值
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

     //所有的返回值前都不需要加任何东西，不管是什么数据类型
    List<Book> getBooks();
    int getBookCount();

    //传参时除了Java基本类型以及String，CharSequence之外的类型
    //都需要在前面加上定向tag(表示数据流向)，具体加什么视需而定
    void setBookPrice(in Book book , int price);
    void setBookName(in Book book , String name);
    void addBookIn(in Book book);
    void addBookOut(out Book book);
    void addBookInout(inout Book book);
    void addBook(in Book book);
}
