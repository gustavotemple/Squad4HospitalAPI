package gestao.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;


@TypeAlias("Leito")
@Document(collection = "leitos")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Leito{

    @Id @JsonProperty(access = Access.READ_ONLY)
	private ObjectId id;
    private ObjectId pacienteID;
    private Date checkin;
	private Date checkout;

    public Leito(){}

    public Leito(ObjectId pacienteID, Date checkin, Date checkout){
        super();                
        this.pacienteID = pacienteID;
        this.checkin = checkin;
        this.checkout = checkout;
    }

    @JsonProperty
    public ObjectId getId(){ return this.id;}
    @JsonProperty
    public ObjectId getPacienteId(){return this.pacienteID;}
    
    public Date getChekIn(){return this.checkin;}

    public void setId(ObjectId id){this.id = id;}
    public void setPacienteId(ObjectId pacienteId){this.pacienteID = pacienteId;}
    public void setCheckin(Date checkin){this.checkin = checkin;}
    public void setCheckout(Date checkout){this.checkout = checkout;}
}