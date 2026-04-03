package com.wakati.entity;

import com.wakati.enums.Status;
import jakarta.persistence.*;
import com.wakati.enums.DealerAssignmentStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "DEALER_ASSIGNMENT")
public class DealerAssignment extends BaseCreatedAtEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String assignmentId;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private User dealer;

    @ManyToOne
    private User superDealer;

    @Enumerated(EnumType.STRING)
    private Status status;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getDealer() {
        return dealer;
    }

    public void setDealer(User dealer) {
        this.dealer = dealer;
    }

    public User getSuperDealer() {
        return superDealer;
    }

    public void setSuperDealer(User superDealer) {
        this.superDealer = superDealer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}