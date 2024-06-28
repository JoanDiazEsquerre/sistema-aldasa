package com.model.aldasa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prospect")
public class Prospect {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idperson")
	private Person person;
	
	@ManyToOne
	@JoinColumn(name="idpersonassessor")
	private Person personAssessor;
	
	@ManyToOne
	@JoinColumn(name="idpersonsupervisor")
	private Person personSupervisor;
	
	@Column(name="dateblock")
	private Date dateBlock;
	
	@ManyToOne
	@JoinColumn(name="idsucursal")
	private Sucursal sucursal;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Date getDateBlock() {
		return dateBlock;
	}
	public void setDateBlock(Date dateBlock) {
		this.dateBlock = dateBlock;
	}
	public Person getPersonAssessor() {
		return personAssessor;
	}
	public void setPersonAssessor(Person personAssessor) {
		this.personAssessor = personAssessor;
	}
	public Person getPersonSupervisor() {
		return personSupervisor;
	}
	public void setPersonSupervisor(Person personSupervisor) {
		this.personSupervisor = personSupervisor;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof Prospect) && (id != null)
            ? id.equals(((Prospect) other).id)
            : (other == this);
    }

   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }

}
