package com.chen.sfs.repository;

import com.chen.sfs.exception.DatabaseOperationException;
import com.chen.sfs.repository.jpa.FilesJpaRepository;
import com.chen.sfs.repository.jpa.entity.FilesEntity;
import com.chen.sfs.repository.jpa.entity.FilesEntity_;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class FilesRepository {

	private final FilesJpaRepository repository;

	public void saveAll(List<FilesEntity> entities) {
		try {
			repository.saveAll(entities);
		} catch (Throwable e) {
			throw new DatabaseOperationException("Failed to save file information", e);
		}
	}

	public boolean exists(String uploader, String hash, String name) {
		if (isEmpty(uploader) || isEmpty(hash) && isEmpty(name)) {
			return false;
		}
		try {
			var ps = eqHash(hash)
				.and(eqName(name))
				.and(eqUploader(uploader));
			return repository.exists(ps);
		} catch (Throwable e) {
			throw new DatabaseOperationException("Failed to query file information", e);
		}
	}

	public Optional<FilesEntity> findById(UUID id) {
		if (isEmpty(id)) {
			return Optional.empty();
		}
		try {
			return repository.findById(id);
		} catch (Throwable e) {
			throw new DatabaseOperationException("Failed to query file information", e);
		}
	}

	public void deleteById(UUID id) {
		if (isEmpty(id)) {
			return;
		}
		try {
			repository.deleteById(id);
		} catch (Throwable e) {
			throw new DatabaseOperationException("Failed to delete file information", e);
		}
	}

	public long count(String hash) {
		if (isEmpty(hash)) {
			return 0;
		}
		try {
			return repository.count(eqHash(hash));
		} catch (Throwable e) {
			throw new DatabaseOperationException("Failed to count file information", e);
		}
	}

	public Page<FilesEntity> page(String uploader, String name, int page, int size) {
		if (isEmpty(uploader)) {
			return Page.empty();
		}
		var sort = JpaSort.of(FilesEntity_.uploadTime).descending();
		var pageReq = PageRequest.of(page, size, sort);
		return repository.findBy(eqUploader(uploader).and(likeName(name)), q -> q.page(pageReq));
	}

	static PredicateSpecification<FilesEntity> eqUploader(String uploader) {
		if ("admin".equals(uploader)) {
			return PredicateSpecification.unrestricted();
		}
		return (f, cb) -> cb.equal(f.get(FilesEntity_.uploader), uploader);
	}

	static PredicateSpecification<FilesEntity> eqHash(String hash) {
		if (isEmpty(hash)) {
			return PredicateSpecification.unrestricted();
		}
		return (f, cb) -> cb.equal(f.get(FilesEntity_.hash), hash);
	}

	static PredicateSpecification<FilesEntity> eqName(String name) {
		if (isEmpty(name)) {
			return PredicateSpecification.unrestricted();
		}
		return (f, cb) -> cb.equal(f.get(FilesEntity_.name), name);
	}

	static PredicateSpecification<FilesEntity> likeName(String name) {
		if (isEmpty(name)) {
			return PredicateSpecification.unrestricted();
		}
		return (f, cb) -> cb.like(f.get(FilesEntity_.name), "%" + name + "%");
	}

}
