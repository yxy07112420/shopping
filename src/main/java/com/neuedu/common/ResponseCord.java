package com.neuedu.common;

/**
 * 状态码
 */
public class ResponseCord {

    public static final Integer SUCCESS = 0;
    public static final Integer ERROR = 1;
    public static final String CURRENTUSER="current_user";
    public static final String TRADE_SUCCESS="TRADE_SUCCESS";
    //定义用户权限（枚举类型）
    public enum UserEnum{
        USER_ADMIN(0,"管理员"),
        USER_COMMON(1,"普通用户")
        ;
        private Integer code;
        private String desc;

        private UserEnum(){

        }
        private UserEnum(Integer code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    //定义商品状态（枚举类型）
    public enum ProductEnum{
        PRODUCT_ONSTATUS(1,"在售"),
        PRODUCT_OFFSTATUS(2,"下架"),
        PRODUCT_DELETE(3,"删除")
        ;
        private Integer code;
        private String desc;

        private ProductEnum(){

        }
        private ProductEnum(Integer code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    //定义购物车是否选中状态（枚举类型）
    public enum CartProductEnum {
        PRODUCT_ISCHECKED(1,"已选中"),
        PRODUCT_NOCHECKED(0,"未选中")
        ;
        private Integer code;
        private String desc;
        private CartProductEnum(){

        }
        private CartProductEnum(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    /**
     * 定义订单的支付状态
     */
    public enum OrderPayEnum {
        ORDER_CANCEL(0,"已取消"),
        ORDER_NOPAY(10,"未付款"),
        ORDER_ACCOUNTPAID(20,"已付款"),
        ORDER_SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"交易成功"),
        ORDER_CLOSE(60,"交易关闭")
        ;
        private Integer code;
        private String desc;
        private OrderPayEnum(){

        }
        private OrderPayEnum(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public  static  OrderPayEnum codeOf(Integer code){
           for (OrderPayEnum orderPayEnum:values()){
               if (code == orderPayEnum.getCode()){
                   return orderPayEnum;
               }
           }
           return null;
        }
    }
    /**
     * 定义支付订单的方式
     */
    public enum OrderPayFormEnum {
        ORDER_ONLINE(1,"在线支付"),
        ;
        private Integer code;
        private String desc;
        private OrderPayFormEnum(){

        }
        private OrderPayFormEnum(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public  static  OrderPayFormEnum codeOf(Integer code){
            for (OrderPayFormEnum orderPayFormEnum:values()){
                if (code == orderPayFormEnum.getCode()){
                    return orderPayFormEnum;
                }
            }
            return null;
        }
    }
    /**
     * 定义支付订单的方式
     */
    public enum PayFormEnum {
        PAY_FROM_ALIPAY(1,"支付宝"),
        ;
        private Integer code;
        private String desc;
        private PayFormEnum(){

        }
        private PayFormEnum(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public  static PayFormEnum codeOf(Integer code){
            for (PayFormEnum payFormEnum:values()){
                if (code == payFormEnum.getCode()){
                    return payFormEnum;
                }
            }
            return null;
        }
    }
}

