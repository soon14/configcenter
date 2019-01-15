package com.asiainfo.configcenter.zookeeper.inner;

import com.asiainfo.configcenter.zookeeper.cczk.Callback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ZookeeperStore extends ConnectionWatcher{

    private static Charset CHARSET = Charset.forName("utf-8");//编码格式

    private static int MAX_RETRIES = 3;//最大重连次数

    protected static int RETRY_PERIOD_SECONDS = 5;//重连超时时间 秒

    public ZookeeperStore(boolean debug, Callback callback) {
        super(debug, callback);
    }

    /**
     *
     * @param path 节点路径
     * @param value 数据
     */

    /**
     * 写数据 有可能连接不上zk,连接不上是需要重新连接
     * @param path 节点路径
     * @param value 节点路径
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void write(String path , String value )throws InterruptedException, KeeperException {
        int retries = 0;
        while (true) {

            try {

                Stat stat = zk.exists(path, false);//判断节点是否存在

                if (stat == null) {

                    throw new KeeperException.NoNodeException("节点:"+ path + "不存在");

                } else {

                    zk.setData(path, value.getBytes(CHARSET), stat.getVersion());
                }

                break;

            }catch (KeeperException.SessionExpiredException e) {

                LOGGER.warn("write connect lost... will retry " + retries + "\t" + e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }


    /**
     *  读取某个节点的数据，并且可以添加节点监听器
     * @param path 节点路径
     * @param watcher 监听器
     * @param stat 版本信息
     * @return 节点数据
     * @throws InterruptedException 异常
     * @throws KeeperException 异常
     */
    public String read(String path, Watcher watcher, Stat stat)throws InterruptedException,KeeperException{
        int retries = 0;
        while (true) {

            try {

                byte[] data = zk.getData(path ,watcher,stat);
                return new String(data);

            }catch (KeeperException.SessionExpiredException e) {

                LOGGER.warn("read connect lost... will retry " + retries + "\t" + e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }

    }


    /**
     * 创建一个节点
     * @param path
     * @param value
     * @param createMode
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public String createNode(String path, String value, List<ACL> aclList, Watcher watcher, CreateMode createMode)
            throws InterruptedException, KeeperException {

        int retries = 0;
        while (true) {

            try {

                if(aclList == null){
                    return zk.create(path, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                }else {
                    return zk.create(path, value.getBytes(CHARSET), aclList, createMode);
                }

            } catch (KeeperException.SessionExpiredException e) {

                LOGGER.warn("createNode connect lost... will retry " + retries + "\t" + e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }

    /**
     * 判断节点是否存在
     *
     * @param path 节点路径
     *
     * @return true:存在 false:不存在
     *
     * @throws InterruptedException 异常
     * @throws KeeperException 异常
     */
    public boolean exists(String path,Watcher watcher) throws InterruptedException, KeeperException {

        int retries = 0;
        while (true) {

            try {

                Stat stat = zk.exists(path, watcher);

                return !(stat == null);


            }  catch (KeeperException.SessionExpiredException e) {

                LOGGER.warn("exists connect lost... will retry " + retries + "\t" + e.toString());

                if (retries++ == MAX_RETRIES) {
                    LOGGER.error("connect final failed");
                    throw e;
                }

                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }


    /**
     * 删除节点
     * @param path 节点路径
     */
    public void deleteNode(String path)throws KeeperException,InterruptedException {

        int retries = 0;
        while (true) {

            try {

                zk.delete(path, -1);
                break;


            } catch (KeeperException.SessionExpiredException e) {

                LOGGER.warn("deleteNode connect lost... will retry " + retries + "\t" + e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }



    }


    /**
     * 获取子节点列表
     * @param path 节点路径
     * @param watcher 观察者
     * @return 字节点列表
     * @throws InterruptedException 异常
     * @throws KeeperException 异常
     */
    public List<String> getChild(String path,Watcher watcher)throws InterruptedException,KeeperException{

        int retries = 0;
        while (true) {

            try {

                return zk.getChildren(path,watcher);

            } catch (KeeperException.SessionExpiredException e) {

                LOGGER.warn("getChild connect lost... will retry " + retries + "\t" + e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }

    }

}
