package com.konaire.simplelist.network

import java.net.InetAddress
import java.net.Socket

import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

/**
 * Created by Evgeny Eliseyev on 25/04/2018.
 */
class DelegatingSSLSocketFactory: SSLSocketFactory() {
    private val delegate: SSLSocketFactory

    init {
        val context = SSLContext.getInstance("TLS")

        context.init(null, null, null)
        delegate = context.socketFactory
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return delegate.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.supportedCipherSuites
    }

    override fun createSocket(socket: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        return configureSocket(delegate.createSocket(socket, host, port, autoClose) as SSLSocket)
    }

    override fun createSocket(host: String, port: Int): Socket {
        return configureSocket(delegate.createSocket(host, port) as SSLSocket)
    }

    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket {
        return configureSocket(delegate.createSocket(host, port, localHost, localPort) as SSLSocket)
    }

    override fun createSocket(host: InetAddress, port: Int): Socket {
        return configureSocket(delegate.createSocket(host, port) as SSLSocket)
    }

    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket {
        return configureSocket(delegate.createSocket(address, port, localAddress, localPort) as SSLSocket)
    }

    private fun configureSocket(socket: SSLSocket): SSLSocket {
        socket.enabledProtocols = arrayOf("TLSv1.1", "TLSv1.2")
        return socket
    }
}