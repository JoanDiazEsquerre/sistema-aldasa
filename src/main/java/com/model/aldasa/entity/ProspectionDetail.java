package com.model.aldasa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prospectiondetail")
public class ProspectionDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idprospection")
	private Prospection prospection;
	
	@ManyToOne
	@JoinColumn(name="idaction")
	private Action action;
	
	@ManyToOne
	@JoinColumn(name="idlote")
	private Lote lote;
	
	private Date date;
	private String comment;
	private boolean scheduled;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Prospection getProspection() {
		return prospection;
	}
	public void setProspection(Prospection prospection) {
		this.prospection = prospection;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isScheduled() {
		return scheduled;
	}
	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}
	public Lote getLote() {
		return lote;
	}
	public void setLote(Lote lote) {
		this.lote = lote;
	}
	
	
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof ProspectionDetail) && (id != null)
            ? id.equals(((ProspectionDetail) other).id)
            : (other == this);
    }



   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
}
