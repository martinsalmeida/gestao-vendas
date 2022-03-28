package com.gestao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gestao.enumeration.UF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ENDERECOS")
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(min = 3, max = 250)
	@Column(nullable = false,  length = 250)
	private String logradouro;
	
	@NotBlank
	@Size(min = 3, max = 200)
	@Column(nullable = false,  length = 200)
	private String bairro;
	
	@NotBlank
	@Size(min = 3, max = 250)
	@Column(nullable = false,  length = 250)
	private String cidade;

	@NotNull(message = "{NotNull.endereco.uf}")
	@Column(nullable = false, length = 2)
	@Enumerated(EnumType.STRING)
	private UF uf;
	
	@NotBlank
    @Size(min = 9, max = 9, message = "{Size.endereco.cep}")
	@Column(nullable = false, length = 9)
	private String cep;
	
	
	@NotNull(message = "{Size.endereco.cep}")
	@Digits(integer = 5, fraction = 0)
	@Column(nullable = false, length = 5)
	private String numero;
	
	@Column(length = 150)
	private String complemento;
	
	
}
