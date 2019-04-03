package gestao.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

import gestao.models.Produto;
import gestao.service.ProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "estoque")
@RequestMapping(path = "/v1")
@ExposesResourceFor(Produto.class)
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;

	@PostMapping("/hospitais/{id}/estoque")
	@ApiOperation(value = "Adiciona um produto no estoque de um hospital")
	public ResponseEntity<Produto> addProduto(@PathVariable ObjectId id, @Valid @RequestBody Produto newProduto) {
		final Produto produto = produtoService.addProduto(id, newProduto);
		
		produto.add(linkTo(methodOn(ProdutoController.class).getProduto(id, produto.getObjectId())).withSelfRel());
		
		return ResponseEntity.ok(produto);
	}

	@GetMapping("/hospitais/{id}/estoque/{produto}")
	@ApiOperation(value = "Retorna mais detalhes de um produto")
	public ResponseEntity<Produto> getProduto(@PathVariable ObjectId id, @PathVariable ObjectId produto) {
		final Produto prod = produtoService.getProduto(id, produto);
		
		prod.add(linkTo(methodOn(ProdutoController.class).getProduto(id, prod.getObjectId())).withSelfRel());
		
		return ResponseEntity.ok(prod);
	}

	@GetMapping("/hospitais/{id}/estoque")
	@ApiOperation(value = "Retorna as informacoes dos produtos existentes no estoque")
	public ResponseEntity<Collection<Produto>> listarProdutos(@PathVariable ObjectId id) {
		return ResponseEntity.ok().body(produtoService.findAll(id));
	}

	@PutMapping("/hospitais/{id}/estoque/{produto}")
	@ApiOperation(value = "Atualiza um produto")
	public ResponseEntity<Produto> updateProduto(@PathVariable ObjectId id, @Valid @RequestBody Produto produtoUpdate, @PathVariable ObjectId produto) {
		final Produto prod = produtoService.updateProduto(id, produtoUpdate, produto);
		
		prod.add(linkTo(methodOn(ProdutoController.class).getProduto(id, prod.getObjectId())).withSelfRel());
		
		return ResponseEntity.ok(prod);
	}

	@DeleteMapping("/hospitais/{id}/estoque/{produto}")
	@ApiOperation(value = "Exclui um produto")
	public ResponseEntity<String> deleteProduto(@PathVariable ObjectId id, @PathVariable ObjectId produto) {
		produtoService.deleteProduto(id, produto);

		return ResponseEntity.ok().body("Produto " + id + " apagado.");
	}

}
