package org.mohamed.gp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerTest implements Runnable {

	int number;

	public ServerTest(int num) {
		this.number = num;
	}

	public void run() {
		String file = "/home/spark/Desktop/dataChunkbb";
		Path path = FileSystems.getDefault().getPath("", file);
		byte data[] = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(data.length);

		int portNumber = 5001;
		String hostName = "localhost";

		Socket socket;
		try {
			socket = new Socket(hostName, portNumber);
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),
					true);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			String shit = new String(data);

			System.out.println("will send");
			// byte[] arr = { 1, 2 , 3 , 5, 6, 6, 7 ,7 };
			long startTime = System.currentTimeMillis();
			socket.getOutputStream().write(data, 0, data.length);

			ObjectOutputStream outputStream = new ObjectOutputStream(
					socket.getOutputStream());
			// outputStream.writeObject(data);
			// PrintWriter writer = new PrintWriter(socket.getOutputStream());

			// outputStream.writeObject(data);
			long endTime = System.currentTimeMillis();
			System.out.println("Time is " + (endTime - startTime));
			System.out.println("here");
			byte[] resultArr = new byte[65536 * 6];
			socket.getInputStream().read(resultArr, 0, 65536 * 6);

			System.out.println("after receiving results");
			DoubleBuffer doubleBuffer = ByteBuffer.wrap(resultArr)
					.order(ByteOrder.LITTLE_ENDIAN).asDoubleBuffer();
			double doubleResult[] = new double[doubleBuffer.remaining()];
			doubleBuffer.get(doubleResult);

			StringBuilder answer = new StringBuilder();
			for (int i = 0; i < 10; i++) {
				int startIndex = i * 6;
				String currentAnswer = new String();
				currentAnswer = doubleResult[startIndex] + " "
						+ doubleResult[startIndex + 1] + " "
						+ doubleResult[startIndex + 2] + " "
						+ doubleResult[startIndex + 3] + " "
						+ doubleResult[startIndex + 4] + " "
						+ doubleResult[startIndex + 5] + "\n";

				answer.append(currentAnswer);
			}

			System.out.println(answer);
			printWriter.close();
			bufferedReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
