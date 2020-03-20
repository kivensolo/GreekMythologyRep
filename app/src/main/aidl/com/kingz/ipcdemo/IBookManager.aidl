// BookManager.aidl
package com.kingz.ipcdemo;
import com.kingz.ipcdemo.ICallback;
import com.kingz.ipcdemo.Book;

// Declare any non-default types here with import statements
interface IBookManager {
    /**
     * Demonstrates some basic types that can be used as parameters and as return values in Aidl
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    //No need add any thing befor Return value
    List<Book> getBooks();
    int getBookCount();

    //If parameters is not basicTypes/String/CharSequence.
    //Need to add a directional tag (indicating the data flow direction) in front of it.
    //What you need to add depends on your needs.
    void setBookPrice(in Book book , int price);
    void setBookName(in Book book , String name);
    void addBookIn(in Book book);
    void addBookOut(out Book book);
    void addBookInout(inout Book book);
    void addBook(in Book book);

    // aidl callback
    void deleteBook(inout Book book,ICallback callback);
}
