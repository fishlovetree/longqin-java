package com.longqin.system.service.impl;

import com.longqin.system.entity.Notice;
import com.longqin.system.mapper.NoticeMapper;
import com.longqin.system.service.INoticeService;
import com.longqin.system.util.OperationLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

	@Autowired
	NoticeMapper noticeMapper;

	@Override
    public Notice getById(int id) {
    	Notice notice = noticeMapper.selectById(id);
    	if (null != notice) {
    		List<String> attachments = noticeMapper.selectNoticeFiles(notice.getNoticeId());
    		notice.setAttachments(String.join(",", attachments));
    	}
    	return notice;
    }

	@Override
    public List<Notice> getPage(int organizationId, String title, String beginDate, String endDate, int startIndex, int pageSize) {
        return noticeMapper.selectPage(organizationId, title, beginDate, endDate, startIndex, pageSize);
    }
    
	@Override
    public int getCount(int organizationId, String title, String beginDate, String endDate) {
        return noticeMapper.selectCount(organizationId, title, beginDate, endDate);
    }

	@OperationLog(title = "删除公告", content = "'公告id：' + #id", operationType = "1")
	@Override
    public int delete(int id) throws Exception {
        return noticeMapper.updateStatus(id);
    }

	@OperationLog(title = "添加公告", content = "'公告标题：' + #entity.getTitle()", operationType = "0")
	@Override
    public int insert(Notice entity) throws Exception {
        return noticeMapper.insert(entity);
    }

	@OperationLog(title = "修改公告", content = "'公告标题：' + #entity.getTitle()", operationType = "2")
	@Override
    public int update(Notice entity) throws Exception {
        return noticeMapper.updateById(entity);
    }

	@Override
    public List<String> getNoticeFiles(int noticeId) {
        return noticeMapper.selectNoticeFiles(noticeId);
    }

	@Override
    public int setNoticeFiles(int noticeId, String filePaths) throws Exception {
    	int result = noticeMapper.deleteNoticeFiles(noticeId);
		if (filePaths != null && filePaths.length() > 0) {
			List<String> filePathArray = Arrays.asList(filePaths.split(",")).stream().collect(Collectors.toList());
		    result = noticeMapper.insertNoticeFiles(noticeId, filePathArray);
		}
		return result;
    }
}
