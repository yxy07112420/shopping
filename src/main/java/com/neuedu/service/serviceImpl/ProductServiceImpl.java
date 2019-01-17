package com.neuedu.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.CategoryService;
import com.neuedu.service.ProductService;
import com.neuedu.utils.DateChangeUtils;
import com.neuedu.utils.FTPUtils;
import com.neuedu.utils.PropertisUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 商品service层的实现类
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    CategoryService categoryService;
    @Override
    public ServerResponse updateOrInsertProduct(Product product) {
        //参数校验
        if(product == null){
            return ServerResponse.responseIsError("商品信息不能为空");
        }
        //设置图片
        //获取详细图片信息
        String subImages = product.getSubImages();
        if(subImages != null && !subImages.equals("")){
            //将图片进行分组
            String[] split = subImages.split(",");
            if(split.length >0){
                //将第一张图给主图
                product.setMainImage(split[0]);
            }
        }
        //执行更新|新增商品信息操作
        if(product.getId() == null){
            //新增商品信息
            int result = productMapper.insert(product);
            if(result == 0){
                return ServerResponse.responseIsError("新增商品信息失败");
            }else {
                return ServerResponse.responseIsSuccess("新增商品信息成功");
            }
        }else {
            //修改商品信息
            int result = productMapper.updateByPrimaryKey(product);
            if(result == 0){
                return ServerResponse.responseIsError("修改商品信息失败");
            }else {
                return ServerResponse.responseIsSuccess("修改商品信息成功");
            }
        }
    }

    @Override
    public ServerResponse set_product_status(Integer productId, Integer status) {
        //参数非空校验
        if(productId == null || productId.equals("")){
            return ServerResponse.responseIsError("商品id不能为空");
        }
        if(status == null || status.equals("")){
            return ServerResponse.responseIsError("商品状态不能为空");
        }
        //执行更新语句
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int result = productMapper.updateProductByProductId(product);
        if(result == 0){
            return ServerResponse.responseIsError("修改商品信息失败");
        }else {
            return ServerResponse.responseIsSuccess("修改商品信息成功");
        }
    }

    /**
     * 根据商品的id查询商品的详细信息
     * @param productId
     * @return
     */
    @Override
    public ServerResponse detail(Integer productId) {
        //参数非空校验
        if(productId == null || productId.equals("")){
            return ServerResponse.responseIsError("商品编号不能为空");
        }
        //根据商品id查询商品信息
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.responseIsError("商品信息不存在");
        }
        //将product转为productDetailVo
        ProductDetailVO productDetailVO = productT0ProductDetailVo(product);
        if(productDetailVO == null){
            return ServerResponse.responseIsError("操作失败");
        }
        return ServerResponse.responseIsSuccess(null,productDetailVO);
    }

    /**
     *将product转为productDetailVo
     */
    private ProductDetailVO productT0ProductDetailVo(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateChangeUtils.dateToString(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateChangeUtils.dateToString(product.getUpdateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setName(product.getName());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setImageHost(PropertisUtils.readByKey("imageHost"));
        /**
         * 获取父类id
         */
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }
        return productDetailVO;
    }
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        //
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectAll();
        List<ProductListVO> productListVOS = new ArrayList<ProductListVO>();
        if(products != null && products.size()>0){
            for (Product p:products) {
                //将product转为productListVO
                ProductListVO productListVO = productToProductListVO(p);
                productListVOS.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOS);
        return ServerResponse.responseIsSuccess(null,pageInfo);
    }

    private ProductListVO productToProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }
    @Override
    public ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        //分页
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectProductByInOrName(productId, productName);
        if(products==null || products.size()==0){
            return ServerResponse.responseIsError("没有查询到数据");
        }
        List<ProductListVO> productListVOList = new ArrayList<ProductListVO>();
        for (Product p:products) {
            //将product转为productListVO
            ProductListVO productListVO = productToProductListVO(p);
            productListVOList.add(productListVO);
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.responseIsSuccess(null,pageInfo);
    }

    /**
     * 图片上传
     * @param file
     * @param path
     * @return
     */
    @Override
    public ServerResponse upload(MultipartFile file, String path) {

        //参数非空校验
        if(file == null || file.equals("")){
            return ServerResponse.responseIsError("没有图片上传");
        }
        //获取图片的名字
        String originalFilename = file.getOriginalFilename();
        System.out.println("图片名字："+originalFilename);
        if(originalFilename == null || originalFilename.equals("")){
            return ServerResponse.responseIsError("没有图片上传");
        }
        //获取图片的扩展名
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成新的名字
        String newName = UUID.randomUUID().toString()+substring;
        //判断图片路径是否存在
        File file1 = new File(path);
        if(file1 == null){
            file1.setWritable(true);
            file1.mkdir();
        }
        File file2 = new File(path,newName);
        //将文件写入到file2中
        try {
            file.transferTo(file2);
            //将图片上传到图片服务器
            FTPUtils.uploadFile(Lists.<File>newArrayList(file2));
            Map<String,String> map = new HashMap<String, String>();
            map.put("uri",newName);
            map.put("url",PropertisUtils.readByKey("imageHost")+newName);
            //c从本地删除应用服务器上的图片
            file2.delete();
            return ServerResponse.responseIsSuccess(null,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 前端查看商品的详细信息
     * @param productId
     * @return
     */
    @Override
    public ServerResponse detail_portal(Integer productId) {
        //参数非空校验
        if(productId == null || productId.equals("")){
            return ServerResponse.responseIsError("商品编号不能为空");
        }
        //根据商品id查询商品信息
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.responseIsError("商品信息不存在");
        }
        //判断商品状态
        if(product.getStatus() != ResponseCord.ProductEnum.PRODUCT_ONSTATUS.getCode()){
            return ServerResponse.responseIsError("该商品已下架或被删除");
        }
        //将product--->productDetailVO
        ProductDetailVO productDetailVO = productT0ProductDetailVo(product);
        //返回结果
        return ServerResponse.responseIsSuccess(null,productDetailVO);
    }

    /**
     * 前台商品搜索排序
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServerResponse list_portal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //参数非空校验：categoryId和leyWord不能同时为空
        if((categoryId == null||categoryId.equals(""))&&(keyword==null||keyword.equals(""))){
            return ServerResponse.responseIsError("参数错误");
        }
        Set<Integer> integerSet = Sets.newHashSet();
        //根据categoryId搜索商品
        if(categoryId!=null){
            //获取类别信息
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
//            if(category == null&&(keyword == null || keyword.equals(""))){
//               //说明没有商品数据
//                PageHelper.startPage(pageNum,pageSize);
//                List<ProductListVO> productListVOS = new ArrayList<ProductListVO>();
//                PageInfo pageInfo = new PageInfo(productListVOS);
//                return ServerResponse.responseIsSuccess(null,pageInfo);
//            }
            if(category == null){
                //说明没有商品数据
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOS = new ArrayList<ProductListVO>();
                PageInfo pageInfo = new PageInfo(productListVOS);
                return ServerResponse.responseIsSuccess(null,pageInfo);
            }
            ServerResponse deep_category = categoryService.get_deep_category(categoryId);
            if(deep_category.isSuccess()){
                integerSet =(Set<Integer>)deep_category.getDate();
            }
        }
        //根据keyword搜索商品
        if(keyword!=null && !keyword.equals("")){
            keyword="%"+keyword+"%";
        }
        //判断orderBy
        if(orderBy.equals("") || orderBy == null){
            PageHelper.startPage(pageNum,pageSize);
        }else {
            String[] s = orderBy.split("_");
            if(s.length>1){
                PageHelper.startPage(pageNum,pageSize,s[0]+" "+s[1]);
            }else {
                PageHelper.startPage(pageNum,pageSize);
            }
        }
        //将list<product>--->List<productListVO>
        List<Product> products = productMapper.selectProductPortal(integerSet, keyword);
        List<ProductListVO> productListVOList = new ArrayList<ProductListVO>();
        if(products!=null && products.size()>0){
            for (Product p:products) {
                //将product转为productListVO
                ProductListVO productListVO = productToProductListVO(p);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        //返回结果
        return ServerResponse.responseIsSuccess(null,pageInfo);
    }
}
