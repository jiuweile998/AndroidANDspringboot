package com.edu.nuc.selftip;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

//文件存储访问工具类
public class DataFileAccess {
    private Context mContext;

    public DataFileAccess(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 判断文件是否存在
     * @param fileName,要检查的文件名
     * @return boolean,true表示存在，false表示不存在
     */

    public boolean isFileExist(String fileName){
        File file = new File(fileName);
        boolean isExisted = file.exists();
        return isExisted;
    }

    /**
     * 创建文件
     */

    public File creatFile(String fileName)throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 拷贝文件,srcFile是源文件，destFile是目标文件
     */

    public boolean copyFileTo(File srcFile,File destFile)throws IOException{
        if(srcFile.isDirectory() || destFile.isDirectory()){
            return false;
        }
        FileInputStream fis = new FileInputStream(srcFile);//在源文件上创建一个输入流管道，用来读数据
        FileOutputStream fos = new FileOutputStream(destFile);//在目标文件上创建一个输出流管道，用来写数据
        byte []bufs = new byte[1024];//创建一个字节数组用来存放输入流管道从源文件读取的字节数据
        int readLen = 0;
        while ((readLen = fis.read(bufs)) != -1){//输入流从源文件中以字节为单位读取字节数据
            fos.write(bufs,0,readLen);//输出流以字节为单位向目标文件中写数据
        }
        fos.flush();//清空输出流管道中的数据，确保数据都写到目标文件中
        fos.close();
        fis.close();
        return true;
    }

    /**
     * 将文件存储到内部存储器的文件夹中
     */

    public void saveFile(String fileName,byte []fileData)throws IOException{
        FileOutputStream fos = mContext.openFileOutput(fileName,Context.MODE_PRIVATE);
        fos.write(fileData);
        fos.flush();
        fos.close();
    }

    //将用户信息保存到内部存储器的文件
    public void saveUserInfoToFile(String fileName, MyUser user){

        try {
            FileOutputStream fos = mContext.openFileOutput(fileName,Context.MODE_PRIVATE);

            byte []idBufs = user.mUserId.getBytes(Charset.forName("UTF-8"));//将用户名字符串以UTF-8编码存放到字节数组中
            fos.write((byte)idBufs.length);//把用户名字节长度写到文件中
            fos.write(idBufs);//把用户名写到文件中

            //将地址写到文件中
            byte []nmBufs = user.mUserName.getBytes(Charset.forName("UTF-8"));
            fos.write((byte)nmBufs.length);
            fos.write(nmBufs);

            //将密码写到文件中
            byte []psdBufs = user.mPassword.getBytes(Charset.forName("UTF-8"));
            fos.write((byte)psdBufs.length);
            fos.write(psdBufs);

            //将电话号码写到文件中
            byte []phoneBufs = user.mUserPhone.getBytes(Charset.forName("UTF-8"));
            fos.write((byte)phoneBufs.length);
            fos.write(phoneBufs);



            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //将保存在内部存储器文件中的用户信息读取出来
    public MyUser readUserInfoFromFile(String fileName){
        MyUser user = null;

        try {
            FileInputStream fis = mContext.openFileInput(fileName);
            int fileLen = fis.available();//获取到文件的大小
            if(fileLen == 0){
                return null;
            }

            user = new MyUser();

            //从文件中读取用户名
            byte bufSize = (byte)fis.read();//读入用户名的长度
            byte[]idBufs = new byte[bufSize];
            fis.read(idBufs);//用字节输入流读出用户名数据存到idBufs里
            user.mUserId = new String(idBufs,"UTF-8");//将字节数组解码为UTF-8格式的字符串并存到user对象中

            //从文件中读取地址
            bufSize = (byte)fis.read();//读入地址的长度
            byte[]nmBufs = new byte[bufSize];
            fis.read(nmBufs);//用字节输入流读出地址数据存到addBufs里
            user.mUserName = new String(nmBufs,"UTF-8");//将字节数组解码为UTF-8格式的字符串并存到user对象中

            //从文件中读取密码
            bufSize = (byte)fis.read();//读入用户名的长度
            byte[]psdBufs = new byte[bufSize];
            fis.read(psdBufs);//用字节输入流读出密码数据存到psdBufs里
            user.mPassword = new String(psdBufs,"UTF-8");//将字节数组解码为UTF-8格式的字符串并存到user对象中

            //从文件中读取电话号码
            bufSize = (byte)fis.read();//读入电话号码的长度
            byte[]phoneBufs = new byte[bufSize];
            fis.read(phoneBufs);//用字节输入流读出电话号码数据存到phoneBufs里
            user.mUserPhone = new String(phoneBufs,"UTF-8");//将字节数组解码为UTF-8格式的字符串并存到user对象中


        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }
}