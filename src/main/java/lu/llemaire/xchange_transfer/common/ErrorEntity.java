package lu.llemaire.xchange_transfer.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEntity {

    private String uuid;
    private String message;
    private LocalDateTime timestamp;
}
