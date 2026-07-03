package com.chen.sfs.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.chen.sfs.repository.jpa.FilesJpaRepository;

@DataJpaTest
@Sql(scripts = "classpath:database/sfs.files.sql")
class FilesRepositoryTest {

	FilesRepository repository;

	@Autowired
	FilesJpaRepository jpaRepository;

	@BeforeEach
	void test() {
		repository = new FilesRepository(jpaRepository);
	}

	@Test
	void exists_without_hash_name() {
		boolean result = repository.exists("", null, "");
		assertThat(result).isFalse();
	}

	@Test
	void exists() {
		boolean result = repository.exists("admin", "1a", "a.txt");
		assertThat(result).isTrue();
	}

	@Test
	void count_without_hash() {
		long result = repository.count(null);
		assertThat(result).isEqualTo(0);
	}

	@Test
	void count() {
		long result = repository.count("1a");
		assertThat(result).isEqualTo(2);
	}

	@Test
	void page() {
		var page = repository.page("admin", "txt", 0, 10);
		assertThat(page).isNotNull();
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getContent()).hasSize(3);
	}

}
