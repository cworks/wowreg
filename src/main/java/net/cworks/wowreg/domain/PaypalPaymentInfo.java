/**
 * This class is generated by jOOQ
 */
package net.cworks.wowreg.domain;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.1" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PaypalPaymentInfo implements java.io.Serializable {

	private static final long serialVersionUID = -1216058292;

	private java.lang.Integer  id;
	private java.lang.String   paypalId;
	private java.sql.Timestamp paypalCreateTime;
	private java.sql.Timestamp paypalUpdateTime;
	private java.lang.String   paypalState;
	private java.lang.Integer  paypalTxAmount;
	private java.lang.String   paypalTxDesc;
	private java.lang.String   paypalApprovalUrl;
	private java.lang.String   paypalExecuteUrl;
	private java.lang.String   paypalSelfUrl;
	private java.lang.String   paypalToken;
	private java.lang.Integer  groupId;

	public PaypalPaymentInfo() {}

	public PaypalPaymentInfo(
		java.lang.Integer  id,
		java.lang.String   paypalId,
		java.sql.Timestamp paypalCreateTime,
		java.sql.Timestamp paypalUpdateTime,
		java.lang.String   paypalState,
		java.lang.Integer  paypalTxAmount,
		java.lang.String   paypalTxDesc,
		java.lang.String   paypalApprovalUrl,
		java.lang.String   paypalExecuteUrl,
		java.lang.String   paypalSelfUrl,
		java.lang.String   paypalToken,
		java.lang.Integer  groupId
	) {
		this.id = id;
		this.paypalId = paypalId;
		this.paypalCreateTime = paypalCreateTime;
		this.paypalUpdateTime = paypalUpdateTime;
		this.paypalState = paypalState;
		this.paypalTxAmount = paypalTxAmount;
		this.paypalTxDesc = paypalTxDesc;
		this.paypalApprovalUrl = paypalApprovalUrl;
		this.paypalExecuteUrl = paypalExecuteUrl;
		this.paypalSelfUrl = paypalSelfUrl;
		this.paypalToken = paypalToken;
		this.groupId = groupId;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.String getPaypalId() {
		return this.paypalId;
	}

	public void setPaypalId(java.lang.String paypalId) {
		this.paypalId = paypalId;
	}

	public java.sql.Timestamp getPaypalCreateTime() {
		return this.paypalCreateTime;
	}

	public void setPaypalCreateTime(java.sql.Timestamp paypalCreateTime) {
		this.paypalCreateTime = paypalCreateTime;
	}

	public java.sql.Timestamp getPaypalUpdateTime() {
		return this.paypalUpdateTime;
	}

	public void setPaypalUpdateTime(java.sql.Timestamp paypalUpdateTime) {
		this.paypalUpdateTime = paypalUpdateTime;
	}

	public java.lang.String getPaypalState() {
		return this.paypalState;
	}

	public void setPaypalState(java.lang.String paypalState) {
		this.paypalState = paypalState;
	}

	public java.lang.Integer getPaypalTxAmount() {
		return this.paypalTxAmount;
	}

	public void setPaypalTxAmount(java.lang.Integer paypalTxAmount) {
		this.paypalTxAmount = paypalTxAmount;
	}

	public java.lang.String getPaypalTxDesc() {
		return this.paypalTxDesc;
	}

	public void setPaypalTxDesc(java.lang.String paypalTxDesc) {
		this.paypalTxDesc = paypalTxDesc;
	}

	public java.lang.String getPaypalApprovalUrl() {
		return this.paypalApprovalUrl;
	}

	public void setPaypalApprovalUrl(java.lang.String paypalApprovalUrl) {
		this.paypalApprovalUrl = paypalApprovalUrl;
	}

	public java.lang.String getPaypalExecuteUrl() {
		return this.paypalExecuteUrl;
	}

	public void setPaypalExecuteUrl(java.lang.String paypalExecuteUrl) {
		this.paypalExecuteUrl = paypalExecuteUrl;
	}

	public java.lang.String getPaypalSelfUrl() {
		return this.paypalSelfUrl;
	}

	public void setPaypalSelfUrl(java.lang.String paypalSelfUrl) {
		this.paypalSelfUrl = paypalSelfUrl;
	}

	public java.lang.String getPaypalToken() {
		return this.paypalToken;
	}

	public void setPaypalToken(java.lang.String paypalToken) {
		this.paypalToken = paypalToken;
	}

	public java.lang.Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(java.lang.Integer groupId) {
		this.groupId = groupId;
	}
}
