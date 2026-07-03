package com.chen.sfs.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(schema = "sfs", name = "users")
public class UsersEntity {

	@Id
	@Column(name = "username", columnDefinition = "text", nullable = false)
	private String username;

	@Column(name = "password", columnDefinition = "text", nullable = false)
	private String password;

	@Column(name = "enabled", columnDefinition = "boolean", nullable = false)
	private Boolean enabled;

	@Column(name = "locked", columnDefinition = "boolean", nullable = false)
	private Boolean locked;

	@Column(name = "last_login_time", columnDefinition = "timestamp", nullable = false)
	private LocalDateTime lastLoginTime;

	@Column(name = "last_password_time", columnDefinition = "timestamp", nullable = false)
	private LocalDateTime lastPasswordTime;

	@JdbcTypeCode(SqlTypes.ARRAY)
	@Column(name = "authorities", columnDefinition = "text array", nullable = false)
	private List<String> authorities;

}
