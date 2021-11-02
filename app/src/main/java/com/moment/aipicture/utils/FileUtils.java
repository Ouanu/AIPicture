package com.moment.aipicture.utils;

import java.io.File;

public class FileUtils {
    /**
     * 判断该文件夹是否存在，否则创建文件夹
     * @param name
     * @return
     */
    public static boolean fileIsExist(String name) {
        File file = new File(name);
        if(file.exists())
            return  true;
        else
            return file.mkdirs();
    }
}
