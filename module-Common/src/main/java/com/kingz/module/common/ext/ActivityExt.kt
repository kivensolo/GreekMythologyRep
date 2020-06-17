package com.kingz.module.common.ext

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * 在Kotlin中，一个内联（ inline  ）函数可以⽀持具体化（ reified  ）的类型参数，
 * 这意味着可以在函数中得到并使用范型类型的Class。
 *
 * 普通的函数（未标记为内联函数的）不能有具体化参数
 */
inline fun <reified T : Activity> Context.startActivity(
    vararg params: Pair<String, String>) {
    val intent = Intent(this, T::class.java)  // 可以具体化泛型
    params.forEach { intent.putExtra(it.first, it.second) }
    startActivity(intent)
}

inline fun <reified T : Activity> Context.startActivity(exec: (intent: Intent) -> Unit) {
    val intent = Intent(this, T::class.java)  // 可以具体化泛型
    exec(intent)
    startActivity(intent)
}

//   reified 关键字演示， 比如有以下方法：
//  fun <T> TreeNode.findParentOfType(clazz: Class<T>): T? {
//      var p = parent
//      while (p != null && !clazz.isInstance(p)) {
//          p = p.parent
//      }
//      @Suppress("UNCHECKED_CAST")
//      return p as T?
//  }
//  最初的调用只能这样： treeNode.findParentOfType(MyTreeNode::class.java)
//  但是我们想要的是只传递一个类型给该函数，即像这样调⽤它：
//  treeNode.findParentOfType<MyTreeNode>()
//  因为内联（ inline ）函数可以⽀持具体化（ reified ）的类型参数，所以可以这样写：
//  inline fun <reified T> TreeNode.findParentOfType(): T? {
//      var p = parent
//      while (p != null && p !is T) {
//          p = p.parent
//      }
//      return p as T?
//  }