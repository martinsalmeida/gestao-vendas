package com.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestao.entity.Cliente;
import com.gestao.service.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService service;

	@GetMapping("/cadastrar")
	public String cadastrar(Cliente cliente) {
		return "/cliente/cadastro";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("clientes", service.findAll());
		return "/cliente/lista";
	}

	@PostMapping("/salvar")
	public String salvar(Cliente cliente, RedirectAttributes attr) {
		String msg;
		if (cliente.getId() == null) {
			msg = "Cliente inserido com sucesso";
		} else {
			msg = "Cliente alterado com sucesso";
		}

		service.saveOrUpdate(cliente);
		attr.addFlashAttribute("success", msg);
		return "redirect:/clientes/cadastrar";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Cliente cliente, ModelMap model) {
		model.addAttribute("cliente", cliente);
		return "/cliente/cadastro";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Cliente cliente, ModelMap model) {

			service.delete(cliente.getId());
			model.addAttribute("success", "Cliente excluído com sucesso!!");
		
		return listar(model);
	}

}
