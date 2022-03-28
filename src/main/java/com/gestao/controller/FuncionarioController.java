package com.gestao.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestao.entity.Cargo;
import com.gestao.entity.Funcionario;
import com.gestao.enumeration.UF;
import com.gestao.service.CargoService;
import com.gestao.service.FuncionarioService;
import com.gestao.util.FuncionarioValidator;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

	@Autowired
	private FuncionarioService service;

	@Autowired
	private CargoService cargoService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new FuncionarioValidator());
	}

	@GetMapping("/cadastrar")
	public String cadastrar(Funcionario funcionario) {
		return "/funcionario/cadastro";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		
		return findPaginated(1, "nome", "asc", model);
	}

	@GetMapping("/buscar/nome")
	public String findByNome(@RequestParam("nome") String nome, ModelMap model) {
		model.addAttribute("funcionarios", service.findByNome(nome));
		return "/funcionario/lista";
	}

	@GetMapping("/buscar/cargo")
	public String findByCargo(@RequestParam("id") Long id, ModelMap model) {
		model.addAttribute("funcionarios", service.findByCargo(id));
		return "/funcionario/lista";
	}

	@GetMapping("/buscar/data")
	public String findByDatas(@RequestParam("entrada") @DateTimeFormat(iso = ISO.DATE) LocalDate entrada,
			@RequestParam("saida") @DateTimeFormat(iso = ISO.DATE) LocalDate saida, ModelMap model) {
		model.addAttribute("funcionarios", service.findByDatas(entrada, saida));
		return "/funcionario/lista";
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attr) {

		if (result.hasErrors()) {
			return "/funcionario/cadastro";
		}

		String msg;
		if (funcionario.getId() == null) {
			msg = "Funcionário inserido com sucesso";
		} else {
			msg = "Funcionário alterado com sucesso";
		}

		service.saveOrUpdate(funcionario);
		attr.addFlashAttribute("success", msg);
		return "redirect:/funcionarios/cadastrar";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Funcionario funcionario, ModelMap model) {
		model.addAttribute("funcionario", funcionario);
		return "/funcionario/cadastro";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Funcionario funcionario, ModelMap model) {

		service.delete(funcionario.getId());
		model.addAttribute("success", "Funcionário excluído com sucesso!!");
		return listar(model);
	}

	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable("pageNo") int pageNo, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, ModelMap model) {

		int pageSize = 2;

		Page<Funcionario> page = service.findPaginated(pageNo, pageSize, sortField, sortDir);

		model.addAttribute("funcionarios", page.getContent());
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		return "/funcionario/lista";
	}

	@ModelAttribute("cargos")
	public List<Cargo> listaDeCargoss() {
		return cargoService.findAll();
	}

	@ModelAttribute("ufs")
	public UF[] getUFs() {
		return UF.values();
	}

}
