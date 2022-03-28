package com.gestao.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestao.entity.Departamento;
import com.gestao.service.DepartamentoService;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {

	@Autowired
	private DepartamentoService service;

	@GetMapping("/cadastrar")
	public String cadastrar(Departamento departamento) {
		return "/departamento/cadastro";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		
		return findPaginated(1, "nome", "asc", model);
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Departamento departamento, BindingResult result,  RedirectAttributes attr) {
		
		
		if(service.existsByNome(departamento.getNome())) {
			result.addError(new ObjectError("nome", "Nome do departamento já existe no sistema."));
		}
		
		if(result.hasErrors()) {
			return "/departamento/cadastro";
		}
		
		
		String msg;
		if (departamento.getId() == null) {
			msg = "Departamento inserido com sucesso";
		} else {
			msg = "Departamento alterado com sucesso";
		}

		service.saveOrUpdate(departamento);
		attr.addFlashAttribute("success", msg);
		return "redirect:/departamentos/cadastrar";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Departamento departamento, ModelMap model) {
		model.addAttribute("departamento", departamento);
		return "/departamento/cadastro";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Departamento departamento, ModelMap model) {

		if (departamento.getCargos().isEmpty()) {
			service.delete(departamento.getId());
			model.addAttribute("success", "Departamento excluído com sucesso!!");
		} else {
			model.addAttribute("fail", "Departamento não pode ser excluído. Possui cargo(s) vinculado(s).");
		}
		return listar(model);
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable("pageNo") int pageNo,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, ModelMap model){
		
		int pageSize = 5;
		
		Page<Departamento> page = service.findPaginated(pageNo, pageSize, sortField, sortDir);
		
		model.addAttribute("departamentos", page.getContent());
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" :"asc");
		
		return "/departamento/lista";
	}

}
