package com.ds.github_repo.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val status = NetworkUtils.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtils.NETWORK_STATUS_NOT_CONNECTED) {
                connectivityReceiverListener!!.onNetworkConnectionChanged(false)
            } else {
                connectivityReceiverListener!!.onNetworkConnectionChanged(true)
            }
        }
    }


    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)

    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}
