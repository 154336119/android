package com.huizhuang.zxsq.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * @ClassName: FileUtil
 * @Description: 文件操作工具
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月3日 下午4:03:42
 */
public class FileUtil {

	public static final String ENCODING_TYPE = "UTF-8";

	public static String getSDPath() {
		// 得到当前外部存储设备的目录( /SDCARD )
		return Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @param fileName
	 * @param isCreate
	 * @return
	 * @throws IOException
	 */
	public static boolean isExist(String fileName, boolean isCreate) throws IOException {
		File file = new File(fileName);
		boolean ret = file.exists();
		if (!ret && isCreate) {
			file.createNewFile();
		}
		return ret;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 */
	public static void deteleFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static void deleteFile(File file) {
		if (!file.exists()) {
			return;
		}

		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				deleteFile(f);
			}
			file.delete();
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public static void createFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
	}

	/**
	 * 读数据
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static byte[] read(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
			int size = is.available();
			byte[] data = new byte[size];
			is.read(data);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				is.close();
			}
		}
		return null;
	}

	/**
	 * 写入数据
	 * 
	 * @param fileName
	 * @param data
	 * @throws IOException
	 */
	public static void write(String fileName, byte[] data) throws IOException {
		FileOutputStream os = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			os = new FileOutputStream(fileName);
			os.write(data);
			os.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				os.close();
			}
		}
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File createSDFile(String fileName) throws IOException {
		String path = getSDPath() + fileName;
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return dirFile;
	}

	/**
	 * 在SD卡上创建目录和文件
	 * 
	 * @param dir
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File createSDFile(String dir, String fileName) throws IOException {
		String path = getSDPath() + dir;
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(path, fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return
	 */
	public static File createSDDir(String dirName) {
		File dir = new File(getSDPath() + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(getSDPath() + fileName);
		return file.exists();
	}

	/**
	 * 判断SD卡上的文件是否存在
	 * 
	 * @param (url + fileName)
	 * @return
	 */
	public static boolean isFileExists(String url) {
		if (TextUtils.isEmpty(url)) {
			return false;
		}
		boolean isexit = true;
		try {
			File f = new File(url);
			if (!f.exists()) {
				isexit = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			isexit = false;
		}
		return isexit;
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public static File write2SDFromInput(String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 将一个字符串写入到SD卡中
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public static File write2SDFromInput(String path, String fileName, String input) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(path + fileName);
			output = new FileOutputStream(file);
			output.write(input.getBytes());
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 从raw读取文本内容
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static String getFromRaw(Context context, int id) {
		StringBuffer result = new StringBuffer();
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		try {
			inputReader = new InputStreamReader(context.getResources().openRawResource(id), "UTF-8");
			bufReader = new BufferedReader(inputReader);
			String lineTxt = null;
			while ((lineTxt = bufReader.readLine()) != null)
				result.append(lineTxt);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputReader != null) {
					inputReader.close();
				}
				if (bufReader != null) {
					bufReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString().trim();
	}

	/**
	 * 从assets读取文本内容
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getFromAssets(Context context, String fileName) {
		StringBuffer result = new StringBuffer();
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		try {
			inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName), "UTF-8");
			bufReader = new BufferedReader(inputReader);
			String lineTxt = null;
			while ((lineTxt = bufReader.readLine()) != null)
				result.append(lineTxt);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputReader != null) {
					inputReader.close();
				}
				if (bufReader != null) {
					bufReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString().trim();
	}
	
	 /**
     * 序列化文件到硬盘
     * 
     * @param context
     * @param name
     * @param obj
     */
    public static void saveFile(Context context, String name, Object obj) {
        if (context == null)
            return;
        File root = context.getFilesDir();
        File file = new File(root, name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            fos.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取本地序列化文件
     * 
     * @param context
     * @param name
     * @return
     */
    public static Object readFile(Context context, String name) {
        File root = context.getFilesDir();
        File file = new File(root, name);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            fis.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object readFileFromAssets(Context context, String name) {
        try {
            
            InputStream fis = context.getAssets().open(name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            fis.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


	public static double getDirSize(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				double size = 0;
				for (File f : children)
					size += getDirSize(f);
				return size;
			} else {// 如果是文件则直接返回其大小,以“兆”为单位
				double size = (double) file.length() / 1024 / 1024;
				return size;
			}
		} else {
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
			return 0.0;
		}
	}

	/**
	 * Uri变成File对象
	 * 
	 * @param originalUri
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static File changeUriToFile(Activity activity, Uri originalUri) {
		String[] proj = {MediaStore.Images.Media.DATA};
		Cursor actualimagecursor = activity.managedQuery(originalUri, proj, null, null, null);
		int index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String imgPath = actualimagecursor.getString(index);
		File originalFile = new File(imgPath);
		return originalFile;
	}


}
