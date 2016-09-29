package cn.wonhigh.retail.fas.common.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import cn.wonhigh.retail.fas.common.enums.BillTypeEnums;
import cn.wonhigh.retail.fas.common.enums.FasBillCodeEnums;
import cn.wonhigh.retail.fas.common.enums.GmsBillStatusEnums;
import cn.wonhigh.retail.fas.common.model.FasBaseModel;
import cn.wonhigh.retail.fas.common.utils.BigDecimalSerializer$2;
import cn.wonhigh.retail.fas.common.utils.JsonDateSerializer$10;

public class BillBuyBalanceAdditionDto extends FasBaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2791357519202068715L;

	/**
	 * 主键ID
	 */
	private String id;

	/**
	 * FAS单据类型
	 */
	private String fasBillCode;

	/**
	 * FAS单据类型名称
	 */
	private String fasBillName;

	/**
	 * 单据编号
	 */
	private String billNo;

	/**
	 * 单据类型(1301-到货单,1333-调货出库单,1335-客残销售出库单)
	 */
	private Integer billType;
	/**
	 * 业务类型(0：订货 ，1：补货 ，2直接)
	 */
	private Integer bizType;

	/**
	 * 相关单号
	 */
	private String refBillNo;

	/**
	 * 原单编号
	 */
	private String originalBillNo;

	/**
	 * 相关单据类型
	 */
	private Integer refBillType;

	/**
	 * 单据状态
	 */
	private Integer status;

	/**
	 * 买方编号
	 */
	private String buyerNo;

	/**
	 * 买方名称
	 */
	private String buyerName;

	/**
	 * 卖方编号
	 */
	private String salerNo;

	/**
	 * 卖方名称
	 */
	private String salerName;

	/**
	 * 发出日期
	 */
	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date sendDate;

	/**
	 * 接收日期
	 */
	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date receiveDate;

	/**
	 * 发出地编号
	 */
	private String sendStoreNo;

	/**
	 * 发出地名称
	 */
	private String sendStoreName;

	/**
	 * 收货地编号
	 */
	private String receiveStoreNo;

	/**
	 * 收货地名称
	 */
	private String receiveStoreName;

	/**
	 * 明细id
	 */
	private String skuId;

	/**
	 * SKU编号
	 */
	private String skuNo;

	/**
	 * 供应商编号
	 */
	private String supplierNo;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 商品编号
	 */
	private String itemNo;

	/**
	 * 商品编码
	 */
	private String itemCode;

	/**
	 * 商品名称
	 */
	private String itemName;

	/**
	 * 品牌编号
	 */
	private String brandNo;

	/**
	 * 品牌名称
	 */
	private String brandName;

	/**
	 * 品牌部编号
	 */
	private String brandUnitNo;

	/**
	 * 品牌部名称
	 */
	private String brandUnitName;

	/**
	 * 大类编号
	 */
	private String categoryNo;

	/**
	 * 大类名称
	 */
	private String categoryName;

	/**
	 * 一级大类
	 */
	private String firstLevelCategoryNo;

	/**
	 * 一级大类名称
	 */
	private String firstLevelCategoryName;

	/**
	 * 二级大类
	 */
	private String secondLevelCategoryNo;

	/**
	 * 二级大类名称
	 */
	private String secondLevelCategoryName;

	/**
	 * 含税单价(结算价格、默认原单据价格)
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal cost;

	/**
	 * 单据价格(原单据价格)
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal billCost;

	/**
	 * 平均成本
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal averageCost;

	/**
	 * 实际出库价格(费用分摊后的价格)
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal actualCost;

	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal otherDeductCost;

	/**
	 * 税率
	 */
	private BigDecimal taxRate;

	/**
	 * 不含税单价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal exclusiveCost;

	/**
	 * 总部成本
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal headquarterCost;

	/**
	 * 卖方地区价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal regionCostSecond;

	/**
	 * 单位成本
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal unitCost;

	/**
	 * 卖方单位成本
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal unitCostSecond;

	/**
	 * 采购价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal purchasePrice;

	/**
	 * 采购本位币金额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal standardPurchasePrice;

	/**
	 * 本位币采购价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal standardPurchaseAmount;

	public BigDecimal getStandardPurchasePrice() {
		return standardPurchasePrice;
	}

	public void setStandardPurchasePrice(BigDecimal standardPurchasePrice) {
		this.standardPurchasePrice = standardPurchasePrice;
	}

	public BigDecimal getStandardPurchaseAmount() {
		return standardPurchaseAmount;
	}

	public void setStandardPurchaseAmount(BigDecimal standardPurchaseAmount) {
		this.standardPurchaseAmount = standardPurchaseAmount;
	}

	public BigDecimal getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(BigDecimal baseCost) {
		this.baseCost = baseCost;
	}

	/**
	 * 总部价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal baseCost;
	/**
	 * 物料价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal materialPrice;

	/**
	 * 厂进价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal factoryPrice;

	/**
	 * 核定价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal aprice;

	/**
	 * OA超额状态
	 */
	private String excessStatus;

	/**
	 * OA超额状态
	 */
	private String excessStatusName;
	/**
	 * 采购额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal purchaseAmount;

	/**
	 * 发出数量
	 */
	private Integer sendQty;

	/**
	 * 接受数量
	 */
	private Integer receiveQty;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 结算单号
	 */
	private String balanceNo;

	/**
	 * 是否拆分单据
	 */
	private Integer isSplit;

	/**
	 * 结算状态
	 */
	private Integer balanceStatus;

	/**
	 * 结算类型
	 */
	private Integer balanceType;

	/**
	 * 结算路径编码
	 */
	private String pathNo;

	/** 发出方订货单位编码 */
	private String orderUnitNoFrom;

	/** 发出方订货单位名称 */
	private String orderUnitNameFrom;

	/** 发出方管理城市编号 */
	private String organNoFrom;

	/** 发出方管理城市名称 */
	private String organNameFrom;

	/** 发出方所属大区编码 */
	private String zoneNoFrom;

	/** 发出方所属大区名称 */
	private String zoneNameFrom;

	/** 接受方订货单位编码 */
	private String orderUnitNo;

	/** 接受方订货单位名称 */
	private String orderUnitName;

	/** 接受方管理城市编号 */
	private String organNo;

	/** 接受方管理城市名称 */
	private String organName;

	/** 接收方所属大区编码 */
	private String zoneNo;

	/** 接收方所属大区名称 */
	private String zoneName;

	/** 年份 */
	private String years;

	/**
	 * 年份名
	 */
	private String yearsName;

	/** 季节 */
	private String season;

	/**
	 * 季节名
	 */
	private String seasonName;

	/** 订货形式 */
	private String orderfrom;

	/** 性别 */
	private String gender;

	/** 牌价 */
	private BigDecimal tagPrice;

	/** 全国统一牌价 */
	private BigDecimal tagPriceNation;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 日期范围
	 */
	private String receiveDateStr;

	/**
	 * 建档人
	 */
	private String createUser;

	/**
	 * 索赔编号
	 */
	private String claimNo;

	/**
	 * 退货编号
	 */
	private String returnNo;

	/* ##############扩展属性#################### */

	/**
	 * 发出金额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal sendAmount;

	/**
	 * 接收金额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal receiveAmount;

	/**
	 * 差异金额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal differenceAmount;

	/**
	 * 单据类型显示名称
	 */
	private String billTypeName;

	/**
	 * 异常原因
	 */
	private String exceptionName;

	/**
	 * 单据状态名称
	 */
	private String statusName;

	/**
	 * 差异数量
	 */
	private Integer differenceQty;

	/**
	 * 一级大类
	 */
	private String oneLevelCategoryNo;

	/**
	 * 一级大类名称
	 */
	private String oneLevelCategoryName;

	/**
	 * 二级大类
	 */
	private String twoLevelCategoryNo;

	/**
	 * 二级大类名称
	 */
	private String twoLevelCategoryName;

	/**
	 * 异常价格是否已更新
	 */
	private Integer costChecked;

	/**
	 * 结算公司
	 */
	private String companyNo;

	/**
	 * 结算公司
	 */
	private String companyName;

	/**
	 * 性别名称
	 */
	private String genderName;

	/**
	 * 供应商组名称
	 */
	private String supplierGroupName;

	/**
	 * 订货类型(期货;现货)
	 */
	private String orderType;
	
	private String styleNo;
	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	/**
	 * 结算号
	 */
	private String settlementNumber;

	private Integer total;

	public String getSettlementNumber() {
		return settlementNumber;
	}

	public void setSettlementNumber(String settlementNumber) {
		this.settlementNumber = settlementNumber;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getExcessStatus() {
		return excessStatus;
	}

	public void setExcessStatus(String excessStatus) {
		this.excessStatus = excessStatus;
	}

	public BigDecimal getAprice() {
		return aprice;
	}

	public void setAprice(BigDecimal aprice) {
		this.aprice = aprice;
	}

	public String getExcessStatusName() {
		return excessStatusName;
	}

	public void setExcessStatusName(String excessStatusName) {
		this.excessStatusName = excessStatusName;
	}

	public BigDecimal getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(BigDecimal purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

	public String getSupplierGroupName() {
		return supplierGroupName;
	}

	public void setSupplierGroupName(String supplierGroupName) {
		this.supplierGroupName = supplierGroupName;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getTagPrice() {
		return tagPrice;
	}

	public void setTagPrice(BigDecimal tagPrice) {
		this.tagPrice = tagPrice;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getOrderfrom() {
		return orderfrom;
	}

	public void setOrderfrom(String orderfrom) {
		this.orderfrom = orderfrom;
	}

	public String getZoneNoFrom() {
		return zoneNoFrom;
	}

	public void setZoneNoFrom(String zoneNoFrom) {
		this.zoneNoFrom = zoneNoFrom;
	}

	public String getZoneNameFrom() {
		return zoneNameFrom;
	}

	public void setZoneNameFrom(String zoneNameFrom) {
		this.zoneNameFrom = zoneNameFrom;
	}

	public String getZoneNo() {
		return zoneNo;
	}

	public void setZoneNo(String zoneNo) {
		this.zoneNo = zoneNo;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getOrderUnitNoFrom() {
		return orderUnitNoFrom;
	}

	public void setOrderUnitNoFrom(String orderUnitNoFrom) {
		this.orderUnitNoFrom = orderUnitNoFrom;
	}

	public String getOrderUnitNameFrom() {
		return orderUnitNameFrom;
	}

	public void setOrderUnitNameFrom(String orderUnitNameFrom) {
		this.orderUnitNameFrom = orderUnitNameFrom;
	}

	public String getOrganNoFrom() {
		return organNoFrom;
	}

	public void setOrganNoFrom(String organNoFrom) {
		this.organNoFrom = organNoFrom;
	}

	public String getOrganNameFrom() {
		return organNameFrom;
	}

	public void setOrganNameFrom(String organNameFrom) {
		this.organNameFrom = organNameFrom;
	}

	public String getOrderUnitNo() {
		return orderUnitNo;
	}

	public void setOrderUnitNo(String orderUnitNo) {
		this.orderUnitNo = orderUnitNo;
	}

	public String getOrderUnitName() {
		return orderUnitName;
	}

	public void setOrderUnitName(String orderUnitName) {
		this.orderUnitName = orderUnitName;
	}

	public String getOrganNo() {
		return organNo;
	}

	public void setOrganNo(String organNo) {
		this.organNo = organNo;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getPathNo() {
		return pathNo;
	}

	public void setPathNo(String pathNo) {
		this.pathNo = pathNo;
	}

	public String getOneLevelCategoryNo() {
		return oneLevelCategoryNo;
	}

	public void setOneLevelCategoryNo(String oneLevelCategoryNo) {
		this.oneLevelCategoryNo = oneLevelCategoryNo;
	}

	public String getOneLevelCategoryName() {
		return oneLevelCategoryName;
	}

	public void setOneLevelCategoryName(String oneLevelCategoryName) {
		this.oneLevelCategoryName = oneLevelCategoryName;
	}

	public String getTwoLevelCategoryNo() {
		return twoLevelCategoryNo;
	}

	public void setTwoLevelCategoryNo(String twoLevelCategoryNo) {
		this.twoLevelCategoryNo = twoLevelCategoryNo;
	}

	public String getTwoLevelCategoryName() {
		return twoLevelCategoryName;
	}

	public void setTwoLevelCategoryName(String twoLevelCategoryName) {
		this.twoLevelCategoryName = twoLevelCategoryName;
	}

	public String getBrandUnitName() {
		return brandUnitName;
	}

	public void setBrandUnitName(String brandUnitName) {
		this.brandUnitName = brandUnitName;
	}

	public String getOriginalBillNo() {
		return originalBillNo;
	}

	public void setOriginalBillNo(String originalBillNo) {
		this.originalBillNo = originalBillNo;
	}

	public BigDecimal getDifferenceAmount() {
		return differenceAmount;
	}

	public void setDifferenceAmount(BigDecimal differenceAmount) {
		this.differenceAmount = differenceAmount;
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public Integer getDifferenceQty() {
		return differenceQty;
	}

	public void setDifferenceQty(Integer differenceQty) {
		this.differenceQty = differenceQty;
	}

	public Integer getBalanceStatus() {
		return balanceStatus;
	}

	public void setBalanceStatus(Integer balanceStatus) {
		this.balanceStatus = balanceStatus;
	}

	public BigDecimal getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public String getStatusName() {
		if (null != status) {
			return GmsBillStatusEnums.getTextByStatus(status);
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * 
	 * {@linkplain #billType}
	 * 
	 * @return the value of bill_buy_balance.bill_type
	 */
	public Integer getBillType() {
		return billType;
	}

	/**
	 * 
	 * {@linkplain #billType}
	 * 
	 * @param billType
	 *            the value for bill_buy_balance.bill_type
	 */
	public void setBillType(Integer billType) {
		this.billType = billType;
		if (null != billType) {
			setBillTypeName(BillTypeEnums.getNameByNo(billType));
		}
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	/**
	 * 
	 * {@linkplain #refBillNo}
	 * 
	 * @return the value of bill_buy_balance.ref_bill_no
	 */
	public String getRefBillNo() {
		return refBillNo;
	}

	/**
	 * 
	 * {@linkplain #refBillNo}
	 * 
	 * @param refBillNo
	 *            the value for bill_buy_balance.ref_bill_no
	 */
	public void setRefBillNo(String refBillNo) {
		this.refBillNo = refBillNo;
	}

	/**
	 * 
	 * {@linkplain #refBillType}
	 * 
	 * @return the value of bill_buy_balance.ref_bill_type
	 */
	public Integer getRefBillType() {
		return refBillType;
	}

	/**
	 * 
	 * {@linkplain #refBillType}
	 * 
	 * @param refBillType
	 *            the value for bill_buy_balance.ref_bill_type
	 */
	public void setRefBillType(Integer refBillType) {
		this.refBillType = refBillType;
	}

	/**
	 * 
	 * {@linkplain #status}
	 * 
	 * @return the value of bill_buy_balance.status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 
	 * {@linkplain #status}
	 * 
	 * @param status
	 *            the value for bill_buy_balance.status
	 */
	public void setStatus(Integer status) {
		this.status = status;
		if (null != status) {
			this.statusName = GmsBillStatusEnums.getTextByStatus(status);
		}
	}

	/**
	 * 
	 * {@linkplain #buyerNo}
	 * 
	 * @return the value of bill_buy_balance.buyer_no
	 */
	public String getBuyerNo() {
		return buyerNo;
	}

	/**
	 * 
	 * {@linkplain #buyerNo}
	 * 
	 * @param buyerNo
	 *            the value for bill_buy_balance.buyer_no
	 */
	public void setBuyerNo(String buyerNo) {
		this.buyerNo = buyerNo;
	}

	/**
	 * 
	 * {@linkplain #buyerName}
	 * 
	 * @return the value of bill_buy_balance.buyer_name
	 */
	public String getBuyerName() {
		return buyerName;
	}

	/**
	 * 
	 * {@linkplain #buyerName}
	 * 
	 * @param buyerName
	 *            the value for bill_buy_balance.buyer_name
	 */
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	/**
	 * 
	 * {@linkplain #salerNo}
	 * 
	 * @return the value of bill_buy_balance.saler_no
	 */
	public String getSalerNo() {
		return salerNo;
	}

	/**
	 * 
	 * {@linkplain #salerNo}
	 * 
	 * @param salerNo
	 *            the value for bill_buy_balance.saler_no
	 */
	public void setSalerNo(String salerNo) {
		this.salerNo = salerNo;
	}

	/**
	 * 
	 * {@linkplain #salerName}
	 * 
	 * @return the value of bill_buy_balance.saler_name
	 */
	public String getSalerName() {
		return salerName;
	}

	/**
	 * 
	 * {@linkplain #salerName}
	 * 
	 * @param salerName
	 *            the value for bill_buy_balance.saler_name
	 */
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}

	/**
	 * 
	 * {@linkplain #sendDate}
	 * 
	 * @return the value of bill_buy_balance.send_date
	 */
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * 
	 * {@linkplain #sendDate}
	 * 
	 * @param sendDate
	 *            the value for bill_buy_balance.send_date
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * 
	 * {@linkplain #receiveDate}
	 * 
	 * @return the value of bill_buy_balance.receive_date
	 */
	public Date getReceiveDate() {
		return receiveDate;
	}

	/**
	 * 
	 * {@linkplain #receiveDate}
	 * 
	 * @param receiveDate
	 *            the value for bill_buy_balance.receive_date
	 */
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	/**
	 * 
	 * {@linkplain #sendStoreNo}
	 * 
	 * @return the value of bill_buy_balance.send_store_no
	 */
	public String getSendStoreNo() {
		return sendStoreNo;
	}

	/**
	 * 
	 * {@linkplain #sendStoreNo}
	 * 
	 * @param sendStoreNo
	 *            the value for bill_buy_balance.send_store_no
	 */
	public void setSendStoreNo(String sendStoreNo) {
		this.sendStoreNo = sendStoreNo;
	}

	/**
	 * 
	 * {@linkplain #sendStoreName}
	 * 
	 * @return the value of bill_buy_balance.send_store_name
	 */
	public String getSendStoreName() {
		return sendStoreName;
	}

	/**
	 * 
	 * {@linkplain #sendStoreName}
	 * 
	 * @param sendStoreName
	 *            the value for bill_buy_balance.send_store_name
	 */
	public void setSendStoreName(String sendStoreName) {
		this.sendStoreName = sendStoreName;
	}

	/**
	 * 
	 * {@linkplain #receiveStoreNo}
	 * 
	 * @return the value of bill_buy_balance.receive_store_no
	 */
	public String getReceiveStoreNo() {
		return receiveStoreNo;
	}

	/**
	 * 
	 * {@linkplain #receiveStoreNo}
	 * 
	 * @param receiveStoreNo
	 *            the value for bill_buy_balance.receive_store_no
	 */
	public void setReceiveStoreNo(String receiveStoreNo) {
		this.receiveStoreNo = receiveStoreNo;
	}

	/**
	 * 
	 * {@linkplain #receiveStoreName}
	 * 
	 * @return the value of bill_buy_balance.receive_store_name
	 */
	public String getReceiveStoreName() {
		return receiveStoreName;
	}

	/**
	 * 
	 * {@linkplain #receiveStoreName}
	 * 
	 * @param receiveStoreName
	 *            the value for bill_buy_balance.receive_store_name
	 */
	public void setReceiveStoreName(String receiveStoreName) {
		this.receiveStoreName = receiveStoreName;
	}

	/**
	 * 
	 * {@linkplain #skuId}
	 * 
	 * @return the value of bill_buy_balance.sku_id
	 */
	public String getSkuId() {
		return skuId;
	}

	/**
	 * 
	 * {@linkplain #skuId}
	 * 
	 * @param skuId
	 *            the value for bill_buy_balance.sku_id
	 */
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	/**
	 * 
	 * {@linkplain #skuNo}
	 * 
	 * @return the value of bill_buy_balance.sku_no
	 */
	public String getSkuNo() {
		return skuNo;
	}

	/**
	 * 
	 * {@linkplain #skuNo}
	 * 
	 * @param skuNo
	 *            the value for bill_buy_balance.sku_no
	 */
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	/**
	 * 
	 * {@linkplain #supplierNo}
	 * 
	 * @return the value of bill_buy_balance.supplier_no
	 */
	public String getSupplierNo() {
		return supplierNo;
	}

	/**
	 * 
	 * {@linkplain #supplierNo}
	 * 
	 * @param supplierNo
	 *            the value for bill_buy_balance.supplier_no
	 */
	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	/**
	 * 
	 * {@linkplain #itemNo}
	 * 
	 * @return the value of bill_buy_balance.item_no
	 */
	public String getItemNo() {
		return itemNo;
	}

	/**
	 * 
	 * {@linkplain #itemNo}
	 * 
	 * @param itemNo
	 *            the value for bill_buy_balance.item_no
	 */
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	/**
	 * 
	 * {@linkplain #itemName}
	 * 
	 * @return the value of bill_buy_balance.item_name
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * 
	 * {@linkplain #itemName}
	 * 
	 * @param itemName
	 *            the value for bill_buy_balance.item_name
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * 
	 * {@linkplain #brandNo}
	 * 
	 * @return the value of bill_buy_balance.brand_no
	 */
	public String getBrandNo() {
		return brandNo;
	}

	/**
	 * 
	 * {@linkplain #brandNo}
	 * 
	 * @param brandNo
	 *            the value for bill_buy_balance.brand_no
	 */
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	/**
	 * 
	 * {@linkplain #brandName}
	 * 
	 * @return the value of bill_buy_balance.brand_name
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * 
	 * {@linkplain #brandName}
	 * 
	 * @param brandName
	 *            the value for bill_buy_balance.brand_name
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * 
	 * {@linkplain #categoryNo}
	 * 
	 * @return the value of bill_buy_balance.category_no
	 */
	public String getCategoryNo() {
		return categoryNo;
	}

	/**
	 * 
	 * {@linkplain #categoryNo}
	 * 
	 * @param categoryNo
	 *            the value for bill_buy_balance.category_no
	 */
	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}

	/**
	 * 
	 * {@linkplain #categoryName}
	 * 
	 * @return the value of bill_buy_balance.category_name
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * 
	 * {@linkplain #categoryName}
	 * 
	 * @param categoryName
	 *            the value for bill_buy_balance.category_name
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * 
	 * {@linkplain #cost}
	 * 
	 * @return the value of bill_buy_balance.cost
	 */
	public BigDecimal getCost() {
		return cost;
	}

	/**
	 * 
	 * {@linkplain #cost}
	 * 
	 * @param cost
	 *            the value for bill_buy_balance.cost
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
		// if(null !=cost){
		// if(cost.intValue() == 0 ){
		// setExceptionName(ExceptionTypeEnums.COST_IS_ZERO.getTypeName());
		// }else if(cost.intValue() == 1){
		// setExceptionName(ExceptionTypeEnums.COST_IS_ONE.getTypeName());
		// }
		// }
	}

	/**
	 * 
	 * {@linkplain #billCost}
	 * 
	 * @return the value of bill_buy_balance.bill_cost
	 */
	public BigDecimal getBillCost() {
		return billCost;
	}

	/**
	 * 
	 * {@linkplain #billCost}
	 * 
	 * @param billCost
	 *            the value for bill_buy_balance.bill_cost
	 */
	public void setBillCost(BigDecimal billCost) {
		this.billCost = billCost;
		// if(null !=billCost){
		// if(billCost.intValue() == 0 ){
		// setExceptionName(ExceptionTypeEnums.COST_IS_ZERO.getTypeName());
		// }else if(billCost.intValue() == 1){
		// setExceptionName(ExceptionTypeEnums.COST_IS_ONE.getTypeName());
		// }
		// }
	}

	/**
	 * 
	 * {@linkplain #taxRate}
	 * 
	 * @return the value of bill_buy_balance.tax_rate
	 */
	public BigDecimal getTaxRate() {
		return taxRate;
	}

	/**
	 * 
	 * {@linkplain #taxRate}
	 * 
	 * @param taxRate
	 *            the value for bill_buy_balance.tax_rate
	 */
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	/**
	 * 
	 * {@linkplain #exclusiveCost}
	 * 
	 * @return the value of bill_buy_balance.exclusive_cost
	 */
	public BigDecimal getExclusiveCost() {
		return exclusiveCost;
	}

	/**
	 * 
	 * {@linkplain #exclusiveCost}
	 * 
	 * @param exclusiveCost
	 *            the value for bill_buy_balance.exclusive_cost
	 */
	public void setExclusiveCost(BigDecimal exclusiveCost) {
		this.exclusiveCost = exclusiveCost;
	}

	/**
	 * 
	 * {@linkplain #sendQty}
	 * 
	 * @return the value of bill_buy_balance.send_qty
	 */
	public Integer getSendQty() {
		return sendQty;
	}

	/**
	 * 
	 * {@linkplain #sendQty}
	 * 
	 * @param sendQty
	 *            the value for bill_buy_balance.send_qty
	 */
	public void setSendQty(Integer sendQty) {
		if (sendQty == null) {
			sendQty = new Integer(0);
		}
		this.sendQty = sendQty;
	}

	/**
	 * 
	 * {@linkplain #receiveQty}
	 * 
	 * @return the value of bill_buy_balance.receive_qty
	 */
	public Integer getReceiveQty() {
		return receiveQty;
	}

	/**
	 * 
	 * {@linkplain #receiveQty}
	 * 
	 * @param receiveQty
	 *            the value for bill_buy_balance.receive_qty
	 */
	public void setReceiveQty(Integer receiveQty) {
		if (receiveQty == null) {
			receiveQty = new Integer(0);
		}
		this.receiveQty = receiveQty;
	}

	/**
	 * 
	 * {@linkplain #orderNo}
	 * 
	 * @return the value of bill_buy_balance.order_no
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * 
	 * {@linkplain #orderNo}
	 * 
	 * @param orderNo
	 *            the value for bill_buy_balance.order_no
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 
	 * {@linkplain #balanceNo}
	 * 
	 * @return the value of bill_buy_balance.balance_no
	 */
	public String getBalanceNo() {
		return balanceNo;
	}

	/**
	 * 
	 * {@linkplain #balanceNo}
	 * 
	 * @param balanceNo
	 *            the value for bill_buy_balance.balance_no
	 */
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}

	/**
	 * 
	 * {@linkplain #isSplit}
	 * 
	 * @return the value of bill_buy_balance.is_split
	 */
	public Integer getIsSplit() {
		return isSplit;
	}

	/**
	 * 
	 * {@linkplain #isSplit}
	 * 
	 * @param isSplit
	 *            the value for bill_buy_balance.is_split
	 */
	public void setIsSplit(Integer isSplit) {
		this.isSplit = isSplit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buyerNo == null) ? 0 : buyerNo.hashCode());
		result = prime * result + ((refBillNo == null) ? 0 : refBillNo.hashCode());
		result = prime * result + ((salerNo == null) ? 0 : salerNo.hashCode());
		return result;
	}

	public Integer getCostChecked() {
		return costChecked;
	}

	public void setCostChecked(Integer costChecked) {
		this.costChecked = costChecked;
	}

	public BigDecimal getHeadquarterCost() {
		return headquarterCost;
	}

	public void setHeadquarterCost(BigDecimal headquarterCost) {
		this.headquarterCost = headquarterCost;
	}

	public BigDecimal getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}

	public String getBrandUnitNo() {
		return brandUnitNo;
	}

	public void setBrandUnitNo(String brandUnitNo) {
		this.brandUnitNo = brandUnitNo;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public BigDecimal getRegionCostSecond() {
		return regionCostSecond;
	}

	public void setRegionCostSecond(BigDecimal regionCostSecond) {
		this.regionCostSecond = regionCostSecond;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public BigDecimal getMaterialPrice() {
		return materialPrice;
	}

	public void setMaterialPrice(BigDecimal materialPrice) {
		this.materialPrice = materialPrice;
	}

	public BigDecimal getFactoryPrice() {
		return factoryPrice;
	}

	public void setFactoryPrice(BigDecimal factoryPrice) {
		this.factoryPrice = factoryPrice;
	}

	public BigDecimal getTagPriceNation() {
		return tagPriceNation;
	}

	public void setTagPriceNation(BigDecimal tagPriceNation) {
		this.tagPriceNation = tagPriceNation;
	}

	public BigDecimal getUnitCostSecond() {
		return unitCostSecond;
	}

	public void setUnitCostSecond(BigDecimal unitCostSecond) {
		this.unitCostSecond = unitCostSecond;
	}

	public String getFasBillCode() {
		return fasBillCode;
	}

	public void setFasBillCode(String fasBillCode) {
		this.fasBillCode = fasBillCode;
		if (null != fasBillCode) {
			setFasBillName(FasBillCodeEnums.getNameByCode(fasBillCode));
		}
	}

	public String getFasBillName() {
		return fasBillName;
	}

	public void setFasBillName(String fasBillName) {
		this.fasBillName = fasBillName;
	}

	public BigDecimal getActualCost() {
		return actualCost;
	}

	public void setActualCost(BigDecimal actualCost) {
		this.actualCost = actualCost;
	}

	public BigDecimal getOtherDeductCost() {
		return otherDeductCost;
	}

	public void setOtherDeductCost(BigDecimal otherDeductCost) {
		this.otherDeductCost = otherDeductCost;
	}

	public BigDecimal getAverageCost() {
		return averageCost;
	}

	public void setAverageCost(BigDecimal averageCost) {
		this.averageCost = averageCost;
	}

	public String getFirstLevelCategoryNo() {
		return firstLevelCategoryNo;
	}

	public void setFirstLevelCategoryNo(String firstLevelCategoryNo) {
		this.firstLevelCategoryNo = firstLevelCategoryNo;
	}

	public String getFirstLevelCategoryName() {
		return firstLevelCategoryName;
	}

	public void setFirstLevelCategoryName(String firstLevelCategoryName) {
		this.firstLevelCategoryName = firstLevelCategoryName;
	}

	public String getSecondLevelCategoryNo() {
		return secondLevelCategoryNo;
	}

	public void setSecondLevelCategoryNo(String secondLevelCategoryNo) {
		this.secondLevelCategoryNo = secondLevelCategoryNo;
	}

	public String getSecondLevelCategoryName() {
		return secondLevelCategoryName;
	}

	public void setSecondLevelCategoryName(String secondLevelCategoryName) {
		this.secondLevelCategoryName = secondLevelCategoryName;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getReceiveDateStr() {
		return receiveDateStr;
	}

	public void setReceiveDateStr(String receiveDateStr) {
		this.receiveDateStr = receiveDateStr;
	}

	public String getYearsName() {
		return yearsName;
	}

	public void setYearsName(String yearsName) {
		this.yearsName = yearsName;
	}

	public String getSeasonName() {
		return seasonName;
	}

	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	/**
	 * 币别编号
	 */
	private String currencyCode;

	/**
	 * 币别名称
	 */
	private String currencyName;

	/**
	 * 汇率
	 */
	private BigDecimal exchangeRate;

	/**
	 * 关税率
	 */
	private BigDecimal tariffRate;

	/**
	 * 增值税率
	 */
	private BigDecimal vatRate;

	/**
	 * 采购费用
	 */
	private BigDecimal purchaseFee;

	/**
	 * 本位币编码
	 */
	private String standardCurrencyCode;

	/**
	 * 本位币名称
	 */
	private String standardCurrencyName;

	/**
	 * 总金额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal totalAmount;

	/**
	 * 地区加价
	 */
	private BigDecimal regionPriceGap;

	/**
	 * 地区金额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal regionAmount;

	/**
	 * 地区价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal regionCost;

	/**
	 * 二级大类
	 */
	private String categorySecondary;

	/**
	 * 三级大类
	 */
	private String categoryTertiary;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改人
	 */
	private String updateUser;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 
	 * {@linkplain #id}
	 * 
	 * @return the value of bill_buy_balance_additional.id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * {@linkplain #id}
	 * 
	 * @param id
	 *            the value for bill_buy_balance_additional.id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * {@linkplain #currencyCode}
	 * 
	 * @return the value of bill_buy_balance_additional.currency_code
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * 
	 * {@linkplain #currencyCode}
	 * 
	 * @param currencyCode
	 *            the value for bill_buy_balance_additional.currency_code
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * 
	 * {@linkplain #currencyCode}
	 * 
	 * @return the value of bill_buy_balance_additional.currency_code
	 */
	public String getBillNo() {
		return billNo;
	}

	/**
	 * 
	 * {@linkplain #currencyCode}
	 * 
	 * @param currencyCode
	 *            the value for bill_buy_balance_additional.currency_code
	 */
	public void setBillNo(String BillNo) {
		this.billNo = BillNo;
	}

	/**
	 * 
	 * {@linkplain #currencyName}
	 * 
	 * @return the value of bill_buy_balance_additional.currency_name
	 */
	public String getCurrencyName() {
		return currencyName;
	}

	/**
	 * 
	 * {@linkplain #currencyName}
	 * 
	 * @param currencyName
	 *            the value for bill_buy_balance_additional.currency_name
	 */
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	/**
	 * 
	 * {@linkplain #exchangeRate}
	 * 
	 * @return the value of bill_buy_balance_additional.exchange_rate
	 */
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	/**
	 * 
	 * {@linkplain #exchangeRate}
	 * 
	 * @param exchangeRate
	 *            the value for bill_buy_balance_additional.exchange_rate
	 */
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	/**
	 * 
	 * {@linkplain #tariffRate}
	 * 
	 * @return the value of bill_buy_balance_additional.tariff_rate
	 */
	public BigDecimal getTariffRate() {
		return tariffRate;
	}

	/**
	 * 
	 * {@linkplain #tariffRate}
	 * 
	 * @param tariffRate
	 *            the value for bill_buy_balance_additional.tariff_rate
	 */
	public void setTariffRate(BigDecimal tariffRate) {
		this.tariffRate = tariffRate;
	}

	/**
	 * 
	 * {@linkplain #vatRate}
	 * 
	 * @return the value of bill_buy_balance_additional.vat_rate
	 */
	public BigDecimal getVatRate() {
		return vatRate;
	}

	/**
	 * 
	 * {@linkplain #vatRate}
	 * 
	 * @param vatRate
	 *            the value for bill_buy_balance_additional.vat_rate
	 */
	public void setVatRate(BigDecimal vatRate) {
		this.vatRate = vatRate;
	}

	/**
	 * 
	 * {@linkplain #purchaseFee}
	 * 
	 * @return the value of bill_buy_balance_additional.purchase_fee
	 */
	public BigDecimal getPurchaseFee() {
		return purchaseFee;
	}

	/**
	 * 
	 * {@linkplain #purchaseFee}
	 * 
	 * @param purchaseFee
	 *            the value for bill_buy_balance_additional.purchase_fee
	 */
	public void setPurchaseFee(BigDecimal purchaseFee) {
		this.purchaseFee = purchaseFee;
	}

	/**
	 * 
	 * {@linkplain #standardCurrenyCode}
	 * 
	 * @return the value of bill_buy_balance_additional.standard_curreny_code
	 */
	public String getStandardCurrencyCode() {
		return standardCurrencyCode;
	}

	/**
	 * 
	 * {@linkplain #standardCurrenyCode}
	 * 
	 * @param standardCurrenyCode
	 *            the value for
	 *            bill_buy_balance_additional.standard_curreny_code
	 */
	public void setStandardCurrencyCode(String standardCurrencyCode) {
		this.standardCurrencyCode = standardCurrencyCode;
	}

	/**
	 * 
	 * {@linkplain #standardCurrenyName}
	 * 
	 * @return the value of bill_buy_balance_additional.standard_curreny_name
	 */
	public String getStandardCurrencyName() {
		return standardCurrencyName;
	}

	/**
	 * 
	 * {@linkplain #standardCurrenyName}
	 * 
	 * @param standardCurrenyName
	 *            the value for
	 *            bill_buy_balance_additional.standard_curreny_name
	 */
	public void setStandardCurrencyName(String standardCurrencyName) {
		this.standardCurrencyName = standardCurrencyName;
	}

	/**
	 * 
	 * {@linkplain #totalAmount}
	 * 
	 * @return the value of bill_buy_balance_additional.total_amount
	 */
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	/**
	 * 
	 * {@linkplain #totalAmount}
	 * 
	 * @param totalAmount
	 *            the value for bill_buy_balance_additional.total_amount
	 */
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * 
	 * {@linkplain #regionPriceGap}
	 * 
	 * @return the value of bill_buy_balance_additional.region_price_gap
	 */
	public BigDecimal getRegionPriceGap() {
		return regionPriceGap;
	}

	/**
	 * 
	 * {@linkplain #regionPriceGap}
	 * 
	 * @param regionPriceGap
	 *            the value for bill_buy_balance_additional.region_price_gap
	 */
	public void setRegionPriceGap(BigDecimal regionPriceGap) {
		this.regionPriceGap = regionPriceGap;
	}

	/**
	 * 
	 * {@linkplain #regionAmount}
	 * 
	 * @return the value of bill_buy_balance_additional.region_amount
	 */
	public BigDecimal getRegionAmount() {
		return regionAmount;
	}

	/**
	 * 
	 * {@linkplain #regionAmount}
	 * 
	 * @param regionAmount
	 *            the value for bill_buy_balance_additional.region_amount
	 */
	public void setRegionAmount(BigDecimal regionAmount) {
		this.regionAmount = regionAmount;
	}

	/**
	 * 
	 * {@linkplain #regionCost}
	 * 
	 * @return the value of bill_buy_balance_additional.region_cost
	 */
	public BigDecimal getRegionCost() {
		return regionCost;
	}

	/**
	 * 
	 * {@linkplain #regionCost}
	 * 
	 * @param regionCost
	 *            the value for bill_buy_balance_additional.region_cost
	 */
	public void setRegionCost(BigDecimal regionCost) {
		this.regionCost = regionCost;
	}

	/**
	 * 
	 * {@linkplain #categorySecondary}
	 * 
	 * @return the value of bill_buy_balance_additional.category_secondary
	 */
	public String getCategorySecondary() {
		return categorySecondary;
	}

	/**
	 * 
	 * {@linkplain #categorySecondary}
	 * 
	 * @param categorySecondary
	 *            the value for bill_buy_balance_additional.category_secondary
	 */
	public void setCategorySecondary(String categorySecondary) {
		this.categorySecondary = categorySecondary;
	}

	/**
	 * 
	 * {@linkplain #categoryTertiary}
	 * 
	 * @return the value of bill_buy_balance_additional.category_tertiary
	 */
	public String getCategoryTertiary() {
		return categoryTertiary;
	}

	/**
	 * 
	 * {@linkplain #categoryTertiary}
	 * 
	 * @param categoryTertiary
	 *            the value for bill_buy_balance_additional.category_tertiary
	 */
	public void setCategoryTertiary(String categoryTertiary) {
		this.categoryTertiary = categoryTertiary;
	}

	/**
	 * 
	 * {@linkplain #createTime}
	 * 
	 * @return the value of bill_buy_balance_additional.create_time
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 
	 * {@linkplain #createTime}
	 * 
	 * @param createTime
	 *            the value for bill_buy_balance_additional.create_time
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 
	 * {@linkplain #updateUser}
	 * 
	 * @return the value of bill_buy_balance_additional.update_user
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * 
	 * {@linkplain #updateUser}
	 * 
	 * @param updateUser
	 *            the value for bill_buy_balance_additional.update_user
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * 
	 * {@linkplain #updateTime}
	 * 
	 * @return the value of bill_buy_balance_additional.update_time
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 
	 * {@linkplain #updateTime}
	 * 
	 * @param updateTime
	 *            the value for bill_buy_balance_additional.update_time
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
