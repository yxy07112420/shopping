package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


/**
 * 商品service层接口
 */
public interface ProductService {
    /**
     * 新增|更新商品信息
     * @param product
     * @return
     */
    ServerResponse updateOrInsertProduct(Product product);

    /**
     * 商品上下架
     * @param productId
     * @param status
     * @return
     */
    ServerResponse set_product_status(Integer productId,Integer status);

    /**
     * 根据商品id查看商品的详细信息
     * @param productId
     * @return
     */
    ServerResponse detail(Integer productId);
    /**
     * 分页查询商品信息
     */
    ServerResponse list(Integer pageNum,Integer pageSize);

    /**
     * 根据商品名或商品id查询商品信息
     * @param productId
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse search(Integer productId,String productName,Integer pageNum,Integer pageSize);

    /**
     * 图片上传
     * @param file
     * @param path
     * @return
     */
    ServerResponse upload(MultipartFile file,String path);

    /**
     * 前端查看商品的详细信息
     * @param productId
     * @return
     */
    ServerResponse detail_portal(Integer productId);

    /**
     *前台——商品搜索排序
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    ServerResponse list_portal(Integer categoryId,String keyword,Integer pageNum, Integer pageSize, String  orderBy);
}
