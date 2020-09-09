package com.lfq.mobileoffice.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * io操作对象
 *
 * @author 李凤强
 */
public class IOUtil {
    /**
     * 把流中的数据写入到另一个流
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IOException
     */
    public static void write(InputStream in, OutputStream out) throws IOException {
        byte[] bytes = new byte[1024 * 1024 * 10];
        int len;
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        out.flush();
        out.close();
    }

    /**
     * 保存员工上传的文件
     *
     * @param filePath      文件保存的相对目录
     * @param employeeId    员工id
     * @param filename      文件名
     * @param multipartFile 要保存的文件
     */
    public static boolean writeEmployeeFile(
            String filePath, int employeeId,
            String filename, MultipartFile multipartFile
    ) {
        try {
            File file = new File(new File(filePath, String.valueOf(employeeId)), filename);
            file.getParentFile().mkdirs();
            write(multipartFile.getInputStream(), new FileOutputStream(file));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
