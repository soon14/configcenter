package com.asiainfo.configcenter.center.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by oulc on 2018/8/3.
 */
public class CcFileUtilsTest {
    @Test
    public void unzip() throws Exception {
        File file = new File("G:\\testfile.zip");
        CcFileUtils.unzip(file);
    }
    @Test
    public void getConfigFileFullPath(){
        String absolutePath = "E:/test1/test2/test3/test4";
        String rootPath = "E:/test1/test2";
        Assert.assertEquals("test3/test4",CcFileUtils.getRelativeFilePath(absolutePath,rootPath));

        absolutePath = "E:/test1/test2/test3/test4/";
        rootPath = "E:/test1/test2/";
        Assert.assertEquals("test3/test4",CcFileUtils.getRelativeFilePath(absolutePath,rootPath));

        absolutePath = "E:/test1/test2/test3/test4/";
        rootPath = "E:/";
        Assert.assertEquals("test1/test2/test3/test4",CcFileUtils.getRelativeFilePath(absolutePath,rootPath));
    }

    @Test
    public void concatFilePath(){
        String filePaths[] = {"E:/test1/","/test2/","test3","/test4","test5"};
        Assert.assertEquals("E:/test1/test2/test3/test4/test5",CcFileUtils.concatFilePath(filePaths));
    }

    @Test
    public void zip()throws Exception{
        File file = new File("/home/oulc/code/config-file-project-git/newOulcApp/env1");
        CcFileUtils.zip(file);
    }

}