package com.neuedu.controller.portal;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台_商品
 */
@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    ProductService productService;
    /**
     * 查询商品的详情（根据商品id）
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(Integer productId){
        return productService.detail_portal(productId);
    }
    /**
     * 商品搜索和排序（根据类别id或关键字）
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(@RequestParam(required = false) Integer categoryId,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false,defaultValue = "") String  orderBy){
        return productService.list_portal(categoryId,keyword,pageNum,pageSize,orderBy);
    }
}
