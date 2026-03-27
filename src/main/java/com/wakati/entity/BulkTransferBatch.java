package com.wakati.entity;

import com.wakati.enums.CommissionMode;
import com.wakati.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "BULK_TRANSFER_BATCH")
public class BulkTransferBatch {

    @Id
    private String batchId;

    @ManyToOne
    @JoinColumn(name = "source_user_id", referencedColumnName = "user_id")
    private User sourceUser;

    @ManyToOne
    @JoinColumn(name = "initiated_by", referencedColumnName = "user_id")
    private User initiatedBy;

    @ManyToOne
    @JoinColumn(name = "requested_by", referencedColumnName = "user_id")
    private User requestedBy;

    private Integer transactionCount;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private CommissionMode commissionMode;

    private Double commissionPercentage;
    private Double totalCommission;
    private Double totalDebit;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Lob
    private String rowsJson;

    @ManyToOne
    @JoinColumn(name = "reviewed_by", referencedColumnName = "user_id")
    private User reviewedBy;

    private LocalDateTime reviewedAt;

    private String rejectionReason;

    private LocalDateTime createdAt;

    // Getters and Setters
}