package com.longqin.business.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.longqin.business.util.ResponseData;
import com.longqin.business.util.ResponseEnum;
import com.longqin.business.util.UploadUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 *  上传类控制器
 * </p>
 *
 * @author longqin
 * @since 2023-11-01
 */
@RestController
@RequestMapping("/upload")
@Api(tags="上传类")
public class UploadController {

	/**
	 * @Description 单文件上传
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "单文件上传", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "File") })
	@ApiResponses({ @ApiResponse(code = 1, message = "上传成功"), @ApiResponse(code = 0, message = "上传失败"),
		@ApiResponse(code = 3, message = "参数错误")  })
	@PostMapping("/uploadFile")
	public ResponseData uploadFile(MultipartFile file) throws Exception {
		if (null == file) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		String os = System.getProperty("os.name").toLowerCase();
		
		String absolutePath = null;
		String[] fileNameArray = new String[]{};
		if (os.contains("windows")) {
			absolutePath = UploadUtils.saveMultipartFile(file, System.getProperty("user.dir") + "/src/main/resources/static/uploads");
			fileNameArray = absolutePath.split("\\\\");
		}else if (os.contains("linux")) {
			absolutePath = UploadUtils.saveMultipartFile(file, "/"+System.getProperty("user.dir") + "/src/main/resources/static/uploads/");
			fileNameArray = absolutePath.split("/");
		}

		if (fileNameArray.length > 0){
			String fileName = fileNameArray[fileNameArray.length - 1];
			String filePath = "/api/uploads/" + fileName;
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "上传成功", filePath);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "上传失败", absolutePath);
		}
	}
	
	/**
	 * @Description 多文件上传
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "多文件上传", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "files", value = "文件集合", required = true, dataType = "MultipartFile[]") })
	@ApiResponses({ @ApiResponse(code = 1, message = "上传成功"), @ApiResponse(code = 0, message = "上传失败"),
		@ApiResponse(code = 3, message = "参数错误")  })
	@PostMapping("/uploadFiles")
	public ResponseData uploadFiles(HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
		Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
		if (null == fileMap || fileMap.size() == 0) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		String os = System.getProperty("os.name").toLowerCase();
		List<Map<String, String>> fileList = new ArrayList<>();
		for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile file = entry.getValue();
			String absolutePath = null;
			String[] fileNameArray = new String[]{};
			if (os.contains("windows")) {
				absolutePath = UploadUtils.saveMultipartFile(file, System.getProperty("user.dir") + "/src/main/resources/static/uploads");
				fileNameArray = absolutePath.split("\\\\");
			}else if (os.contains("linux")) {
				absolutePath = UploadUtils.saveMultipartFile(file, "/"+System.getProperty("user.dir") + "/src/main/resources/static/uploads/");
				fileNameArray = absolutePath.split("/");
			}
			if (fileNameArray.length > 0){
				String fileName = fileNameArray[fileNameArray.length - 1];
				String filePath = "/api/bus/uploads/" + fileName;
				Map<String, String> map = new HashMap<>();
				map.put("name", fileName);
				map.put("url", filePath);
				fileList.add(map);
			}
		}
		if (fileList.size() > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "上传成功", fileList);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "上传失败", null);
		}
	}
}
