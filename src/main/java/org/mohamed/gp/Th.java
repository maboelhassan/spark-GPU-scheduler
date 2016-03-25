package org.mohamed.gp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.mohamed.gp.Main.CudaLib;

public class Th implements Runnable {

	int number;

	public Th(int num) {
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
		double result[] = new double[(65536 + 100) * 6];

		ShortBuffer shortBuffer = ByteBuffer.wrap(data)
				.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
		short shortArray[] = new short[shortBuffer.remaining()];

		shortBuffer.get(shortArray);

		int numberOfResults[] = new int[1];
		long start = System.currentTimeMillis();
		System.out.println("start time for " + this.number + " " + start);
		System.out.println("*********************************");

		CudaLib.cudaLib.entry(shortArray, result, numberOfResults, 0, 33554432);
		long end = System.currentTimeMillis();
		System.out.println("end time for " + this.number + " " + end);
		System.out.println("Time is: " + (end - start) + " for thread "
				+ this.number);

	}

}
