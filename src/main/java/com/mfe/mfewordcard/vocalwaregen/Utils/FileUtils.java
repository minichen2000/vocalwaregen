package com.mfe.mfewordcard.vocalwaregen.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	
	
	static public void mkdirs(String filePath) {
        File theFile = new File(filePath);
        File targetDir;
        if (!filePath.endsWith("/")) {
            targetDir = theFile.getParentFile();
        } else {
            targetDir = theFile;
        }

        targetDir.mkdirs();
    }

	static public List<String> readFileToStringArray(String filename,
			String ignoreLinePre, String encoding) {
		ArrayList<String> rlt;
		BufferedReader input = null;
		InputStream fis;
		try {
			fis = new FileInputStream(filename);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

		InputStreamReader ir = null;
		if (null == encoding) {
			ir = new InputStreamReader(fis);
		} else {
			try {
				ir = new InputStreamReader(fis, encoding);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					fis.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return null;
				}
				return null;
			}
		}

		input = new BufferedReader(ir);

		String text;

		rlt = new ArrayList<String>();
		try {
			while ((text = input.readLine()) != null) {
				text = text.trim();
				if ((null != ignoreLinePre && text.startsWith(ignoreLinePre)) || text.isEmpty()) {
					continue;
				}
				rlt.add(text);

			}
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				fis.close();
				ir.close();
				input.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
			return null;
		}

		return rlt;
	}

	static public boolean writeStringToFile(String str, String filename,
			boolean append) {
		return writeStringToFile(str, filename, null, append);
	}

	static public boolean writeStringToFile(String str, String filename,
			String encoding, boolean append) {
		mkdirs(filename);

		FileOutputStream output = null;
		OutputStreamWriter writer = null;
		try {
			output = new FileOutputStream(filename, append);
			if (null == encoding) {
				writer = new OutputStreamWriter(output);
			} else {
				writer = new OutputStreamWriter(output, encoding);
			}
			writer.write(str);
			writer.close();
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				if (null != output)
					output.close();
				if (null != writer)
					writer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
			return false;
		}
		return true;
	}

	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getBytes(String filePath){
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 根据byte数组，生成文件
	 */
	public static boolean saveBytes(byte[] bytes, String destFile) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			mkdirs(destFile);
			file = new File(destFile);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);
			bos.flush();
			bos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
