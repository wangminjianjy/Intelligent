package cn.com.sample.intelligent.manager;

import android.content.Context;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.List;

import cn.com.sample.intelligent.util.DateUtil;

/**
 * Created by 王敏健 on 2018/12/13.
 */

public class FTPManager {

    private Context mContext;
    FTPClient ftpClient = null;

    public FTPManager(Context mContext) {
        this.mContext = mContext;
        ftpClient = new FTPClient();
    }

    /**
     * 连接到FTP服务器
     *
     * @param host     ftp服务器域名
     * @param username 访问用户名
     * @param password 访问密码
     * @param port     端口
     * @return 是否连接成功
     */
    public boolean ftpConnect(String host, String username, String password, int port) {
        try {
            closeFTP();
            ftpClient.setDataTimeout(30000);//设置连接超时时间
            ftpClient.connect(host, port);
            // 根据返回的状态码，判断链接是否建立成功
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                boolean status = ftpClient.login(username, password);
                /*
                 * 设置文件传输模式
                 * 避免一些可能会出现的问题，在这里必须要设定文件的传输格式。
                 * 在这里我们使用BINARY_FILE_TYPE来传输文本、图像和压缩文件。
                 */
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                return status;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 创建文件夹
    public boolean createDirectory(String path) throws Exception {
        boolean bool = false;
        int start = 0;
        int end = 0;
        if (path.startsWith("/")) {
            start = 1;
        }
        end = path.indexOf("/", start);
        while (true) {
            String subDirectory = path.substring(start, end);
            if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                ftpClient.makeDirectory(subDirectory);
                ftpClient.changeWorkingDirectory(subDirectory);
                bool = true;
            }
            start = end + 1;
            end = path.indexOf("/", start);
            if (end == -1) {
                break;
            }
        }
        return bool;
    }

    // 实现上传文件的功能
    public synchronized boolean uploadFile(int netState, List<String> pathList, String serverPath) throws Exception {
        boolean uploadFlag = false;
        String folderStr = DateUtil.getDateTime();
        String[] pathArray = serverPath.split("/");
        if (netState == 2) {
            serverPath = File.separator + pathArray[pathArray.length - 1] + File.separator + folderStr + File.separator;
        } else {
            serverPath = File.separator + pathArray[pathArray.length - 1] + File.separator;
        }
        createDirectory(serverPath); // 如果文件夹不存在，创建文件夹
        for (String localPath : pathList) {
            // 上传文件之前，先判断本地文件是否存在
            File localFile = new File(localPath);
            if (!localFile.exists()) {
                uploadFlag = false;
                break;
            }
            String fileName = localFile.getName();
            // 如果本地文件存在，服务器文件也在，上传文件，这个方法中也包括了断点上传
            long localSize = localFile.length(); // 本地文件的长度
            FTPFile[] files = ftpClient.listFiles(fileName);
            long serverSize = 0;
            if (files.length == 0) {
                serverSize = 0;
            } else {
                serverSize = files[0].getSize(); // 服务器文件的长度
            }
            if (localSize <= serverSize) {
                if (ftpClient.deleteFile(fileName)) {
                    serverSize = 0;
                }
            }
            RandomAccessFile raf = new RandomAccessFile(localFile, "r");
            // 进度
            long step = localSize / 100;
            long process = 0;
            long currentSize = 0;
            // 好了，正式开始上传文件
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setRestartOffset(serverSize);
            raf.seek(serverSize);
            OutputStream output = ftpClient.appendFileStream(fileName);
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = raf.read(b)) != -1) {
                output.write(b, 0, length);
                currentSize = currentSize + length;
                if (currentSize / step != process) {
                    process = currentSize / step;
                    if (process % 10 == 0) {
                        System.out.println("上传进度：" + process);
                    }
                }
            }
            output.flush();
            output.close();
            raf.close();
            if (ftpClient.completePendingCommand()) {
                uploadFlag = true;
            } else {
                uploadFlag = false;
                break;
            }
        }
        return uploadFlag;
    }

    // 实现下载文件功能，可实现断点下载
    public synchronized boolean downloadFile(String localPath, String serverPath) throws Exception {
        // 先判断服务器文件是否存在
        String filePath = SharedPreferencesManager.getAppPath(mContext);
//        filePath = "Upload/BanBenGuanLi/BanBen";
//        ftpClient.changeWorkingDirectory(filePath);
        FTPFile[] files = ftpClient.listFiles(filePath);
        if (files.length == 0) {
            return false;
        }
        filePath = filePath + File.separator + files[0].getName();
        SharedPreferencesManager.saveAppName(mContext, files[0].getName());
        File localFilePath = new File(localPath);
        if (!localFilePath.exists()) {
            localFilePath.mkdirs();
        }
        localPath = localPath + files[0].getName();
        // 接着判断下载的文件是否能断点下载
        long serverSize = files[0].getSize(); // 获取远程文件的长度
        File localFile = new File(localPath);
        long localSize = 0;
        if (localFile.exists()) {
            File file = new File(localPath);
            file.delete();
        }
        // 进度
        long step = serverSize / 100;
        long process = 0;
        long currentSize = 0;
        // 开始准备下载文件
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        OutputStream out = new FileOutputStream(localFile, true);
        ftpClient.setRestartOffset(localSize);
        InputStream input = ftpClient.retrieveFileStream(filePath);
        byte[] b = new byte[1024];
        int length = 0;
        while ((length = input.read(b)) != -1) {
            out.write(b, 0, length);
            currentSize = currentSize + length;
            if (currentSize / step != process) {
                process = currentSize / step;
                if (process % 10 == 0) {
                    System.out.println("下载进度：" + process);
                }
            }
        }
        out.flush();
        out.close();
        input.close();
        // 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
        if (ftpClient.completePendingCommand()) {
            return true;
        } else {
            return false;
        }
    }

    // 如果ftp上传打开，就关闭掉
    public void closeFTP() throws Exception {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }
}
