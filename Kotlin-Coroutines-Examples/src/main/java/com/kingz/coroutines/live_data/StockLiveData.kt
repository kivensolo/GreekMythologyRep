package com.kingz.coroutines.live_data

import androidx.lifecycle.LiveData
import java.math.BigDecimal

/**
 * author: King.Z <br>
 * date:  2020/6/9 21:20 <br>
 * description: 如果观察者的生命周期处于 STARTED 或 RESUMED 状态，
 * 则 LiveData 会认为该观察者处于活跃状态。
 * 以下示例代码说明了如何扩展 LiveData 类： <br>
 */
class StockLiveData(symbol: String) : LiveData<BigDecimal>() {
    private val stockManager = StockManager(symbol)

    private val listener = { price: BigDecimal ->
        value = price
    }
    // private SimplePriceListener listener = new SimplePriceListener() {
    //     @Override
    //     public void onPriceChanged(BigDecimal price) {
    //         setValue(price);
    //     }
    // };

    // 当 LiveData 对象具有活跃观察者时，会调用 onActive() 方法
    override fun onActive() {
        stockManager.requestPriceUpdates(listener)
    }

    // 当 LiveData 对象没有任何活跃观察者时，会调用 onInactive() 方法
    override fun onInactive() {
        // 由于没有观察者在监听，因此没有理由与 StockManager 服务保持连接。
        stockManager.removeUpdates(listener)
    }
}

class StockManager(val symbol: String){
    fun requestPriceUpdates(func:(price:BigDecimal) -> Unit){
    }

    fun removeUpdates(func:(price:BigDecimal) -> Unit){

    }
}