package com.longqin.system.service.impl;

import com.longqin.system.entity.Log;
import com.longqin.system.mapper.LogMapper;
import com.longqin.system.service.ILogService;
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
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

	@Autowired
	LogMapper logMapper;
	
	@Override
	public Log getById(int id)
    {
        return logMapper.selectById(id);
    }

	@Override
    public List<Log> getPage(int organizationId, String beginDate, String endDate, int startIndex, int pageSize)
    {
        return logMapper.selectPage(organizationId, beginDate, endDate, startIndex, pageSize);
    }
    
	@Override
    public int getCount(int organizationId, String beginDate, String endDate)
    {
        return logMapper.selectCount(organizationId, beginDate, endDate);
    }

	@Override
    public int insert(Log entity) throws Exception
    {
        return logMapper.insert(entity);
    }
}
