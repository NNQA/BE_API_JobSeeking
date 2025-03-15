package com.quocanh.doan.Model;

import com.quocanh.doan.Model.Enum.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Data
@Builder
@Table(name = "tokens")
@AllArgsConstructor
@NoArgsConstructor
public class TokenStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private boolean revoked;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;


    /**
     * Kiểm tra xem người dùng có thể yêu cầu lại xác thực hay không.
     *
     * @param minutes Khoảng thời gian tối thiểu giữa các lần request.
     * @return True nếu có thể request lại, False nếu chưa đủ thời gian chờ.
     */
    public boolean canRequestAgain(int minutes) {
        if (createdDateTime == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        long minutesSinceCreate = ChronoUnit.MINUTES.between(createdDateTime, now);

        return minutesSinceCreate >= minutes;
    }

    /**
     * Kiểm tra token còn hạn không.
     *
     * @return True nếu token đã hết hạn, False nếu còn hạn.
     */
    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }
}
