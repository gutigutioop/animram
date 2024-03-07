package com.example.animram.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;


	@Entity
	@Table(name = "ages")
	@Data
	@EqualsAndHashCode(callSuper = false)
	public class Age extends AbstractEntity implements Serializable {
	@Id
	@SequenceGenerator(name = "ages_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;
	
}
