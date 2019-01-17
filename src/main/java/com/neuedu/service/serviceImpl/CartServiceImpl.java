package com.neuedu.service.serviceImpl;

import com.google.common.collect.Lists;
import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.CartService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.vo.CartProductVo;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Override
    public ServerResponse add(Integer userId, Integer productId, Integer count) {

        //参数非空校验
        if(productId == null || count == null){
            return ServerResponse.responseIsError("参数不能为空");
        }
        //根据userId和productId查询购物车信息
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        //校验查询的购物车信息
        if(cart == null){
            //没有当前购物信息,增加购物车信息
            Cart cart1 = new Cart();
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(count);
            cart1.setChecked(ResponseCord.CartProductEnum.PRODUCT_ISCHECKED.getCode());
            int result = cartMapper.insert(cart1);
            if(result > 0){
                CartVO cartVO = getCartVO(cart1.getUserId());
                return ServerResponse.responseIsSuccess(null,cartVO);
            }else {
                return ServerResponse.responseIsError("添加购物车失败");
            }
        }else {
            //有当前购物车信息，更新购物车信息
            Cart cart1 = new Cart();
            cart1.setQuantity(count);
            cart1.setUserId(cart.getUserId());
            cart1.setProductId(cart.getProductId());
            cart1.setChecked(ResponseCord.CartProductEnum.PRODUCT_ISCHECKED.getCode());
            cart1.setId(cart.getId());
            //执行更新操作
            int result = cartMapper.updateByPrimaryKey(cart1);
            if(result > 0){
                CartVO cartVO = getCartVO(cart1.getUserId());
                return ServerResponse.responseIsSuccess(null,cartVO);
            }else {
                return ServerResponse.responseIsError("添加购物车失败");
            }
        }
    }

    /**
     * 获取CartVo对象
     */
    private CartVO getCartVO(Integer userId){
        CartVO cartVO = new CartVO();
        //根据userId查询购物车信息
        List<Cart> carts = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> productVoList = Lists.newArrayList();
        //定义购物车的总价格
        BigDecimal cartTotalPrice = new BigDecimal("0");
        //有购物车信息
        if(carts.size() > 0 && carts != null){
            for (Cart cart:carts) {
                CartProductVo cartProductVO = getCartProductVO(cart);
                System.out.println("总的价格："+cartProductVO.getProductTotalPrice());
                cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                productVoList.add(cartProductVO);
            }
        }
        cartVO.setCartProductVos(productVoList);
        //计算购物车的总价格
        cartVO.setCartTotalPrice(cartTotalPrice);
        //校验购物车中的商品是否全选----判断checked=0的字段
        int checkedAll = cartMapper.isCheckedAll(userId);
        if(checkedAll > 0){
            cartVO.setAllChecked(false);
        }else {
            cartVO.setAllChecked(true);
        }
        return cartVO;
    }
    /**
     * 将cart--->cartProductVO
     */
    private CartProductVo getCartProductVO(Cart cart){
        CartProductVo cartProductVo = new CartProductVo();
        cartProductVo.setId(cart.getId());
        cartProductVo.setProductId(cart.getProductId());
        cartProductVo.setUserId(cart.getUserId());
        cartProductVo.setProductChecked(cart.getChecked());
        //根据库存id获取库存信息
        Product product = productMapper.selectByPrimaryKey(cart.getProductId());
        //允许购买的最大数量
        int limitQuantity  = 0;
        if(product != null){
            //有库存信息
            cartProductVo.setProductMainImage(product.getMainImage());
            cartProductVo.setProductName(product.getName());
            cartProductVo.setProductPrice(product.getPrice());
            cartProductVo.setProductStatus(product.getStatus());
            cartProductVo.setProductStock(product.getStock());
            cartProductVo.setProductSubtitle(product.getSubtitle());

            //获取商品的库存
            int stock = product.getStock();
            if(cart.getQuantity() > stock){
                //商品库存不足
                limitQuantity = stock;
                //更新购物车中商品的数量
                cart.setQuantity(stock);
                cart.setUserId(cart.getUserId());
                cart.setProductId(cart.getProductId());
                cart.setChecked(ResponseCord.CartProductEnum.PRODUCT_ISCHECKED.getCode());
                cart.setId(cart.getId());
                //执行更新操作
                int result = cartMapper.updateByPrimaryKey(cart);
                cartProductVo.setLimitQuantity("LIMIT_NUM_FAIL ");

            }else {
                //有商品数
                limitQuantity = cart.getQuantity();
                cartProductVo.setLimitQuantity("LIMIT_NUM_SUCCESS");
            }
            System.out.println("购物车中的数量："+limitQuantity);
            cartProductVo.setQuantity(limitQuantity);
            System.out.println("产品价格："+product.getPrice());
            cartProductVo.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
        }
        return cartProductVo;
    }

    @Override
    public ServerResponse list(Integer userId) {
        List<Cart> carts = cartMapper.selectCartByUserId(userId);
        if(carts != null && carts.size()>0){
            CartVO cartVO = getCartVO(userId);
            return ServerResponse.responseIsSuccess(null,cartVO);
        }else {
            return ServerResponse.responseIsError("当前没有购物");
        }
    }

    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {
        //参数非空校验
        if(productId == null || count == null){
            return ServerResponse.responseIsError("参数不能为空");
        }
        //根据userId和productId查询购物车信息
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if(cart!=null){
            //更新数量
            cart.setQuantity(count);
            //更新改购物车
            int result = cartMapper.updateByPrimaryKey(cart);
        }else {
            return ServerResponse.responseIsError("购物车中，没有当前商品信息");
        }
        return ServerResponse.responseIsSuccess(null,getCartVO(userId));
    }

    @Override
    public ServerResponse update_cart(Integer userId, String productIds) {
        //参数非空校验
        if(productIds == null || productIds.equals("")){
            return ServerResponse.responseIsError("参数不能为空");
        }
        //将id转为list集合
        List<Integer> productIdList = Lists.newArrayList();
        String[] split = productIds.split(",");
        if(split.length>0&&split!=null){
            for(String pId:split){
                productIdList.add(Integer.parseInt(pId));
            }
        }
        //删除
        int result = cartMapper.deleteChartByUserIdAndProductList(userId, productIdList);
        if(result==0){
            return ServerResponse.responseIsError("删除失败");
        }
        return ServerResponse.responseIsSuccess(null,getCartVO(userId));
    }

    /**
     * 选中或取消某一件商品
     * @param userId
     * @param productId
     * @return
     */
    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer check) {
        //非空校验
        if(check == null){
            return ServerResponse.responseIsError("参数不能为空");
        }
        //调用dao层
        int result = cartMapper.checkedCart(userId, productId,check);
        if(result==0){
            return ServerResponse.responseIsError("操作有误");
        }
        return ServerResponse.responseIsSuccess(null,getCartVO(userId));
    }

    @Override
    public ServerResponse select_cart_product_count(Integer userId) {
        int count = cartMapper.select_cart_product_count(userId);
        return ServerResponse.responseIsSuccess(null,count);
    }
}
