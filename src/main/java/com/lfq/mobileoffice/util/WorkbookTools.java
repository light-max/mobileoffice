package com.lfq.mobileoffice.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 工作簿工具类
 *
 * @author 李凤强
 */
public class WorkbookTools {
    /**
     * 把工作簿对象转换为{@link ResponseEntity}对象
     *
     * @param workbook 工作簿
     * @param filename 文件名不需要加后缀名
     * @return
     * @throws IOException
     */
    public static ResponseEntity<byte[]> toResponseEntity(XSSFWorkbook workbook, String filename) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        byte[] bytes = out.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        String mediaType = "application/octet-stream";
        filename = new String(filename.getBytes("GBK"), StandardCharsets.ISO_8859_1);
        headers.add("Content-Disposition", "filename=" + filename+".xlsx");
        return ResponseEntity.ok().headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType(mediaType))
                .body(bytes);
    }
}
