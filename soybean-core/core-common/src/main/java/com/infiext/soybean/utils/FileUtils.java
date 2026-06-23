package com.infiext.soybean.utils;

import java.io.File;
import java.time.LocalDate;

public class FileUtils {
    
    public static String buildFilePath(String fileName){
        LocalDate now = LocalDate.now();
        String yearPath = String.valueOf(now.getYear());
        String monthPath = String.format("%02d", now.getMonthValue());
        String dayPath = String.format("%02d", now.getDayOfMonth());
        return yearPath + File.separator + monthPath + File.separator + dayPath + File.separator + fileName;
    }
    
    /**
     * 格式化文件大小
     *
     * @param size 文件大小（字节）
     * @return 格式化后的文件大小字符串
     */
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2fKB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2fMB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2fGB", size / (1024.0 * 1024 * 1024));
        }
    }

}
