package lu.llemaire.xchange_transfer.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Accessors(chain = true)
@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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