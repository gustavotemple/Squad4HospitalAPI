package com.acelera.squad.four.hospital.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;


@TypeAlias("Leito")
@Document(collection = "leitos")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Leito{

    @Id
	@JsonProperty(access = Access.READ_ONLY)
	private String id;
    private String pacienteID;
    private Date checkin;
	//private Date checkout;

    public Leito(){}

    public Leito(String pacienteID, Date checkin){
        super();                
        this.pacienteID = pacienteID;
        this.checkin = checkin;
    }

    @JsonProperty
    public String getId(){ return this.id;}
    @JsonProperty
    public String getPacienteId(){return this.pacienteID;}
    
    public Date getChekIn(){return this.checkin;}

    public void setId(String id){this.id = id;}
    public void setPacienteId(String pacienteId){this.pacienteID = pacienteId;}
    public void setCheckin(Date checkin){this.checkin = checkin;}
}