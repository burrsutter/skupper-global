package com.burrsutter;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Work extends PanacheEntity {
    public String message;
    public long result; 
    public String processedby;
    public String location;
    public long processingtime;    
}
