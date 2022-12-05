package dev.tolstov.buspark.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "modelName"
)
@JsonSubTypes( {
        @JsonSubTypes.Type(value = Address.class, name="address"),
        @JsonSubTypes.Type(value = BusStop.class, name="busStop")
} )
public abstract class BPEntity { }
