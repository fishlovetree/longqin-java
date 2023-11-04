package com.longqin.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.longqin.system.config.RequiredPermission;
import com.longqin.system.entity.Notice;
import com.longqin.system.service.INoticeService;
import com.longqin.system.util.ResponseData;
import com.longqin.system.util.ResponseEnum;
import com.longqin.system.util.SessionUtil;
import com.longqin.system.util.UploadUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@RestController
@RequestMapping("/notice")
@Api(tags="公告类")
public class NoticeController {

	@Autowired
	INoticeService noticeService;
	
	/**
	 * @Description 获取单条公告
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取单条公告", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "noticeId", value = "公告ID", required = true, dataType = "Integer") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getNoticeById")
	@RequiredPermission("notice:view")
	public ResponseData getNoticeById(int noticeId) {
		Notice notice = noticeService.getById(noticeId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", notice);
	}

	/**
	 * @Description 分页获取公告列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "分页获取公告", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "title", value = "检索条件：标题", dataType = "String"), 
		@ApiImplicitParam(name = "beginDate", value = "检索条件：开始时间", dataType = "String"),
		@ApiImplicitParam(name = "endDate", value = "检索条件：结束时间", dataType = "String"),
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer"),
		@ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"), @ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getNoticePage")
	@RequiredPermission("notice:view")
	public ResponseData getNoticePage(String title, String beginDate, String endDate, Integer page, Integer size) {
		if (null == page || null == size) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<Notice> noticeList = noticeService.getPage(SessionUtil.getSessionUser().getOrganizationId(), title, beginDate, endDate, (page - 1) * size, size);
		int total = noticeService.getCount(SessionUtil.getSessionUser().getOrganizationId(), title, beginDate, endDate);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", noticeList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}

	/**
	 * @Description 添加公告
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "添加公告", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "notice", value = "公告实体项", required = true, dataType = "Notice"),
		@ApiImplicitParam(name = "files", value = "附件", dataType = "MultipartFile[]")})
	@ApiResponses({ @ApiResponse(code = 1, message = "添加成功"), @ApiResponse(code = 0, message = "添加失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/create")
	@RequiredPermission("notice:view")
	public ResponseData create(Notice notice, MultipartFile[] files) throws Exception {
		if (null == notice) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		// 上传文件
		List<String> filePaths = new ArrayList<String>();
		if(null != files) {
			for(int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				String fileName = file.getOriginalFilename();
    			String filePath = UploadUtils.coverUpload(file.getInputStream(), fileName);
    			if(!filePath.equals("")) {
    				filePaths.add(filePath);
    			}
			}
		}
		notice.setCreator(SessionUtil.getSessionUser().getUserId());
		if (null == notice.getOrganizationId()) {
			notice.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
        }
		int noticeId = noticeService.insert(notice);
		if (noticeId > 0) {
			noticeService.setNoticeFiles(noticeId, String.join(",", filePaths));
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", noticeId);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", noticeId);
		}
	}

	/**
	 * @Description 修改公告
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "修改公告", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "notice", value = "公告实体项", required = true, dataType = "Notice"),
		@ApiImplicitParam(name = "files", value = "附件", dataType = "MultipartFile[]")})
	@ApiResponses({ @ApiResponse(code = 1, message = "修改成功"), @ApiResponse(code = 0, message = "修改失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/update")
	@RequiredPermission("notice:view")
	public ResponseData update(Notice notice, MultipartFile[] files) throws Exception {
		if (null == notice) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		// 上传文件
		List<String> filePaths = new ArrayList<String>();
		if(null != files) {
			for(int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				String fileName = file.getOriginalFilename();
    			String filePath = UploadUtils.coverUpload(file.getInputStream(), fileName);
    			if(!filePath.equals("")) {
    				filePaths.add(filePath);
    			}
			}
		}
		int result = noticeService.update(notice);
		if (result > 0) {
			noticeService.setNoticeFiles(notice.getNoticeId(), String.join(",", filePaths));
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}

	/**
	 * @Description 删除公告
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "删除公告", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "noticeId", value = "公告ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "删除成功"), @ApiResponse(code = 0, message = "删除失败") })
	@PostMapping("/delete")
	@RequiredPermission("notice:view")
	public ResponseData delete(int noticeId) throws Exception {
		int result = noticeService.delete(noticeId);
		if (result >= 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}
	
	/**
	 * @Description 附件上传
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "附件上传", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "files", value = "附件", required = true, dataType = "MultipartFile[]") })
	@ApiResponses({ @ApiResponse(code = 1, message = "上传成功"), @ApiResponse(code = 0, message = "上传失败"),
		@ApiResponse(code = 3, message = "参数错误")  })
	@PostMapping("/uploadFiles")
	@RequiredPermission("notice:view")
	public ResponseData uploadFiles(MultipartFile[] files) throws Exception {
		if (null == files || files.length == 0) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<String> filePaths = new ArrayList<String>();
		for(int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			String fileName = file.getOriginalFilename();
			String filePath = UploadUtils.coverUpload(file.getInputStream(), fileName);
			if(!filePath.equals("")) {
				filePaths.add(filePath);
			}
		}
		if (filePaths.size() > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "上传成功", filePaths);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "上传失败", filePaths);
		}
	}
}
