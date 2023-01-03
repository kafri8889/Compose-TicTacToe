package com.anafthdev.tictactoe.common

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.anafthdev.tictactoe.data.Constants
import com.anafthdev.tictactoe.extension.toast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.io.*
import java.util.*
import javax.inject.Inject


@SuppressLint("MissingPermission")
class BluetoothController @Inject constructor(
	private val context: Context
) {
	
	private lateinit var connectedThread: ConnectedThread
	private lateinit var mainSocket: BluetoothSocket
	
	private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
		override fun handleMessage(msg: Message) {
			when (msg.what) {
				Constants.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
					BluetoothChatService.STATE_CONNECTED -> {
						"Kongnek".toast(context)
					}
					BluetoothChatService.STATE_CONNECTING -> {
						"Kongnekting...".toast(context)
					}
					BluetoothChatService.STATE_LISTEN, BluetoothChatService.STATE_NONE -> {
						"Listen or non".toast(context)
					}
				}
				Constants.MESSAGE_WRITE -> {
					val writeBuf = msg.obj as ByteArray
					// construct a string from the buffer
					val writeMessage = String(writeBuf)
					"Mesek werait: $writeMessage".toast(context)
				}
				Constants.MESSAGE_READ -> {
					val readBuf = msg.obj as ByteArray
					// construct a string from the valid bytes in the buffer
					val readMessage = String(readBuf, 0, msg.arg1)
					"Mesek rid: $readMessage".toast(context)
				}
				Constants.MESSAGE_DEVICE_NAME -> {
					// save the connected device's name
					val mConnectedDeviceName = msg.data.getString(Constants.DEVICE_NAME)
				}
				Constants.MESSAGE_TOAST -> {
					"Towas ${msg.data.getString(Constants.TOAST)}".toast(context)
				}
			}
		}
	}
	
	private val SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
	private val PBAB_UUID = UUID.fromString("0000112f-0000-1000-8000-00805f9b34fb")
	
	private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
	private val bluetoothAdapter = bluetoothManager.adapter
	val bluetoothService = BluetoothChatService(context, mHandler)
	
	private val _availableBluetoothDevice = MutableStateFlow(emptyList<BluetoothDevice>())
	val availableBluetoothDevice: StateFlow<List<BluetoothDevice>> = _availableBluetoothDevice.asStateFlow()
	
	private val _isDiscovering = MutableStateFlow(false)
	val isDiscovering: StateFlow<Boolean> = _isDiscovering.asStateFlow()
	
	private var listener: BluetoothListener? = null
	private var discoveryListener: Job? = null
	
	suspend fun addDevice(device: BluetoothDevice) {
		val newDevices = availableBluetoothDevice.value.toMutableList().apply {
			add(device)
		}
		
		_availableBluetoothDevice.emit(
			newDevices.distinctBy { it.address }
		)
	}
	
	fun isBluetoothEnabled(): Boolean {
		return bluetoothAdapter.isEnabled
	}
	
	fun isPaired(device: BluetoothDevice): Boolean {
		return device in bluetoothAdapter.bondedDevices
	}
	
	fun connect(device: BluetoothDevice) {
		bluetoothService.connect(device, false)
//		ConnectThread(device).start()
//		ConnectThread(
//			device = device
//		).start()
	}
	
	fun tryWrite() {
		connectedThread.beginListenForData()
//		writeThread.write()
		connectedThread.write("tot".toByteArray())
	}
	
	fun tryRead() {
//		connectedThread.isSocketConnected.toast(context)
		AcceptThread().start()
	}
	
	@SuppressLint("MissingPermission")
	fun startDiscovery() {
		if (bluetoothAdapter.isDiscovering) {
			bluetoothAdapter.cancelDiscovery()
		}
		
		CoroutineScope(Dispatchers.Main).launch {
			_isDiscovering.emit(true)
		}
		
		discoveryListener = CoroutineScope(Dispatchers.IO).launch {
			while (true) {
				delay(1000)
				
				val discovering = bluetoothAdapter.isDiscovering
				
				_isDiscovering.emit(discovering)
				
				if (!discovering) cancel("Discovering finished")
			}
		}
		
		discoveryListener!!.start()
		
		bluetoothAdapter.startDiscovery()
	}
	
	fun setListener(l: BluetoothListener) {
		listener = l
	}
	
	@SuppressLint("MissingPermission")
	fun onDestroy() {
		bluetoothAdapter.cancelDiscovery()
		
		if (this::connectedThread.isInitialized) {
			connectedThread.cancel()
		}
	}
	
	inner class ConnectThread(
		private val device: BluetoothDevice
	): Thread() {
		
		private val socket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
			device.createInsecureRfcommSocketToServiceRecord(PBAB_UUID)
		}
		
		override fun run() {
			bluetoothAdapter.cancelDiscovery()
			
			socket?.let { mSocket ->
				try {
					mSocket.connect()
					mainSocket = mSocket
					connectedThread = ConnectedThread(mSocket)
					connectedThread.start()
//					connectedThread.write("tot".toByteArray())
					listener?.onConnected()
				} catch (e: IOException) {
					cancel()
					
					listener?.onFailedToConnect()
					Timber.e(e, "Cannot connect")
				}
			}
		}
		
		fun cancel() {
			try {
				socket?.close()
			} catch (e: IOException) {
				Timber.e(e, "Cannot close socket")
			}
		}
	}
	
	private inner class AcceptThread : Thread() {
		
		private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
			bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord("Tes", SPP_UUID)
		}
		
		override fun run() {
			// Keep listening until exception occurs or a socket is returned.
			var shouldLoop = true
			while (shouldLoop) {
				val socket: BluetoothSocket? = try {
					mmServerSocket?.accept()
				} catch (e: IOException) {
					Timber.e(e, "Socket's accept() method failed")
					shouldLoop = false
					null
				}
				socket?.also {
					"Konektet".toast(context)
					mainSocket = it
					connectedThread = ConnectedThread(it)
					connectedThread.start()
					shouldLoop = false
				}
			}
		}
		
		// Closes the connect socket and causes the thread to finish.
		fun cancel() {
			try {
				mmServerSocket?.close()
			} catch (e: IOException) {
				Timber.e(e, "Could not close the connect socket")
			}
		}
	}
	
	inner class ConnectedThread(private val socket: BluetoothSocket): Thread() {
		
		private val inStream: DataInputStream = DataInputStream(socket.inputStream)
		private val outStream: DataOutputStream = DataOutputStream(socket.outputStream)
		private val buffer: ByteArray = ByteArray(1024) // buffer store for the stream
		
		val isSocketConnected: Boolean
			get() = socket.isConnected
		
		override fun run() {
			var bytes = 0
			while (true) {
				try {
					Timber.i("opt: ${inStream.readUTF()}")
					bytes += inStream.read(buffer, bytes, buffer.size - bytes)
				} catch (e: IOException) {
					Timber.e(e, "elor: ${e.message}")
					break
				}
			}
		}
		
		fun write(data: ByteArray) {
			try {
				outStream.write(data)
				Timber.i("Data written")
			} catch (e: IOException) {
				Timber.e(e, "Error occurred when sending data")
				
				listener?.onFailedToWrite()
				return
			}
		}
		
		fun beginListenForData() {
			val handler = Handler()
			val delimiter: Byte = 10 //This is the ASCII code for a newline character
			var stopWorker = false
			var readBufferPosition = 0
			val readBuffer = ByteArray(1024)
			val workerThread = Thread {
				while (!currentThread().isInterrupted && !stopWorker) {
					try {
						val bytesAvailable: Int = inStream.available()
						if (bytesAvailable > 0) {
							val packetBytes = ByteArray(bytesAvailable)
							inStream.read(packetBytes)
							for (i in 0 until bytesAvailable) {
								val b = packetBytes[i]
								if (b == delimiter) {
									val encodedBytes = ByteArray(readBufferPosition)
									System.arraycopy(
										readBuffer,
										0,
										encodedBytes,
										0,
										encodedBytes.size
									)
									val data = String(encodedBytes, Charsets.US_ASCII)
									readBufferPosition = 0
									handler.post{
										data.toast(context)
									}
								} else {
									readBuffer[readBufferPosition++] = b
								}
							}
						}
					} catch (ex: IOException) {
						stopWorker = true
					}
				}
			}
			workerThread.start()
		}
		
		fun cancel() {
			try {
				socket.close()
			} catch (e: IOException) {
				Timber.e(e, "Cannot close socket")
			}
		}
	}
	
	interface BluetoothListener {
		
		fun onConnected()
		
		fun onFailedToConnect()
		
		fun onFailedToWrite()
	}
	
	companion object {
		const val MESSAGE_READ: Int = 0
		const val MESSAGE_WRITE: Int = 1
	}
}
