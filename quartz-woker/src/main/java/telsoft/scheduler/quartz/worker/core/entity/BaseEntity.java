package telsoft.scheduler.quartz.worker.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.util.Date;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Basic
    @Column(name = "CREATE_DATE", updatable = false)
    protected Date createDate;

    @CreatedBy
    @Basic
    @Column(name = "CREATE_BY", updatable = false)
    protected String createBy;

    @Basic
    @Column(name = "UPDATE_DATE")
    protected Date updateDate;

    @LastModifiedBy
    @Basic
    @Column(name = "UPDATE_BY")
    protected String updateBy;

    @PrePersist
    protected void prePersist() {
        this.createDate = new Date();
        this.updateDate = new Date();
        String username = "System";
        this.createBy = username;
        this.updateBy = username;
    }

    @PreUpdate
    private void preUpdate() {
        this.updateDate = new Date();
        this.updateBy = "System";
    }
}
