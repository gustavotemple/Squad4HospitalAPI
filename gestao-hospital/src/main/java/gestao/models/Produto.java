package gestao.models;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.DecimalMin;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import gestao.validation.EnumValidator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TypeAlias("Produto")
@Document(collection = "estoque")
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Produtos e Bolsas de Sangue do Estoque")
public class Produto extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Tipo {
		COMUM("COMUM"), SANGUE("SANGUE");

		private final String tipo;

		Tipo(final String tipo) {
			this.tipo = tipo;
		}

		@Override
		public String toString() {
			return tipo;
		}
	}

	@Id
	@JsonProperty(access = Access.READ_ONLY)
	private ObjectId _id;
	@NotBlank(message = "Nome do produto nao preenchido")
	@ApiModelProperty(notes = "Nome do item de estoque")
	private String nome;
	@JsonInclude(Include.NON_NULL)
	@ApiModelProperty(notes = "Descricao do item de estoque")
	private String descricao;
	@DecimalMin(value = "1", message = "A quantidade precisa ser maior que uma unidade")
	@ApiModelProperty(notes = "Quantidade do item de estoque")
	private int quantidade;
	@NotBlank(message = "Tipo do produto nao preenchido")
	@ApiModelProperty(notes = "Tipo do item de estoque")
	@EnumValidator(enumClazz = Tipo.class, message = "O estoque apenas suporta produtos do tipo COMUM e SANGUE")
	private String tipo;

	public Produto() {
	}

	public Produto(String nome, String descricao, int quantidade, String tipo) {
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
		if (Objects.isNull(this._id))
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
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
