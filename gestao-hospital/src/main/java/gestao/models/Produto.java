package gestao.models;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@TypeAlias("Produto")
@Document(collection = "estoque")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Produto extends ResourceSupport implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum Type {
		COMUM, SANGUE
	}

	@Id
	@JsonProperty(access = Access.READ_ONLY)
	private ObjectId _id;
	@NotEmpty(message = "Nome do item de estoque nao preenchido")
	private String nome;
	private String descricao = "";
	private int quantidade;
	private Produto.Type tipo;

	public Produto() {
	}

	public Produto(ObjectId _id, String nome, String descricao, int quantidade, Produto.Type tipo) {
		this._id = _id;
		this.nome = nome;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.tipo = tipo;
	}

	@JsonProperty("id")
	public String get_id() {
		return _id.toHexString();
	}
	
	@JsonIgnore
	public ObjectId getObjectId() {
		return _id;
	}

	@JsonIgnore
	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Produto.Type getTipo() {
		return tipo;
	}

	public void setTipo(Produto.Type tipo) {
		this.tipo = tipo;
	}

	public Produto build(Produto prod) {
		this._id = prod.getObjectId();
		this.nome = prod.getNome();
		this.descricao = prod.getDescricao();
		this.quantidade = prod.getQuantidade();
		this.tipo = prod.getTipo();
		return this;
	}
}
