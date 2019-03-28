package com.acelera.squad.four.hospital.controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acelera.squad.four.hospital.models.Produto;
import com.acelera.squad.four.hospital.service.ProdutoService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "estoque")
@RequestMapping("/v1")
@ExposesResourceFor(Produto.class)
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;

	@PostMapping("/hospitais/{id}/estoque")
	public ResponseEntity<Produto> addProduto(@PathVariable ObjectId id, @Valid @RequestBody Produto newProduto) {
		return ResponseEntity.ok(produtoService.addProduto(id, newProduto));
	}

	@GetMapping("/hospitais/{id}/estoque/{produto}")
	public Produto getProduto(@PathVariable ObjectId id, @PathVariable String produto) {
		return produtoService.getProduto(id, produto);
	}

	@GetMapping("/hospitais/{id}/estoque")
	public ResponseEntity<Collection<Produto>> listarProdutos(@PathVariable ObjectId id) {
		return ResponseEntity.ok().body(produtoService.findAll(id));
	}

	@PutMapping("/hospitais/{id}/estoque/{produto}")
	public ResponseEntity<Produto> updateProduto(@PathVariable ObjectId id, @Valid @RequestBody Produto produtoUpdate, @PathVariable String produto) {
		return ResponseEntity.ok(produtoService.updateProduto(id, produtoUpdate, produto));
	}

	@DeleteMapping("/hospitais/{id}/estoque/{produto}")
	public ResponseEntity<String> deleteUser(@PathVariable ObjectId id, @PathVariable String produto) {
		produtoService.deleteProduto(id, produto);

		return ResponseEntity.ok().body("Produto " + id + " apagado.");
	}

}
