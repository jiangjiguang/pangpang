package com.pangpang.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by root on 16-9-11.
 */
public class FileUtil {
    public static void main(String[] args) {

    }
    public static boolean createFile(String path, String flag) {
        if (StringUtils.isBlank(path)) {
            return false;
        }

        path = RegexUtil.toBackslash(path);

        if (path.endsWith(File.separator)) {
            return false;
        }

        File file = new File(path);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }

        if (!StringUtils.isBlank(flag) && flag.equals("r")) {
            try {
                file.deleteOnExit();
                return file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        if (file.exists()) {
            return true;
        }
        try {
            return file.createNewFile();
        } catch (Exception ex) {
            return false;
        }
    }
}
