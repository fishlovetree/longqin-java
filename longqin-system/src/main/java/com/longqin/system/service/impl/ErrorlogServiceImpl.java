package com.longqin.system.service.impl;

import com.longqin.system.entity.Errorlog;
import com.longqin.system.mapper.ErrorlogMapper;
import com.longqin.system.service.IErrorlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@Service
public class ErrorlogServiceImpl extends ServiceImpl<ErrorlogMapper, Errorlog> implements IErrorlogService {

	@Autowired
	ErrorlogMapper logMapper;
	
	@Override
	public Errorlog getById(int id) {
        return logMapper.selectById(id);
    }

	@Override
    public List<Errorlog> getPage(String beginDate, String endDate, int startIndex, int pageSize) {
        return logMapper.selectPage(beginDate, endDate, startIndex, pageSize);
    }
    
	@Override
    public int getCount(String beginDate, String endDate) {
        return logMapper.selectCount(beginDate, endDate);
    }

	@Override
    public int insert(Errorlog entity) {
        return logMapper.insert(entity);
    }
}
