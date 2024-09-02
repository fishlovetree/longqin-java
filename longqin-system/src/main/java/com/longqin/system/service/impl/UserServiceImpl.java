package com.longqin.system.service.impl;

import com.longqin.system.entity.Department;
import com.longqin.system.entity.Position;
import com.longqin.system.entity.Role;
import com.longqin.system.entity.User;
import com.longqin.system.mapper.DepartmentMapper;
import com.longqin.system.mapper.PositionMapper;
import com.longqin.system.mapper.RoleMapper;
import com.longqin.system.mapper.UserMapper;
import com.longqin.system.service.IUserService;
import com.longqin.system.util.ExcelUtil;
import com.longqin.system.util.MD5Util;
import com.longqin.system.util.OperationLog;
import com.longqin.system.util.ResponseData;
import com.longqin.system.util.ResponseEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired
	PositionMapper positionMapper;
	
	@Autowired
	RoleMapper roleMapper;
	
	private final static String EXCEL2003 = "xls";
	private final static String EXCEL2007 = "xlsx";

	@Override
    public User getById(int id) {
        return userMapper.selectById(id);
    }

	@Override
    public User getByName(String name) {
        return userMapper.selectByName(name);
    }

	@Override
    public int getCountByName(String name, int id) {
        return userMapper.selectCountByName(name, id);
    }

	@Override
    public List<User> getPage(int organizationId, String departmentId, String nickName, int startIndex, int pageSize) {
        return userMapper.selectPage(organizationId, departmentId, nickName, startIndex, pageSize);
    }
    
	@Override
    public int getCount(int organizationId, String departmentId, String nickName) {
        return userMapper.selectCount(organizationId, departmentId, nickName);
    }

	@OperationLog(title = "删除账号", content = "'账号id：' + #id", operationType = "1")
	@Override
    public int delete(int id) throws Exception {
        return userMapper.updateStatus(id);
    }

	@OperationLog(title = "修改密码", content = "'账号id：' + #id", operationType = "2")
	@Override
    public int updatePassword(int id, String password) throws Exception {
        return userMapper.updatePassword(MD5Util.MD5(password), id);
    }

	@OperationLog(title = "添加账号", content = "'账号名称：' + #entity.getUserName()", operationType = "0")
	@Override
    public int insert(User entity) throws Exception {
    	int count = userMapper.selectCountByName(entity.getUserName(), 0);
		if (count > 0) {
			// 账号已存在
			return -2;
		}
		entity.setPassword(MD5Util.MD5(entity.getPassword()));

        int result = userMapper.insert(entity);
        if (result > 0){
        	setRole(entity.getUserId(), entity.getRoleIds());
        }
        return result;
    }

	@OperationLog(title = "修改账号", content = "'账号名称：' + #entity.getUserName()", operationType = "2")
	@Override
    public int update(User entity) throws Exception {
    	int count = userMapper.selectCountByName(entity.getUserName(), entity.getUserId());
		if (count > 0) {
			// 账号已存在
			return -2;
		}
        int result = userMapper.updateById(entity);
        if (result > 0){
        	setRole(entity.getUserId(), entity.getRoleIds());
        }
        return result;
    }

	@Override
    public List<Integer> getRoles(int userId) {
        return userMapper.selectUserRoles(userId);
    }

	@OperationLog(title = "设置账号角色", content = "'账号id：' + #userId + ', 角色id：' + #roleIds", operationType = "7")
	@Override
    public int setRole(int userId, String roleIds) throws Exception {
    	int result = userMapper.deleteUserRole(userId);
		if (roleIds != null && roleIds.length() > 0) {
			List<Integer> roleIdArray = Arrays.asList(roleIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		    result = userMapper.insertUserRole(userId, roleIdArray);
		}
		return result;
    }
	
	/**
	 *  解决下拉框过长不显示问题
	 * @param workbook
	 * @param deptList 下拉数据数组
	 * @param sheet
	 * @param firstRow 开始行 
	 * @param endRow 结束行
	 * @param cellNum 下拉框所在的列
	 * @param sheetIndex 隐藏sheet序号
	 */
	private static void setLongHSSFValidation(XSSFWorkbook workbook, String[] deptList, Sheet sheet, int firstRow, int endRow, int cellNum, int sheetIndex) {				
		String hiddenName = "hidden" + cellNum;
		// 1.创建隐藏的sheet页。 
		Sheet hidden = workbook.createSheet(hiddenName);
		// 2.循环赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
		for (int i = 0, length = deptList.length; i < length; i++) {
			hidden.createRow(i).createCell(0).setCellValue(deptList[i]);
		}
		Name category1Name = workbook.createName();
		category1Name.setNameName(hiddenName);
		// 3 A1:A代表隐藏域创建第N列createCell(N)时。以A1列开始A行数据获取下拉数组
		category1Name.setRefersToFormula(hiddenName + "!" + "$A$1:$A$" + deptList.length);

		// 四个参数分别是：起始行、终止行、起始列、终止列
		DataValidationHelper helper = sheet.getDataValidationHelper();
		DataValidationConstraint constraint = helper.createFormulaListConstraint(hiddenName);
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, cellNum, cellNum);
		DataValidation dataValidation = helper.createValidation(constraint, regions);
		if (dataValidation instanceof XSSFDataValidation) {
			// 数据校验
			dataValidation.setSuppressDropDownArrow(true);
			dataValidation.setShowErrorBox(true);
		} else {
			dataValidation.setSuppressDropDownArrow(false);
		}
		// 作用在目标sheet上
		sheet.addValidationData(dataValidation);
		// 设置hiddenSheet隐藏
		workbook.setSheetHidden(sheetIndex, true);
	}
	
	/**
	 * @Title exportTemplate
	 * @param organizationId 数据权限
	 * @return
	 * @Description 导出用户导入模板
	 * @Author hxl
	 * @Date 2024年8月9日
	 */
	@Override
	public void exportTemplate(Integer organizationId){
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();

        // 设置列宽    
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);
        sheet.setColumnWidth(5, 6000);
        sheet.setColumnWidth(6, 6000);

        // 设置字体    
        XSSFFont headfont = workbook.createFont();
        headfont.setFontName("黑体");
        headfont.setFontHeight((short)400);

        // 另一个样式    
        XSSFCellStyle headstyle = workbook.createCellStyle();
        headstyle.setFont(headfont);
        headstyle.setAlignment(HorizontalAlignment.CENTER);
        headstyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
        headstyle.setLocked(true);
        headstyle.setWrapText(true);// 自动换行 

        // 另一个字体样式    
        XSSFFont columnHeadFont = workbook.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeight((short)220);

        // 列标题的样式    
        XSSFCellStyle columnHeadStyle = workbook.createCellStyle();
        columnHeadStyle.setFont(columnHeadFont);
        columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中    
        columnHeadStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);// 上下居中    
        columnHeadStyle.setLocked(true);
        columnHeadStyle.setWrapText(true);
        columnHeadStyle.setLeftBorderColor(IndexedColors.BLACK.index);
        columnHeadStyle.setBorderLeft(BorderStyle.THIN);
        columnHeadStyle.setRightBorderColor(IndexedColors.BLACK.index);
        columnHeadStyle.setBorderRight(BorderStyle.THIN);
        columnHeadStyle.setTopBorderColor(IndexedColors.BLACK.index);
        columnHeadStyle.setBorderTop(BorderStyle.THIN);
        columnHeadStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        columnHeadStyle.setBorderBottom(BorderStyle.THIN);

        XSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeight((short)220);

        // 另一个样式    
        XSSFCellStyle centerstyle = workbook.createCellStyle();
        centerstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        centerstyle.setFillForegroundColor(IndexedColors.WHITE.index);
        centerstyle.setFont(font);
        centerstyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中    
        centerstyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);// 上下居中   
        centerstyle.setWrapText(true);
        centerstyle.setLeftBorderColor(IndexedColors.BLACK.index);
        centerstyle.setBorderLeft(BorderStyle.THIN);
        centerstyle.setRightBorderColor(IndexedColors.BLACK.index);
        centerstyle.setBorderRight(BorderStyle.THIN);
        centerstyle.setTopBorderColor(IndexedColors.BLACK.index);
        centerstyle.setBorderTop(BorderStyle.THIN);
        centerstyle.setBottomBorderColor(IndexedColors.BLACK.index);
        centerstyle.setBorderBottom(BorderStyle.THIN);

        // handling header.  
        XSSFRow row0 = sheet.createRow(0);
        row0.setHeight((short)300);
        XSSFCell cell = row0.createCell(0);
        cell.setCellValue("账号名");
        cell.setCellStyle(columnHeadStyle);

        cell = row0.createCell(1);
        cell.setCellValue("昵称");
        cell.setCellStyle(columnHeadStyle);

        cell = row0.createCell(2);
        cell.setCellValue("手机号");
        cell.setCellStyle(columnHeadStyle);

        cell = row0.createCell(3);
        cell.setCellValue("邮箱");
        cell.setCellStyle(columnHeadStyle);
        
        cell = row0.createCell(4);
        cell.setCellValue("所属部门");
        cell.setCellStyle(columnHeadStyle);

        cell = row0.createCell(5);
        cell.setCellValue("职位");
        cell.setCellStyle(columnHeadStyle);
        
        cell = row0.createCell(6);
        cell.setCellValue("角色");
        cell.setCellStyle(columnHeadStyle);
        
        int firstRow = 1;
        int endRow = 500;

		// ----------------------设计列数据格式：下拉框---------------
        // 获取部门列表
        List<Department> deptList = departmentMapper.selectList(organizationId);
		if(deptList.size() > 0) {
			String[] depts = new String[deptList.size()];
			int index = 0;
			for (Department department : deptList) {
				String name = department.getDepartmentName() + "(" + department.getDepartmentId() + ")";
				depts[index] = name;
				index++;
			}
	        setLongHSSFValidation(workbook, depts, sheet, firstRow, endRow, 4, 1);
		}
		
		// 获取职位列表
        List<Position> positionList = positionMapper.selectList(organizationId);
		if(positionList.size() > 0) {
			String[] positions = new String[positionList.size()];
			int index = 0;
			for (Position position : positionList) {
				String name = position.getPositionName() + "(" + position.getPositionId() + ")";
				positions[index] = name;
				index++;
			}
	        setLongHSSFValidation(workbook, positions, sheet, firstRow, endRow, 5, 2);
		}
		
		// 获取角色列表
        List<Role> roleList = roleMapper.selectList(organizationId);
		if(roleList.size() > 0) {
			String[] roles = new String[roleList.size()];
			int index = 0;
			for (Role role : roleList) {
				String name = role.getRoleName() + "(" + role.getRoleId() + ")";
				roles[index] = name;
				index++;
			}
	        setLongHSSFValidation(workbook, roles, sheet, firstRow, endRow, 6, 3);
		}
        
		try {
			
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletResponse response = sra.getResponse();
			String fileName = "用户导入模板.xlsx";

            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @Title importUser
	 * @param organizationId 数据权限
	 * @param file 数据文件
	 * @return
	 * @Description 导入用户
	 * @Author hxl
	 * @Date 2024年8月9日
	 */
	@Override
	public ResponseData importUser(Integer organizationId, MultipartFile file){
		String fileName = file.getOriginalFilename();//通过文件导入
		if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "Excel文件格式不正确", null);
		} 
		List<User> userList = new ArrayList<User>();
		Workbook workbook = null;
		try {
			InputStream is = file.getInputStream();//通过文件导入

			ZipSecureFile.setMinInflateRatio(-1.0d);
			if (fileName.endsWith(EXCEL2007)) {
				workbook = new XSSFWorkbook(is);
			}
			if (fileName.endsWith(EXCEL2003)) {
				workbook = new HSSFWorkbook(is);
			}
			
			String[] fields = { "账号名", "昵称", "手机号", "邮箱", "所属部门", "职位", "角色"};
			Map<Integer, String> reflectionMap = new HashMap<>();
			
			if (workbook != null) {
				// 默认读取第一个sheet
				Sheet sheet = workbook.getSheetAt(0);
				
				for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
					Row row = sheet.getRow(i);
					// 首行 提取表头
					if (i == 0) {
						// 检查表头列是否齐全
						for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
							Cell cell = row.getCell(j);
							String cellValue = ExcelUtil.getCellValue(cell);
							// 得到Field 对象对应的列序号
							if (ArrayUtils.contains(fields, cellValue)) {
								reflectionMap.put(j, cellValue);
							}
						}
						if(fields.length > reflectionMap.size()) {
							return new ResponseData(ResponseEnum.ERROR.getCode(), "检查表格列数", null);
						}
					} else {
						User user = new User();
						user.setOrganizationId(organizationId);
						
						Cell cell = row.getCell(0);
						String value = ExcelUtil.getCellValue(cell);
						if (StringUtil.isEmpty(value)){
							return new ResponseData(ResponseEnum.ERROR.getCode(), "第" + i + "行的账号名为空", null);
						}
						user.setUserName(value);
						
						cell = row.getCell(1);
						value = ExcelUtil.getCellValue(cell);
						user.setNickName(value);
						
						cell = row.getCell(2);
						value = ExcelUtil.getCellValue(cell);
						user.setPhone(value);
						
						cell = row.getCell(3);
						value = ExcelUtil.getCellValue(cell);
						user.setEmail(value);
						
						cell = row.getCell(4);
						value = ExcelUtil.getCellValue(cell);
						if (!StringUtil.isEmpty(value)){
							int index = value.indexOf("(");
							if (index >=0 ){
								String id = String.valueOf(value.charAt(index + 1));
								user.setDepartmentId(Integer.parseInt(id));
							}
						}
						
						cell = row.getCell(5);
						value = ExcelUtil.getCellValue(cell);
						if (!StringUtil.isEmpty(value)){
							int index = value.indexOf("(");
							if (index >=0 ){
								String id = String.valueOf(value.charAt(index + 1));
								user.setPositionId(Integer.parseInt(id));
							}
						}
						
						cell = row.getCell(6);
						value = ExcelUtil.getCellValue(cell);
						if (!StringUtil.isEmpty(value)){
							int index = value.indexOf("(");
							if (index >=0 ){
								String id = String.valueOf(value.charAt(index + 1));
								user.setRoleIds(id);
							}
						}
						userList.add(user);
						
						User exist = userMapper.selectByName(user.getUserName());
						if (exist != null){
							user.setUserId(exist.getUserId());
							update(user);
						}
						else{
							user.setPassword("11111111");
							insert(user);
						}
					}
				}
			}
		} catch (Exception e) {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "读取Excel文件时，抛出异常：" + e.getMessage(), null);
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					return new ResponseData(ResponseEnum.ERROR.getCode(), "结束读取Excel文件时，抛出异常：" + e.getMessage(), null);
				}
			}
		}
		
		if(userList.size() == 0){
			return new ResponseData(ResponseEnum.ERROR.getCode(), "Excel文件为空", null);
		} else {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "导入成功");
		}
	}
	
	/**
	 * @Title export
	 * @param organizationId 数据权限
	 * @return
	 * @Description 导出用户
	 * @Author hxl
	 * @Date 2024年8月9日
	 */
	@Override
	public void export(Integer organizationId, String departmentId, String nickName){
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();

        // 设置列宽    
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);
        sheet.setColumnWidth(5, 6000);

        // 设置字体    
        XSSFFont headfont = workbook.createFont();
        headfont.setFontName("黑体");
        headfont.setFontHeight((short)400);

        // 另一个样式    
        XSSFCellStyle headstyle = workbook.createCellStyle();
        headstyle.setFont(headfont);
        headstyle.setAlignment(HorizontalAlignment.CENTER);
        headstyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
        headstyle.setLocked(true);
        headstyle.setWrapText(true);// 自动换行 

        // 另一个字体样式    
        XSSFFont columnHeadFont = workbook.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeight((short)220);

        // 列标题的样式    
        XSSFCellStyle columnHeadStyle = workbook.createCellStyle();
        columnHeadStyle.setFont(columnHeadFont);
        columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中    
        columnHeadStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);// 上下居中    
        columnHeadStyle.setLocked(true);
        columnHeadStyle.setWrapText(true);
        columnHeadStyle.setLeftBorderColor(IndexedColors.BLACK.index);
        columnHeadStyle.setBorderLeft(BorderStyle.THIN);
        columnHeadStyle.setRightBorderColor(IndexedColors.BLACK.index);
        columnHeadStyle.setBorderRight(BorderStyle.THIN);
        columnHeadStyle.setTopBorderColor(IndexedColors.BLACK.index);
        columnHeadStyle.setBorderTop(BorderStyle.THIN);
        columnHeadStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        columnHeadStyle.setBorderBottom(BorderStyle.THIN);

        XSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeight((short)220);

        // 另一个样式    
        XSSFCellStyle centerstyle = workbook.createCellStyle();
        centerstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        centerstyle.setFillForegroundColor(IndexedColors.WHITE.index);
        centerstyle.setFont(font);
        centerstyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中    
        centerstyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);// 上下居中   
        centerstyle.setWrapText(true);
        centerstyle.setLeftBorderColor(IndexedColors.BLACK.index);
        centerstyle.setBorderLeft(BorderStyle.THIN);
        centerstyle.setRightBorderColor(IndexedColors.BLACK.index);
        centerstyle.setBorderRight(BorderStyle.THIN);
        centerstyle.setTopBorderColor(IndexedColors.BLACK.index);
        centerstyle.setBorderTop(BorderStyle.THIN);
        centerstyle.setBottomBorderColor(IndexedColors.BLACK.index);
        centerstyle.setBorderBottom(BorderStyle.THIN);

        // handling header.  
        XSSFRow row0 = sheet.createRow(0);
        row0.setHeight((short)300);
        XSSFCell cell = row0.createCell(0);
        cell.setCellValue("账号名");
        cell.setCellStyle(columnHeadStyle);

        cell = row0.createCell(1);
        cell.setCellValue("昵称");
        cell.setCellStyle(columnHeadStyle);

        cell = row0.createCell(2);
        cell.setCellValue("手机号");
        cell.setCellStyle(columnHeadStyle);

        cell = row0.createCell(3);
        cell.setCellValue("邮箱");
        cell.setCellStyle(columnHeadStyle);
        
        cell = row0.createCell(4);
        cell.setCellValue("所属部门");
        cell.setCellStyle(columnHeadStyle);

        cell = row0.createCell(5);
        cell.setCellValue("职位");
        cell.setCellStyle(columnHeadStyle);
        
        int rowIndex = 1;
        List<User> list = userMapper.selectPage(organizationId, departmentId, nickName, 0, 500);
        for (int i = 0; i < list.size(); i++)
        {
        	User item = list.get(i);
            XSSFRow dataRow = sheet.createRow(rowIndex);
            dataRow.setHeight((short)300);

            cell = dataRow.createCell(0);
            cell.setCellValue(item.getUserName());
            cell.setCellStyle(centerstyle);

            cell = dataRow.createCell(1);
            cell.setCellValue(item.getNickName());
            cell.setCellStyle(centerstyle);

            cell = dataRow.createCell(2);
            cell.setCellValue(item.getPhone());
            cell.setCellStyle(centerstyle);

            cell = dataRow.createCell(3);
            cell.setCellValue(item.getEmail());
            cell.setCellStyle(centerstyle);
            
            cell = dataRow.createCell(4);
            cell.setCellValue(item.getDepartmentName());
            cell.setCellStyle(centerstyle);
            
            cell = dataRow.createCell(5);
            cell.setCellValue(item.getPositionName());
            cell.setCellStyle(centerstyle);
            
            rowIndex++;
        }

		try {
			
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletResponse response = sra.getResponse();
			SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");// 获取日期
			String date = df.format(new Date());
			String fileName = "用户列表" + date + ".xlsx";

            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
    public int getUserPositionLevel(int userId) {
        return userMapper.selectUserPositionLevel(userId);
    }

	// 获取流程处理人
	@Override
    public List<Integer> getFlowHandlers(Integer userId, Integer departmentId, Integer positionId, int submitterId) {
        List<Integer> handlers = new ArrayList<Integer>();
        User submitter = userMapper.selectById(submitterId);
        // 先判断节点是否配置指定用户
        if (userId != null && userId != 0) {
            handlers.add(userId);
        }
        else if (positionId != null && positionId != 0) {
            if (departmentId != null && departmentId != 0) {
                // 指定部门和职位
                handlers = userMapper.selectUserIdByDeptAndPosition(departmentId, positionId);
            }
            else {
                // 只指定了职位,根据用户所属部门向上逐级查找
            	handlers = getUserByPosition(positionId, submitter.getDepartmentId());
            }
        }
        else if (departmentId != null && departmentId != 0) {
            // 只指定了部门,根据用户职位向上逐级查找
        	handlers = getUserByDepartment(submitter.getPositionId(), departmentId);
        }
        else {
            // 啥都没指定
        	handlers = getUserRecursion(submitter.getPositionId(), submitter.getDepartmentId());
        }
        return handlers;
    }

    // 指定职位，根据用户所属部门向上逐级查找
    private List<Integer> getUserByPosition(int positionId, int departmentId) {
    	List<Integer> handlers = userMapper.selectUserIdByDeptAndPosition(departmentId, positionId);
        if (handlers == null || handlers.size() == 0) {
            Department dept = departmentMapper.selectById(departmentId);
            int parentId = dept.getParentId();
            if (parentId != -1) {
                // 递归获取处理人
            	handlers = getUserByPosition(positionId, parentId);
            }
        }
        return handlers;
    }

    // 指定部门，根据用户所属职位向上逐级查找
    private List<Integer> getUserByDepartment(int positionId, int departmentId) {
    	List<Integer> handlers = new ArrayList<>();
        Position position = positionMapper.selectById(positionId);
        int parentId = position.getParentId();
        if (parentId != -1) {
        	handlers = userMapper.selectUserIdByDeptAndPosition(departmentId, parentId);
            if (handlers == null || handlers.size() == 0) {
                // 递归获取处理人
            	handlers = getUserByDepartment(parentId, departmentId);
            }
        }
        return handlers;
    }

    // 未指定部门和职位，根据用户所属职位和部门向上逐级查找
    private List<Integer> getUserRecursion(int positionId, int departmentId) {
    	List<Integer> handlers = new ArrayList<>();
        Position position = positionMapper.selectById(positionId);
        if (position != null && position.getParentId() != -1) {
            int parentId = position.getParentId();
            handlers = userMapper.selectUserIdByDeptAndPosition(departmentId, parentId);
            if (handlers == null || handlers.size() == 0) {
                // 递归部门获取处理人
            	handlers = getUserByPosition(parentId, departmentId);
                if (handlers == null || handlers.size() == 0) {
                    // 递归职位和部门获取处理人
                	handlers = getUserRecursion(parentId, departmentId);
                }
            }
        }
        return handlers;
    }
}
