import request from "@/utils/request";

const ORGANIZATION_BASE_URL = "/organization";

class OrganizationAPI {

  /** 根据id获取公司数据 */
  static getById(organizationId: number) {
    return request<any, PageResult<OrganizationForm[]>>({
      url: `${ORGANIZATION_BASE_URL}/getById?orgId=${organizationId}`,
      method: "get",
    });
  }

  /** 获取公司分页数据 */
  static getPage(queryParams?: OrganizationPageQuery) {
    return request<any, PageResult<OrganizationPageVO[]>>({
      url: `${ORGANIZATION_BASE_URL}/getOrganizationPage`,
      method: "get",
      params: queryParams,
    });
  }

  /** 获取公司下拉数据源 */
  static getList() {
    return request<any, OrganizationPageVO[]>({
      url: `${ORGANIZATION_BASE_URL}/getOrganizationList`,
      method: "get",
    });
  }

  /** 添加公司 */
  static add(data: OrganizationForm) {
    return request({
      url: `${ORGANIZATION_BASE_URL}/create`,
      method: "post",
      data: data,
    });
  }

  /**
   * 更新公司
   *
   * @param id 公司ID
   * @param data 角色表单数据
   */
  static update(data: OrganizationForm) {
    return request({
      url: `${ORGANIZATION_BASE_URL}/update`,
      method: "post",
      data: data,
    });
  }

  /**
   * 删除公司
   *
   * @param organizationId 公司ID
   */
  static deleteById(organizationId: number) {
    return request({
      url: `${ORGANIZATION_BASE_URL}/delete?organizationId=${organizationId}`,
      method: "post",
    });
  }
}

export default OrganizationAPI;

/** 公司分页查询参数 */
export interface OrganizationPageQuery extends PageQuery {
  /** 搜索关键字 */
  keywords?: string;
}

/** 公司分页对象 */
export interface OrganizationPageVO {
  /** 公司编码 */
  organizationCode?: string;
  /** 公司ID */
  organizationId?: number;
  /** 公司名称 */
  organizationName?: string;
  /** 地址 */
  address?: string;
  /** 电话 */
  phone?: string;
  /** logo */
  logoPath?: string;
  /** 系统名称 */
  systemName?: string;
  /** 简介 */
  introduction?: string;
  /** 公司状态 */
  status?: number;
  /** 创建时间 */
  createTime?: Date;
}

/** 公司表单对象 */
export interface OrganizationForm {
  /** 公司编码 */
  organizationCode?: string;
  /** 公司ID */
  organizationId?: number;
  /** 公司名称 */
  organizationName?: string;
  /** 地址 */
  address?: string;
  /** 电话 */
  phone?: string;
  /** logo */
  logoPath?: string;
  /** 系统名称 */
  systemName?: string;
  /** 简介 */
  introduction?: string;
}
