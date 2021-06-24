package com.zeke.demo.device.bluetooth

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zeke.demo.R

/**
 * author：ZekeWang
 * date：2021/6/24
 * description：
 */
class BleAdapter @JvmOverloads constructor(
    data: MutableList<BluetoothDeviceWrapper>? = null
) : BaseQuickAdapter<BluetoothDeviceWrapper, BaseViewHolder>(R.layout.item_ble_device_info, data) {
    var clickListener: DeviceClickListener? = null

    open interface DeviceClickListener {
        fun onClick(item: BluetoothDeviceWrapper, adapterPosition: Int)
    }

    override fun convert(holder: BaseViewHolder, item: BluetoothDeviceWrapper) {
        holder.apply {
            setText(R.id.le_device_name, item.mDevice?.name ?: "未知设备")
            setText(R.id.le_device_address, item.mDevice?.address)
            setText(R.id.le_device_distance, String.format("距离(m):%.2f", item.mDistance))
            itemView.setOnClickListener {
                clickListener?.onClick(item, holder.adapterPosition)
            }
        }
    }
}