package gestao.models;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@TypeAlias("Leito")
@Document(collection = "leitos")
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Leitos e seus atributos")
public class Leito {

	@Id @JsonProperty(access = Access.READ_ONLY)
	private ObjectId id;
	@ApiModelProperty(notes = "Identificao do paciente")
	private ObjectId pacienteId;
	@ApiModelProperty(notes = "Data de checkin")
	private Date checkin;
	@JsonInclude(Include.NON_NULL)
	@ApiModelProperty(notes = "Data de checkout")
	private Date checkout;

	public Leito() {
	}

	public Leito(ObjectId pacienteId, Date checkin, Date checkout) {
		this.pacienteId = pacienteId;
		this.checkin = checkin;
		this.checkout = checkout;
	}

	public String getId() {
		return id.toHexString();
	}
	
	@JsonIgnore
	public ObjectId getObjectId() {
		return id;
	}

	public void setId(ObjectId id) {
		if (Objects.isNull(this.id))
			this.id = id;
	}

	@JsonIgnore
	public ObjectId getPacienteObjectId() {
		return pacienteId;
	}
	
	public String getPacienteId() {
		return pacienteId.toHexString();
	}

	public void setPacienteId(ObjectId pacienteId) {
		this.pacienteId = pacienteId;
	}

	public Date getCheckin() {
		return checkin;
	}

	public void setCheckin(Date checkin) {
		this.checkin = checkin;
	}

	public Date getCheckout() {
		return checkout;
	}

	public void setCheckout(Date checkout) {
		this.checkout = checkout;
	}

}