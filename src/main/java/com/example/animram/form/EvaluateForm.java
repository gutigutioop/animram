package com.example.animram.form;

import com.example.animram.entity.Animal;

//import com.example.animram.entity.Animal;

import lombok.Data;

@Data
public class EvaluateForm {
	private Long id;

	private Long userId;

	private Long animalId;

	private Animal animal;
}
