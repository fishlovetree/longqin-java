package com.longqin.system.mapper;

import com.longqin.system.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    Menu selectById(Integer id);
	
    List<Menu> selectList();
    
    int updateStatus(Integer menuId);
    
    int updateByUrl(@Param(value="menuName")String menuName, @Param(value="menuUrl") String menuUrl);
    
    List<Menu> selectUserMenuList(@Param(value="userId")Integer userId, @Param(value="organizationId") Integer organizationId);
    
    int selectCountByName(@Param(value="menuName")String menuName, @Param(value="menuId") Integer menuId);
    
    int selectChildrenCount(@Param(value="parentId") Integer parentId);
}
