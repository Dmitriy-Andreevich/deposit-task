package space.meduzza.deposittask.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@MappedSuperclass
public class BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Min(0)
	@Max(Long.MAX_VALUE)
	private long id;

	@CreationTimestamp
	private Timestamp createTime;

	@UpdateTimestamp
	private Timestamp updateTime;
}
