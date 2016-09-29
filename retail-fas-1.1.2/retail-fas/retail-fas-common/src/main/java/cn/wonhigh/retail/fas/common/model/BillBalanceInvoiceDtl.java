package cn.wonhigh.retail.fas.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import cn.wonhigh.retail.fas.common.utils.JsonDateDeserialize$10;
import cn.wonhigh.retail.fas.common.utils.JsonDateSerializer$10;
import cn.wonhigh.retail.fas.common.utils.ShardingUtil;

/**
 * 请写出类的用途 
 * @author wangyj
 * @date  2015-01-08 11:08:00
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
public class BillBalanceInvoiceDtl extends FasBaseModel {

	private static final long serialVersionUID = 6008079654456642086L;

    /**
     * 分库字段
     */
    private String shardingFlag;
    
	/**
     * 开票申请单号
     */
    private String billNo;

    /**
     * 结算单号
     */
    private String balanceNo;

    /**
     * 结算起始日期
     */
    @JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserialize$10.class)
    private Date balanceStartDate;

    /**
     * 结算终止日期
     */
    @JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserialize$10.class)
    private Date balanceEndDate;

    /**
     * 品牌
     */
    private String brandName;

    /**
     * 品牌编号
     */
    private String brandNo;

    /**
     * 大类编号
     */
    private String categoryNo;

    /**
     * 大类
     */
    private String categoryName;

	/**
     * 管理城市编号
     */
    private String organNo;

    /**
     * 管理城市名称
     */
    private String organName;

    /**
     * 开票名称
     */
    private String salerName;

    /**
     * 数量
     */
    private Integer qty;

    /**
     * 金额
     */
    private BigDecimal sendAmount;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 不含税金额
     */
    private BigDecimal noTaxAmount;

    /**
     * 预估成本
     */
    private BigDecimal estimatedAmount;

    /**
     * 终端收入金额
     */
    private BigDecimal posEarningAmount;

    /**
     * 店铺简称
     */
    private String shortName;

    /**
     * 店铺全称
     */
    private String fullName;

    /**
     * 店铺编码
     */
    private String shopNo;

	 /**
     * 店铺小类 (销售类型(门店必填, A0:商场店中店 A1:商场独立店 A2:商场特卖店 A3:商场寄卖店 BJ:独立街边店 
	 BM:MALL B3:独立寄卖店, D0:批发加盟店 D1:批发团购店 D2:批发员购店 D3:批发调货店)
     */
    private String retailType;

    /**
     * 合同扣率
     */
    private BigDecimal contractRate;

    /**
     * 实际扣率
     */
    private BigDecimal actualRate;
    
    /**
     * 单据类型
     */
    private String balanceType;

	 /**
     * 销售明细成本汇总
     */
    private BigDecimal costTotal;

    /**
     * 备注，在销售明细成本有为0时，需要增加备注说明
     */
    private String remark;
    
    /**
     * 一级大类编码
     */
    private String cateNo;
    
    /**
     * 一级大类名称
     */
    private String cateName;
    
	/**
     * 公司编码(开票方)-卖方
     */
    private String salerNo;
    
    public String getShardingFlag() {
		return shardingFlag;
	}

	public void setShardingFlag(String shardingFlag) {
		this.shardingFlag = shardingFlag;
	}

    /**
     * 
     * {@linkplain #billNo}
     *
     * @return the value of bill_balance_invoice_dtl.bill_no
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * 
     * {@linkplain #billNo}
     * @param billNo the value for bill_balance_invoice_dtl.bill_no
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    /**
     * 
     * {@linkplain #balanceNo}
     *
     * @return the value of bill_balance_invoice_dtl.balance_no
     */
    public String getBalanceNo() {
        return balanceNo;
    }

    /**
     * 
     * {@linkplain #balanceNo}
     * @param balanceNo the value for bill_balance_invoice_dtl.balance_no
     */
    public void setBalanceNo(String balanceNo) {
        this.balanceNo = balanceNo;
    }

    /**
     * 
     * {@linkplain #balanceStartDate}
     *
     * @return the value of bill_balance_invoice_dtl.balance_start_date
     */
    public Date getBalanceStartDate() {
        return balanceStartDate;
    }

    /**
     * 
     * {@linkplain #balanceStartDate}
     * @param balanceStartDate the value for bill_balance_invoice_dtl.balance_start_date
     */
    public void setBalanceStartDate(Date balanceStartDate) {
        this.balanceStartDate = balanceStartDate;
    }

    /**
     * 
     * {@linkplain #balanceEndDate}
     *
     * @return the value of bill_balance_invoice_dtl.balance_end_date
     */
    public Date getBalanceEndDate() {
        return balanceEndDate;
    }

    /**
     * 
     * {@linkplain #balanceEndDate}
     * @param balanceEndDate the value for bill_balance_invoice_dtl.balance_end_date
     */
    public void setBalanceEndDate(Date balanceEndDate) {
        this.balanceEndDate = balanceEndDate;
    }

    /**
     * 
     * {@linkplain #brandName}
     *
     * @return the value of bill_balance_invoice_dtl.brand_Name
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 
     * {@linkplain #brandName}
     * @param brandName the value for bill_balance_invoice_dtl.brand_Name
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 
     * {@linkplain #brandNo}
     *
     * @return the value of bill_balance_invoice_dtl.brand_no
     */
    public String getBrandNo() {
        return brandNo;
    }

    /**
     * 
     * {@linkplain #brandNo}
     * @param brandNo the value for bill_balance_invoice_dtl.brand_no
     */
    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }

    /**
     * 
     * {@linkplain #categoryNo}
     *
     * @return the value of bill_balance_invoice_dtl.category_no
     */
    public String getCategoryNo() {
        return categoryNo;
    }

    /**
     * 
     * {@linkplain #categoryNo}
     * @param categoryNo the value for bill_balance_invoice_dtl.category_no
     */
    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo;
    }

    /**
     * 
     * {@linkplain #categoryName}
     *
     * @return the value of bill_balance_invoice_dtl.category_name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 
     * {@linkplain #categoryName}
     * @param categoryName the value for bill_balance_invoice_dtl.category_name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

	 /**
     * 
     * {@linkplain #organNo}
     *
     * @return the value of bill_balance_invoice_dtl.organ_no
     */
    public String getOrganNo() {
        return organNo;
    }

    /**
     * 
     * {@linkplain #organNo}
     * @param organNo the value for bill_balance_invoice_dtl.organ_no
     */
    public void setOrganNo(String organNo) {
        this.organNo = organNo;
    }

    /**
     * 
     * {@linkplain #organName}
     *
     * @return the value of bill_balance_invoice_dtl.organ_name
     */
    public String getOrganName() {
        return organName;
    }

    /**
     * 
     * {@linkplain #organName}
     * @param organName the value for bill_balance_invoice_dtl.organ_name
     */
    public void setOrganName(String organName) {
        this.organName = organName;
    }

    /**
     * 
     * {@linkplain #salerName}
     *
     * @return the value of bill_balance_invoice_dtl.saler_name
     */
    public String getSalerName() {
        return salerName;
    }

    /**
     * 
     * {@linkplain #salerName}
     * @param salerName the value for bill_balance_invoice_dtl.saler_name
     */
    public void setSalerName(String salerName) {
        this.salerName = salerName;
    }

    /**
     * 
     * {@linkplain #qty}
     *
     * @return the value of bill_balance_invoice_dtl.qty
     */
    public Integer getQty() {
        return qty;
    }

    /**
     * 
     * {@linkplain #qty}
     * @param qty the value for bill_balance_invoice_dtl.qty
     */
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    /**
     * 
     * {@linkplain #sendAmount}
     *
     * @return the value of bill_balance_invoice_dtl.send_amount
     */
    public BigDecimal getSendAmount() {
        return sendAmount;
    }

    /**
     * 
     * {@linkplain #sendAmount}
     * @param sendAmount the value for bill_balance_invoice_dtl.send_amount
     */
    public void setSendAmount(BigDecimal sendAmount) {
        this.sendAmount = sendAmount;
    }

    /**
     * 
     * {@linkplain #taxRate}
     *
     * @return the value of bill_balance_invoice_dtl.tax_rate
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    /**
     * 
     * {@linkplain #taxRate}
     * @param taxRate the value for bill_balance_invoice_dtl.tax_rate
     */
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * 
     * {@linkplain #taxAmount}
     *
     * @return the value of bill_balance_invoice_dtl.tax_amount
     */
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    /**
     * 
     * {@linkplain #taxAmount}
     * @param taxAmount the value for bill_balance_invoice_dtl.tax_amount
     */
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    /**
     * 
     * {@linkplain #noTaxAmount}
     *
     * @return the value of bill_balance_invoice_dtl.no_tax_amount
     */
    public BigDecimal getNoTaxAmount() {
        return noTaxAmount;
    }

    /**
     * 
     * {@linkplain #noTaxAmount}
     * @param noTaxAmount the value for bill_balance_invoice_dtl.no_tax_amount
     */
    public void setNoTaxAmount(BigDecimal noTaxAmount) {
        this.noTaxAmount = noTaxAmount;
    }

    /**
     * 
     * {@linkplain #estimatedAmount}
     *
     * @return the value of bill_balance_invoice_dtl.estimated_amount
     */
    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    /**
     * 
     * {@linkplain #estimatedAmount}
     * @param estimatedAmount the value for bill_balance_invoice_dtl.estimated_amount
     */
    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    /**
     * 
     * {@linkplain #posEarningAmount}
     *
     * @return the value of bill_balance_invoice_dtl.pos_earning_amount
     */
    public BigDecimal getPosEarningAmount() {
        return posEarningAmount;
    }

    /**
     * 
     * {@linkplain #posEarningAmount}
     * @param posEarningAmount the value for bill_balance_invoice_dtl.pos_earning_amount
     */
    public void setPosEarningAmount(BigDecimal posEarningAmount) {
        this.posEarningAmount = posEarningAmount;
    }

    /**
     * 
     * {@linkplain #shortName}
     *
     * @return the value of bill_balance_invoice_dtl.short_name
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 
     * {@linkplain #shortName}
     * @param shortName the value for bill_balance_invoice_dtl.short_name
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * 
     * {@linkplain #fullName}
     *
     * @return the value of bill_balance_invoice_dtl.full_name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 
     * {@linkplain #fullName}
     * @param fullName the value for bill_balance_invoice_dtl.full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 
     * {@linkplain #shopNo}
     *
     * @return the value of bill_balance_invoice_dtl.shop_no
     */
    public String getShopNo() {
        return shopNo;
    }

    /**
     * 
     * {@linkplain #shopNo}
     * @param shopNo the value for bill_balance_invoice_dtl.shop_no
     */
    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

	/**
     * 
     * {@linkplain #retailType}
     *
     * @return the value of bill_balance_invoice_dtl.retail_type
     */
    public String getRetailType() {
        return retailType;
    }

    /**
     * 
     * {@linkplain #retailType}
     * @param retailType the value for bill_balance_invoice_dtl.retail_type
     */
    public void setRetailType(String retailType) {
        this.retailType = retailType;
    }

    /**
     * 
     * {@linkplain #contractRate}
     *
     * @return the value of bill_balance_invoice_dtl.contract_rate
     */
    public BigDecimal getContractRate() {
        return contractRate;
    }

    /**
     * 
     * {@linkplain #contractRate}
     * @param contractRate the value for bill_balance_invoice_dtl.contract_rate
     */
    public void setContractRate(BigDecimal contractRate) {
        this.contractRate = contractRate;
    }

    /**
     * 
     * {@linkplain #actualRate}
     *
     * @return the value of bill_balance_invoice_dtl.actual_rate
     */
    public BigDecimal getActualRate() {
        return actualRate;
    }

    /**
     * 
     * {@linkplain #actualRate}
     * @param actualRate the value for bill_balance_invoice_dtl.actual_rate
     */
    public void setActualRate(BigDecimal actualRate) {
        this.actualRate = actualRate;
    }

	 /**
     * 
     * {@linkplain #costTotal}
     *
     * @return the value of bill_balance_invoice_dtl.cost_total
     */
    public BigDecimal getCostTotal() {
        return costTotal;
    }

    /**
     * 
     * {@linkplain #costTotal}
     * @param costTotal the value for bill_balance_invoice_dtl.cost_total
     */
    public void setCostTotal(BigDecimal costTotal) {
        this.costTotal = costTotal;
    }

    /**
     * 
     * {@linkplain #remark}
     *
     * @return the value of bill_balance_invoice_dtl.remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 
     * {@linkplain #remark}
     * @param remark the value for bill_balance_invoice_dtl.remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public String getCateNo() {
		return cateNo;
	}

	public void setCateNo(String cateNo) {
		this.cateNo = cateNo;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	
	/**
     * 
     * {@linkplain #salerNo}
     *
     * @return the value of bill_balance_invoice_dtl.saler_no
     */
    public String getSalerNo() {
        return salerNo;
    }

    /**
     * 
     * {@linkplain #salerNo}
     * @param salerNo the value for bill_balance_invoice_dtl.saler_no
     */
    public void setSalerNo(String salerNo) {
        this.salerNo = salerNo;
        setShardingFlag(ShardingUtil.getShardingFlagByCompanyNo(salerNo));
    }
}