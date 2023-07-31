package com.kingz.coroutines.multicast

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zeke.example.coroutines.R
import kotlinx.android.synthetic.main.activity_udp_tcp.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class LsrActivity: AppCompatActivity() {
    private val IPV4_MULTI_CAST_PROT = 34888
    private val IPV4_MULTI_CAST_GROUP = "239.255.255.252" //224.0.0.225也可以  224.0.0.1 不行


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_udp_tcp)
        lifecycleScope.launch(Dispatchers.IO) {
            startMulticastListener()
        }
    }

    private fun startMulticastListener() {
        var socket: MulticastSocket? = null
        var group: InetAddress? = null
        Log.d("Kingz","监听器启动…")
        try {
            // 创建组播Socket并指定端口
            socket = MulticastSocket(IPV4_MULTI_CAST_PROT)
            // 指定组播地址
            group = InetAddress.getByName(IPV4_MULTI_CAST_GROUP)
            // 将socket加入到组播地址中
            socket.joinGroup(group)
            val buffer = ByteArray(523)
            val packet = DatagramPacket(buffer, buffer.size)
            //Thread receiverThread = new Thread(() -> {
            var contentData: String
            while (true) {
                try { // 循环监听
                    socket.receive(packet) //阻塞方法，buffer填充满了，才会返回数据
                    contentData = getPacketContentData(packet.getData())
                    if (contentData != "") {
                        Log.d("Kingz","接收到组播数据…")
//                        destoryMulticast(socket, group)
//                        break

                        val content = udpMsgText.text.toString()
                        udpMsgText.handler.post {
                            udpMsgText.text = (content + "\n" + contentData)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            println("尝试进行TCP链接......")
            // TODO 切换为TCP长链接
            // 连接后，就发心跳，保持心跳为了逻辑上得知Server是否已断开
            val split = contentData.split(",").toTypedArray()
            if (split.size > 2) {
                val serverHost = split[0]
                val serverPort = split[1]
                // 创建客户端Socket对象，连接服务器
                val clientSocket = Socket(serverHost, serverPort.toInt())
                // 获取输入流和输出流
                val outToServer: OutputStream = clientSocket.getOutputStream()
                val inFromServer: InputStream = clientSocket.getInputStream()

                // 向服务器发送数据
                val message = "@@Hello, Server! I'm shakeHands with you."
                val bytes: ByteArray = message.toByteArray(StandardCharsets.UTF_8)
                outToServer.write(bytes)

                // 接收服务器返回的数据
                val baos = ByteArrayOutputStream()
                val data = ByteArray(1024)
                var length: Int
                while (inFromServer.read(data).also { length = it } != -1) {
                    baos.write(data, 0, length)
                }
                val response: String = baos.toString("UTF-8")

                // 输出服务器返回的数据
                println("Response from server: $response")

                // 关闭连接
                //clientSocket.close();
            }

            //});
            //receiverThread.start();
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            destoryMulticast(socket, group)
        }
    }

    private fun destoryMulticast(socket: MulticastSocket?, group: InetAddress?) {
        if (socket != null) {
            try {
                // 关闭socket并退出组播地址
                socket.leaveGroup(group)
                socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getPacketContentData(data: ByteArray): String {
        val version = ByteArray(2)
        val len = ByteArray(2)
        val content = ByteArray(512)
        var offset = 7 //Header len
        System.arraycopy(data, offset, version, 0, version.size)
        offset += version.size
        System.arraycopy(data, offset, len, 0, len.size)
        offset += len.size
        val contentLen: Short = ByteBuffer.wrap(len).getShort()
        System.arraycopy(data, offset, content, 0, contentLen.toInt())
        val contentStr = String(content, StandardCharsets.UTF_8)
        val format = java.lang.String.format(
            "Ver=%d Len=%d Content=%s\n",
            ByteBuffer.wrap(version).getShort(), contentLen, contentStr
        )
        println("Received udp message: \n$format")
        return contentStr
    }
}