package com.sbkj.car.service.impl;

import com.sbkj.car.component.exception.SbException;
import com.sbkj.car.mapper.FileMapper;
import com.sbkj.car.model.FilePo;
import com.sbkj.car.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: 臧东运
 * @CreateTime: 2019/4/29 11:22
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    /**
     * 上传文件
     * @param filePo 文件地址
     * @return
     */
    @Override
    public void saveFile(FilePo filePo) throws SbException {
        if (fileMapper.saveFile(filePo) < 1) {
            throw new SbException("保存文件失败!");
        }
    }
}
