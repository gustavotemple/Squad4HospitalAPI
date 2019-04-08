package gestao.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import gestao.configuration.ApplicationConfig;

@TypeAlias("Hospital")
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = ApplicationConfig.HOSPITAIS)
@ApiModel(description = "Hospital e seus atributos")
public class Hospital extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId _id;
	@Indexed(name="nomeHospital", unique=true)
	@ApiModelProperty(notes = "Nome do hospital")
	private String nome;
	@ApiModelProperty(notes = "Endereco do hospital")
	private String endereco;
	@ApiModelProperty(notes = "Leitos totais do hospital")
	private Integer leitosTotais;
	@JsonIgnore @ApiModelProperty(notes = "Localizacao do hospital")
	private GeoJsonPoint localizacao;
	@DBRef @ApiModelProperty(notes = "Pacientes do hospital")
	private Collection<Paciente> pacientes = new ArrayList<Paciente>();
	@DBRef @ApiModelProperty(notes = "Estoque do hospital")
	private Collection<Produto> estoque = new ArrayList<Produto>();
	@DBRef @ApiModelProperty(notes = "Leitos do hospital")
	private Collection<Leito> leitos = new ArrayList<Leito>();
	
	public Hospital(String nome, String endereco, Integer leitosTotais) {
		this.nome = nome;
		this.endereco = endereco;
		this.leitosTotais = leitosTotais;
	}

	public Hospital() {
	}

	@JsonProperty("id")
	public String get_id() {
		return _id.toHexString();
	}
	
	@JsonIgnore
	public ObjectId getObjectId() {
		return _id;
	}

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

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public int getLeitosTotais() {
		return leitosTotais;
	}

	public void setLeitosTotais(int leitosTotais) {
		this.leitosTotais = leitosTotais;
	}
	public int getLeitosDisponiveis() {
		return leitosTotais - leitos.size();
	}

	public GeoJsonPoint getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(GeoJsonPoint localizacao) {
		this.localizacao = localizacao;
	}

	public Collection<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(Collection<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public Collection<Produto> getEstoque() {
		return estoque;
	}

	public void setEstoque(Collection<Produto> estoque) {
		this.estoque = estoque;
	}

	public Collection<Leito> getLeitos() {
		return this.leitos;
	}

	public void setLeitos(Collection<Leito> leitos) {
		this.leitos = leitos;
	}

	public void addLeito(Leito leito){
		this.leitos.add(leito);
	}

	public void removeLeito(Leito leito){
		this.leitos.remove(leito);
	}

}
