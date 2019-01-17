package com.neuedu.service.serviceImpl;

import com.google.common.collect.Sets;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public ServerResponse get_category(Integer categoryId) {
        //参数非空校验
        if(categoryId == null){
            return ServerResponse.responseIsError("类别不能为空");
        }
        //根据该类别查询是否存在该类别
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category == null){
            return  ServerResponse.responseIsError("该类别不存在");
        }
        //查询子类别
        List<Category> categories = categoryMapper.selectChildCategoryByCategoryId(categoryId);
        return ServerResponse.responseIsSuccess(null,categories);
    }

    /**
     * 添加节点
     * @param parentId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
        //参数非空校验
        if(parentId == null || parentId.equals("")){
            return ServerResponse.responseIsError("父节点编号不能为空");
        }
        if(categoryName == null || categoryName.equals("")){
            return ServerResponse.responseIsError("节点姓名不能为空");
        }
        //判断该节点是否存在
        int nameIsExists = categoryMapper.selectCategoryInfoByCategoryName(categoryName);
        if(nameIsExists > 0 ){
            return ServerResponse.responseIsError("该类别名已存在");
        }
        //添加节点
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(1);
        int insert = categoryMapper.insert(category);
        if(insert == 0){
            return ServerResponse.responseIsError("添加失败");
        }
        return ServerResponse.responseIsSuccess("添加成功");
    }

    /**
     * 修改节点信息
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {
        //参数非空校验
        if(categoryId == null || categoryId.equals("")){
            return ServerResponse.responseIsError("节点编号不能为空");
        }
        if(categoryName == null || categoryName.equals("")){
            return ServerResponse.responseIsError("节点姓名不能为空");
        }
        //根据类别id校验当前类别是否存在
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category == null){
            return ServerResponse.responseIsError("该类别不存在");
        }
        //修改类别信息
        category.setName(categoryName);
        int updateCount = categoryMapper.updateByPrimaryKey(category);
        if(updateCount == 0){
            return ServerResponse.responseIsError("修改失败");
        }
        return ServerResponse.responseIsSuccess("修改成功");
    }

    /**
     * 查询节点和他的后代节点信息
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //参数非空校验
        if(categoryId == null || categoryId.equals("")){
            return ServerResponse.responseIsError("节点编号不能为空");
        }
        //递归调用
        Set<Category> categorySet = Sets.newHashSet();
        categorySet = getChildCategory(categorySet, categoryId);
        Set<Integer> integerSet = Sets.newHashSet();
        Iterator<Category> iterator = categorySet.iterator();
        while (iterator.hasNext()){
            Category next = iterator.next();
            integerSet.add(next.getId());
        }

        return ServerResponse.responseIsSuccess(null,integerSet);
    }
    //递归获取节点信息
    private Set<Category> getChildCategory(Set<Category>  categorySet,Integer categoryId){
        //获取当前节点信息
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        //查找子节点信息（平级）
        List<Category> categories = categoryMapper.selectChildCategoryByCategoryId(categoryId);
        if(categories!=null && categories.size()>0){
            for (Category c :categories) {
                getChildCategory(categorySet,c.getId());
            }
        }
        return categorySet;
    }

}
