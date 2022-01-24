package name.junnikym.consumptionHistory.history.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import name.junnikym.consumptionHistory.member.domain.Member;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Builder
public class History implements Serializable {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	// 금액
	private Long amount;

	// 결제 내용 메모 요약본 (제목)
	private String summaryMemo;

	// 결제 내용 메모 자세히
	private String detailMemo;

	// 삭제 여부
	@Column(nullable = false)
	@ColumnDefault("false")
	private Boolean isDeleted;

	@CreationTimestamp
	private Timestamp createAt;

	@UpdateTimestamp
	private Timestamp updateAt;



	@ManyToOne
	@JsonManagedReference
	private Member writer;




	public void setWriter(Member writer) {
		this.writer = writer;
	}



	@PrePersist
	public void prePersist() {
		if(this.isDeleted == null) this.isDeleted=false;
	}
}
