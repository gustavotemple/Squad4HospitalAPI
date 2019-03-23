package com.acelera.squad.four.hospital.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acelera.squad.four.hospital.models.Produto;
import com.acelera.squad.four.hospital.service.ProdutoService;

@RestController
@RequestMapping("/v1")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;

	@PostMapping("/produtos")
	public ResponseEntity<Produto> addProduto(@Valid @RequestBody Produto newProduto) {
		return ResponseEntity.ok(produtoService.addProduto(newProduto));
	}

	@GetMapping("/produtos")
	public ResponseEntity<Produto> getProduto(@RequestParam String id) {
		return ResponseEntity.ok(produtoService.getProduto(id));
	}

	@GetMapping("/produtos/todos")
	public ResponseEntity<Object> listaBarbeiros() {
		return ResponseEntity.ok().body(produtoService.findAll());
	}

	@PutMapping
	public ResponseEntity<Produto> updateProduto(@Valid @RequestBody Produto produtoUpdate, @RequestParam String id) {
		return ResponseEntity.ok(produtoService.updateProduto(produtoUpdate, id));
	}

	@DeleteMapping("/produtos/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable String id) {
		produtoService.deleteProduto(id);

		return ResponseEntity.ok().body("Produto " + id + " apagado.");
	}

}
