import request from "@/utils/request";

// const AUTH_BASE_URL = "/api/v1/auth";
const FORM_BASE_URL = "/bus/desForm";

class DesformAPI {
  /** 新增表单接口*/
  static createForm(data: FormData) {
    return request({
      url: `${FORM_BASE_URL}/create`,
      method: "post",
      data: data,
    });
  }

  /** 修改表单接口*/
  static updateForm(data: FormData) {
    return request({
      url: `${FORM_BASE_URL}/update`,
      method: "post",
      data: data,
    });
  }

  /**
   * 获取表单分页列表
   *
   * @param queryParams 查询参数
   */
   static getPage(queryParams: FormPageQuery) {
    return request<any, PageResult<FormPageVO[]>>({
      url: `${FORM_BASE_URL}/getFormPage`,
      method: "get",
      params: queryParams,
    });
  }

  /**
   * 根据id获取表单信息
   *
   * @param id 表单ID
   */
   static getById(id: number) {
    return request<any, FormData>({
      url: `${FORM_BASE_URL}/getById?id=${id}`,
      method: "get",
    });
  }

  /**
   * 删除表单
   *
   * @param id 表单ID
   */
   static deleteById(id: number) {
    return request({
      url: `${FORM_BASE_URL}/delete?id=${id}`,
      method: "post",
    });
  }

  /**
   * 获取表单数据库列
   *
   * @param tableName 数据库表名
   */
   static getTableColumns(tableName: string) {
    return request<any, DesFormColumn>({
      url: `${FORM_BASE_URL}/getTableColumns?tableName=${tableName}`,
      method: "get",
    });
  }

  /**
   * 根据数据库表名获取表单信息
   *
   * @param tableName 数据库表名
   */
   static getByTableName(tableName: string) {
    return request<any, FormData>({
      url: `${FORM_BASE_URL}/getByTableName?tableName=${tableName}`,
      method: "get",
    });
  }
}

export default DesformAPI;

/** 保存表单请求参数 */
export interface FormData {
  /** 表单id */
  id?: number;
  /** 表单json */
  jsonData?: string;
  /** 数据库表名 */
  tableName?: string;
  /** 表单名 */
  formName?: string;
}

/**
 * 表单分页查询对象
 */
 export interface FormPageQuery extends PageQuery {
  /** 搜索关键字 */
  keywords?: string;
}

/** 表单分页对象 */
export interface FormPageVO {
  /** id */
  id?: string;
  /** 表单json */
  jsonData?: string;
  /** 数据库表名 */
  tableName?: string;
  /** 表单名 */
  formName?: string;
}

/** 表单数据库列 */
export interface DesFormColumn { 
  /** 字段名 */
  columnName?: string;
  /** 数据类型 */
  columnType?: string;
  /** 允许为空 */
  isNull?: string;
  /** 字段描述 */
  description?: string;
  /** 字段列宽 */
  width?:number;
  /** 搜索条件 */
  searchType?:string;
  /** 排序方式 */
  orderby?:string;
  /** 计算公式 */
  formula?:string;
  /** 公式值 */
  formulaValue?:string;
}
