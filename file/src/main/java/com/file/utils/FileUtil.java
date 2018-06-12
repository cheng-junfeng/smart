package com.file.utils;

import com.file.bean.FileInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<FileInfo> getFiles(String path) {
        ArrayList<FileInfo> allInfo = new ArrayList<>();
        File myPath = new File(path);
        if (myPath != null && myPath.exists()) {
            String rootPath = myPath.getAbsolutePath();
            String[] allFiles = myPath.list();
            if (allFiles != null && allFiles.length > 0) {
                for (String fileName : allFiles) {
                    String thisPath = rootPath + File.separator + fileName;
                    File tempFile = new File(thisPath);
                    if (tempFile != null && tempFile.exists()) {
                        FileInfo tempInfo = new FileInfo();
                        tempInfo.appName = tempFile.getName();
                        tempInfo.appPath = tempFile.getAbsolutePath();
                        tempInfo.permission = "" + tempFile.canRead() + tempFile.canWrite() + tempFile.canExecute();
                        tempInfo.isDir = tempFile.isDirectory();
                        tempInfo.size = tempFile.length();
                        tempInfo.isHidden = tempFile.isHidden();
                        allInfo.add(tempInfo);
                    }
                }
            }
        }
        return allInfo;
    }

    public static String getFileContent(String path) {
        File thisFile = new File(path);

        String content;
        StringBuffer strBuff = new StringBuffer();
        if (thisFile != null && thisFile.exists()) {
            try {
                FileReader fr = new FileReader(thisFile);
                BufferedReader br = new BufferedReader(fr);
                //一行一行读取 。在电子书程序上经常会用到。
                while ((content = br.readLine()) != null) {
                    strBuff.append(content + "\r\n");
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strBuff.toString();
    }
}
