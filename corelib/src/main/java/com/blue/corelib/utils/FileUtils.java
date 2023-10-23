package com.blue.corelib.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.blue.corelib.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * 对文件的操作工具类
 * <p>
 * 1、删除文件/文件夹，如果是文件夹，会连同他的子目录一起删除
 * 2、获得文件或者文件夹中文件的大小
 * 3、获得文件的大小，显示格式为GB形式
 * 4、将文件大小显示为GB,MB等形式
 * 5、获得文件名
 * 6、新建一个文件目录
 * 7、获取本应用图片缓存目录
 * 8、在指定的位置创建指定的文件
 * 9、在指定的位置创建文件夹
 * 10、删除指定的文件
 * 11、删除指定的文件夹
 * 12、复制文件/文件夹 若要进行文件夹复制，请勿将目标文件夹置于源文件夹中
 * 13、获取SD卡根目录
 * 14.获取本应用图片缓存目录
 * 15.删除文件 这里仅仅会删除某个文件夹下的文件，假设传入的directory是个文件，将不做处理
 */
public final class FileUtils {

    private static long fileLen = 0;

    /**
     * 1、删除文件/文件夹，如果是文件夹，会连同他的子目录一起删除
     *
     * @param filePath 文件路径
     */
    public static void delFilesFromPath(File filePath) {
        if (filePath == null) {
            return;
        }
        if (!filePath.exists()) {
            return;
        }
        File[] files = filePath.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                files[i].delete();
            } else {
                delFilesFromPath(files[i]);
                files[i].delete();// 刪除文件夾
            }
        }
    }

    /**
     * 2、获得文件或者文件夹中文件的大小
     *
     * @param filePath 文件路径
     * @return
     */
    public static long getFileLen(File filePath) {
        fileLen = 0;
        return getFileLenFromPath(filePath);
    }

    /**
     * 传入文件夹
     *
     * @param filePath
     * @return
     */
    private static long getFileLenFromPath(File filePath) {
        File[] files = filePath.listFiles();
        if (files == null) {
            return 0;
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                fileLen += files[i].length();
            } else {
                getFileLenFromPath(files[i]);
            }
        }
        return fileLen;
    }

    /**
     * 3、获得文件的大小，显示格式为GB形式
     *
     * @param filePath
     * @return
     */
    public static String size(File filePath) {
        if (filePath == null) {
            return "0字节";
        }
        if (!filePath.exists()) {
            return "0字节";
        }
        long fileLen2 = getFileLen(filePath);
        String size = size(fileLen2);
        return size;
    }

    /**
     * 4、将文件大小显示为GB,MB等形式
     *
     * @param size 文件大小
     * @return
     */
    public static String size(long size) {
        DecimalFormat df = new DecimalFormat("0.##");
        if (size / (1024 * 1024 * 1024) > 0) {
            double tmpSize = (size * 1.0) / (1024 * 1024 * 1024);
            return "" + df.format(tmpSize) + "GB";
        } else if (size / (1024 * 1024) > 0) {
            double tmpSize = (size * 1.0) / (1024 * 1024);
            return "" + df.format(tmpSize) + "MB";
        } else if (size / 1024 > 0) {
            double tmpSize = (size * 1.0) / 1024;
            return "" + df.format(tmpSize) + "KB";
        } else
            return "" + size + "B";
    }

    /**
     * 8、在指定的位置创建指定的文件
     *
     * @param filePath 完整的文件路径
     * @param mkdir    是否创建相关的文件夹
     * @throws IOException
     */
    public static void mkFile(String filePath, boolean mkdir) throws IOException {
        File file = new File(filePath);
        /**
         * mkdirs()创建多层目录，mkdir()创建单层目录
         * writeObject时才创建磁盘文件。
         * 若不创建文件，readObject出错。
         */
        file.getParentFile().mkdirs();
        file.createNewFile();
        file = null;
    }

    /**
     * 9、在指定的位置创建文件夹
     *
     * @param dirPath 文件夹路径
     * @return 若创建成功，则返回True；反之，则返回False
     */
    public static boolean mkDir(String dirPath) {
        return new File(dirPath).mkdirs();
    }

    /**
     * 10、删除指定的文件
     *
     * @param filePath 文件路径
     * @return 若删除成功，则返回True；反之，则返回False
     */
    public static boolean delFile(String filePath) {
        return new File(filePath).delete();
    }

    /**
     * 11、删除指定的文件夹
     *
     * @param dirPath 文件夹路径
     * @param delFile 文件夹中是否包含文件
     * @return 若删除成功，则返回True；反之，则返回False
     */
    public static boolean delDir(String dirPath, boolean delFile) {
        if (delFile) {
            File file = new File(dirPath);
            if (file.isFile()) {
                return file.delete();
            } else if (file.isDirectory()) {
                if (file.listFiles().length == 0) {
                    return file.delete();
                } else {
                    int zFiles = file.listFiles().length;
                    File[] delfile = file.listFiles();
                    for (int i = 0; i < zFiles; i++) {
                        if (delfile[i].isDirectory()) {
                            delDir(delfile[i].getAbsolutePath(), true);
                        }
                        delfile[i].delete();
                    }
                    return file.delete();
                }
            } else {
                return false;
            }
        } else {
            return new File(dirPath).delete();
        }
    }

    /**
     * 12、复制文件/文件夹 若要进行文件夹复制，请勿将目标文件夹置于源文件夹中
     *
     * @param source   源文件（夹）
     * @param target   目标文件（夹）
     * @param isFolder 若进行文件夹复制，则为True；反之为False
     * @throws IOException
     */
    public static void copy(String source, String target, boolean isFolder) throws IOException {
        if (isFolder) {
            new File(target).mkdirs();
            File a = new File(source);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (source.endsWith(File.separator)) {
                    temp = new File(source + file[i]);
                } else {
                    temp = new File(source + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(target + File.separator + temp.getName().toString());
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copy(source + File.separator + file[i], target + File.separator + file[i], true);
                }
            }
        } else {
            int byteread = 0;
            File oldfile = new File(source);
            if (oldfile.exists()) {
                InputStream inputStream = new FileInputStream(source);
                File file = new File(target);
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while ((byteread = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, byteread);
                }
                inputStream.close();
                outputStream.close();
            }
        }
    }


    /**
     * 13.SD卡根目录.
     */
    public static File getRootPath() {
        File path = null;
        if (SDCardUtils.isSDCardEnable()) {
            path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }

    /**
     * 14、获取本应用图片缓存目录
     */
    public static File getCacheFolder(Context context) {
        File folder = new File(context.getCacheDir(), "IMAGECACHE");
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }

    /**
     * 15.删除文件 这里仅仅会删除某个文件夹下的文件，假设传入的directory是个文件，将不做处理
     */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files == null) {
                return;
            }
            for (File item : files) {
                item.delete();
            }
        }
    }
    /**
     * 从路径中提取文件名
     */
    public static String parseFileNameFrom(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        int pos = path.lastIndexOf("/");
        if (pos >= 0) {
            if (pos == path.length() - 1) {
                return null;
            }
            path = path.substring(pos + 1);
        }
        return path;
    }
    /**
     * 16.获取本地手机相册路径
     */
    public static String getLocalCameraPath(Context context) {
        // vivo手机单独处理
        String rootPath = getRootPath().getAbsolutePath();
        String vivoPath = rootPath + File.separator + context.getString(R.string.sd_camera);
        if (new File(vivoPath).exists()) {
            return rootPath + File.separator + context.getString(R.string.sd_camera);
        } else {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "Camera";
        }
    }
    //获取data需要保存的jpg地址
    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }
}
