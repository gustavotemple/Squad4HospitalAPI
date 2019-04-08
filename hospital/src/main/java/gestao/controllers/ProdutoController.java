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

import gestao.configuration.ApplicationConfig;
import gestao.models.Produto;
import gestao.service.ProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "estoque")
@RequestMapping(path = ApplicationConfig.BASE_URL + "/{id}/estoque")
@ExposesResourceFor(Produto.class)
public class ProdutoController {

	private ProdutoService produtoService;

	@Autowired
	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@PostMapping
	@ApiOperation(value = "Adiciona um produto no estoque de um hospital")
	public ResponseEntity<Produto> addProduto(@PathVariable("id") ObjectId hospitalId,
			@Valid @RequestBody Produto newProduto) {
		final Produto produto = produtoService.addProduto(hospitalId, newProduto);

		produto.add(linkTo(methodOn(ProdutoController.class).getProduto(hospitalId, produto.getObjectId())).withSelfRel());

		return ResponseEntity.ok(produto);
	}

	@GetMapping("/{produto}")
	@ApiOperation(value = "Retorna mais detalhes de um produto")
	public ResponseEntity<Produto> getProduto(@PathVariable("id") ObjectId hospitalId,
			@PathVariable("produto") ObjectId produtoId) {
		final Produto produto = produtoService.getProduto(hospitalId, produtoId);

		produto.add(linkTo(methodOn(ProdutoController.class).getProduto(hospitalId, produto.getObjectId())).withSelfRel());

		return ResponseEntity.ok(produto);
	}

	@GetMapping
	@ApiOperation(value = "Retorna as informacoes dos produtos existentes no estoque")
	public ResponseEntity<Collection<Produto>> listarEstoque(@PathVariable("id") ObjectId hospitalId) {
		return ResponseEntity.ok().body(produtoService.findAll(hospitalId));
	}
	
	@GetMapping("/produtos")
	@ApiOperation(value = "Retorna as informacoes dos produtos comuns existentes no estoque")
	public ResponseEntity<Collection<Produto>> listarProdutos(@PathVariable("id") ObjectId hospitalId) {
		return ResponseEntity.ok().body(produtoService.findAllProdutos(hospitalId));
	}
	
	@GetMapping("/bolsas")
	@ApiOperation(value = "Retorna as informacoes das bolsas de sangue existentes no estoque")
	public ResponseEntity<Collection<Produto>> listarBolsas(@PathVariable("id") ObjectId hospitalId) {
		return ResponseEntity.ok().body(produtoService.findAllBolsas(hospitalId));
	}

	@PutMapping("/{produto}")
	@ApiOperation(value = "Atualiza um produto")
	public ResponseEntity<Produto> updateProduto(@PathVariable("id") ObjectId hospitalId,
			@Valid @RequestBody Produto produtoUpdate, @PathVariable("produto") ObjectId produtoId) {
		final Produto produto = produtoService.updateProduto(hospitalId, produtoUpdate, produtoId);

		produto.add(linkTo(methodOn(ProdutoController.class).getProduto(hospitalId, produto.getObjectId())).withSelfRel());

		return ResponseEntity.ok(produto);
	}

	@DeleteMapping("/{produto}")
	@ApiOperation(value = "Exclui um produto")
	public ResponseEntity<String> deleteProduto(@PathVariable("id") ObjectId hospitalId,
			@PathVariable("produto") ObjectId produtoId) {
		produtoService.deleteProduto(hospitalId, produtoId);

		return ResponseEntity.ok().body("Produto " + produtoId + " apagado.");
	}

}
