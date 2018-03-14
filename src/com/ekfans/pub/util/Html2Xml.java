package com.ekfans.pub.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.w3c.tidy.Tidy;

public class Html2Xml {
	public static void convert(String outFileName, String errOutFileName, String htmlStr) {
		if (StringUtil.isEmpty(outFileName) || StringUtil.isEmpty(htmlStr)) {
			return;
		}

		File file = new File(outFileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		BufferedInputStream in;
		FileOutputStream out;
		Tidy tidy = new Tidy();
		// Tell Tidy to convert HTML to XML
		tidy.setXmlOut(true);
		try {
			// Set file for error messages
			tidy.setErrout(new PrintWriter(new FileWriter(errOutFileName), true));
			tidy.setInputEncoding("UTF-8");
			tidy.setOutputEncoding("UTF-8");
			tidy.setEncloseText(false);
			// u = new URL(url);
			// Create input and output streams
			// in = new BufferedInputStream(new FileInputStream(htmlStr));
			ByteArrayInputStream bais = new ByteArrayInputStream(htmlStr.getBytes("UTF-8"));
			in = new BufferedInputStream(bais);
			out = new FileOutputStream(outFileName);
			// Convert files
			tidy.parse(in, out);
			// Clean up
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
