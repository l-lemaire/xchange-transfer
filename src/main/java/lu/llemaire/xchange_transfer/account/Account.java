package lu.llemaire.xchange_transfer.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter @Setter
@Entity
@Table(name = "account", schema = "xchange_transfer")
public class Account {
    @Id
    @Column(name = "account_id", nullable = false)
    private Long id;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @ColumnDefault("0.0")
    @Column(name = "balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

}