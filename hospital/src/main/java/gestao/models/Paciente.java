package gestao.models;

import java.io.Serializable;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TypeAlias("Paciente")
@Document(collection = "pacientes")
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Paciente e seus atributos")
public class Paciente extends ResourceSupport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum Type {
		M, F
	}

	@Id @JsonProperty(access = Access.READ_ONLY)
	private ObjectId _id;
	@NotBlank(message = "Nome do paciente nao preenchido")
	@ApiModelProperty(notes = "Nome do paciente")
	private String nome;
	@Indexed(name="cpfPaciente", unique=true)
	@NotBlank(message = "CPF do paciente nao preenchido")
	@ApiModelProperty(notes = "CPF do paciente")
	private String cpf;
	@JsonInclude(Include.NON_NULL)
	private Paciente.Type sexo;

	public Paciente() {
	}

	public Paciente(String nome, String cpf, Paciente.Type sexo) {
		this.nome = nome;
		this.cpf = cpf;
		this.sexo = sexo;
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Paciente.Type getSexo() {
		return sexo;
	}

	public void setSexo(Paciente.Type sexo) {
		this.sexo = sexo;
	}

	public Paciente build(Paciente novoPaciente) {
		this._id = novoPaciente.getObjectId();
		this.nome = novoPaciente.getNome();
		this.cpf = novoPaciente.getCpf();
		this.sexo = novoPaciente.getSexo();
		return this;
	}

}
