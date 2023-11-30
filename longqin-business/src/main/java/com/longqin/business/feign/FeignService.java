package com.longqin.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.longqin.business.entity.Errorlog;
import com.longqin.business.entity.Log;
import com.longqin.business.util.ResponseData;

/**
 * feign远程接口
 */
@FeignClient(value = "longqin-system", fallback = FeignServiceFallback.class)
public interface FeignService {

	/**
	 * @Description 记录日志
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@RequestMapping(value = "/log/addLog", method = RequestMethod.POST, headers = {
			"servie-key=longqin-business", "request-uri=/log/addLog"})
	public ResponseData addLog(@RequestBody Log log);

	/**
	 * @Description 记录异常日志
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@RequestMapping(value = "/errorlog/addException", method = RequestMethod.POST, headers = {
			"servie-key=longqin-business", "request-uri=/errorlog/addException"})
	public ResponseData addException(@RequestBody Errorlog errorlog);
	
	/**
	 * @Description 获取用户职级
	 * @Author longqin
	 * @Time: 2023年11月03日
	 */
	@RequestMapping(value = "/user/getUserPositionLevel", method = RequestMethod.GET, headers = {
			"servie-key=longqin-business", "request-uri=/user/getUserPositionLevel"})
	public ResponseData getUserPositionLevel(@RequestParam(value = "userId") Integer userId);
	
	/**
	 * @Description 获取流程处理人
	 * @Author longqin
	 * @Time: 2023年11月03日
	 */
	@RequestMapping(value = "/user/getFlowHandlers", method = RequestMethod.GET, headers = {
			"servie-key=longqin-business", "request-uri=/user/getFlowHandlers"})
	public ResponseData getFlowHandlers(@RequestParam(value = "userId") Integer userId, 
			@RequestParam(value = "departmentId") Integer departmentId, @RequestParam(value = "positionId") Integer positionId,
			@RequestParam(value = "submitterId") Integer submitterId);
	
	/**
	 * @Description 生成菜单
	 * @Author longqin
	 * @Time: 2023年11月28日
	 */
	@RequestMapping(value = "/menu/diyTableMenu", method = RequestMethod.POST, headers = {
			"servie-key=longqin-business", "request-uri=/menu/diyTableMenu"})
	public ResponseData diyTableMenu(@RequestParam(value = "menuName") String menuName, @RequestParam(value = "menuUrl") String menuUrl,
			@RequestParam(value = "groupSeq") Integer groupSeq, @RequestParam(value = "isEdit") boolean isEdit);
}
