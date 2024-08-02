package com.longqin.business.service.impl;

import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.DesFormColumn;
import com.longqin.business.entity.DesFormData;
import com.longqin.business.mapper.DesFormMapper;
import com.longqin.business.service.IDesFormService;
import com.longqin.business.util.OperationLog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author longqin
 * @since 2023-10-31
 */
@Service
public class DesFormServiceImpl extends ServiceImpl<DesFormMapper, DesForm> implements IDesFormService {

	@Autowired
	DesFormMapper desFormMapper;
	
	@OperationLog(title = "创建表单", content = "'表单名称：' + #entity.getFormName()", operationType = "0")
	@Override
    public int create(DesForm entity) throws Exception {
		String tableName = "des_" + entity.getTableName() + "_" + entity.getCreator() + "_" + entity.getOrganizationId();
    	entity.setTableName(tableName);
		int count = desFormMapper.selectCountByTableName(tableName, 0);
		if (count > 0) {
			// 表名已存在
			return -2;
		}
    	JSONObject jsonObj = JSONObject.parseObject(entity.getJsonData());
    	JSONArray jsonArray = jsonObj.getJSONArray("widgetList");
    	if (jsonArray == null){
    		return -1;
    	}
    	StringBuilder columns = new StringBuilder();
    	for(int i = 0; i < jsonArray.size(); i++){
            JSONObject obj = (JSONObject)jsonArray.get(i);
            String columnType = "";
            String type = obj.getString("type");
            JSONObject options = obj.getJSONObject("options");
            switch (type)
            {
                case "input":
                    columnType = "varchar(100)";
                    break;
                case "textarea":
                    columnType = "varchar(510)";
                    break;
                case "editor":
                    columnType = "text";
                    break;
                case "upload":
                    columnType = "varchar(200)";
                    break;
                case "tags":
                    columnType = "varchar(200)";
                    break;
                case "tips":
                case "note":
                case "subtraction":
                case "tab":
                case "grid":
                case "space":
                    continue;
                default:
                    columnType = "varchar(50)";
                    break;
            }
            String isNull = "true".equals(options.getString("required")) ? "NOT NULL" : "NULL DEFAULT NULL";
            columns.append("`").append(options.getString("name")).append("` ").append(columnType)
            .append(" CHARACTER SET utf8 COLLATE utf8_general_ci ").append(isNull).append(" COMMENT '")
            .append(options.getString("label")).append("',");
    	}
    	int result = desFormMapper.createFormTable(tableName, columns.toString(), entity.getFormName());
    	if (result >= 0){
    		result = desFormMapper.insert(entity);
    	}
    	return result;
    }
	
	@OperationLog(title = "修改表单", content = "'表单名称：' + #entity.getFormName()", operationType = "2")
	@Override
    public int update(DesForm entity) throws Exception {
		int count = desFormMapper.selectCountByTableName(entity.getTableName(), entity.getId());
		if (count > 0) {
			// 表名已存在
			return -2;
		}
		// 若数据库表已存在，旧表置为备份表
		int tableCount = desFormMapper.selectTableCount(entity.getTableName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String datetime = sdf.format(new Date());
		if (tableCount > 0){
			desFormMapper.renameTable(entity.getTableName(), entity.getTableName() + "_bak" + datetime);
		}
		JSONArray jsonArray = JSONArray.parseArray(entity.getJsonData());
    	StringBuilder columns = new StringBuilder();
    	for(int i = 0; i < jsonArray.size(); i++){
            JSONObject obj = (JSONObject)jsonArray.get(i);
            String columnType = "";
            String tag = obj.getString("tag");
            switch (tag)
            {
                case "input":
                    columnType = "varchar(100)";
                    break;
                case "textarea":
                    columnType = "varchar(510)";
                    break;
                case "editor":
                    columnType = "text";
                    break;
                case "upload":
                    columnType = "varchar(200)";
                    break;
                case "tags":
                    columnType = "varchar(200)";
                    break;
                case "tips":
                case "note":
                case "subtraction":
                case "tab":
                case "grid":
                case "space":
                    continue;
                default:
                    columnType = "varchar(50)";
                    break;
            }
            String isNull = "true".equals(obj.getString("required")) ? "NOT NULL" : "NULL DEFAULT NULL";
            columns.append("`").append(obj.getString("name")).append("` ").append(columnType)
            .append(" CHARACTER SET utf8 COLLATE utf8_general_ci ").append(isNull).append(" COMMENT '")
            .append(obj.getString("label")).append("',");
    	}
    	int result = desFormMapper.createFormTable(entity.getTableName(), columns.toString(), entity.getFormName());
    	if (result >= 0){
    		result = desFormMapper.updateById(entity);
    	}
    	return result;
    }
	
	@OperationLog(title = "删除表单", content = "'表单id：' + #id", operationType = "1")
	@Override
    public int delete(int id) throws Exception {
		return desFormMapper.deleteForm(id);
    }
	
	@Override
    public DesForm getById(int id) {
        return desFormMapper.selectById(id);
    }

	@Override
    public List<DesForm> getPage(int organizationId, int startIndex, int pageSize) {
        return desFormMapper.selectPage(organizationId, startIndex, pageSize);
    }
    
	@Override
    public int getCount(int organizationId) {
        return desFormMapper.selectCount(organizationId);
    }
	
	// 根据数据库表名获取字段集合
	@Override
    public List<DesFormColumn> getTableColumns(String tableName) {
        List<DesFormColumn> columns = desFormMapper.selectTableColumns(tableName);
        if (columns != null && columns.size() != 0) {
            DesFormColumn creatorName = new DesFormColumn();
            creatorName.setColumnName("creatorName");
            creatorName.setDescription("创建者名称");
            columns.add(creatorName);
        }
        return columns;
    }
	
	@Override
	public int insertFormData(DesFormData entity){
		return desFormMapper.insertFormData(entity);
	}
	
	@Override
	public int updateFormData(String tableName, List<String> columns, List<String> vals, Integer id){
		if (null == columns || columns.size() == 0 || null == vals || columns.size() != vals.size()){
			return -2; //参数错误
		}
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < columns.size(); i++){
			String column = columns.get(i);
			String val = vals.get(i);
			sb.append(column + "=" + val + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return desFormMapper.updateFormData(tableName, sb.toString(), id);
	}
}
