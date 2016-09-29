package cn.wonhigh.retail.fas.api.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请写出类的用途 
 * @author user
 * @date  2016-09-12 18:14:08
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public class CustomerOrderRemain {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 批发订单号
     */
    private String orderNo;

    /**
     * 批发订单金额
     */
    private BigDecimal amount;

    /**
     * 批发出库单金额
     */
    private BigDecimal outAmount;

    /**
     * 批发出库单返利金额
     */
    private BigDecimal outRebateAmount;

    /**
     * 批发退货单金额
     */
    private BigDecimal returnAmount;

    /**
     * 关联扣项金额
     */
    private BigDecimal deductionAmount;

    /**
     * 关联收款单金额
     */
    private BigDecimal receiveAmount;

    /**
     * 订单余额
     */
    private BigDecimal remainingAmount;

    /**
     * 公司编码
     */
    private String companyNo;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 客户编码
     */
    private String customerNo;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 批发退货出库金额
     */
    private BigDecimal returnOutAmount;

    /**
     * 订单日期
     */
    private Date orderDate;

    /**
     * 冻结订单金额
     */
    private BigDecimal frozenOrderAmount;

    /**
     * 解冻金额
     */
    private BigDecimal unfreezeOrderAmount;

    /**
     * 订单类型(0-订货,1-现货,2-补货)
     */
    private Integer wholesaleOrderType;

    /**
     * 
     * {@linkplain #id}
     *
     * @return the value of customer_order_remain.id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * {@linkplain #id}
     * @param id the value for customer_order_remain.id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * {@linkplain #orderNo}
     *
     * @return the value of customer_order_remain.order_no
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 
     * {@linkplain #orderNo}
     * @param orderNo the value for customer_order_remain.order_no
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 
     * {@linkplain #amount}
     *
     * @return the value of customer_order_remain.amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 
     * {@linkplain #amount}
     * @param amount the value for customer_order_remain.amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 
     * {@linkplain #outAmount}
     *
     * @return the value of customer_order_remain.out_amount
     */
    public BigDecimal getOutAmount() {
        return outAmount;
    }

    /**
     * 
     * {@linkplain #outAmount}
     * @param outAmount the value for customer_order_remain.out_amount
     */
    public void setOutAmount(BigDecimal outAmount) {
        this.outAmount = outAmount;
    }

    /**
     * 
     * {@linkplain #outRebateAmount}
     *
     * @return the value of customer_order_remain.out_rebate_amount
     */
    public BigDecimal getOutRebateAmount() {
        return outRebateAmount;
    }

    /**
     * 
     * {@linkplain #outRebateAmount}
     * @param outRebateAmount the value for customer_order_remain.out_rebate_amount
     */
    public void setOutRebateAmount(BigDecimal outRebateAmount) {
        this.outRebateAmount = outRebateAmount;
    }

    /**
     * 
     * {@linkplain #returnAmount}
     *
     * @return the value of customer_order_remain.return_amount
     */
    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    /**
     * 
     * {@linkplain #returnAmount}
     * @param returnAmount the value for customer_order_remain.return_amount
     */
    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    /**
     * 
     * {@linkplain #deductionAmount}
     *
     * @return the value of customer_order_remain.deduction_amount
     */
    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    /**
     * 
     * {@linkplain #deductionAmount}
     * @param deductionAmount the value for customer_order_remain.deduction_amount
     */
    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    /**
     * 
     * {@linkplain #receiveAmount}
     *
     * @return the value of customer_order_remain.receive_amount
     */
    public BigDecimal getReceiveAmount() {
        return receiveAmount;
    }

    /**
     * 
     * {@linkplain #receiveAmount}
     * @param receiveAmount the value for customer_order_remain.receive_amount
     */
    public void setReceiveAmount(BigDecimal receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    /**
     * 
     * {@linkplain #remainingAmount}
     *
     * @return the value of customer_order_remain.remaining_amount
     */
    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    /**
     * 
     * {@linkplain #remainingAmount}
     * @param remainingAmount the value for customer_order_remain.remaining_amount
     */
    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    /**
     * 
     * {@linkplain #companyNo}
     *
     * @return the value of customer_order_remain.company_no
     */
    public String getCompanyNo() {
        return companyNo;
    }

    /**
     * 
     * {@linkplain #companyNo}
     * @param companyNo the value for customer_order_remain.company_no
     */
    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    /**
     * 
     * {@linkplain #companyName}
     *
     * @return the value of customer_order_remain.company_name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 
     * {@linkplain #companyName}
     * @param companyName the value for customer_order_remain.company_name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 
     * {@linkplain #customerNo}
     *
     * @return the value of customer_order_remain.customer_no
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 
     * {@linkplain #customerNo}
     * @param customerNo the value for customer_order_remain.customer_no
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 
     * {@linkplain #customerName}
     *
     * @return the value of customer_order_remain.customer_name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 
     * {@linkplain #customerName}
     * @param customerName the value for customer_order_remain.customer_name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 
     * {@linkplain #createTime}
     *
     * @return the value of customer_order_remain.create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 
     * {@linkplain #createTime}
     * @param createTime the value for customer_order_remain.create_time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     * {@linkplain #updateTime}
     *
     * @return the value of customer_order_remain.update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 
     * {@linkplain #updateTime}
     * @param updateTime the value for customer_order_remain.update_time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 
     * {@linkplain #returnOutAmount}
     *
     * @return the value of customer_order_remain.return_out_amount
     */
    public BigDecimal getReturnOutAmount() {
        return returnOutAmount;
    }

    /**
     * 
     * {@linkplain #returnOutAmount}
     * @param returnOutAmount the value for customer_order_remain.return_out_amount
     */
    public void setReturnOutAmount(BigDecimal returnOutAmount) {
        this.returnOutAmount = returnOutAmount;
    }

    /**
     * 
     * {@linkplain #orderDate}
     *
     * @return the value of customer_order_remain.order_date
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * 
     * {@linkplain #orderDate}
     * @param orderDate the value for customer_order_remain.order_date
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * 
     * {@linkplain #frozenOrderAmount}
     *
     * @return the value of customer_order_remain.frozen_order_amount
     */
    public BigDecimal getFrozenOrderAmount() {
        return frozenOrderAmount;
    }

    /**
     * 
     * {@linkplain #frozenOrderAmount}
     * @param frozenOrderAmount the value for customer_order_remain.frozen_order_amount
     */
    public void setFrozenOrderAmount(BigDecimal frozenOrderAmount) {
        this.frozenOrderAmount = frozenOrderAmount;
    }

    /**
     * 
     * {@linkplain #unfreezeOrderAmount}
     *
     * @return the value of customer_order_remain.unfreeze_order_amount
     */
    public BigDecimal getUnfreezeOrderAmount() {
        return unfreezeOrderAmount;
    }

    /**
     * 
     * {@linkplain #unfreezeOrderAmount}
     * @param unfreezeOrderAmount the value for customer_order_remain.unfreeze_order_amount
     */
    public void setUnfreezeOrderAmount(BigDecimal unfreezeOrderAmount) {
        this.unfreezeOrderAmount = unfreezeOrderAmount;
    }

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getWholesaleOrderType() {
		return wholesaleOrderType;
	}

	public void setWholesaleOrderType(Integer wholesaleOrderType) {
		this.wholesaleOrderType = wholesaleOrderType;
	}

}