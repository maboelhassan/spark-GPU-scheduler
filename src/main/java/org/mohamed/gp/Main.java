package org.mohamed.gp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class Main {

	public static void main(String[] args) throws IOException {
		// String file = "/home/spark/Desktop/dataChunkbb";
		// Path path = FileSystems.getDefault().getPath("", file);
		// byte data[] = Files.readAllBytes(path);
		// System.out.println(data.length);
		// double result[] = new double[(65536 + 100) * 6];
		//
		// ShortBuffer shortBuffer = ByteBuffer.wrap(data)
		// .order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
		// short shortArray[] = new short[shortBuffer.remaining()];
		//
		// shortBuffer.get(shortArray);

		// for (int i = 0; i < 10; i++) {
		// int numberOfResults[] = new int[1];
		// System.out.println("*********************************");
		//
		// long start = System.currentTimeMillis();
		//
		// CudaLib.cudaLib.entry(shortArray, result, numberOfResults, 0,
		// 33554432);
		// long end = System.currentTimeMillis();
		// System.out.println(numberOfResults[0]);
		// System.out.println("time is : " + (end - start));
		// System.out.println("*********************************");
		// }

		for (int i = 0; i < 1; i++) {

			// Th th = new Th(i);
			ServerTest serverTest = new ServerTest(i);
			Thread thread = new Thread(serverTest, "name");
			thread.start();
		}

	}

	interface CudaLib extends Library {
		CudaLib cudaLib = (CudaLib) Native.loadLibrary(
				"/home/spark/Desktop/libcuda.so", CudaLib.class);

		public void entry(short[] input, double[] output,
				int[] numberOfResults, int kafkaMessageId, int inputSize);

	}

}
