import request from "@/utils/request";

const POSITION_BASE_URL = "/position";

class PositionAPI {
  /**
   * 获取职位列表
   *
   * @returns 职位列表
   */
  static getList() {
    return request<any, PositionVO[]>({
      url: `${POSITION_BASE_URL}/getPositionList`,
      method: "get",
    });
  }

  /**
   * 获取职位树形列表
   *
   * @returns 职位树形列表
   */
   static getTree() {
    return request<any, PositionTree[]>({
      url: `${POSITION_BASE_URL}/getPositionTree`,
      method: "get",
    });
  }

  /**
   * 新增职位
   *
   * @param data 职位表单数据
   * @returns 请求结果
   */
  static add(data: PositionForm) {
    return request({
      url: `${POSITION_BASE_URL}/create`,
      method: "post",
      data: data,
    });
  }

  /**
   * 修改职位
   *
   * @param data 职位表单数据
   * @returns 请求结果
   */
  static update(data: PositionForm) {
    return request({
      url: `${POSITION_BASE_URL}/update`,
      method: "post",
      data: data,
    });
  }

  /**
   * 删除职位
   *
   * @param id 职位ID
   * @returns 请求结果
   */
  static deleteById(id: number) {
    return request({
      url: `${POSITION_BASE_URL}/delete?positionId=${id}`,
      method: "post",
    });
  }
}

export default PositionAPI;

/** 职位类型 */
export interface PositionVO {
  /** 子职位 */
  children?: PositionVO[];
  /** 创建时间 */
  createTime?: Date;
  /** 职位D */
  positionId?: number;
  /** 职位名称 */
  positionName?: string;
  /** 父职位ID */
  parentId?: number;
  /** 职位等级：1-基层，2-中层，3-高层 */
  positionLevel?: number;
  /** 描述 */
  description?: string;
  /** 状态(1:启用；0:删除) */
  status?: number;
}

/** 职位树 */
export interface PositionTree {
  /** 子职位 */
  children?: PositionTree[];
  /** 职位ID */
  id?: number;
  /** 职位ID */
  positionId?: number;
  /** 职位名称 */
  positionName?: string;
  /** 职位名称 */
  title?: string;
  /** 父职位ID */
  parentId?: number;
  /** 职位等级：1-基层，2-中层，3-高层 */
  positionLevel?: number;
  /** 描述 */
  description?: string;
  /** 组织机构id */
  organizationId?: number;
}

/** 职位表单类型 */
export interface PositionForm {
  /** 职位ID(新增不填) */
  positionId?: number;
  /** 职位名称 */
  positionName?: string;
  /** 父职位ID */
  parentId?: number;
  /** 职位等级：1-基层，2-中层，3-高层 */
  positionLevel?: number;
  /** 描述 */
  description?: string;
  /** 所属公司 */
  organizationId?: number;
}
