package greencity.entity;

import greencity.enums.GoalStatus;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "user_goals")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"user"})
@Builder
public class UserGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private HabitAssign habitAssign;

    @ManyToOne(fetch = FetchType.LAZY)
    private Goal goal;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private GoalStatus status = GoalStatus.ACTIVE;

    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss.zzz")
    private LocalDateTime dateCompleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGoal)) {
            return false;
        }
        UserGoal userGoal = (UserGoal) o;
        return id.equals(userGoal.id)
            && habitAssign.equals(userGoal.habitAssign)
            && Objects.equals(goal, userGoal.goal)
            && status == userGoal.status
            && Objects.equals(dateCompleted, userGoal.dateCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, habitAssign, goal, status, dateCompleted);
    }
}