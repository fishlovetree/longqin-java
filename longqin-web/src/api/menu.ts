import request from "@/utils/request";
// 菜单基础URL
const MENU_BASE_URL = "/menu";

class MenuAPI {
  /**
   * 获取菜单树形列表
   *
   * @param queryParams 查询参数
   * @returns 菜单树形列表
   */
  static getMenuTree() {
    return request<any, Menu[]>({
      url: `${MENU_BASE_URL}/getMenuTree`,
      method: "get",
    });
  }

  /**
   * 添加菜单
   *
   * @param data 菜单表单数据
   * @returns 请求结果
   */
  static add(data: Menu) {
    return request({
      url: `${MENU_BASE_URL}/create`,
      method: "post",
      data: data,
    });
  }

  /**
   * 修改菜单
   *
   * @param data 菜单表单数据
   * @returns 请求结果
   */
  static update(data: Menu) {
    return request({
      url: `${MENU_BASE_URL}/update`,
      method: "post",
      data: data,
    });
  }

  /**
   * 删除菜单
   *
   * @param id 菜单ID
   * @returns 请求结果
   */
  static deleteById(id: number) {
    return request({
      url: `${MENU_BASE_URL}/delete?menuId=${id}`,
      method: "post",
    });
  }
}

export default MenuAPI;

/** 菜单视图对象 */
export interface Menu {
  /** 子菜单 */
  children?: Menu[];
  /** 菜单路径 */
  menuUrl?: string;
  /** ICON */
  menuIcon?: string;
  /** 菜单ID */
  menuId?: number;
  /** 菜单名称 */
  menuName?: string;
  /** 父菜单ID */
  parentId?: number;
  /** 菜单排序(数字越小排名越靠前) */
  groupSeq?: number;
}
