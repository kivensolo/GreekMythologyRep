package com.nioserver;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: King.Z
 * @since: 2020/11/26 20:29 <br>
 * @desc: 基于NIO实现的反应器模式服务端
 *
 * 知识点: NIO + TCP + 反应器模式 + HTTP协议
 *
 * //TODO 优化各种header和状态码
 */
public class NIOHttpServer {
    /**
     * Listening new tcp connect.
     * Create a Socketchannel for new connect.
     */
    private volatile ServerSocketChannel mServerSocketChannel;
    /**
     * NIO channel selector
     */
    private volatile Selector _selector;
    /**
     * Reactor thread pool. Accept clients requests.
     */
    private ThreadPoolExecutor _reactorPool;
    /**
     * Local server address.
     */
    private String _localAddr;
    /**
     * Local server prot.
     */
    private int _localPort;

    public NIOHttpServer(String addr, int port) {
        initServer(addr, port);
    }

    public NIOHttpServer(int port) {
        initServer(null, port);
    }

    /**
     * Init server.
     *
     * @param addr ip addr.
     * @param port ip port.
     */
    private void initServer(@Nullable String addr, int port) {
        _localAddr = addr;
        _localPort = port;
        final ThreadGroup workerGroup = new ThreadGroup("NIO HTTP Server");
        //ThreadPool of Reactor.
        _reactorPool = new ThreadPoolExecutor(1, 16, 10,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(16),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NotNull Runnable r) {
                        return new Thread(workerGroup, r, "Reactor");
                    }
                });

        //Thread of Acceptor.
        Thread mAcceptorWorker = new Thread(workerGroup, "Acceptor") {
            @Override
            public void run() {
                doWork();
            }
        };
        mAcceptorWorker.start();
    }

    /**
     * Acceptor thread listening client tcp connect.
     */
    private void doWork() {
        synchronized (this) {
            if (_selector == null) {
                try {
                    _selector = Selector.open();
                    SelectorProvider provider = _selector.provider();
                    // Creatr IP Socket Address
                    InetSocketAddress localAddr = TextUtils.isEmpty(_localAddr) ?
                            new InetSocketAddress(_localPort) :
                            new InetSocketAddress(_localAddr, _localPort);
                    // Opens a server socket channel.
                    mServerSocketChannel = provider.openServerSocketChannel();
                    // 等同于 mServerSocketChannel = ServerSocketChannel.open()
                    mServerSocketChannel.configureBlocking(false);
                    // A server socket associated with socketChannel.
                    ServerSocket socket = mServerSocketChannel.socket();
                    // Bind and listen the address.
                    socket.bind(localAddr);

                    mServerSocketChannel.register(_selector,
                            mServerSocketChannel.validOps(),
                            mServerSocketChannel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Selector selector;
        while ((selector = _selector) != null && selector.isOpen()) {
            try {
                // blocking selection operation
                selector.select();

                Set<SelectionKey> selectionKeySet;
                synchronized (selector) {
                    selectionKeySet = selector.selectedKeys();
                }
                // Gets an iterator for the selected item in the selector,
                // which is the registered event.
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();

                SelectionKey selectionKey;
                while (iterator.hasNext()) {
                    synchronized (selectionKeySet) {
                        selectionKey = iterator.next();
                        // Delete iterator that have already been selected to prevent duplicate processing
                        iterator.remove();
                    }
                    handleWithSelectionKey(selector, selectionKey);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Handle events with SelectionKey.
     */
    private void handleWithSelectionKey(Selector selector, SelectionKey key){
        if(key == null){
            return;
        }
        // Get attached Handler.
        HttpServerHandler attachedHandler = (HttpServerHandler) key.attachment();
        if (!key.isValid()) {
            attachedHandler.terminate();
            return;
        }

        try {
            if (key.isAcceptable()) {
                // Check current socketChannel is avlid.
                SocketChannel socketChannel = mServerSocketChannel.accept();
                if (socketChannel == null) {
                    return;
                }
                // Register channel to this socketSelector and set Read mode.
                socketChannel.configureBlocking(false);
                HttpServerHandler handler = createHandler(this, socketChannel);
                socketChannel.register(selector, SelectionKey.OP_READ, handler);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            if (key.isWritable()){
                attachedHandler.notifyWritable();
                return;
            }

            if(key.isReadable()){
                attachedHandler.notifyReadable();
            }

        } catch (CancelledKeyException e) {
            attachedHandler.terminate();
        } catch (Exception e) {
            attachedHandler.terminate();
            e.printStackTrace();
        }
    }

    /**
     * Create a http server handler to deal http work.
     * @param server         NIOHttpServer object.
     * @param socketChannel  SocketChannel
     * @return HttpServerHandler
     */
    @SuppressWarnings("WeakerAccess")
    protected HttpServerHandler createHandler(NIOHttpServer server,
                                              SocketChannel socketChannel) {
		return new HttpServerHandler(server, socketChannel);
	}

    /**
     * Diapatch a Http Request to handler.
     *
     * @param handler   Handler to deal http request.
     * @param request   Http request.
     */
    public void dispatchRequest(final HttpServerHandler handler,
                                 final HttpServerRequest request) {
        _reactorPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    handler.handleHttpRequest(request);
                } catch (IOException e) {
                    handler.terminate();
                    e.printStackTrace();
                }
            }
        });
    }

    public Selector getSelector() {
        return _selector;
    }

    public ThreadPoolExecutor getReactorPool() {
        return _reactorPool;
    }
}
