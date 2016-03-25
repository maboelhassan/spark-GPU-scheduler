package org.mohamed.gp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class SocketTest {

	public static void main(String[] args) throws UnknownHostException,
			IOException {

		int portNumber = 5001;
		String hostName = "localhost";

		Socket socket = new Socket(hostName, portNumber);
		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),
				true);
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));

		int send[] = new int[3];

		ByteBuffer byteBuffer = ByteBuffer.allocate(4 * send.length).order(
				ByteOrder.LITTLE_ENDIAN);
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		intBuffer.put(send);
		byte[] sendInBytes = byteBuffer.array();
		socket.getOutputStream().write(sendInBytes, 0, sendInBytes.length);

		byte arr[] = new byte[12];
		socket.getInputStream().read(arr, 0, 12);
		intBuffer = ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN)
				.asIntBuffer();
		int intArray[] = new int[intBuffer.remaining()];

		intBuffer.get(intArray);
		for (int i = 0; i < intArray.length; i++) {
			System.out.println(intArray[i]);
		}

	}
}
