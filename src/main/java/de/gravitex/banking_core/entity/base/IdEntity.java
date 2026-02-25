package de.gravitex.banking_core.entity.base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@MappedSuperclass
@Data
public class IdEntity extends NoIdEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_name")
	@SequenceGenerator(name = "seq_name", sequenceName = "booking_id_seq", allocationSize = 1)
	private Long id;
}