package org.openmrs.module.insuranceclaims.api.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Model class that represents an insurance claim item.
 * Represents the relation between claim and item.
 */
@Entity(name = "iclm.InsuranceClaimItem ")
@Table(name = "iclm_claim_item")
@Inheritance(strategy = InheritanceType.JOINED)
public class InsuranceClaimItem extends AbstractBaseOpenmrsData {

	private static final long serialVersionUID = -6769445113735423802L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "iclm_claim_item_id")
	private Integer id;

	@Basic
	@Column(name = "quantity_provided")
	private Integer quantityProvided;

	@Basic
	@Column(name = "quantity_approved")
	private Integer quantityApproved;

	@Basic
	@Column(name = "price_approved")
	private BigDecimal priceApproved;

	@Basic
	@Column(name = "explanation", columnDefinition = "TEXT")
	private String explanation;

	@Basic
	@Column(name = "justification", columnDefinition = "TEXT")
	private String justification;

	@Basic
	@Column(name = "rejection_reason", length = 255)
	private String rejectionReason;

	@ManyToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "item", nullable = false)
	private ProvidedItem item;

	@ManyToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "claim", nullable = false)
	private InsuranceClaim claim;

	@Basic
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private InsuranceClaimItemStatus status;

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantityProvided() {
		return quantityProvided;
	}

	public void setQuantityProvided(Integer quantityProvided) {
		this.quantityProvided = quantityProvided;
	}

	public Integer getQuantityApproved() {
		return quantityApproved;
	}

	public void setQuantityApproved(Integer quantityApproved) {
		this.quantityApproved = quantityApproved;
	}

	public BigDecimal getPriceApproved() {
		return priceApproved;
	}

	public void setPriceApproved(BigDecimal priceApproved) {
		this.priceApproved = priceApproved;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public ProvidedItem getItem() {
		return item;
	}

	public void setItem(ProvidedItem item) {
		this.item = item;
	}

	public InsuranceClaim getClaim() {
		return claim;
	}

	public void setClaim(InsuranceClaim claim) {
		this.claim = claim;
	}

	public InsuranceClaimItemStatus getStatus() {
		return status;
	}

	public void setStatus(InsuranceClaimItemStatus status) {
		this.status = status;
	}
}
