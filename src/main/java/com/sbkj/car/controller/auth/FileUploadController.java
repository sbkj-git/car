package com.sbkj.car.controller.auth;

import com.sbkj.car.common.SbkjTool;
import com.sbkj.car.component.exception.SbException;
import com.sbkj.car.enums.StatusEnum;
import com.sbkj.car.model.FilePo;
import com.sbkj.car.model.ResponseBody;
import com.sbkj.car.service.FileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * @Description: 文件上传controller
 * @Author: 臧东运
 * @CreateTime: 2019/4/23 11:14
 */
@RestController
@RequestMapping("/auth")
public class FileUploadController {
    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * 上传图片服务器的地址
     **/
    @Value("${url.imgServer}")
    private String imgServer;

    @Autowired
    private FileService fileService;

    /**
     * 文件上传(本地)
     *
     * @param uploadFile
     * @return
     * @throws Exception
     */
    @RequestMapping("/fileUploadLocal")
    public ResponseBody fileUploadLocal(MultipartFile uploadFile) throws Exception {
        try {
            // 磁盘位置
            String path = null;
            // 文件上传位置
            String fileUrl = null;
            //1.获取文件上传的位置
            URL uploadUrl = this.getClass().getResource("/static/uploads");
            if (uploadUrl != null) {
                path = uploadUrl.getPath().substring(0, uploadUrl.getPath().lastIndexOf("/uploads"));
            } else {
                path = this.getClass().getResource("/").getPath() + "/static";
            }
            //2.为了防止一个目录下文件过多，创建一个二级目录
            fileUrl = "/uploads/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //3.判断文件是否存在
            File file = new File(path, fileUrl);
            if (!file.exists()) {
                file.mkdirs();
            }
            //4.取出文件名
            String fileName = uploadFile.getOriginalFilename();
            // 为了防止文件重名，随机化文件名
            String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            fileName = uuid + "_" + fileName;
            // 写文件
            path += fileUrl;
            uploadFile.transferTo(new File(path, fileName));
            // 存文件
            fileUrl = fileUrl + "/" + fileName;
            FilePo filePo = new FilePo();
            filePo.setName(fileName);
            filePo.setUrl(fileUrl);
            filePo.setCreateTime(new Date());
            filePo.setUpdateTime(new Date());
            fileService.saveFile(filePo);
            return SbkjTool.responseReturn(StatusEnum.UPLOADSUCCESS, filePo);
        } catch (Exception sb) {
            logger.info(sb.getMessage());
            throw new SbException("文件上传出现异常!");
        }
    }


}