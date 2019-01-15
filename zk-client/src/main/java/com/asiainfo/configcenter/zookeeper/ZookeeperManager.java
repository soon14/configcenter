package com.asiainfo.configcenter.zookeeper;

import com.asiainfo.configcenter.zookeeper.cczk.Callback;
import com.asiainfo.configcenter.zookeeper.cczk.vo.DefaultCallback;
import com.asiainfo.configcenter.zookeeper.inner.ZookeeperStore;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class ZookeeperManager {

    private String defaultPrefixStr;


    private ZookeeperStore zookeeperStore;

    private ZookeeperManager(){

    }

    private static class InstanceHolder{
        private static ZookeeperManager zookeeperManager = new ZookeeperManager();
    }

    public static ZookeeperManager getInstance(){
        return InstanceHolder.zookeeperManager;
    }


    /**
     * 初始化
     * @param host zk ip
     * @param defaultPrefixStr 默认前缀
     * @param debug debug模式  暂时没有功能
     * @throws IOException 异常
     * @throws InterruptedException 异常
     */
    public void init(String host ,String defaultPrefixStr ,boolean debug)throws IOException,InterruptedException{
        init(host, defaultPrefixStr, debug, new DefaultCallback());
    }

    public void init(String host , String defaultPrefixStr , boolean debug, Callback callback)throws IOException,InterruptedException{
        zookeeperStore = new ZookeeperStore(debug, callback);
        zookeeperStore.connect(host);
        if(defaultPrefixStr == null){
            defaultPrefixStr = "";
        }else if(defaultPrefixStr.endsWith("/")){
            throw new IllegalArgumentException("defaultPrefixStr can not end with /,you can set defaultPrefixStr empty");
        }
        this.defaultPrefixStr = defaultPrefixStr;
    }

    /**
     *
     * 创建节点 最基本(调用fixPath)
     * @param path 路径
     * @param value 数据
     * @param aclList 访问控制
     * @param watcher 监听器
     * @param createMode 节点类型
     * @throws InterruptedException 异常
     * @throws KeeperException 异常
     */
    public void createNode(String path, String value, List<ACL> aclList, Watcher watcher, CreateMode createMode)throws InterruptedException, KeeperException{
        zookeeperStore.createNode(fixPath(path),value,aclList,watcher,createMode);
    }

    /**
     * 删除节点 根节点不能删除 最基本(调用fixPath)
     * @param path 路径
     */
        public void deleteNode(String path)throws KeeperException,InterruptedException{
        zookeeperStore.deleteNode(fixPath(path));
    }

    /**
     *
     * 读数据 最基本(调用fixPath)
     * @param path 路径
     * @param watcher 监听器
     * @param stat 版本信息
     * @return 节点数据
     * @throws InterruptedException 异常
     * @throws KeeperException 异常
     */
    public String readNodeData(String path, Watcher watcher, Stat stat)throws InterruptedException,KeeperException{
        return zookeeperStore.read(fixPath(path),watcher,stat);
    }


    /**
     * 写数据 最基本(调用fixPath)
     * @param path 路径
     * @param value 数据
     * @throws InterruptedException 异常
     * @throws KeeperException 异常
     */
    public void writeNodeData(String path, String value)throws InterruptedException, KeeperException {
        zookeeperStore.write(fixPath(path),value);
    }

    /**
     * 判断节点是否存在  最基本(调用fixPath)
     * @param path 节点路径
     * @param watcher 监听器
     * @return 存在：true  不存在:false
     * @throws InterruptedException 异常
     * @throws KeeperException 异常
     */
    public boolean existNode(String path,Watcher watcher)throws InterruptedException, KeeperException{
        return zookeeperStore.exists(fixPath(path),watcher);
    }




    public String readNodeData(String path,Watcher watcher)throws InterruptedException,KeeperException{
        return readNodeData(path,watcher,null);
    }

    public String readNodeData(String path)throws InterruptedException,KeeperException{
        return readNodeData(path,null,null);
    }





    /**
     * 创建节点
     * @param path 节点路径
     * @param value 数据
     * @param createMode 节点类型
     * @throws InterruptedException 异常
     * @throws KeeperException 异常
     */
    public void createNode(String path, String value, CreateMode createMode)throws InterruptedException, KeeperException{
        createNode(path,value,null,null,createMode);
    }

    /**
     * 获取子节点列表 最基本(调用fixPath)
     * @param path 路径
     * @param watcher 监听器
     * @return 子节点列表
     * @throws InterruptedException 异常
     * @throws KeeperException 异常
     */
    public List<String> getChild(String path,Watcher watcher)throws InterruptedException,KeeperException{
        return zookeeperStore.getChild(fixPath(path),watcher);
    }



    /**
     * 递归删除节点
     * @param path 节点路径
     * @throws KeeperException 异常
     * @throws InterruptedException 异常
     */
    public void deleteNodeRecursion(String path)throws KeeperException,InterruptedException{
        //遍历所有的节点
        List<String> child = getChild(path,null);
        if(child != null && child.size() > 0){
            for(String childPath: child){
                deleteNodeRecursion(path + "/" +childPath);
            }
        }
        deleteNode(path);
    }

    /**
     *  判断节点是否存在
     * @param path 节点路径
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public boolean existNode(String path)throws InterruptedException, KeeperException{
        return existNode(path,null);
    }


    /**
     * 预处理路径
     * @param path 路径
     * @return
     */
    private String fixPath(String path){
        String finalStr = defaultPrefixStr + path;
        if(finalStr.length() != 1 && finalStr.endsWith("/")){
            finalStr = finalStr.substring(0,finalStr.length()-1);
        }
        return finalStr;
    }


}
