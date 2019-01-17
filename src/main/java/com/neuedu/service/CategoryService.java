package com.neuedu.service;

import com.neuedu.common.ServerResponse;

/**
 * 类别业务层
 */
public interface CategoryService {
    /**
     * 查询某一类别的子类（不含有后代）
     */
    ServerResponse get_category(Integer categoryId);

    /**
     * 添加节点
     */
    ServerResponse add_category(Integer parentId,String categoryName);
    /**
     * 修改节点
     */
    ServerResponse set_category_name(Integer categoryId,String categoryName);
    /**
     * 查询节点和他的后代节点
     */
    ServerResponse get_deep_category(Integer categoryId);
}
